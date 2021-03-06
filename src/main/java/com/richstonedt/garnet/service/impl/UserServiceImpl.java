/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.richstonedt.garnet.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.richstonedt.garnet.common.contants.GarnetContants;
import com.richstonedt.garnet.common.utils.IdGeneratorUtil;
import com.richstonedt.garnet.common.utils.PageUtil;
import com.richstonedt.garnet.interceptory.JwtToken;
import com.richstonedt.garnet.interceptory.LogRequired;
import com.richstonedt.garnet.interceptory.LoginMessage;
import com.richstonedt.garnet.mapper.BaseMapper;
import com.richstonedt.garnet.mapper.UserMapper;
import com.richstonedt.garnet.model.*;
import com.richstonedt.garnet.model.criteria.*;
import com.richstonedt.garnet.model.message.MessageDescription;
import com.richstonedt.garnet.model.parm.UserParm;
import com.richstonedt.garnet.model.view.*;
import com.richstonedt.garnet.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, UserCriteria, Long> implements UserService {

    private static final String LOGINMESSAGE_STATUS_FALSE = "false";

    private static final String LOGINMESSAGE_STATUS_SUCCESS = "success";

    private static final String STRING_ACCESSTOKEN = "#accessToken";

    private static final String STRING_REFRESHTOKEN = "#refreshToken";

    private static final Long APPLICATIONID_NULL = 0L;

    private static final Long TENANTID__NULL = 0L;

    private static final String TENANT_RELATED_ALLUSER_Y = "Y";

    private BeanCopier daoToViewCopier = BeanCopier.create(Resource.class, RefreshTokenResourceView.class,
        false);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCredentialService userCredentialService;

    @Autowired
    private UserTenantService userTenantService;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private RouterGroupService routerGroupService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ApplicationTenantService applicationTenantService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private GroupRoleService groupRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ResourceDynamicPropertyService resourceDynamicPropertyService;

    @Autowired
    private CommonService commonService;

    @Override
    public BaseMapper getBaseMapper() {
        return this.userMapper;
    }

    @LogRequired(module = "用户管理模块", method = "新增用户")
    @Override
    public Long insertUser(UserView userView) {

        String credential = userView.getPassword();
        User user = userView.getUser();

        user.setId(IdGeneratorUtil.generateId());
        UserCredential userCredential = new UserCredential();
        userCredential.setExpiredDateTime(userView.getExpiredDateTime());
        userCredential.setCredential(credential);
        userCredential.setId(IdGeneratorUtil.generateId());
        userCredential.setUserId(user.getId());
        Long currentTime = System.currentTimeMillis();
        userCredential.setCreatedTime(currentTime);
        userCredential.setModifiedTime(currentTime);

        user.setCreatedTime(currentTime);
        user.setModifiedTime(currentTime);

        //检查用户名称是否已被使用
        checkDuplicateName(user);

        this.insertSelective(user);
        userCredentialService.insertSelective(userCredential);
        //自动关联选择了 默认关联所有用户 的租户
        this.relatedAllUserTenant(userView);
        //关联选择的租户
        List<Long> tenantIds = userView.getTenantIds();
        if (!CollectionUtils.isEmpty(tenantIds)) {
            this.relatedTenants(tenantIds, user.getId());
        }
        //关联选择的Garnet组
        List<Long> groupIds = userView.getGroupIds();
        if (!CollectionUtils.isEmpty(groupIds)) {
            this.relatedGarnetGroups(groupIds, user.getId());
        }

        //User - tenant中间表
        if (!ObjectUtils.isEmpty(userView.getUserTenants())) {
            for (UserTenant userTenant : userView.getUserTenants()) {
                userTenant.setId(IdGeneratorUtil.generateId());
                userTenant.setUserId(user.getId());
                userTenantService.insertSelective(userTenant);
            }
        }

        //User - Group 中间表
        if (!ObjectUtils.isEmpty(userView.getGroupUsers())) {
            for (GroupUser groupUser : userView.getGroupUsers()) {
                groupUser.setId(IdGeneratorUtil.generateId());
                groupUser.setUserId(user.getId());
                groupUserService.insertSelective(groupUser);
            }
        }
        return user.getId();
    }

    /**
     * 为用户关联租户
     *
     * @param tenantIds
     * @param userId
     */
    private void relatedTenants(List<Long> tenantIds, Long userId) {
        UserTenant userTenant = new UserTenant();
        userTenant.setUserId(userId);
        for (Long tenantId : tenantIds) {
            userTenant.setId(IdGeneratorUtil.generateId());
            userTenant.setTenantId(tenantId);
            boolean isRealted = this.checkUserTenantRealted(tenantId, userId);
            //如果用户和此租户还没有关联，则关联它们
            if (!isRealted) {
                userTenantService.insertSelective(userTenant);
            }
        }

        Log log = new Log();
        log.setMessage("用户管理模块");
        log.setOperation("用户绑定租户");
        commonService.insertLog(log);
    }

    private void relatedGarnetGroups(List<Long> groupIds, Long userId) {
        GroupUser groupUser = new GroupUser();
        groupUser.setUserId(userId);
        for (Long groupId : groupIds) {
            groupUser.setId(IdGeneratorUtil.generateId());
            groupUser.setUserId(userId);
            groupUser.setGroupId(groupId);
            boolean isRelated = this.checkUserGroupRealted(groupId, userId);
            //如果还没有关联,则关联
            if (!isRelated) {
                groupUserService.insertSelective(groupUser);
            }
        }

        Log log = new Log();
        log.setMessage("用户管理模块");
        log.setOperation("用户绑定绑定Garnet组");
        commonService.insertLog(log);
    }

    private boolean checkUserGroupRealted(Long groupId, Long userId) {
        GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
        groupUserCriteria.createCriteria().andUserIdEqualTo(userId).andGroupIdEqualTo(groupId);
        List<GroupUser> groupUserList = groupUserService.selectByCriteria(groupUserCriteria);
        if (!CollectionUtils.isEmpty(groupUserList)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过用户id得到租户id
     *
     * @param userId
     * @return
     */
    @Override
    public String getTenantByUser(Long userId) {
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andUserIdEqualTo(userId);
        List<UserTenant> userTenants = userTenantService.selectByCriteria(userTenantCriteria);
        long tenantId = userTenants.get(1).getTenantId();
        Tenant tenant = tenantService.selectByPrimaryKey(tenantId);
        return tenant.getName();

    }

    /**
     * 检查用户和租户是否已经关联
     *
     * @param tenantId
     * @param userId
     * @return
     */
    private boolean checkUserTenantRealted(Long tenantId, Long userId) {
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andTenantIdEqualTo(tenantId).andUserIdEqualTo(userId);
        List<UserTenant> userTenants = userTenantService.selectByCriteria(userTenantCriteria);
        if (!CollectionUtils.isEmpty(userTenants)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查用户名称是否已被使用
     *
     * @param user
     */
    private void checkDuplicateName(User user) {
        UserCriteria userCriteria = new UserCriteria();
        userCriteria.createCriteria().andUserNameEqualTo(user.getUserName()).andStatusEqualTo(1);
        User user1 = this.selectSingleByCriteria(userCriteria);
        if (!ObjectUtils.isEmpty(user1) && user1.getId().longValue() != user.getId().longValue()) {
            throw new RuntimeException("账号已存在");
        }
    }

    @LogRequired(module = "用户模块", method = "配置用户")
    @Override
    public void updateUser(UserView userView) {

        //检查用户名称是否已被使用
        User user = userView.getUser();
        checkDuplicateName(user);
        Long loginUserId = userView.getLoginUserId();

        //更新密码表

        UserCredentialCriteria userCredentialCriteria = new UserCredentialCriteria();
        userCredentialCriteria.createCriteria().andUserIdEqualTo(userView.getUser().getId());
        UserCredential userCredential = userCredentialService.selectSingleByCriteria(userCredentialCriteria);

        if (!userCredential.getExpiredDateTime().equals(userView.getExpiredDateTime())) {
            userCredential.setExpiredDateTime(userView.getExpiredDateTime());
            userCredential.setModifiedTime(System.currentTimeMillis());
        }

        if (!userCredential.getCredential().equals(userView.getPassword())) {
            userCredential.setCredential(userView.getPassword());
            userCredential.setModifiedTime(System.currentTimeMillis());
        }

        userCredentialService.updateByPrimaryKeySelective(userCredential);

        //更新User表
        user.setModifiedTime(System.currentTimeMillis());
        this.updateByPrimaryKeySelective(user);

        //User - tenant中间表
        if (!ObjectUtils.isEmpty(userView.getUserTenants())) {
            UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
            userTenantCriteria.createCriteria().andUserIdEqualTo(userView.getUser().getId());
            userTenantService.deleteByCriteria(userTenantCriteria);
            for (UserTenant userTenant : userView.getUserTenants()) {
                userTenant.setId(IdGeneratorUtil.generateId());
                userTenant.setUserId(userView.getUser().getId());
                userTenantService.insertSelective(userTenant);
            }
        }

        //User - Group 中间表
        if (!ObjectUtils.isEmpty(userView.getGroupUsers())) {
            GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
            groupUserCriteria.createCriteria().andUserIdEqualTo(userView.getUser().getId());
            groupUserService.deleteByCriteria(groupUserCriteria);
            for (GroupUser groupUser : userView.getGroupUsers()) {
                groupUser.setId(IdGeneratorUtil.generateId());
                groupUser.setUserId(userView.getUser().getId());
                groupUserService.insertSelective(groupUser);
            }
        }

        //自动关联选择了 默认关联所有用户 的租户
        this.relatedAllUserTenant(userView);
        //查询有权限看到的租户列表
        List<Tenant> tenantList = tenantService.getTenantListByUserId(loginUserId);
        List<Long> tenantIdList = new ArrayList<>();
        for (Tenant tenant : tenantList) {
            tenantIdList.add(tenant.getId());
        }
        if (CollectionUtils.isEmpty(tenantIdList)) {
            tenantIdList.add(GarnetContants.NON_VALUE);
        }
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andTenantIdIn(tenantIdList).andUserIdEqualTo(user.getId());
        userTenantService.deleteByCriteria(userTenantCriteria);

        //关联选择的租户
        List<Long> tenantIds = userView.getTenantIds();
        if (!CollectionUtils.isEmpty(tenantIds)) {
            this.relatedTenants(tenantIds, user.getId());
        }

        //查询有权限看到的Garnet组列表
        List<Group> groupList = groupService.getGarnetGroupList(loginUserId);
        List<Long> groupIdList = new ArrayList<>();
        for (Group group : groupList) {
            groupIdList.add(group.getId());
        }
        if (groupIdList.size() == 0) {
            groupIdList.add(GarnetContants.NON_VALUE);
        }
        GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
        groupUserCriteria.createCriteria().andGroupIdIn(groupIdList).andUserIdEqualTo(user.getId());
        groupUserService.deleteByCriteria(groupUserCriteria);
        //关联选择的Garnet组
        List<Long> groupIds = userView.getGroupIds();
        if (!CollectionUtils.isEmpty(groupIds)) {
            this.relatedGarnetGroups(groupIds, user.getId());
        }

    }

    @Override
    public void deleteUser(UserView userView) {

        User user = userView.getUser();

        //删除密码表
        UserCredentialCriteria userCredentialCriteria = new UserCredentialCriteria();
        userCredentialCriteria.createCriteria().andUserIdEqualTo(user.getId());
        userCredentialService.deleteByCriteria(userCredentialCriteria);

        //删除User - tenant中间表
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andUserIdEqualTo(userView.getUser().getId());
        userTenantService.deleteByCriteria(userTenantCriteria);

        //删除User - Group 中间表
        GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
        groupUserCriteria.createCriteria().andUserIdEqualTo(userView.getUser().getId());
        groupUserService.deleteByCriteria(groupUserCriteria);
        this.deleteByPrimaryKey(user.getId());
    }

    @Override
    public PageUtil queryUsersByParms(UserParm userParm) {
        UserCriteria userCriteria = new UserCriteria();
        userCriteria.setOrderByClause(GarnetContants.ORDER_BY_CREATED_TIME);
        UserCriteria.Criteria criteria = userCriteria.createCriteria();
        //查询没被删除的user
        criteria.andStatusEqualTo(1);

        if (!StringUtils.isEmpty(userParm.getSearchName())) {
            criteria.andUserNameLike("%" + userParm.getSearchName() + "%");
        }

        //根据userId ,获取tenantId列表
        if (!StringUtils.isEmpty(userParm.getUserId())) {
            ReturnTenantIdView returnTenantIdView = this.getTenantIdsByUserId(userParm.getUserId());
            List<Long> tenantIds = returnTenantIdView.getTenantIds();

            //如果不是属于garnet的超级管理员，根据tenantId返回
            if (!returnTenantIdView.isSuperAdmin() || (returnTenantIdView.isSuperAdmin() && !commonService.superAdminBelongGarnet(userParm.getUserId()))) {
                //根据tenantIds获取关联的userIds
                List<Long> userIdList = getUserIdList(tenantIds);
                criteria.andIdIn(userIdList);
            } else {
                //如果是garnet的超级管理员，直接返回所有user列表
                PageUtil result = new PageUtil(this.selectByCriteria(userCriteria), (int) this.countByCriteria(userCriteria), userParm.getPageSize(), userParm.getPageNumber());
                return result;
            }

        } else {
            //没有传入userId，直接返回null
            return new PageUtil(null, 0, userParm.getPageSize(), userParm.getPageNumber());
        }

        List<User> users = this.selectByCriteria(userCriteria);

        //当登录用户不是超级管理员的时候，去除belongToGarnet为Y的用户
        User loginUser = this.selectByPrimaryKey(userParm.getUserId());
        boolean isBelongToGarnet = commonService.superAdminBelongGarnet(userParm.getUserId());
        if (!isBelongToGarnet) {
            List<User> userList = new ArrayList<>();
            for (User user : users) {
                boolean b = commonService.superAdminBelongGarnet(user.getId());
                if (!b) {
                    userList.add(user);
                }
            }
            users = userList;
        }

        List<User> usersWithTenant = new ArrayList<>();
        for (User user : users) {
            Long userId = user.getId();
            UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
            userTenantCriteria.createCriteria().andUserIdEqualTo(userId);
            List<UserTenant> userTenants = userTenantService.selectByCriteria(userTenantCriteria);
            //如果此用户有关联租户，则添加进返回的列表
            if (!CollectionUtils.isEmpty(userTenants)) {
                usersWithTenant.add(user);
            }
        }

        PageUtil result = new PageUtil(usersWithTenant, (int) this.countByCriteria(userCriteria), userParm.getPageSize(), userParm.getPageNumber());
        return result;
    }

    /**
     * 根据租户id获得用户ids
     *
     * @param tenantIds
     * @return
     */
    private List<Long> getUserIdList(List<Long> tenantIds) {
        if (tenantIds.size() == 0) {
            tenantIds.add(GarnetContants.NON_VALUE);
        }

        //根据tenantId获取user列表
        List<Long> userIdList = this.getUserIdsByTenantIds(tenantIds);
        if (userIdList.size() == 0) {
            userIdList.add(GarnetContants.NON_VALUE);
        }
        return userIdList;
    }

    private List<Long> getUserIdsByTenantIds(List<Long> tenantIds) {
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andTenantIdIn(tenantIds);
        List<UserTenant> userTenants1 = userTenantService.selectByCriteria(userTenantCriteria);
        List<Long> userIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userTenants1) && userTenants1.size() > 0) {
            for (UserTenant userTenant : userTenants1) {
                userIds.add(userTenant.getUserId());
            }
        }
        // 如果userIds 不为空
        if (!CollectionUtils.isEmpty(userIds)) {
            List<Long> userIdList = new ArrayList<>();
            //隐藏 admin
            for (Long userId : userIds) {
                if (userId.longValue() != GarnetContants.GARNET_USER_ID) {
                    userIdList.add(userId);
                }
            }
            return userIdList;

        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 1. 判断参数是否正确（userName, password, appCode）
     * 2. 账号密码核验正确，生成token并计算过期时间
     * 3. 查询数据库，判断用户在数据库中是否已有token,有则更新，无则添加
     * 4. 返回状态码 201 给前端
     *
     * @param loginView
     * @return
     * @throws Exception
     */
    @Override
    public LoginMessage userLogin(LoginView loginView) throws Exception {

        UserCriteria userCriteria = new UserCriteria();
        LoginMessage loginMessage = new LoginMessage();
        userCriteria.createCriteria().andUserNameEqualTo(loginView.getUserName()).andStatusEqualTo(1);
        User user = this.selectSingleByCriteria(userCriteria);

        if (StringUtils.isEmpty(loginView.getUserName()) || StringUtils.isEmpty(loginView.getPassword())) {
            loginMessage.setMessage("账号或密码为空");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        if (ObjectUtils.isEmpty(user)) {
            loginMessage.setMessage("用户名或者密码错误");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        if (StringUtils.isEmpty(loginView.getAppCode())) {
            loginMessage.setMessage("AppCode不能为空");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        ApplicationCriteria applicationCriteria = new ApplicationCriteria();
        applicationCriteria.createCriteria().andAppCodeEqualTo(loginView.getAppCode()).andStatusEqualTo(1);
        Application application = applicationService.selectSingleByCriteria(applicationCriteria);
        if (ObjectUtils.isEmpty(application)) {
            loginMessage.setMessage("应用不存在");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //获取密码,验证密码是否正确
        UserCredentialCriteria userCredentialCriteria = new UserCredentialCriteria();
        userCredentialCriteria.createCriteria().andUserIdEqualTo(user.getId());
        UserCredential userCredential = userCredentialService.selectSingleByCriteria(userCredentialCriteria);
        if (!userCredential.getCredential().equals(loginView.getPassword())) {
            loginMessage.setMessage("用户名或者密码错误");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //生成token返回
        String token = JwtToken.createToken(user.getUserName(), userCredential.getCredential(), loginView.getAppCode(), System.currentTimeMillis());
        loginMessage.setMessage("登录成功");
        loginMessage.setCode(201);
        loginMessage.setUser(user);
        loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_SUCCESS);
        //拼接返回前端的token
//        token = token.split(".")[2];
        String accessTokenReturn = token + "#" + loginView.getAppCode() + "#" + loginView.getUserName() + "#" + System.currentTimeMillis() + STRING_ACCESSTOKEN;
        String refreshTokenReturn = token + "#" + loginView.getAppCode() + "#" + loginView.getUserName() + "#" + System.currentTimeMillis() + STRING_REFRESHTOKEN;
        loginMessage.setAccessToken(accessTokenReturn);
        loginMessage.setRefreshToken(refreshTokenReturn);

        TokenCriteria tokenCriteria = new TokenCriteria();
        tokenCriteria.createCriteria().andUserNameEqualTo(loginView.getUserName());
        Token tokenOld = tokenService.selectSingleByCriteria(tokenCriteria);

        //如果是第一次登录，将token插入数据库
        if (ObjectUtils.isEmpty(tokenOld)) {
            Token token1 = new Token();
            token1.setCreatedTime(System.currentTimeMillis());
            token1.setModifiedTime(System.currentTimeMillis());
            Long expiredTime = token1.getCreatedTime() + 30 * 60000L;
            token1.setExpireTime(expiredTime);
            token1.setId(IdGeneratorUtil.generateId());
            token1.setUserName(loginView.getUserName());
            token1.setToken(token);
            tokenService.insertSelective(token1);
        } else {
            //不是第一次登录，更新token
            tokenOld.setToken(token);
            tokenOld.setModifiedTime(System.currentTimeMillis());
            Long expiredTime = tokenOld.getModifiedTime() + GarnetContants.TOKEN_EXPIRED_TIME;
            tokenOld.setExpireTime(expiredTime);
            tokenOld.setToken(token);
            tokenService.updateByPrimaryKeySelective(tokenOld);
        }

        //查询登录用户关联的tenantIds
        //*******************************change by ming***********************************
        ApplicationTenantCriteria applicationTenantCriteria = new ApplicationTenantCriteria();
        applicationTenantCriteria.createCriteria().andApplicationIdEqualTo(application.getId());
        List<ApplicationTenant> applicationTenants = applicationTenantService.selectByCriteria(applicationTenantCriteria);
        List<Long> applicationTenantsIds = new ArrayList<>();
        for (ApplicationTenant applicationTenant : applicationTenants) {
            applicationTenantsIds.add(applicationTenant.getTenantId());
        }
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andTenantIdIn(applicationTenantsIds).andUserIdEqualTo(user.getId());
        List<UserTenant> userTenants = userTenantService.selectByCriteria(userTenantCriteria);
        List<Long> tenantsIds = new ArrayList<>();

        for (UserTenant userTenant : userTenants) {
            tenantsIds.add(userTenant.getTenantId());
        }

        if (tenantsIds.size() == 0) {
            tenantsIds.add(GarnetContants.NON_VALUE);
        }

        TenantCriteria tenantCriteria = new TenantCriteria();
        tenantCriteria.createCriteria().andIdIn(tenantsIds);
        List<Tenant> tenants = tenantService.selectByCriteria(tenantCriteria);
        Map<String, Long> tenantNameAndIdMap = new HashMap<String, Long>();
        for (Tenant tenant : tenants) {
            tenantNameAndIdMap.put(tenant.getName(), tenant.getId());
        }
        loginMessage.setUserTenantNameAndIdMap(tenantNameAndIdMap);
        //******************************************************************

        ReturnTenantIdView returnTenantIdView = this.getTenantIdsByUserId(user.getId());
        List<Long> tenantIdList = returnTenantIdView.getTenantIds();
        loginMessage.setTenantIdList(tenantIdList);

        return loginMessage;
    }

    @Override
    public UserView getUserById(UserParm userParm) {
        Long userId = userParm.getUser().getId();
        Long loginUserId = userParm.getLoginUserId();
        User user = this.selectByPrimaryKey(userId);
        UserView userView = new UserView();
        userView.setUser(user);

        UserCredentialCriteria userCredentialCriteria = new UserCredentialCriteria();
        userCredentialCriteria.createCriteria().andUserIdEqualTo(user.getId());
        UserCredential userCredential = userCredentialService.selectSingleByCriteria(userCredentialCriteria);
        userView.setPassword(userCredential.getCredential());
        userView.setExpiredDateTime(userCredential.getExpiredDateTime());

        List<Long> tenantIds = new ArrayList<>();
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andUserIdEqualTo(userId);
        List<UserTenant> userTenants = userTenantService.selectByCriteria(userTenantCriteria);
        for (UserTenant userTenant : userTenants) {
            tenantIds.add(userTenant.getTenantId());
        }
        userView.setTenantIds(tenantIds);

        List<Group> groupList = groupService.getGarnetGroupList(loginUserId);
        List<Long> groupIds = new ArrayList<>();
        List<Long> groupIdList = new ArrayList<>();
        for (Group group : groupList) {
            groupIdList.add(group.getId());
        }
        if (groupIdList.size() == 0) {
            groupIdList.add(GarnetContants.NON_VALUE);
        }
        GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
        groupUserCriteria.createCriteria().andUserIdEqualTo(userId).andGroupIdIn(groupIdList);
        List<GroupUser> groupUserList = groupUserService.selectByCriteria(groupUserCriteria);

        for (GroupUser groupUser : groupUserList) {
            groupIds.add(groupUser.getGroupId());
        }
        userView.setGroupIds(groupIds);

        return userView;
    }

    @Override
    public User getUserByUserName(String userName) {
        UserCriteria userCriteria = new UserCriteria();
        userCriteria.createCriteria().andUserNameEqualTo(userName).andStatusEqualTo(1);
        User user = this.selectSingleByCriteria(userCriteria);
        return user;
    }

    @LogRequired(module = "用户模块", method = "删除用户")
    @Override
    public void updateStatusById(User user, Long loginUserId) {

        if (user.getId().longValue() == GarnetContants.GARNET_USER_ID) {
            throw new RuntimeException("不能删除超级管理员");
        }

        if (user.getId().longValue() == loginUserId) {
            throw new RuntimeException("不能删除自己");
        }

        //删除关联外键
        if (!ObjectUtils.isEmpty(user) && !ObjectUtils.isEmpty(user.getId())) {
            UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
            userTenantCriteria.createCriteria().andUserIdEqualTo(user.getId());
            userTenantService.deleteByCriteria(userTenantCriteria);

            GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
            groupUserCriteria.createCriteria().andUserIdEqualTo(user.getId());
            groupUserService.deleteByCriteria(groupUserCriteria);

            UserCredentialCriteria userCredentialCriteria = new UserCredentialCriteria();
            userCredentialCriteria.createCriteria().andUserIdEqualTo(user.getId());
            userCredentialService.deleteByCriteria(userCredentialCriteria);
        }

        Long currentTime = System.currentTimeMillis();
        user.setModifiedTime(currentTime);
        user.setStatus(0);
        this.updateByPrimaryKeySelective(user);

    }

    @Override
    public List<User> queryUserByTenantId(UserParm userParm) {
        Long tenantId = userParm.getTenantId();
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andTenantIdEqualTo(tenantId);
        List<UserTenant> userTenants = userTenantService.selectByCriteria(userTenantCriteria);
        List<User> users = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userTenants)) {
            for (UserTenant userTenant : userTenants) {
                Long userId = userTenant.getUserId();
                User user = this.selectByPrimaryKey(userId);
                if (!ObjectUtils.isEmpty(user)) {
                    users.add(user);
                }
            }
        }

        return users;
    }

    /**
     * 刷新token
     * 1. 能通过拦截器，说明传入的token是正确且有效的,故不再验证token是否正确
     * 2. 取出token中携带的信息
     * 3. 验证基础信息是否正确
     * 4. 根据username 查询密码
     * 5. 生成新的token
     * 6. 根据userName拿出token并更新到数据库
     * 7. 取出资源列表
     * 8. 处理返回
     *
     * @param tokenRefreshView
     * @return
     * @throws Exception
     */
    @Override
    public LoginMessage refreshToken(TokenRefreshView tokenRefreshView) throws Exception {
        //能通过拦截器，说明token是正确且有效的,故不再验证token是否正确
        LoginMessage loginMessage = new LoginMessage();

        //取出token中携带的信息
        String tokenWithAppCode = tokenRefreshView.getRefreshToken();

        if (StringUtils.isEmpty(tokenWithAppCode)) {
            throw new RuntimeException("请输入token");
        }

        String[] tokenParams = tokenWithAppCode.split("#");
        String token = tokenParams[0];
//        String appCode = tokenParams[1];
        String appCode = tokenRefreshView.getAppCode();
        String userName = tokenRefreshView.getUserName();
//        String userName = tokenParams[2];
        String createTime = tokenParams[3];

        //验证基础信息是否正确
        LoginMessage loginMessage2 = this.checkRefreshTokenData(tokenRefreshView);
        if (loginMessage2.getCode() == 401 || loginMessage2.getCode() == 403 || "false".equals(loginMessage2.getLoginStatus())) {
            return loginMessage2;
        }

        //根据username 查询密码
        UserCredential userCredential = userCredentialService.getCredentialByUserName(userName);
        String password = userCredential.getCredential();

        //更新token
        Long currentTime = System.currentTimeMillis();
        String tokenNew = JwtToken.createToken(userName, password, appCode, currentTime);
        String accessTokenReturn = tokenNew + "#" + appCode + "#" + userName + "#" + currentTime + STRING_ACCESSTOKEN;
        String refreshTokenReturn = tokenNew + "#" + appCode + "#" + userName + "#" + currentTime + STRING_REFRESHTOKEN;

        //根据userName拿出token并更新到数据库
        TokenCriteria tokenCriteria = new TokenCriteria();
        tokenCriteria.createCriteria().andUserNameEqualTo(userName);
        Token token1 = tokenService.selectSingleByCriteria(tokenCriteria);
        token1.setToken(tokenNew);
        token1.setModifiedTime(currentTime);
        token1.setExpireTime((long) currentTime + GarnetContants.TOKEN_EXPIRED_TIME);
        tokenService.updateByPrimaryKeySelective(token1);

        loginMessage.setMessage("token刷新成功");
        loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_SUCCESS);
        loginMessage.setCode(201);
        loginMessage.setAccessToken(accessTokenReturn);
        loginMessage.setRefreshToken(refreshTokenReturn);

        List<Long> tenantIdList = tokenRefreshView.getTenantIdList();
        if (CollectionUtils.isEmpty(tenantIdList) || tenantIdList.size() == 0) {
            throw new RuntimeException("租户id列表不能为空");
        }

        //取出资源列表
        LoginMessage loginMessage1 = this.getResourcesWhenRefreshToken(userCredential, appCode, loginMessage, tenantIdList);
        List<RefreshTokenResourceView> refreshTokenResourceViewList = loginMessage1.getRefreshTokenResourceList();
        List<List<ResourceDynamicProperty>> resourceDynamicPropertyList = loginMessage1.getResourceDynamicPropertyList();

        if (CollectionUtils.isEmpty(resourceDynamicPropertyList)) {
            resourceDynamicPropertyList = new ArrayList<>();
        }


//        loginMessage.setResourceList(resourceList);
//        loginMessage.setResourceDynamicPropertyList(resourceDynamicPropertyList);

        Map<String, List> typeResourceListMap = new HashMap<>();
        for (List<ResourceDynamicProperty> resourceDynamicProperties : resourceDynamicPropertyList) {
            String type = resourceDynamicProperties.get(0).getType();
            List<RefreshTokenResourceView> refreshTokenResourceViews = new ArrayList<>();
            for (RefreshTokenResourceView refreshTokenResourceView : refreshTokenResourceViewList) {
                if (type.equals(refreshTokenResourceView.getType())) {
                    refreshTokenResourceViews.add(refreshTokenResourceView);
                }
            }
            typeResourceListMap.put(type, refreshTokenResourceViews);
        }

        loginMessage.setTypeResourceListMap(typeResourceListMap);
        loginMessage.setResourceDynamicPropertyList(new ArrayList<List<ResourceDynamicProperty>>());
        loginMessage.setRefreshTokenResourceList(new ArrayList<RefreshTokenResourceView>());
//        loginMessage.setRefreshTokenResourceList(refreshTokenResourceViewList);
//        loginMessage.setResourceListWithReadlyOnly(resourceListWithReadlyOnly);
//        loginMessage.setGetResourceListWithEdit(resourceListWithEdit);

        return loginMessage;
    }

    /**
     * 获取resource列表并分组返回
     *
     * @param userCredential
     * @param appCode
     * @param loginMessage
     * @return
     */
    private LoginMessage getResourcesWhenRefreshToken(UserCredential userCredential, String appCode, LoginMessage loginMessage, List tenantIdList) {
        //获取返回资源
        //根据appCode拿 application
        ApplicationCriteria applicationCriteria = new ApplicationCriteria();
        applicationCriteria.createCriteria().andAppCodeEqualTo(appCode).andStatusEqualTo(1);
        Application application = applicationService.selectSingleByCriteria(applicationCriteria);
        if (ObjectUtils.isEmpty(application)) {
            return loginMessage;
        }
        Long applicationId = application.getId();

        //根据username 拿 group
        GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
        groupUserCriteria.createCriteria().andUserIdEqualTo(userCredential.getUserId());
        List<GroupUser> groupUserList = groupUserService.selectByCriteria(groupUserCriteria);
        if (CollectionUtils.isEmpty(groupUserList)) {
            return loginMessage;
        }

        List<Long> groupIds1 = new ArrayList<>();
        for (GroupUser groupUser : groupUserList) {
            Long groupId = groupUser.getGroupId();
            groupIds1.add(groupId);
        }
        if (groupIds1.size() == 0) {
            groupIds1.add(GarnetContants.NON_VALUE);
        }

        //根据TenantIdList和applicationId 拿Groups
        List<Group> groups1 = new ArrayList<>();
        List<Group> groups2 = new ArrayList<>();
        List<Group> groups3 = new ArrayList<>();
        GroupCriteria groupCriteria1 = new GroupCriteria();
        groupCriteria1.createCriteria().andTenantIdIn(tenantIdList).andIdIn(groupIds1).andStatusEqualTo(1).andApplicationIdEqualTo(APPLICATIONID_NULL);
        groups1 = groupService.selectByCriteria(groupCriteria1);

        GroupCriteria groupCriteria2 = new GroupCriteria();
        groupCriteria2.createCriteria().andTenantIdEqualTo(TENANTID__NULL).andIdIn(groupIds1).andStatusEqualTo(1).andApplicationIdEqualTo(applicationId);
        groups2 = groupService.selectByCriteria(groupCriteria2);

        GroupCriteria groupCriteria3 = new GroupCriteria();
        groupCriteria3.createCriteria().andTenantIdIn(tenantIdList).andIdIn(groupIds1).andStatusEqualTo(1).andApplicationIdEqualTo(applicationId);
        groups3 = groupService.selectByCriteria(groupCriteria3);

        List<Group> groups = new ArrayList<>();
        groups.addAll(groups1);
        groups.addAll(groups2);
        groups.addAll(groups3);
        if (CollectionUtils.isEmpty(groups)) {
            return loginMessage;
        }

        //根据group 拿 role
        List<Long> groupIds = new ArrayList<>();
        for (Group group : groups) {
            groupIds.add(group.getId());
        }
        if (groupIds.size() == 0) {
            groupIds.add(GarnetContants.NON_VALUE);
        }

        //根据appid和tenantIds获取roleIds
        List<Role> roles1 = new ArrayList<>();
        List<Role> roles2 = new ArrayList<>();
        List<Role> roles3 = new ArrayList<>();

        RoleCriteria roleCriteria1 = new RoleCriteria();
        roleCriteria1.createCriteria().andTenantIdIn(tenantIdList).andApplicationIdEqualTo(APPLICATIONID_NULL).andStatusEqualTo(1);
        roles1 = roleService.selectByCriteria(roleCriteria1);

        RoleCriteria roleCriteria2 = new RoleCriteria();
        roleCriteria2.createCriteria().andTenantIdEqualTo(TENANTID__NULL).andApplicationIdEqualTo(applicationId).andStatusEqualTo(1);
        roles2 = roleService.selectByCriteria(roleCriteria2);

        RoleCriteria roleCriteria3 = new RoleCriteria();
        roleCriteria3.createCriteria().andTenantIdIn(tenantIdList).andApplicationIdEqualTo(applicationId).andStatusEqualTo(1);
        roles3 = roleService.selectByCriteria(roleCriteria3);

        List<Role> roleList = new ArrayList<>();
        roleList.addAll(roles1);
        roleList.addAll(roles2);
        roleList.addAll(roles3);

        List<Long> roleIdList = new ArrayList<>();
        for (Role role : roleList) {
            roleIdList.add(role.getId());
        }
        if (roleIdList.size() == 0) {
            roleIdList.add(GarnetContants.NON_VALUE);
        }

        //通过中间表拿关联的role
        GroupRoleCriteria groupRoleCriteria = new GroupRoleCriteria();
        groupRoleCriteria.createCriteria().andGroupIdIn(groupIds).andRoleIdIn(roleIdList);
        List<GroupRole> groupRoleList = groupRoleService.selectByCriteria(groupRoleCriteria);
        if (CollectionUtils.isEmpty(groupRoleList)) {
            return loginMessage;
        }

        List<Long> roleIds = new ArrayList<>();
        for (GroupRole groupRole : groupRoleList) {
            Long roleId = groupRole.getRoleId();
            roleIds.add(roleId);
        }
        if (roleIds.size() == 0) {
            roleIds.add(GarnetContants.NON_VALUE);
        }

        //根据appId和tenantIds获取permissions
        List<Permission> permissionList1 = new ArrayList<>();
        List<Permission> permissionList2 = new ArrayList<>();
        List<Permission> permissionList3 = new ArrayList<>();

        PermissionCriteria permissionCriteria1 = new PermissionCriteria();
        permissionCriteria1.createCriteria().andTenantIdIn(tenantIdList).andApplicationIdEqualTo(APPLICATIONID_NULL).andStatusEqualTo(1);
        permissionList1 = permissionService.selectByCriteria(permissionCriteria1);

        PermissionCriteria permissionCriteria2 = new PermissionCriteria();
        permissionCriteria2.createCriteria().andTenantIdEqualTo(TENANTID__NULL).andApplicationIdEqualTo(applicationId).andStatusEqualTo(1);
        permissionList2 = permissionService.selectByCriteria(permissionCriteria2);

        PermissionCriteria permissionCriteria3 = new PermissionCriteria();
        permissionCriteria3.createCriteria().andTenantIdIn(tenantIdList).andApplicationIdEqualTo(applicationId).andStatusEqualTo(1);
        permissionList3 = permissionService.selectByCriteria(permissionCriteria3);

        List<Permission> permissions = new ArrayList<>();
        permissions.addAll(permissionList1);
        permissions.addAll(permissionList2);
        permissions.addAll(permissionList3);

        List<Long> permissionIdList = new ArrayList<>();
        for (Permission permission : permissions) {
            permissionIdList.add(permission.getId());
        }

        //根据role拿permission
        RolePermissionCriteria rolePermissionCriteria = new RolePermissionCriteria();
        rolePermissionCriteria.createCriteria().andRoleIdIn(roleIds).andPermissionIdIn(permissionIdList);
        List<RolePermission> rolePermissionList = rolePermissionService.selectByCriteria(rolePermissionCriteria);
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            return loginMessage;
        }

        List<Long> permissionIds = new ArrayList<>();
        for (RolePermission rolePermission : rolePermissionList) {
            Long permissionId = rolePermission.getPermissionId();
            permissionIds.add(permissionId);
        }
        if (permissionIds.size() == 0) {
            permissionIds.add(GarnetContants.NON_VALUE);
        }

        PermissionCriteria permissionCriteria = new PermissionCriteria();
        permissionCriteria.createCriteria().andIdIn(permissionIds).andStatusEqualTo(1);
        List<Permission> permissionList = permissionService.selectByCriteria(permissionCriteria);
        if (CollectionUtils.isEmpty(permissionList)) {
            return loginMessage;
        }


        //根据tenantIds和applicationIds 获取resourceDymicProperties
        List<ResourceDynamicProperty> resourceDynamicPropertyList1 = new ArrayList<>();
        List<ResourceDynamicProperty> resourceDynamicPropertyList2 = new ArrayList<>();
        List<ResourceDynamicProperty> resourceDynamicPropertyList3 = new ArrayList<>();
        //应用级
        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria1 = new ResourceDynamicPropertyCriteria();
        resourceDynamicPropertyCriteria1.createCriteria().andTenantIdEqualTo(TENANTID__NULL).andApplicationIdEqualTo(applicationId);
        resourceDynamicPropertyList1 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria1);
        //租户级
        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria2 = new ResourceDynamicPropertyCriteria();
        resourceDynamicPropertyCriteria2.createCriteria().andApplicationIdEqualTo(APPLICATIONID_NULL).andTenantIdIn(tenantIdList);
        resourceDynamicPropertyList2 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria2);
        //租户+应用
        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria3 = new ResourceDynamicPropertyCriteria();
        resourceDynamicPropertyCriteria3.createCriteria().andTenantIdIn(tenantIdList).andApplicationIdEqualTo(applicationId);
        resourceDynamicPropertyList3 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria3);

        List<ResourceDynamicProperty> resourceDynamicPropertyList = new ArrayList<>();
        resourceDynamicPropertyList.addAll(resourceDynamicPropertyList1);
        resourceDynamicPropertyList.addAll(resourceDynamicPropertyList2);
        resourceDynamicPropertyList.addAll(resourceDynamicPropertyList3);
        List<Long> resourceDynamicPropertyIds = new ArrayList<>();
        for (ResourceDynamicProperty resourceDynamicProperty : resourceDynamicPropertyList) {
            resourceDynamicPropertyIds.add(resourceDynamicProperty.getId());
        }
        if (resourceDynamicPropertyIds.size() == 0) {
            resourceDynamicPropertyIds.add(GarnetContants.NON_VALUE);
        }

        //根据permissionList 查询resourceDynamicProperties
        List<ResourceDynamicProperty> resourceDynamicPropertyList4 = new ArrayList<>();
        String action;
        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria;
        for (Permission permission : permissionList) {
            action = permission.getAction();
            resourceDynamicPropertyCriteria = new ResourceDynamicPropertyCriteria();
            resourceDynamicPropertyCriteria.createCriteria().andActionsLike("%" + action + "%").andIdIn(resourceDynamicPropertyIds);
            List<ResourceDynamicProperty> resourceDynamicProperties1 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria);
            resourceDynamicPropertyList4.addAll(resourceDynamicProperties1);
        }

        if (CollectionUtils.isEmpty(resourceDynamicPropertyList4)) {
            return loginMessage;
        }
        List<String> resourceDynamicPropTypeList = new ArrayList<>();
        Set<String> resourceDynamicPropTypeSet = new HashSet<>();
        for (ResourceDynamicProperty resourceDynamicProperty : resourceDynamicPropertyList4) {
            if (!resourceDynamicPropTypeSet.contains(resourceDynamicProperty.getType())) {
                resourceDynamicPropTypeList.add(resourceDynamicProperty.getType());
                resourceDynamicPropTypeSet.add(resourceDynamicProperty.getType());
            }
        }

        //根据appId和tenantIds获取resources
        List<Resource> resourceList1 = new ArrayList<>();
        List<Resource> resourceList2 = new ArrayList<>();
        List<Resource> resourceList3 = new ArrayList<>();
        List<Resource> resourceList4 = new ArrayList<>();

        ResourceCriteria resourceCriteria1 = new ResourceCriteria();
        resourceCriteria1.createCriteria().andTenantIdIn(tenantIdList).andApplicationIdEqualTo(APPLICATIONID_NULL);
        resourceList1 = resourceService.selectByCriteria(resourceCriteria1);

        ResourceCriteria resourceCriteria2 = new ResourceCriteria();
        resourceCriteria2.createCriteria().andTenantIdEqualTo(TENANTID__NULL).andApplicationIdEqualTo(applicationId);
        resourceList2 = resourceService.selectByCriteria(resourceCriteria2);

        ResourceCriteria resourceCriteria3 = new ResourceCriteria();
        resourceCriteria3.createCriteria().andTenantIdIn(tenantIdList).andApplicationIdEqualTo(applicationId);
        resourceList3 = resourceService.selectByCriteria(resourceCriteria3);

        resourceList4.addAll(resourceList1);
        resourceList4.addAll(resourceList2);
        resourceList4.addAll(resourceList3);

        List<Long> resourceIds = new ArrayList<>();
        for (Resource resource : resourceList4) {
            resourceIds.add(resource.getId());
        }

        if (resourceIds.size() == 0) {
            resourceIds.add(GarnetContants.NON_VALUE);
        }

        //通过权限的通配符、资源配置类型 查询相对应的resource
        List<Resource> resourceList = new ArrayList<>();
        for (Permission permission : permissionList) {
            String resourcePathWildcard = permission.getResourcePathWildcard();
            if (!StringUtils.isEmpty(resourcePathWildcard)) {
                ResourceCriteria resourceCriteria = new ResourceCriteria();
                tenantIdList.add(0);
                resourceCriteria.createCriteria().andPathLike(resourcePathWildcard).andIdIn(resourceIds).andTypeIn(resourceDynamicPropTypeList);

                List<Resource> resources = resourceService.selectByCriteria(resourceCriteria);
                resourceList.addAll(resources);
            }
        }
        if (CollectionUtils.isEmpty(resourceList)) {
            return loginMessage;
        }

        //添加action到resource;
//        List<List<ResourceDynamicProperty>> resourceDynamicPropertyList = new ArrayList<>();
        List<Resource> resourcesWithAction = new ArrayList<>();
        for (Resource resource : resourceList) {
            ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria4 = new ResourceDynamicPropertyCriteria();
            resourceDynamicPropertyCriteria4.createCriteria().andTypeEqualTo(resource.getType());
            List<ResourceDynamicProperty> resourceDynamicProperties = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria4);
//                resourceDynamicPropertyList.add(resourceDynamicProperties);
            resource.setActions(resourceDynamicProperties.get(0).getActions());
            resourcesWithAction.add(resource);
        }

        //对resource列表进行去重处理
        LoginMessage loginMessage1 = this.getResourcesDstinct(permissionList, resourcesWithAction);


//        loginMessage.setResourceList(loginMessage1.getResourceList());
        loginMessage.setResourceDynamicPropertyList(loginMessage1.getResourceDynamicPropertyList());
        loginMessage.setRefreshTokenResourceList(loginMessage1.getRefreshTokenResourceList());
//        loginMessage.setGetResourceListWithEdit(loginMessage1.getGetResourceListWithEdit());
//        loginMessage.setResourceListWithReadlyOnly(loginMessage1.getResourceListWithReadlyOnly());


        return loginMessage;
    }

    /**
     * 对resource列表进行去重
     * 1. 根据permission的action匹配资源
     * 2. 对resource去重
     * 3. 通过resource列表获取 ResourceDynamicProperty列表
     *
     * @param resourceList
     * @return
     */
    private LoginMessage getResourcesDstinct(List<Permission> permissionList, List<Resource> resourceList) {
        LoginMessage loginMessage = new LoginMessage();

        List<RefreshTokenResourceView> refreshTokenResourceViews = new ArrayList<>();

        //根据permission的action匹配资源
        List<Resource> resourceList1 = getResources(permissionList, resourceList);

        //对resource去重
        Set<Long> resourceSet = new HashSet<>();
        List<Resource> resourceList2 = new ArrayList<>();
        RefreshTokenResourceView refreshTokenResourceView;
        for (Resource resource : resourceList1) {
            Long resourceId = resource.getId();
            if (!resourceSet.contains(resourceId)) {
                resourceSet.add(resourceId);
                resourceList2.add(resource);
            }
        }

        for (Resource resource : resourceList2) {
            refreshTokenResourceView = new RefreshTokenResourceView();
            daoToViewCopier.copy(resource, refreshTokenResourceView, null);
            refreshTokenResourceView.setAction(resource.getActions());
            refreshTokenResourceViews.add(refreshTokenResourceView);
        }

        //通过resource列表获取 ResourceDynamicProperty列表
        List<List<ResourceDynamicProperty>> resourceDynamicPropertyList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(resourceList2) && resourceList2.size() > 0) {
            for (Resource resource : resourceList2) {
                ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria = new ResourceDynamicPropertyCriteria();
                resourceDynamicPropertyCriteria.createCriteria().andTypeEqualTo(resource.getType());
                List<ResourceDynamicProperty> resourceDynamicProperties = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria);
                resourceDynamicPropertyList.add(resourceDynamicProperties);
            }
        }

        loginMessage.setRefreshTokenResourceList(refreshTokenResourceViews);
