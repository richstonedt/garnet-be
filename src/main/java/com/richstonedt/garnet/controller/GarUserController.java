
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

import com.richstonedt.garnet.common.utils.GarnetUtils;
import com.richstonedt.garnet.common.utils.PageUtils;
//import com.richstonedt.garnet.model.GarUser;
//import com.richstonedt.garnet.service.GarUserService;
import com.richstonedt.garnet.service.GarUserSynchronizeService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <b><code>UserController</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2017/8/21 15:20
 *
 * @author Sun Jinpeng
 * @version 1.0.0
 * @since garnet-core-be-fe 1.0.0
 */
@RestController
@RequestMapping("/v1.0")
@Api(tags = "[Garnet]用户管理接口")
public class GarUserController {

    /**
     * The constant LOG.
     *
     * @since garnet-core-be-fe 1.0.0
     */
//    private static Logger LOG = LoggerFactory.getLogger(GarUserController.class);
//    /**
//     * The Sys user service.
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Autowired
//    private GarUserService garUserService;
//    @Autowired
//    private GarUserSynchronizeService garUserSynchronizeService;
//    /**
//     * 同步门户网站用户.
//     *
//     * @param
//     * @return
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    @RequestMapping(value = "/UserSynchronize", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "同步用户")
//    public void  UserSynchronize(HttpServletResponse response) throws IOException {
//        try {
//            garUserSynchronizeService.userSynchronize();
//            response.getWriter().print("<script>alert('ok')</script>");
//        }
//        catch(Exception e){
//            response.getWriter().print("<script>alert('Sorry，System erro!please try again')</script>");
//        }
//        }
//    /**
//     * 所有用户列表
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]查询用户列表", notes = "Get user list!")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> getUserList(
//            @RequestParam(value = "page") int page, @RequestParam(value = "limit") int limit,
//            @RequestParam(value = "searchName", required = false) String searchName) {
//        try {
//            List<GarUser> results = garUserService.getUserList(page, limit, searchName);
//            int totalCount = garUserService.getUserCount();
//            PageUtils pageUtils = new PageUtils(results, totalCount, limit, page);
//            return new ResponseEntity<>(pageUtils, HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get user list :", t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
//
//    /**
//     * 修改登录用户密码
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]修改密码", notes = "Change password!")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> password(
//            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId") Long userId,
//            @ApiParam(value = "password,旧密码", required = true) @RequestParam(value = "password") String password,
//            @ApiParam(value = "newPassword,新密码", required = true) @RequestParam(value = "newPassword") String newPassword) {
//        try {
//            garUserService.changePassword(userId, password, newPassword);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get user list :", t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
//
//    /**
//     * 用户信息
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]查询单个用户信息", notes = "Get user info!")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> getUserById(@ApiParam(value = "userId", required = true) @PathVariable("userId") Long userId) {
//        try {
//            GarUser garUser = garUserService.getUserById(userId);
//            return new ResponseEntity<Object>(garUser, HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get user list :", t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
//
//    /**
//     * 保存用户
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]新增用户", notes = "Save garUser!")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> saveUser(@RequestBody GarUser garUser) {
//        try {
//            garUserService.saveUser(garUser);
//            return new ResponseEntity<Object>(HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get garUser list :", t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
//
//    /**
//     * 修改用户
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/user", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]修改用户信息", notes = "Update garUser info !")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> update(@RequestBody GarUser garUser) {
//        try {
//            garUserService.updateUser(garUser);
//            return new ResponseEntity<Object>(HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get garUser list :", t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
//
//    /**
//     * 删除用户
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/user", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]批量删除用户", notes = "Get menu list!")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> deleteUsers(@ApiParam(value = "用户ID,用‘,’号隔开") @RequestParam(value = "userIds") String userIds) {
//        try {
//            List<Long> userIdList = GarnetUtils.converStringToList(userIds);
//            garUserService.deleteUsers(userIdList);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get user list :", t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
}
