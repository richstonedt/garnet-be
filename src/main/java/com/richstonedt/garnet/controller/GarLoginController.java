
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.richstonedt.garnet.common.utils.GarnetUtils;
import com.richstonedt.garnet.common.utils.PropertiesUtils;
import com.richstonedt.garnet.common.utils.Result;
//import com.richstonedt.garnet.model.GarUser;
//import com.richstonedt.garnet.model.view.model.GarUserLogin;
//import com.richstonedt.garnet.service.GarUserService;
import com.richstonedt.garnet.service.GarUserTokenService;
import io.swagger.annotations.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 * @since garnet-core-be-fe 1.0.0
 */
@RestController
@Api(tags = "[Garnet]登录相关接口")
public class GarLoginController {

    /**
     * The constant LOG.
     *
     * @since garnet-core-be-fe 1.0.0
     */
//    private static Logger LOG = LoggerFactory.getLogger(GarLoginController.class);
//
//    /**
//     * The Producer.
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Autowired
//    private Producer producer;
//
//    /**
//     * The Sys user token service.
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Autowired
//    private GarUserTokenService garUserTokenService;
//
//    /**
//     * The GarUser service.
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Autowired
//    private GarUserService garUserService;
//
//    /**
//     * The constant kaptchaMap.
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    private static Map<String, String> kaptchaMap = new HashMap<>();
//
//    /**
//     * Kaptcha.
//     *
//     * @return the kaptcha
//     * @throws IOException the io exception
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
//    @ApiOperation(value = "[Garnet]获取验证码", notes = "Get kaptcha")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> getKaptcha(@ApiParam(value = "nowTime,当前时间戳", required = true) @RequestParam(value = "nowTime") String nowTime) throws IOException {
//        try {
//            //生成文字验证码
//            String text = producer.createText();
//            //生成图片验证码
//            BufferedImage image = producer.createImage(text);
//            kaptchaMap.put(nowTime, text);
//
//            // transform to byte
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            ImageIO.write(image, "jpg", stream);
//            byte[] result = stream.toByteArray();
//
//            // modify header of response
//            HttpHeaders header = new HttpHeaders();
//            header.setContentType(MediaType.IMAGE_JPEG);
//            header.setCacheControl("no-store, no-cache");
//            return new ResponseEntity<>(result, header, HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get kaptcha :" + nowTime, t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
//
//    /**
//     * 登录
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/sys/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]登录接口", notes = "Login")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public Map<String, Object> login(@ApiParam(value = "loginFrom,从哪个项目登录(如果从权限系统登录:loginFrom=garnet;其他项目任意)")
//                                     @RequestParam(value = "loginFrom") String loginFrom, @RequestBody GarUserLogin user) throws IOException {
//        try {
//            String kaptcha = kaptchaMap.get(user.getNowTime());
//            if (!user.getCaptcha().equalsIgnoreCase(kaptcha)) {
//                kaptchaMap.remove("kaptcha");
//                return Result.error("验证码不正确");
//            }
//            kaptchaMap.remove("kaptcha");
//            //用户信息
//            GarUser userEntity = garUserService.queryByUserName(user.getUsername());
//            //账号不存在
//            if (userEntity == null) {
//                return Result.error("账号或密码不正确");
//            }
//            //账号用户名、密码错误
//            if (!userEntity.getPassword().equals(new Sha256Hash(user.getPassword(), userEntity.getSalt()).toHex())) {
//                return Result.error("账号或密码不正确");
//            }
//            if ("garnet".equals(loginFrom)) {
//                if (userEntity.getAdmin() != 1) {
//                    return Result.error("没有权限登录该系统");
//                }
//            }
//            //账号锁定
//            if (userEntity.getStatus() == 0) {
//                return Result.error("账号已被锁定,请联系管理员");
//            }
//            //生成token，并保存到数据库
//            Result result = garUserTokenService.createGarnetToken(userEntity.getUserId());
//            String gempileToken = garUserTokenService.createGempileToken(userEntity.getUserId());
//            result.put("gempileToken", gempileToken);
//            return result;
//        } catch (Throwable t) {
//            LOG.error("Failed to Login:", t);
//            return Result.error("服务器异常！");
//        }
//    }
//    /**
//     * 根据token免密登录
//     * @param token
//     * @author liuruojing
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @RequestMapping(value = "/sys/loginByToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]登录接口", notes = "Login")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public Map<String, Object> loginByToken(@ApiParam(value = "token")@RequestParam(value = "token") String token) throws IOException {
//            try {
//                //根据token调用门户网站获取username
//                String username = getUserNameFromRemoteSystem(token);
//                if (username == null) {
//                    return Result.error("未登录");
//                }
//                //用户信息
//                GarUser userEntity = garUserService.queryByUserName(username);
//                //账号不存在
//                if (userEntity == null) {
//                    return Result.error("本地账号不存在");
//                }
//                //账号锁定
//                if (userEntity.getStatus() == 0) {
//                    return Result.error("账号已被锁定,请联系管理员");
//                }
//                //生成token，并保存到数据库
//                Result result = garUserTokenService.createGarnetToken(userEntity.getUserId());
//                String gempileToken = garUserTokenService.createGempileToken(userEntity.getUserId());
//                result.put("gempileToken", gempileToken);
//                result.put("username",username);
//                return result;
//
//            }
//            catch (IOException e) {
//                LOG.error("Failed to Login:", e);
//                return Result.error("从门户认证用户信息失败，请重试！");
//            }
//            catch (Throwable e) {
//                LOG.error("Failed to Login:", e);
//                return Result.error("服务器异常！");
//            }
//    }
//
//    /**
//     * 调用远程门户验证token是否登录 登录成功返回用户名，否则返回null.
//     *
//     * @param token
//     * @return String
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private String getUserNameFromRemoteSystem(final String token) throws IOException {
//        String username = null;
//        if (token != null) {
//            //连接远程门户接口获取响应内容
//            String content = GetDataFromRemoteSystem(token);
//            LOG.debug("门户数据返回："+content);
//            if (content != null) {
//                username = parseContent(content);
//            }
//        }
//        return username;
//    }
//
//    private String GetDataFromRemoteSystem(String token) throws IOException {
//        // 创建Httpclient对象
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//        String content = null;
//        try {
//            String url = PropertiesUtils.read("interface.properties","J_MH_02_url");
//            //拼接token
//            url = url + "?token=" + token;
//            // 创建http GET请求
//            HttpGet httpGet = new HttpGet(url);
//            // 执行请求
//            response = httpclient.execute(httpGet);
//            // 判断返回状态是否为200
//            if (response.getStatusLine().getStatusCode() == 200) {
//                // 获取服务端返回的数据
//                content = EntityUtils.toString(response.getEntity(),
//                        "UTF-8");
//            }
//            return content;
//        }
////        catch (HttpHostConnectException e) {
////          //内网认证ping不通，进行外网认证
////            try {
////                String url = PropertiesUtils.read("interface.properties", "J_MH_02_url_out");
////                //拼接token
////                url = url + "?token=" + token;
////                // 创建http GET请求
////                HttpGet httpGet = new HttpGet(url);
////                // 执行请求
////                response = httpclient.execute(httpGet);
////                // 判断返回状态是否为200
////                if (response.getStatusLine().getStatusCode() == 200) {
////                    // 获取服务端返回的数据
////                    content = EntityUtils.toString(response.getEntity(),
////                            "UTF-8");
////                }
////                return content;
////            } catch (Exception ex) {
////                LOG.debug("getUserFromRemotoSystem has Exception", ex);
////                return null;
////            }
////
////        }
//       finally {
//            if (response != null) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    LOG.debug("close response failed", e);
//                }
//            }
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                LOG.debug("close httpclient  failed", e);
//            }
//        }
//
//    }
//
//    private String parseContent(final String content) {
//        String username = null;
//        JsonParser parser = new JsonParser();
//        JsonObject jsonObject  = parser.parse(content).getAsJsonObject();
//        String result = jsonObject.get("result").getAsString();
//        //认证失败
//        if (result.equals("fail")) {
//            return username;
//        } else { //认证成功 返回登录账号
//            JsonObject userInfo = jsonObject.get("userInfo").getAsJsonObject();
//            username = userInfo.get("user_account").getAsString();
//            return username;
//        }
//    }
}
