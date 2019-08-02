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
package com.richstonedt.garnet.controller;

import com.google.code.kaptcha.Producer;
import com.richstonedt.garnet.common.utils.GarnetRsUtil;
import com.richstonedt.garnet.common.utils.PageUtil;
import com.richstonedt.garnet.exception.GarnetServiceExceptionUtils;
import com.richstonedt.garnet.interceptory.LogRequired;
import com.richstonedt.garnet.interceptory.LoginMessage;
import com.richstonedt.garnet.interceptory.LoginRequired;
import com.richstonedt.garnet.model.User;
import com.richstonedt.garnet.model.message.*;
import com.richstonedt.garnet.model.parm.UserParm;
import com.richstonedt.garnet.model.view.*;
import com.richstonedt.garnet.service.UserService;
import com.richstonedt.garnet.strategy.LoginRecorder;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b><code>UserController</code></b>
 * <p/>
 * User的具体实现
 * <p/>
 * <b>Creation Time:</b> Wed Feb 28 18:34:04 CST 2018.
 *
 * @author ming
 * @version 1.0.0
 */
@Api(value = "[Garnet]用户接口")
@RestController
@RequestMapping(value = "/api/v1.0")
public class UserController {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    /**
     * The constant kaptchaMap.
     */
    private static final Map<String, String> kaptchaMap = new ConcurrentHashMap<>();

    /**
     * The constant loginSessionMap.
     */
    private static final Map<String, Object> loginSessionMap = new ConcurrentHashMap<>();

    private static final Map<String, String> loginTokenMap = new ConcurrentHashMap<>();


    private static final String LOGINMESSAGE_STATUS_SUCCESS = "success";

    private static final String ERROR = "SYSTEM ERROR";

    /**
     * The service.
     */
    @Autowired
    private UserService userService;

    @Autowired
    private Producer producer;