//        loginMessage.setResourceList(resourceList2);
        loginMessage.setResourceDynamicPropertyList(resourceDynamicPropertyList);
        return loginMessage;
    }

    /**
     * 根据权限中的action匹配resources
     *
     * @param permissionList
     * @param resourceList
     * @return
     */
    private List<Resource> getResources(List<Permission> permissionList, List<Resource> resourceList) {
        List<Resource> resourceList1 = new ArrayList<>();
        for (Permission permission : permissionList) {
            String action = permission.getAction();
            String pattern = ".*" + action + ".*";
            for (Resource resource : resourceList) {
                String actions = resource.getActions();

                //add by ming 这句是添加的
                resource.setActions(action);

                if (actions.matches(pattern)) {
                    String[] actionList = actions.split(">");
                    //如果不是同级，返回action内容
                    if (actionList.length > 1) {
                        //处理要返回的action值
                        String action1 = this.getAction(action);
                        resource.setActions(action1);
                    }
                    resourceList1.add(resource);
                }
            }
        }
        return resourceList1;
    }

    /**
     * 处理要返回的action值
     *
     * @param action
     * @return
     */
    private String getAction(String action) {

        if ("read".equals(action)) {
            return action;
        } else {
            String action1 = "edit";
            return action1;
        }
    }

    private LoginMessage checkRefreshTokenData(TokenRefreshView tokenRefreshView) {
        LoginMessage loginMessage = new LoginMessage();
        String tokenWithAppCode = tokenRefreshView.getRefreshToken();
        String[] tokenParams = tokenWithAppCode.split("#");
        System.out.println(tokenParams.length);
        String token = tokenParams[0];
        String appCode = tokenParams[1];
        String userName = tokenParams[2];
        String createTime = tokenParams[3];


        if (StringUtils.isEmpty(userName) || !userName.equals(tokenRefreshView.getUserName())) {
            loginMessage.setMessage("token错误");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //如果要跳转的appCode不存在，返回错误
        if (StringUtils.isEmpty(tokenRefreshView.getAppCode())) {
            loginMessage.setMessage("appCode不能为空");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //根据username 查询密码，如果查不到则账号有误
        User user = userService.getUserByUserName(userName);
        if (ObjectUtils.isEmpty(user)) {
            loginMessage.setMessage("token错误");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        UserCredential userCredential = userCredentialService.getCredentialByUserName(userName);
        if (ObjectUtils.isEmpty(userCredential)) {
            loginMessage.setMessage(MessageDescription.LOGIN_USERNAME_NOT_EXIST);
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        String password = userCredential.getCredential();
        Map<String, Claim> tokenPlayload = null;
        try {
            //用密码解密token
            tokenPlayload = JwtToken.verifyToken(token, password);
        } catch (Exception e) {
            loginMessage.setMessage("账号错误,请输入正确账号");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //初始登录的应用 所在的组
        String routerGroupName = routerGroupService.getGroupNameByAppCode(appCode);
        //要跳转的应用 所在的组
        String routerGroupNameGo = routerGroupService.getGroupNameByAppCode(tokenRefreshView.getAppCode());
        //应用没有被添加进应用组或不在同一个应用组，无法访问
        if (StringUtils.isEmpty(routerGroupName) || StringUtils.isEmpty(routerGroupNameGo) || !routerGroupName.equals(routerGroupNameGo)) {
            loginMessage.setMessage("应用不存在或不在同一个应用组");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(403);
            return loginMessage;
        }
        return loginMessage;
    }


    @LogRequired(module = "token模块", method = "Garnet刷新token")
    @Override
    public LoginMessage garnetRefreshToken(LoginView loginView) throws Exception {
        //能通过拦截器，说明token是正确且有效的,故不再验证token是否正确
        LoginMessage loginMessage = new LoginMessage();

        //取出token中携带的信息
        String tokenWithAppCode = loginView.getToken();

        if (StringUtils.isEmpty(tokenWithAppCode)) {
            throw new RuntimeException("token不能为空");
        }

        String[] tokenParams = tokenWithAppCode.split("#");
        String token = tokenParams[0];
        String appCode = tokenParams[1];
        String userName = tokenParams[2];
        String createTime = tokenParams[3];

        //如果要跳转的appCode不存在，返回错误
        if (StringUtils.isEmpty(loginView.getAppCode())) {
            loginMessage.setMessage("appCode不能为空");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //根据username 查询密码，如果查不到则账号有误
        UserCredential userCredential = userCredentialService.getCredentialByUserName(userName);
        if (ObjectUtils.isEmpty(userCredential)) {
            loginMessage.setMessage("用户不存在");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        String password = userCredential.getCredential();
        Map<String, Claim> tokenPlayload = null;
        try {
            //用密码解密token
            tokenPlayload = JwtToken.verifyToken(token, password);
        } catch (Exception e) {
            loginMessage.setMessage("账号或密码错误");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //初始登录的应用 所在的组
        String routerGroupName = routerGroupService.getGroupNameByAppCode(appCode);
        //应用没有被添加进应用组或不在同一个应用组，无法访问
        if (StringUtils.isEmpty(routerGroupName)) {
            loginMessage.setMessage("没有权限");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(403);
            return loginMessage;
        }

        //更新token
        Long currentTime = System.currentTimeMillis();
        String tokenNew = JwtToken.createToken(userName, password, appCode, currentTime);
        String accessTokenReturn = tokenNew + "#" + appCode + "#" + userName + "#" + currentTime + STRING_ACCESSTOKEN;
        String refreshTokenReturn = tokenNew + "#" + appCode + "#" + userName + "#" + currentTime + STRING_REFRESHTOKEN;

        //根据userName拿出token并更新到数据库
        TokenCriteria tokenCriteria = new TokenCriteria();
        tokenCriteria.createCriteria().andUserNameEqualTo(userName);
        Token token1 = tokenService.selectSingleByCriteria(tokenCriteria);
        token1.setToken(tokenNew);
        token1.setModifiedTime(currentTime);
        token1.setExpireTime(currentTime + GarnetContants.TOKEN_EXPIRED_TIME);
        tokenService.updateByPrimaryKeySelective(token1);

        loginMessage.setMessage("token刷新成功");
        loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_SUCCESS);
        loginMessage.setCode(201);
        loginMessage.setAccessToken(accessTokenReturn);
        loginMessage.setRefreshToken(refreshTokenReturn);

        return loginMessage;
    }

    @LogRequired(module = "登录模块", method = "Garnet登录")
    @Override
    public LoginMessage garLogin(GarLoginView garLoginView) throws Exception {
        UserCriteria userCriteria = new UserCriteria();
        LoginMessage loginMessage = new LoginMessage();
        userCriteria.createCriteria().andUserNameEqualTo(garLoginView.getUserName()).andStatusEqualTo(1);
        User user = this.selectSingleByCriteria(userCriteria);

        if (StringUtils.isEmpty(garLoginView.getUserName()) || StringUtils.isEmpty(garLoginView.getPassword())) {
            loginMessage.setMessage("账号或密码为空");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        if (ObjectUtils.isEmpty(user)) {
            loginMessage.setMessage("用户名或者密码错误");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //获取密码,验证密码是否正确
        UserCredentialCriteria userCredentialCriteria = new UserCredentialCriteria();
        userCredentialCriteria.createCriteria().andUserIdEqualTo(user.getId());
        UserCredential userCredential = userCredentialService.selectSingleByCriteria(userCredentialCriteria);
        if (!userCredential.getCredential().equals(garLoginView.getPassword())) {
            loginMessage.setMessage("用户名或者密码错误");
            loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_FALSE);
            loginMessage.setCode(401);
            return loginMessage;
        }

        //生成token返回
        String token = JwtToken.createToken(user.getUserName(), userCredential.getCredential(), garLoginView.getAppCode(), System.currentTimeMillis());
        loginMessage.setMessage("登录成功");
        loginMessage.setCode(201);
        loginMessage.setUser(user);
        loginMessage.setLoginStatus(LOGINMESSAGE_STATUS_SUCCESS);
        //拼接返回前端的token
        String accessTokenReturn = token + "#" + garLoginView.getAppCode() + "#" + garLoginView.getUserName() + "#" + System.currentTimeMillis() + "#access_token";
        String refreshTokenReturn = token + "#" + garLoginView.getAppCode() + "#" + garLoginView.getUserName() + "#" + System.currentTimeMillis() + "#refresh_token";
        loginMessage.setAccessToken(accessTokenReturn);
        loginMessage.setRefreshToken(refreshTokenReturn);

        TokenCriteria tokenCriteria = new TokenCriteria();
        tokenCriteria.createCriteria().andUserNameEqualTo(garLoginView.getUserName());
        Token tokenOld = tokenService.selectSingleByCriteria(tokenCriteria);

        //如果是第一次登录，将token插入数据库
        if (ObjectUtils.isEmpty(tokenOld)) {
            Token token1 = new Token();
            token1.setCreatedTime(System.currentTimeMillis());
            token1.setModifiedTime(System.currentTimeMillis());
            Long expiredTime = token1.getCreatedTime() + 30 * 60000L;
            token1.setExpireTime(expiredTime);
            token1.setId(IdGeneratorUtil.generateId());
            token1.setUserName(garLoginView.getUserName());
            token1.setToken(token);
            tokenService.insertSelective(token1);
        } else {
            //不是第一次登录，更新token
            tokenOld.setToken(token);
            tokenOld.setModifiedTime(System.currentTimeMillis());
            Long expiredTime = tokenOld.getModifiedTime() + GarnetContants.TOKEN_EXPIRED_TIME;
            tokenOld.setExpireTime(expiredTime);
            tokenOld.setToken(token);
            tokenService.updateByPrimaryKeySelective(tokenOld);
        }

        return loginMessage;
    }

    @Override
    public ReturnTenantIdView getTenantIdsByUserId(Long userId) {
        //根据userId 查 tenantId列表
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andUserIdEqualTo(userId);
        List<UserTenant> userTenants = userTenantService.selectByCriteria(userTenantCriteria);
        List<Long> tenantIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userTenants) && userTenants.size() > 0) {
            for (UserTenant userTenant : userTenants) {
                //用户绑定的所有tenantIds
                tenantIds.add(userTenant.getTenantId());
            }
        }

        if (tenantIds.size() == 0) {
            tenantIds.add(GarnetContants.NON_VALUE);
        }

        //获取权限列表
        List<Permission> permissions = getPermissions(userId);

        List<Resource> resourceList = new ArrayList<>();
        for (Permission permission : permissions) {
            ResourceCriteria resourceCriteria = new ResourceCriteria();
            resourceCriteria.createCriteria().andTenantIdIn(tenantIds).andPathLike(permission.getResourcePathWildcard());
            resourceList.addAll(resourceService.selectByCriteria(resourceCriteria));
        }

        //判断是否拥有超级权限
        boolean flag = false;
        for (Resource resource : resourceList) {
            if (GarnetContants.RESOURCE_PERMISSION.equals(resource.getVarchar00())) {
                flag = true;
            }
        }

        ReturnTenantIdView returnTenantIdView = new ReturnTenantIdView();
        returnTenantIdView.setSuperAdmin(flag);

        if (flag) {
            TenantCriteria tenantCriteria = new TenantCriteria();
            tenantCriteria.createCriteria().andStatusEqualTo(1);
            List<Tenant> tenants = tenantService.selectByCriteria(tenantCriteria);
            List<Long> teantIdList = new ArrayList<>();
            for (Tenant tenant : tenants) {
                teantIdList.add(tenant.getId());
            }

            //判断是否是garnet用户
            teantIdList = commonService.dealTenantIdsIfGarnet(userId, teantIdList);
            returnTenantIdView.setTenantIds(teantIdList);
            return returnTenantIdView;
        } else {
            //没有superAll权限，去掉garnet租户id
            List<Long> tempIds = new ArrayList<>();
            for (Long tenantId : tenantIds) {
                if(tenantId.longValue() != GarnetContants.GARNET_TENANT_ID.longValue()){
                    tempIds.add(tenantId);
                }
            }
            returnTenantIdView.setTenantIds(tempIds);
            return returnTenantIdView;
        }

    }

    private List<Permission> getPermissions(Long userId) {
        //change by ming
        GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
        groupUserCriteria.createCriteria().andUserIdEqualTo(userId);
        List<GroupUser> groupUsers = groupUserService.selectByCriteria(groupUserCriteria);

        GroupRoleCriteria groupRoleCriteria = new GroupRoleCriteria();

        List<Long> groupIds = new ArrayList<>();
        for (GroupUser groupUser : groupUsers) {
            groupIds.add(groupUser.getGroupId());
        }

        if (groupIds.size() == 0) {
            groupIds.add(GarnetContants.NON_VALUE);
        }

        groupRoleCriteria.createCriteria().andGroupIdIn(groupIds);
        List<GroupRole> groupRoles = groupRoleService.selectByCriteria(groupRoleCriteria);
        List<Long> roleIds = new ArrayList<>();
        for (GroupRole groupRole : groupRoles) {
            roleIds.add(groupRole.getRoleId());
        }

        if (roleIds.size() == 0) {
            roleIds.add(GarnetContants.NON_VALUE);
        }

        RolePermissionCriteria rolePermissionCriteria = new RolePermissionCriteria();
        rolePermissionCriteria.createCriteria().andRoleIdIn(roleIds);

        List<RolePermission> rolePermissions = rolePermissionService.selectByCriteria(rolePermissionCriteria);

        List<Long> permissionIds = new ArrayList<>();
        for (RolePermission rolePermission : rolePermissions) {
            permissionIds.add(rolePermission.getPermissionId());
        }

        if (permissionIds.size() == 0) {
            permissionIds.add(GarnetContants.NON_VALUE);
        }

        PermissionCriteria permissionCriteria = new PermissionCriteria();
        permissionCriteria.createCriteria().andIdIn(permissionIds).andStatusEqualTo(1);

        //====================
        return permissionService.selectByCriteria(permissionCriteria);
    }

    @LogRequired(module = "用户管理模块", method = "修改密码")
    @Override
    public void updateUserPassword(UserCredentialView userCredentialView) {
        if (ObjectUtils.isEmpty(userCredentialView) || ObjectUtils.isEmpty(userCredentialView.getUserId()) ||
            StringUtils.isEmpty(userCredentialView.getPassword()) || StringUtils.isEmpty(userCredentialView.getNewPassword())) {
            throw new RuntimeException("请输入正确参数");
        }

        UserCredentialCriteria userCredentialCriteria = new UserCredentialCriteria();
        userCredentialCriteria.createCriteria().andUserIdEqualTo(userCredentialView.getUserId());
        UserCredential userCredential = userCredentialService.selectSingleByCriteria(userCredentialCriteria);
        if (!ObjectUtils.isEmpty(userCredential)) {
            if (userCredentialView.getPassword().equals(userCredential.getCredential())) {

                if (userCredentialView.getNewPassword().equals(userCredential.getCredential())) {
                    throw new RuntimeException("新密码不能与旧密码相同");
                }

                userCredential.setModifiedTime(System.currentTimeMillis());
                userCredential.setCredential(userCredentialView.getNewPassword());
                userCredentialService.updateByPrimaryKeySelective(userCredential);
            } else {
                throw new RuntimeException("初始密码不正确");
            }
        }
    }

    @Override
    public List<User> queryUsers() {
        UserCriteria userCriteria = new UserCriteria();
        userCriteria.createCriteria().andStatusEqualTo(1);
        List<User> userList = this.selectByCriteria(userCriteria);
        return userList;
    }

    @Override
    public List<User> queryUserByApplicationId(UserParm userParm) {
        Long applicationId = userParm.getApplicationId();
        Long tenantId = userParm.getTenantId();

        ApplicationTenantCriteria applicationTenantCriteria = new ApplicationTenantCriteria();
        ApplicationTenantCriteria.Criteria criteria = applicationTenantCriteria.createCriteria();

        if (!ObjectUtils.isEmpty(tenantId) && tenantId.longValue() != 0) {
            criteria.andTenantIdEqualTo(tenantId);
        }

        if (!ObjectUtils.isEmpty(applicationId) && applicationId.longValue() != 0) {
            criteria.andApplicationIdEqualTo(applicationId);
        }

        List<ApplicationTenant> applicationTenantList = applicationTenantService.selectByCriteria(applicationTenantCriteria);

        if (CollectionUtils.isEmpty(applicationTenantList) || applicationTenantList.size() == 0) {
            return new ArrayList<>();
        }

        List<Long> tenantIdList = new ArrayList<>();
        for (ApplicationTenant applicationTenant : applicationTenantList) {
            tenantIdList.add(applicationTenant.getTenantId());
        }

        List<Long> userIdList = getUserIdsByTenantIds(tenantIdList);

        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andTenantIdIn(tenantIdList).andUserIdEqualTo(GarnetContants.GARNET_USER_ID);
        List<UserTenant> userTenants = userTenantService.selectByCriteria(userTenantCriteria);
        if (!CollectionUtils.isEmpty(userTenants) && userTenants.size() > 0) {
            userIdList.add(GarnetContants.GARNET_USER_ID);
        }

        List<User> userList = new ArrayList<>();
        User user;
        for (Long userId : userIdList) {
            user = this.selectByPrimaryKey(userId);
            userList.add(user);
        }

        return userList;
    }

    @Override
    public List<User> queryUserByParams(UserParm userParm) {
        Long applicationId = userParm.getApplicationId();
        Long tenantId = userParm.getTenantId();

        List<User> userList = new ArrayList<>();
        if ((!ObjectUtils.isEmpty(tenantId) && tenantId.longValue() != 0) && (ObjectUtils.isEmpty(applicationId) || applicationId.longValue() == 0)) {
            //租户级
            userList = this.queryUserByTenantId(userParm);
        } else if ((!ObjectUtils.isEmpty(applicationId) && applicationId.longValue() != 0) && (ObjectUtils.isEmpty(tenantId) || tenantId.longValue() == 0)) {
            //应用级
            userList = this.queryUserByApplicationId(userParm);
        } else {
            //应用+租户
            List<User> userList1 = this.queryUserByTenantId(userParm);
            List<User> userList2 = this.queryUserByApplicationId(userParm);
            userList.addAll(userList1);
            userList.addAll(userList2);
        }

        //去重
        List<User> users = new ArrayList<>();
        Set<Long> userIdSet = new HashSet<>();
        for (User user : userList) {
            // user 可能为null! Jaffray 2019.07.17
            if (user != null && !userIdSet.contains(user.getId())) {
                users.add(user);
                userIdSet.add(user.getId());
            }
        }

        return users;
    }

    @Override
    public Boolean validateUserInfo(String userName, String password) {

        UserCredential userCredential = userCredentialService.getCredentialByUserName(userName);
        if (ObjectUtils.isEmpty(userCredential)) {
            return false;
        }

        if (!password.equals(userCredential.getCredential())) {
            return false;
        }

        return true;
    }

    @LogRequired(module = "用户管理模块", method = "查询用户列表")
    @Override
    public PageUtil getUsersByParams(UserParm userParm) {
        Long userId = userParm.getUserId();
        UserCriteria userCriteria = new UserCriteria();
        userCriteria.setOrderByClause(GarnetContants.ORDER_BY_CREATED_TIME);
        UserCriteria.Criteria criteria = userCriteria.createCriteria();
        criteria.andStatusEqualTo(1);

        if (!StringUtils.isEmpty(userParm.getSearchName())) {
            criteria.andUserNameLike("%" + userParm.getSearchName() + "%");
        }

        String level = resourceService.getLevelByUserIdAndPath(userId, GarnetContants.GARNET_DATA_USERMANAGE_QUERY_PATH);
        List<User> userList = new ArrayList<>();
        if (Integer.valueOf(level) == 1) {
            userList = this.selectByCriteria(userCriteria);
        } else if (Integer.valueOf(level) == 2) {
            //非Garnet数据
            List<Long> tenantIdList = commonService.getTenantIdsNotGarnet(userId);
            UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
            userTenantCriteria.createCriteria().andTenantIdIn(tenantIdList);
            List<UserTenant> userTenantList = userTenantService.selectByCriteria(userTenantCriteria);
            List<Long> userIdList = new ArrayList<>();
            for (UserTenant userTenant : userTenantList) {
                userIdList.add(userTenant.getUserId());
            }
            if (userIdList.size() == 0) {
                userIdList.add(GarnetContants.NON_VALUE);
            }
            criteria.andIdIn(userIdList);
            userList = this.selectByCriteria(userCriteria);
        } else if (Integer.valueOf(level) == 3) {
            //本用户为租户管理员的租户所关联用户,且不能为平台管理员的用户
            List<Tenant> tenantList = tenantService.getTenantManageListByUserId(userId);
            List<Long> tenantIdList = new ArrayList<>();
            for (Tenant tenant : tenantList) {
                tenantIdList.add(tenant.getId());
            }
            if (CollectionUtils.isEmpty(tenantIdList)) {
                tenantIdList.add(GarnetContants.NON_VALUE);
            }

            UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
            userTenantCriteria.createCriteria().andTenantIdIn(tenantIdList);
            List<UserTenant> userTenantList = userTenantService.selectByCriteria(userTenantCriteria);
            List<Long> userIdList = new ArrayList<>();
            for (UserTenant userTenant : userTenantList) {
                userIdList.add(userTenant.getUserId());
            }

            //平台管理员组中关联的用户
            GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
            groupUserCriteria.createCriteria().andGroupIdEqualTo(GarnetContants.GARNET_GROUP_ID);
            List<GroupUser> groupUserList = groupUserService.selectByCriteria(groupUserCriteria);
            List<Long> userIds = new ArrayList<>();
            for (GroupUser groupUser : groupUserList) {
                userIds.add(groupUser.getUserId());
            }

            //去掉平台管理员组中关联的用户
            userIdList.removeAll(userIds);

            if (userIdList.size() == 0) {
                userIdList.add(GarnetContants.NON_VALUE);
            }
            criteria.andIdIn(userIdList);
            userList = this.selectByCriteria(userCriteria);
        } else if (Integer.valueOf(level) == 4) {
            //本用户
            User user = this.selectByPrimaryKey(userId);
            userList.add(user);
        }

        PageUtil pageUtil = new PageUtil(userList, userList.size(), userParm.getPageSize(), userParm.getPageNumber());
        return pageUtil;
    }

    /**
     * 关联选择了 默认关联所有用户 的租户
     */
    private void relatedAllUserTenant(UserView userView) {

//        Long userId = userView.getUser().getId();
//        Long loginUserId = userView.getLoginUserId();
//
//        List<Resource> resourceList = resourceService.getResourcesByUserId(loginUserId);
//        boolean isRelateAllUsers = false;
//        for (Resource resource : resourceList) {
//            if (resource.getId().longValue() == GarnetContants.GARNET_RESOURCE_USERRELATION_ID.longValue()) {
//                isRelateAllUsers = true;
//            }
//        }
//
//        if (isRelateAllUsers) {
//            List<Tenant> tenantList = this.getTenantListRelateAllUsers();
//            Long userTenantId = IdGeneratorUtil.generateId();
//            UserTenantCriteria userTenantCriteria;
//            for (Tenant tenant : tenantList) {
//                //删除关联
//                userTenantCriteria = new UserTenantCriteria();
//                userTenantCriteria.createCriteria().andTenantIdEqualTo(tenant.getId()).andUserIdEqualTo(userId);
//                userTenantService.deleteByCriteria(userTenantCriteria);
//
//                UserTenant userTenant = new UserTenant();
//                userTenant.setUserId(userId);
//                userTenant.setTenantId(tenant.getId());
//                userTenant.setId(userTenantId);
//                userTenantService.insertSelective(userTenant);
//                userTenantId = userTenantId + 1L;
//            }
//        }

        Long userId = userView.getUser().getId();
        Long loginUserId = userView.getLoginUserId();
        TenantCriteria tenantCriteria = new TenantCriteria();
        tenantCriteria.createCriteria().andStatusEqualTo(1).andRelatedAllUsersEqualTo(TENANT_RELATED_ALLUSER_Y);
        List<Tenant> tenants = tenantService.selectByCriteria(tenantCriteria);

        Long userTenantId = IdGeneratorUtil.generateId();
        UserTenantCriteria userTenantCriteria;
        for (Tenant tenant : tenants) {
            //删除关联
            userTenantCriteria = new UserTenantCriteria();
            userTenantCriteria.createCriteria().andTenantIdEqualTo(tenant.getId()).andUserIdEqualTo(userId);
            userTenantService.deleteByCriteria(userTenantCriteria);

            UserTenant userTenant = new UserTenant();
            userTenant.setUserId(userId);
            userTenant.setTenantId(tenant.getId());
            userTenant.setId(userTenantId);
            userTenantService.insertSelective(userTenant);
            userTenantId = userTenantId + 1L;
        }

        Log log = new Log();
        log.setMessage("用户管理模块");
        log.setOperation("用户绑定租户");
        commonService.insertLog(log);
    }


    /**
     * 获取所有选择了 默认关联所有用户 的租户列表
     *
     * @return
     */
    private List<Tenant> getTenantListRelateAllUsers() {
        UserCriteria userCriteria = new UserCriteria();
        userCriteria.createCriteria().andStatusEqualTo(1);
        List<User> userList = this.selectByCriteria(userCriteria);

        //拥有 关联所有用户 资源的用户
        List<User> userRelationList = new ArrayList<>();
        for (User user : userList) {
            List<Resource> resourceList = resourceService.getResourcesByUserId(user.getId());
            for (Resource resource : resourceList) {
                if (resource.getId().longValue() == GarnetContants.GARNET_RESOURCE_USERRELATION_ID.longValue()) {
                    userRelationList.add(user);
                }
            }
        }

        List<Long> userIdList = new ArrayList<>();
        for (User user : userRelationList) {
            userIdList.add(user.getId());
        }

        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andUserIdIn(userIdList);
        List<UserTenant> userTenantList = userTenantService.selectByCriteria(userTenantCriteria);
        List<Long> tenantIdList = new ArrayList<>();
        for (UserTenant userTenant : userTenantList) {
            tenantIdList.add(userTenant.getTenantId());
        }

        TenantCriteria tenantCriteria = new TenantCriteria();
        tenantCriteria.createCriteria().andIdIn(tenantIdList).andStatusEqualTo(1);
        List<Tenant> tenantList = tenantService.selectByCriteria(tenantCriteria);

        return tenantList;
    }
}