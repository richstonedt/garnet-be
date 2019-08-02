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

import com.richstonedt.garnet.common.contants.GarnetContants;
import com.richstonedt.garnet.common.utils.IdGeneratorUtil;
import com.richstonedt.garnet.model.*;
import com.richstonedt.garnet.model.criteria.GroupCriteria;
import com.richstonedt.garnet.model.criteria.GroupUserCriteria;
import com.richstonedt.garnet.model.criteria.UserCriteria;
import com.richstonedt.garnet.model.criteria.UserTenantCriteria;
import com.richstonedt.garnet.model.view.UserView;
import com.richstonedt.garnet.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private UserTenantService userTenantService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private LogService logService;

    @Autowired
    private ResourceService resourceService;


    @Override
    public List<Long> dealTenantIdsIfGarnet(Long userId, List<Long> tenantIds) {

        boolean isBelongToGarnet = this.superAdminBelongGarnet(userId);

        //如果不是garnet用户，去掉Garnet租户
        if (!isBelongToGarnet) {
            List<Long> tempIds = new ArrayList<>();
            for (Long tenantId : tenantIds) {
                if(tenantId.longValue() != GarnetContants.GARNET_TENANT_ID.longValue()){
                    tempIds.add(tenantId);
                }
            }
            return tempIds;
        }
        return tenantIds;

//        UserView userView = userService.getUserById(userId);
//
//        if (("N").equals(userView.getUser().getBelongToGarnet())) {
//            List<Long> tempIds = new ArrayList<>();
//            for (Long tenantId : tenantIds) {
//                if(tenantId.longValue() != GarnetContants.GARNET_TENANT_ID.longValue()){
//                    tempIds.add(tenantId);
//                }
//            }
//            return tempIds;
//        }
//        return tenantIds;

    }

    @Override
    public List<Long> dealGroupIdsIfGarnet(Long userId, List<Long> groupIds) {

        //是否是garnet用户
        boolean b = this.superAdminBelongGarnet(userId);
        List<Long> returnGroupIds = new ArrayList<>();

        //如果不是Garnet用户
        if (!b) {
            GroupCriteria groupCriteria = new GroupCriteria();
            groupCriteria.createCriteria().andIdIn(groupIds);
            List<Group> groups = groupService.selectByCriteria(groupCriteria);

            for(Group group : groups){
                if(group.getTenantId().longValue() != GarnetContants.GARNET_TENANT_ID.longValue()){
                    returnGroupIds.add(group.getId());
                }
            }
            return returnGroupIds;
        } else {
            //如果是，原封返回
            return groupIds;
        }
    }

    @Override
    public boolean insertLog(Log log){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String cookie = request.getHeader("Cookie");
        if (StringUtils.isEmpty(cookie)) {
            return false;
        }
        String userName = this.getCookieValue(cookie, "userName");
        if(userName == null || userName == "") {
            userName = (String)request.getSession().getAttribute("userName");
        }

        String ip = null;
        try {
//            ip = InetAddress.getLocalHost().getHostAddress();
            ip = getClientIpAddr(request);
        } catch (Exception e) {
            return false;
        }

        Long time = System.currentTimeMillis();

        log.setId(IdGeneratorUtil.generateId());
        log.setUserName(userName);
        log.setCreatedTime(time);
        log.setModifiedTime(time);
        log.setIp(ip);

        logService.insertSelective(log);

        return true;
    }

    @Override
    public List<Long> getTenantIdsNotGarnet(Long userId) {
        UserTenantCriteria userTenantCriteria = new UserTenantCriteria();
        userTenantCriteria.createCriteria().andUserIdEqualTo(userId);
        List<UserTenant> userTenantList = userTenantService.selectByCriteria(userTenantCriteria);
        List<Long> tenantIdList = new ArrayList<>();
        for (UserTenant userTenant : userTenantList) {
            if (userTenant.getTenantId().longValue() != GarnetContants.GARNET_APPLICATION_ID.longValue()) {
                tenantIdList.add(userTenant.getTenantId());
            }
        }

        if (tenantIdList.size() == 0) {
            tenantIdList.add(GarnetContants.NON_VALUE);
        }

        return tenantIdList;
    }

    @Override
    public boolean superAdminBelongGarnet(Long userId) {

        List<Resource> resources = resourceService.getResourcesByUserId(userId);
        if (!CollectionUtils.isEmpty(resources) && resources.size() > 0) {
            for (Resource resource : resources) {
                String type = resource.getType();
                if (GarnetContants.GARNET_RESOURCE_DYNAMICPROPERTY_BELONGTOGARNET.equals(type)) {
                    return true;
                }
            }
        }

        return false;

//        User user = userService.selectByPrimaryKey(userId);
//        if (!ObjectUtils.isEmpty(user) && "Y".equals(user.getBelongToGarnet())) {
//            return true;
//        }
//        return false;
    }

    /**
     * 获取Cookie中存的值
     * @param cookie
     * @param name
     * @return
     */
    private String getCookieValue(String cookie, String name) {

        String n = name + "=";

        String[] c = cookie.split(";");

        for (String s : cookie.split(";")) {
            String s1 = s.trim();
            if (s1.indexOf(n) == 0) {
                return s1.substring(n.length(), s1.length());
            }
        }

        return "";
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     * add by Jaffray 2019.07.29
     * @return ip
     */
    private String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        //System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            //System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            //System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            //System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            //System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            //System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            //System.out.println("getRemoteAddr ip: " + ip);
        }
        //System.out.println("获取客户端ip: " + ip);
        return ip;
    }

}