    /**
     * Create user response entity.
     *
     * @param userView  the user view
     * @param ucBuilder the uc builder
     * @return the response entity
     */
    @LoginRequired
    @ApiOperation(value = "[Garnet]创建用户", notes = "创建一个用户")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation", responseHeaders = @ResponseHeader(name = "location", description = "URL of new created resource", response = String.class)),
            @ApiResponse(code = 409, message = "conflict"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createUser(
            @ApiParam(value = "用户", required = true) @RequestBody UserView userView,
            UriComponentsBuilder ucBuilder) {
        String error = "Failed to add entity! " + MessageDescription.OPERATION_INSERT_FAILURE;

        try {
            // 保存实体
            Long id = userService.insertUser(userView);
            // 获取刚刚保存的实体
            User user = userService.selectByPrimaryKey(id);

            UserView userView1 = new UserView();

            userView1.setUser(user);

            // 设置http的headers
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/api/v1/users/{id}")
                    .buildAndExpand(id).toUri());
            // 封装返回信息
            GarnetMessage<UserView> garnetMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_INSERT_SUCCESS, userView1);
            return new ResponseEntity<>(garnetMessage, headers, HttpStatus.CREATED);
        } catch (Throwable t) {
            error = t.getMessage();
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    /**
     * Delete user response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @LoginRequired
    @ApiOperation(value = "[Garnet]删除用户", notes = "通过id删除用户")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteUser(
            @ApiParam(value = "用户id", required = true) @PathVariable(value = "id") long id) {
        try {
            User user = new User();
            user.setId(id);
            UserView userView = new UserView();
            userView.setUser(user);
            userService.deleteUser(userView);
            // 封装返回信息
            GarnetMessage<UserView> garnetMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_DELETE_SUCCESS, null);
            return new ResponseEntity<>(garnetMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to delete entity! " + MessageDescription.OPERATION_DELETE_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    /**
     * Delete users response entity.
     *
     * @param loginUserId the login user id
     * @param ids         the ids
     * @return the response entity
     */
    @LoginRequired
    @ApiOperation(value = "[Garnet]删除用户", notes = "批量删除用户")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteUsers(
            @ApiParam(value = "登录用户id", required = true) @RequestParam(value = "loginUserId") Long loginUserId,
            @ApiParam(value = "ids,用‘,’隔开", required = true) @RequestParam(value = "ids") String ids) {
        try {
            for (String id : ids.split(",")) {
                User user = new User();
                user.setId(Long.parseLong(id));
                userService.updateStatusById(user, loginUserId);
            }

            // 封装返回信息
            GarnetMessage<UserView> garnetMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_DELETE_SUCCESS, null);
            return new ResponseEntity<>(garnetMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to delete entities! " + MessageDescription.OPERATION_DELETE_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    @LoginRequired
    @ApiOperation(value = "[Garnet]更新用户", notes = "更新用户信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful"),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 409, message = "conflict"),
            @ApiResponse(code = 500, message = "internal Server Error")})
    @RequestMapping(value = "/users", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateUsers(
            @ApiParam(value = "用户信息", required = true) @RequestBody UserView userView) {
        try {

            userService.updateUser(userView);
            // 封装返回信息
            GarnetMessage<UserView> garnetMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_UPDATE_SUCCESS, userView);
            return new ResponseEntity<>(garnetMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to update entity! " + MessageDescription.OPERATION_UPDATE_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    /**
     * Update password response entity.
     *
     * @param userCredentialView the user credential view
     * @return the response entity
     */
    @LoginRequired
    @ApiOperation(value = "[Garnet]更新密码", notes = "更新用户密码")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful"),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 409, message = "conflict"),
            @ApiResponse(code = 500, message = "internal Server Error")})
    @RequestMapping(value = "/users/password", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updatePassword(
            @ApiParam(value = "用户信息", required = true) @RequestBody UserCredentialView userCredentialView) {
        try {
            userService.updateUserPassword(userCredentialView);
            // 封装返回信息
            GarnetMessage<UserCredentialView> garnetMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_UPDATE_SUCCESS, null);
            return new ResponseEntity<>(garnetMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to update entity! " + MessageDescription.OPERATION_UPDATE_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    @LoginRequired
    @ApiOperation(value = "[Garnet]获取单个用户", notes = "通过id获取用户")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getUser(
            @ApiParam(value = "用户id", required = true) @PathVariable(value = "id") long id,
            @ApiParam(value = "登录用户Id", required = false) @RequestParam(value = "loginUserId", defaultValue = "0", required = false) Long loginUserId) {
        try {
            UserParm userParm = new UserParm();
            User user = new User();
            user.setId(id);
            userParm.setUser(user);
            userParm.setLoginUserId(loginUserId);
            UserView userView = userService.getUserById(userParm);
            /*if (userView.getUser() != null) {
                userView.getUser().setModifiedTime(null);
                userView.getUser().setCreatedTime(null);
                userView.getUser().setMobileNumber(null);
                userView.getUser().setEmail(null);
            }*/
            // 封装返回信息
            GarnetMessage<UserView> garnetMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_QUERY_SUCCESS, userView);
            return new ResponseEntity<>(garnetMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to get entity!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    /**
     * Gets users.
     *
     * @param userId     the user id
     * @param tenantId   the tenant id
     * @param searchName the search name
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return the users
     */
    @LogRequired(module = "用户模块", method = "获取用户列表")
    @LoginRequired
    @ApiOperation(value = "[Garnet]获取用户列表", notes = "通过查询条件获取用户列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getUsers(
            @ApiParam(value = "用户名Id", defaultValue = "", required = false) @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "租户Id", defaultValue = "", required = false) @RequestParam(value = "tenantId", defaultValue = "", required = false) Long tenantId,
            @ApiParam(value = "搜索", defaultValue = "", required = false) @RequestParam(value = "searchName", defaultValue = "", required = false) String searchName,
            @ApiParam(value = "页数", defaultValue = "0", required = false) @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @ApiParam(value = "每页加载量", defaultValue = "10", required = false) @RequestParam(value = "limit", defaultValue = "10", required = false) int pageSize) {
        try {

            UserParm userParm = new UserParm();
            userParm.setUserId(userId);
            userParm.setTenantId(tenantId);
            userParm.setPageSize(pageSize);
            userParm.setSearchName(searchName);
            userParm.setPageNumber(pageNumber);
            PageUtil result = userService.getUsersByParams(userParm);
            // 封装返回信息
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to get entities!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    /**
     * Gets roles.
     *
     * @return the roles
     */
    @ApiOperation(value = "[Garnet]获取所有用户", notes = "获取所有用户")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/usertree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getRoles() {
        String message = "Failed to get entities!";
        try {
            List<User> users = userService.queryUsers();
            // 封装返回信息
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Throwable t) {
            String error = message + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    /**
     * Login response entity.
     *
     * @param request   the request
     * @param response  the response
     * @param loginView the login view
     * @return the response entity
     */
    @ApiOperation(value = "[Garnet]用户登录", notes = "用户登录")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation", responseHeaders = @ResponseHeader(name = "location", description = "URL of new created resource", response = LoginMessage.class)),
            @ApiResponse(code = 409, message = "conflict"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response,
//            @ApiParam(value = "token", required = false) @RequestParam(value = "token") String token,
                                   @ApiParam(value = "用户", required = true) @RequestBody LoginView loginView) {
        LoginMessage loginMessage = new LoginMessage();
        String userName = loginView.getUserName();
        request.getSession().setAttribute("userName", userName);
        try {
            //禁止登录
            if (LoginRecorder.isForbidden(userName)) {
                loginMessage.setMessage("您已经连续" + LoginRecorder.LIMIT_LOGIN_FAIL_TIMES + "次输错密码，请" + LoginRecorder.DELAY_MINUTE + "分钟后再尝试");
                loginMessage.setLoginStatus("false");
                loginMessage.setCode(401);
            } else {
                loginMessage = userService.userLogin(loginView);
                //登录失败
                if (loginMessage.getCode() == 401) {
                    LoginRecorder.doRecord(userName);
                } else {
                    LoginRecorder.remove(userName);
                }
            }
            response.setHeader("accessToken", loginMessage.getAccessToken());
            response.setHeader("refreshToken", loginMessage.getRefreshToken());

            /*if (loginMessage.getUser() != null) {
                loginMessage.getUser().setEmail(null);
                loginMessage.getUser().setMobileNumber(null);
                loginMessage.getUser().setCreatedTime(null);
                loginMessage.getUser().setModifiedTime(null);
            }*/
            //返回一个唯一标识符，为判断当前的登录状态
            if (loginMessage.getLoginStatus().equals(LOGINMESSAGE_STATUS_SUCCESS)) {
                String token = UUID.randomUUID().toString().replace("-", "");
                loginMessage.setToken(token);
                //添加token，用于判断是否已经处于登录状态
                loginTokenMap.put(userName, token);
            }
            return new ResponseEntity<>(loginMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = t.getMessage();
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, "服务器发生错误", null);
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    @ApiOperation(value = "[Garnet]第三方应用验证用户是否登录", notes = "是否登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful", responseHeaders = @ResponseHeader(name = "location", description = "URL of new created resource", response = LoginMessage.class)),
            @ApiResponse(code = 403, message = "forbidden"),
            @ApiResponse(code = 500, message = "internal server error")})
    @GetMapping(value = "/user/checkLogin")
    @CrossOrigin
    public ResponseEntity<?> getLoginSession(@ApiParam(value = "用户名", required = true) @RequestParam(value = "userName", required = true) String
                                                     userName,
                                             @ApiParam(value = "token", required = true) @RequestParam(value = "token", required = true) String token) throws IOException {
        try {
            String oldToken = loginTokenMap.get(userName);
            boolean b = !StringUtils.isEmpty(oldToken) && oldToken.equals(token);
            GarnetMessage<Boolean> torinoSrcMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_INSERT_SUCCESS, b);
            return new ResponseEntity<>(torinoSrcMessage, HttpStatus.CREATED);
        } catch (Throwable t) {
            LOG.error("Failed to get Kaptcha ", t);
            return GarnetRsUtil.newResponseEntity(t);
        }
    }


    /**
     * Refresh token response entity.
     *
     * @param request          the request
     * @param response         the response
     * @param token            the token
     * @param tokenRefreshView the token refresh view
     * @return the response entity
     */
    @ApiOperation(value = "[Garnet]刷新token", notes = "刷新token")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation", responseHeaders = @ResponseHeader(name = "location", description = "URL of new created resource", response = LoginMessage.class)),
            @ApiResponse(code = 409, message = "conflict"),
            @ApiResponse(code = 500, message = "internal server error")})
    @LoginRequired
    @RequestMapping(value = "/users/refreshtoken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "token", required = false) @RequestParam(value = "token") String token,
            @ApiParam(value = "用户", required = true) @RequestBody TokenRefreshView tokenRefreshView) {
        String error = "Failed to add entity! " + MessageDescription.OPERATION_INSERT_FAILURE;
        try {
            LoginMessage loginMessage = userService.refreshToken(tokenRefreshView);
            if (StringUtils.isEmpty(loginMessage.getAccessToken()) || StringUtils.isEmpty(loginMessage.getRefreshToken())) {
                error = "刷新失败，请重新登录";
            }
            response.setHeader("accessToken", loginMessage.getAccessToken());
            response.setHeader("refreshToken", loginMessage.getRefreshToken());
//            response.setHeader("Access-Control-Allow-Origin", "*");
            // 设置http的headers
            return new ResponseEntity<>(loginMessage, HttpStatus.OK);
        } catch (Throwable t) {
            error = t.getMessage();
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, "服务异常", null);
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    /**
     * Garnet refresh token response entity.
     *
     * @param request   the request
     * @param response  the response
     * @param token     the token
     * @param loginView the login view
     * @return the response entity
     */
    @ApiOperation(value = "[Garnet]garnet刷新token", notes = "garnet刷新token")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation", responseHeaders = @ResponseHeader(name = "location", description = "URL of new created resource", response = String.class)),
            @ApiResponse(code = 409, message = "conflict"),
            @ApiResponse(code = 500, message = "internal server error")})
    @LoginRequired
    @RequestMapping(value = "/users/garnetrefreshtoken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> garnetRefreshToken(
            HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "token", required = false) @RequestParam(value = "token") String token,
            @ApiParam(value = "用户", required = true) @RequestBody LoginView loginView) {
        String error = "Failed to add entity! " + MessageDescription.OPERATION_INSERT_FAILURE;
        try {
            LoginMessage loginMessage = userService.garnetRefreshToken(loginView);
            if (StringUtils.isEmpty(loginMessage.getAccessToken()) || StringUtils.isEmpty(loginMessage.getRefreshToken())) {
                error = "刷新失败，请重新登录";
            }
            String sessionId = (String) loginSessionMap.get(loginView.getUserName());
            response.setHeader("sessionId", sessionId);
            response.setHeader("accessToken", loginMessage.getAccessToken());
            response.setHeader("refreshToken", loginMessage.getRefreshToken());
//            loginMessage.setSessionId(sessionId);
//            System.out.println(response.getHeader("sessionId"));
//            System.out.println(response.getHeaderNames());
            // 设置http的headers
            return new ResponseEntity<>(loginMessage, HttpStatus.OK);
        } catch (Throwable t) {
            error = t.getMessage();
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
        }
    }

    /**
     * Gets groups by tenant id.
     *
     * @param userId   the user id
     * @param tenantId the tenant id
     * @return the groups by tenant id
     */
    @LoginRequired
    @ApiOperation(value = "[Garnet]根据租户id获取用户列表", notes = "通过查询条件获取用户列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users/tenantId/{tenantId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getGroupsByTenantId(
            @ApiParam(value = "用户id", defaultValue = "0", required = false) @RequestParam(value = "userId", defaultValue = "0", required = false) Long userId,
            @ApiParam(value = "tenantId", required = true) @PathVariable(value = "tenantId") Long tenantId) {
        try {
            UserParm userParm = new UserParm();
            userParm.setUserId(userId);
            userParm.setTenantId(tenantId);
            List<User> users = userService.queryUserByTenantId(userParm);
            // 封装返回信息
//            GarnetMessage<PageInfo<Group>> torinoSrcMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_QUERY_SUCCESS, pageInfo);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to get entities!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    /**
     * Gets groups by application id.
     *
     * @param userId        the user id
     * @param applicationId the application id
     * @return the groups by application id
     */
    @LoginRequired
    @ApiOperation(value = "[Garnet]根据应用id获取用户列表", notes = "通过查询条件获取用户列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users/applicationId/{applicationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getGroupsByApplicationId(
            @ApiParam(value = "用户id", defaultValue = "0", required = false) @RequestParam(value = "userId", defaultValue = "0", required = false) Long userId,
            @ApiParam(value = "applicationId", required = true) @PathVariable(value = "applicationId") Long applicationId) {
        try {
            UserParm userParm = new UserParm();
            userParm.setUserId(userId);
            userParm.setApplicationId(applicationId);
            List<User> users = userService.queryUserByApplicationId(userParm);
            // 封装返回信息
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to get entities!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    /**
     * Gets groups by params.
     *
     * @param userId        the user id
     * @param tenantId      the tenant id
     * @param applicationId the application id
     * @return the groups by params
     */
    @LoginRequired
    @ApiOperation(value = "[Garnet]根据应用id或租户id获取用户列表", notes = "通过查询条件获取用户列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users/byparams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getGroupsByParams(
            @ApiParam(value = "用户id", defaultValue = "0", required = false) @RequestParam(value = "userId", defaultValue = "0", required = false) Long userId,
            @ApiParam(value = "租户id", defaultValue = "0", required = false) @RequestParam(value = "tenantId", defaultValue = "0", required = false) Long tenantId,
            @ApiParam(value = "应用id", defaultValue = "0", required = false) @RequestParam(value = "applicationId", defaultValue = "0", required = false) Long applicationId) {
        try {
            UserParm userParm = new UserParm();
            userParm.setUserId(userId);
            userParm.setApplicationId(applicationId);
            userParm.setTenantId(tenantId);
            List<User> users = userService.queryUserByParams(userParm);
            // 封装返回信息
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to get entities!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    @LoginRequired
    @ApiOperation(value = "[Garnet]根据用户id得到租户", notes = "根据用户id得到租户")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users/getTenantByUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getTenantByUser(
            @ApiParam(value = "用户id", defaultValue = "0", required = false) @RequestParam(value = "userId", defaultValue = "0", required = false) Long userId
    ) {
        try {
            long useId = userId;
            String tenantName = userService.getTenantByUser(useId);
            // 封装返回信息
            return new ResponseEntity<>(tenantName, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to get entities!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    /**
     * Garnet login response entity.
     *
     * @param request      the request
     * @param response     the response
     * @param garLoginView the gar login view
     * @return the response entity
     */
    @ApiOperation(value = "[Garnet]garnet登录", notes = "garnet登录")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation", responseHeaders = @ResponseHeader(name = "location", description = "URL of new created resource", response = String.class)),
            @ApiResponse(code = 409, message = "conflict"),
            @ApiResponse(code = 500, message = "internal server error")})
    @RequestMapping(value = "/users/garnetlogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> garnetLogin(HttpServletRequest request, HttpServletResponse response,
                                         @ApiParam(value = "用户", required = true) @RequestBody GarLoginView garLoginView) {
        String nowTime = garLoginView.getNowTime();
        LoginMessage loginMessage = new LoginMessage();
        String userName = garLoginView.getUserName();
        //是否由于频繁输错密码而导致禁止登录
        if (LoginRecorder.isForbidden(userName)) {
            //移除验证码
            kaptchaMap.remove(nowTime);
            loginMessage.setMessage("您已经连续" + LoginRecorder.LIMIT_LOGIN_FAIL_TIMES + "次输错密码，请" + LoginRecorder.DELAY_MINUTE + "分钟后再尝试");
            loginMessage.setLoginStatus("false");
            loginMessage.setCode(401);
        }
        //正常登录
        else {
            try {
                //验证验证码
                String kaptcha = kaptchaMap.get(nowTime);
                if (StringUtils.isEmpty(garLoginView.getKaptcha()) || !garLoginView.getKaptcha().equalsIgnoreCase(kaptcha)) {
                    loginMessage.setMessage("验证码不正确");
                    loginMessage.setLoginStatus("false");
                    loginMessage.setCode(401);
                }
                //验证账号密码
                else {
                    loginMessage = userService.garLogin(garLoginView);
                    //账号密码错误，记录失败次数
                    if (loginMessage.getCode() == 401) {
                        LoginRecorder.doRecord(userName);
                    }
                    //登录成功
                    else {
                        //清除登录记录
                        LoginRecorder.remove(userName);
                        //更新sessionId
                        HttpSession sessionNew = request.getSession(true);
                        String sessionId = sessionNew.getId();
                        loginSessionMap.put(userName, sessionId);
//                        loginMessage.setSessionId(sessionId);
                        sessionNew.setAttribute("userName", userName);
                    }
                }


            } catch (Throwable t) {
                String error = t.getMessage();
                LOG.error(error, t);
                GarnetMessage<GarnetErrorResponseMessage> garnetMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, "服务器发生错误", null);
                return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(garnetMessage, t);
            } finally {
                //always clear the fucking verifyCode
                kaptchaMap.remove(nowTime);
            }
        }
        response.setHeader("accessToken", loginMessage.getAccessToken());
        response.setHeader("refreshToken", loginMessage.getRefreshToken());
        if (loginMessage.getUser() != null) {
            loginMessage.getUser().setEmail(null);
            loginMessage.getUser().setMobileNumber(null);
            loginMessage.getUser().setCreatedTime(null);
            loginMessage.getUser().setModifiedTime(null);
        }
        return new ResponseEntity<>(loginMessage, HttpStatus.OK);
    }

    /**
     * Gets kaptcha.
     *
     * @param nowTime the now time
     * @param oldTime the old time
     * @return the kaptcha
     * @throws IOException the io exception
     */
    @ApiOperation(value = "[Garnet]获取验证码", notes = "Get Kaptcha")
    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful query"),
            @ApiResponse(code = 500, message = "internal server error")})
    public ResponseEntity<?> getKaptcha(
            @ApiParam(value = "nowTime,当前时间毫秒值", required = true) @RequestParam(value = "nowTime") String nowTime,
            @ApiParam(value = "oldTime,上一张验证码的毫秒值", required = false) @RequestParam(value = "oldTime", required = false) String
                    oldTime) throws IOException {
        try {
            //生成文字验证码
            String text = producer.createText();
            //生成图片验证码
            BufferedImage image = producer.createImage(text);
            if (!StringUtils.isEmpty(oldTime)) {
                kaptchaMap.remove(oldTime);
            }
            kaptchaMap.put(nowTime, text);
            // transform to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", stream);
            byte[] result = stream.toByteArray();

            // modify header of response
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.IMAGE_JPEG);
            header.setCacheControl("no-store, no-cache");
            return new ResponseEntity<>(result, header, HttpStatus.OK);
        } catch (Throwable t) {
            LOG.error("Failed to get Kaptcha ", t);
            return GarnetRsUtil.newResponseEntity(t);
        }
    }

    @ApiOperation(value = "[Garnet]验证验证码是否正确", notes = "验证验证码是否正确")
    @RequestMapping(value = "/validatecaptcha", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful query"),
            @ApiResponse(code = 500, message = "internal server error")})
    public ResponseEntity<?> validateCaptcha(
            @ApiParam(value = "userName", defaultValue = "", required = true) @RequestParam(value = "userName", defaultValue = "", required = true) String
                    userName,
            @ApiParam(value = "captcha", required = true) @RequestParam(value = "captcha", defaultValue = "", required = true) String
                    captcha,
            @ApiParam(value = "nowTime,当前时间毫秒值", defaultValue = "", required = true) @RequestParam(value = "nowTime", defaultValue = "", required = true) String
                    nowTime,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginMessage loginMessage = new LoginMessage();
            String kaptcha = kaptchaMap.get(nowTime);
            if (StringUtils.isEmpty(captcha) || !kaptcha.equalsIgnoreCase(captcha)) {
                loginMessage.setMessage("验证码不正确");
                loginMessage.setLoginStatus("false");
                loginMessage.setCode(401);
            } else {
                loginMessage.setMessage("验证码正确");
                loginMessage.setLoginStatus("success");
                loginMessage.setCode(200);

                HttpSession sessionNew = request.getSession(true);
                String sessionId = sessionNew.getId();
                //判断是否第一次登录
                String sessionIdOld = (String) loginSessionMap.get(userName);
                if (!StringUtils.isEmpty(sessionIdOld)) {
                    loginSessionMap.remove(userName);
                }
                //将新的sessionId存入
                loginSessionMap.put(userName, sessionId);
            }
            return new ResponseEntity<>(loginMessage, HttpStatus.OK);
        } catch (Throwable t) {
            LOG.error("Failed to get Kaptcha ", t);
            return GarnetRsUtil.newResponseEntity(t);
        } finally {
            //just clear the fucking verifyCode
            kaptchaMap.remove(nowTime);
        }

    }

    @ApiOperation(value = "[Garnet]验证用户名和密码是否正确", notes = "验证用户名和密码是否正确")
    @RequestMapping(value = "/validateuserinfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful query"),
            @ApiResponse(code = 500, message = "internal server error")})
    public ResponseEntity<?> validateUserNameAndPassword(
            @ApiParam(value = "userName", defaultValue = "", required = true) @RequestParam(value = "userName", defaultValue = "", required = true) String
                    userName,
            @ApiParam(value = "password", defaultValue = "", required = true) @RequestParam(value = "password", defaultValue = "", required = true) String
                    password) {
        try {
            boolean b = userService.validateUserInfo(userName, password);
            return new ResponseEntity<>(b, HttpStatus.OK);
        } catch (Throwable t) {
            LOG.error("Failed to get Kaptcha ", t);
            return GarnetRsUtil.newResponseEntity(t);
        }
    }

    /**
     * Gets login session.
     *
     * @param request  the request
     * @param response the response
     * @param userName the user name
     * @return the login session
     * @throws IOException the io exception
     */
    @ApiOperation(value = "[Garnet]验证是否已经有人登录过", notes = "验证是否已经有人登录过")
    @RequestMapping(value = "/checklogined", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful query"),
            @ApiResponse(code = 500, message = "internal server error")})
    public ResponseEntity<?> getLoginSession(HttpServletRequest request, HttpServletResponse response,
                                             @ApiParam(value = "用户名", required = true) @RequestParam(value = "userName", required = true) String
                                                     userName) throws IOException {
        try {
            String sessionIdOld = (String) loginSessionMap.get(userName);
//            HttpSession session = request.getSession(true);
            HttpSession session = request.getSession();
            String sessionId = session.getId();

            boolean b;
            if (!StringUtils.isEmpty(sessionIdOld) && sessionId.equals(sessionIdOld)) {
                b = true;
            } else {
                b = false;
            }

            GarnetMessage<Boolean> torinoSrcMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_INSERT_SUCCESS, b);
            return new ResponseEntity<>(torinoSrcMessage, HttpStatus.CREATED);
        } catch (Throwable t) {
            LOG.error("Failed to get Kaptcha ", t);
            return GarnetRsUtil.newResponseEntity(t);
        }
    }
}
