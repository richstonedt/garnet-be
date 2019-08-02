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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.richstonedt.garnet.service.GarUserSynchronizeService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import com.richstonedt.garnet.common.exception.GarnetServiceException;
import com.richstonedt.garnet.common.utils.HttpClientUtils;
import com.richstonedt.garnet.common.utils.PropertiesUtils;
import com.richstonedt.garnet.dao.GarUserSynchronizeMapper;
//import com.richstonedt.garnet.model.GarUser;
import com.richstonedt.garnet.model.GarUserSynchronize;

/**
 * <b><code>UserSynchronizeServiceImpl</code></b>
 * <p>
 * 用户同步具体实现.
 * <p>
 * <b>Creation Time:</b> 2018/9/5 14:30.
 *
 * @author liuruojing
 * @since garnet-core-be-fe 0.1.0
 */
@Service
public class GarUserSynchronizeServiceImpl implements GarUserSynchronizeService {
    /**
     * 全量同步的请求报文.
     *
     * @since ${PROJECT_NAME} 0.1.0
     */
//    private String batchSaveRequestData = "<soapenv:Envelope xmlns:"
//            + "soapenv='http://schemas.xmlsoap.org/soap/envelope/' \n"
//            + "xmlns:dat='http://datasynccxf.webservice.dmc.zyzx.com/'>\n"
//            + "<soapenv:Header/>\n" + "<soapenv:Body>\n" + "<dat:batchSave/>\n"
//            + "</soapenv:Body>\n" + "</soapenv:Envelope>\n";
//
//    /**
//     * 增量同步请求报文.
//     *
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private String incrementRequestData = "<soapenv:Envelope xmlns:"
//            + "soapenv='http://schemas.xmlsoap.org/soap/envelope/'\n"
//            + "xmlns:dat='http://datasynccxf.webservice.dmc.zyzx.com/'>\n"
//            + "<soapenv:Header/>\n" + "<soapenv:Body>\n"
//            + "<dat:incrementSave>\n"
//            + "<endTime>2016-08-12 12:00:00</endTime>\n"
//            + "</dat:incrementSave>\n"
//            + "</soapenv:Body>\n" + "</soapenv:Envelope>\n";
//
//    /**
//     * LOG.
//     *
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private static final Logger LOG = LoggerFactory
//            .getLogger(GarUserSynchronizeServiceImpl.class);
//
//    /**
//     * garUserSynchronizeMapper.
//     *
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    @Autowired
//    private GarUserSynchronizeMapper garUserSynchronizeMapper;
//
//    /**
//     * garUserService.
//     *
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    @Autowired
//    private GarUserServiceImpl garUserService;
//
//    /**
//     * 用户同步.
//     *
//     * @param
//     * @return
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    @Transactional
//    @Override
//    public final void userSynchronize() {
//        try {
//            LOG.info("----用户同步任务开始----");
//            // 获取上一次用户同步的时间
//            String lastUpdateTime = getLastUpdateTime();
//            // 根据上一次同步时间确定接口名称，前者全量同步，后者增量同步
//            String interFaceName = lastUpdateTime == null ? "J_MH_04_url"
//                    : "J_MH_05_url";
//            // 记录这次的同步时间，下次从这个时间开始同步，网络存在时间开销，
//            // 导致下次可能会同步重复的数据，在保存时做过滤处理
//            Date currentTime = new Date();
//            // 向远程服务器拉取未同步用户信息
//            List<GarUser> users = getUsersFromRemoteSystem(interFaceName,
//                    lastUpdateTime);
//            // 保存用户
//            saveUsers(users);
//            // 更新同步时间
//            updateTime(currentTime);
//            LOG.info("----用户同步任务结束----");
//        } catch (Throwable e) {
//            LOG.debug("用户同步定时任务发生异常", e);
//            // 抛出此异常确保事务正常回滚
//            throw new RuntimeException();
//        }
//    }
//
//    /**
//     * 从远程门户获取同步用户.
//     *
//     * @param interfaceName 指定门户接口名称：全量同步，增量同步
//     * @param lastUpdateTime 上一次更新时间
//     * @return 返回从门户网站拉取的所有未同步用户
//     * @throws Exception e
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private List<GarUser> getUsersFromRemoteSystem(final String interfaceName,
//            final String lastUpdateTime) throws Exception {
//        List<GarUser> users = null;
//        CloseableHttpClient httpClient;
//        CloseableHttpResponse response = null;
//        try {
//            // 从连接池获取连接
//            httpClient = HttpClientUtils.getConnection();
//            // 获取webservice服务地址
//            String url = PropertiesUtils.read("interface.properties",
//                    interfaceName);
//            HttpPost httppost = new HttpPost(url);
//            // 构建请求报文
//            String soapRequestData = getRequstData(interfaceName,
//                    lastUpdateTime);
//            LOG.debug("---------------请求报文----------------");
//            LOG.debug(soapRequestData);
//            LOG.debug("---------------请求报文结束----------------");
//            // soap协议的格式，定义了方法和参数
//            HttpEntity re = new StringEntity(soapRequestData, HTTP.UTF_8);
//            httppost.setHeader("Content-Type",
//                    "application/soap+xml; charset=utf-8");
//            httppost.setEntity(re);
//            response = httpClient.execute(httppost); // 调用接口
//            if (response.getStatusLine().getStatusCode() == 200) { // 调用状态
//                String xmlString = EntityUtils.toString(response.getEntity(), "UTF-8");
//                users = parseXMLString(xmlString); // 解析接口返回的值
//            }
//            return users;
//        } finally {
//            if (response != null) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    LOG.debug("close response failed", e);
//                }
//            }
//
//        }
//    }
//
//    /**
//     * 根据接口名称和上次更新时间确定要发送的SOAP数据.
//     *
//     * @param interfaceName 接口名称
//     * @param lastUpdateTime 上次用户同步时间
//     * @return String
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private String getRequstData(final String interfaceName,
//            final String lastUpdateTime) {
//
//        if (interfaceName == null) {
//            throw new NullPointerException("param not allow to be null");
//        }
//        // 全量新增用户
//        if ("J_MH_04_url".equals(interfaceName)) {
//            return batchSaveRequestData;
//        }
//        // 增量新增用户 返回对应的请求体
//        if ("J_MH_05_url".equals(interfaceName)) {
//            // 将报文中日期替换为更新时间
//            String realIncrementRequestData = replaceEndTime(
//                    incrementRequestData, lastUpdateTime);
//            return realIncrementRequestData;
//        }
//
//        throw new NullPointerException("param illegal");
//    }
//
//    /**
//     * 替换掉日期参数.
//     *
//     * @param incrementRequestData 原始请求数据
//     * @param lastUpdateTime  最后一次更新时间
//     * @return String
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private static String replaceEndTime(final String incrementRequestData,
//            final String lastUpdateTime) {
//        String regex = "<endTime>.*?</endTime>";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(incrementRequestData);
//        return matcher.replaceAll("<endTime>" + lastUpdateTime + "</endTime>");
//
//    }
//
//    /**
//     * 将数据解析成GarUser的集合.
//     *
//     * @param xmlString 原始数据
//     * @return List<GarUser>
//     * @author liuruojing
//     * @throws DocumentException 异常.
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private List<GarUser> parseXMLString(final String xmlString)
//            throws DocumentException {
//        LOG.debug("-------------------------门户返回-数据---------------------\n\n\n");
//        LOG.debug(xmlString);
//        LOG.debug("-------------------------门户返回结束----------------------\n\n\n");
//        List<GarUser> users = new ArrayList<>();
//        String regex = "<userInfoMsgs>.*?</userInfoMsgs>";
//        Pattern pattern = Pattern.compile(regex,
//                Pattern.MULTILINE | Pattern.DOTALL); // 编译正则,开启多行模式
//        Matcher matcher = pattern.matcher(xmlString);
//        while (matcher.find()) { // 逐个进行懒惰式匹配，每次获取一个UserInfo的xml数据段
//            // 得到匹配的字符串，并转换成GarUser
//            GarUser user = parseUserInfoXml(matcher.group());
//            if (user != null) {
//                users.add(user); // 如果不为空，添加到list中
//            }
//        }
//        return users;
//    }
//
//    /**
//     * 将单个UserInfo的xml片段转成GarUser对象.
//     *
//     * @param userInfo 原始报文
//     * @return GarUser
//     * @author liuruojing
//     * @throws  DocumentException e.
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private GarUser parseUserInfoXml(final String userInfo) {
//        LOG.debug("-------------------------门户单个数据---------------------\n\n\n");
//        LOG.debug(userInfo);
//        LOG.debug("-------------------------门户单个返回结束----------------------\n\n\n");
//        GarUser user = null;
//        //替换非法的xml字符
//        String replacedUserInfo = userInfo.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
//        try {
//            Document doc = DocumentHelper.parseText(replacedUserInfo); // 将字符串转为XML
//            Element rootNode = doc.getRootElement(); // 获取根节点
//            List<Element> userAccount = rootNode.elements("userAccount");
//            // 得到子节点emailUrl节点
//            List<Element> emailUrl = rootNode.elements("emailUrl");
//            List<Element> phoneNum = rootNode.elements("phoneNum");
//            List<Element> isManager = rootNode.elements("isManager");
//            if (isManager.size() > 0) {  //获取到节点数据
//                user = new GarUser();
//                // isManager=1表示超级管理员，否则为普通用户
//                int admin = Integer
//                        .parseInt((String) isManager.get(0).getData()) == 1 ? 2 : 0;
//                // 用户名加上指定后缀，避免与原有用户重名
//                user.setUserId(new Date().getTime());
//                user.setUsername((String)userAccount.get(0).getData());
//                user.setPassword(userAccount.get(0).getData() + "@12345");
//                user.setEmail((String) emailUrl.get(0).getData());
//                user.setMobile((String) phoneNum.get(0).getData());
//                user.setAdmin(admin);
//                user.setStatus(1);
//                LOG.debug("-------------------------门户实例数据---------------------\n\n\n");
//                LOG.debug(user.getUsername() + "  " + user.getPassword() + "  " + user.getEmail() + "  " + user.getMobile());
//                LOG.debug("-------------------------门户实例结束----------------------\n\n\n");
//            }
//
//            return user;
//        } catch (DocumentException e) { //非法xml字符导致的解析失败，不应该影响后面用户的同步
//            return null;
//        }
//
//    }
//
//    /**
//     * 获取上一次更新用户的时间.
//     *
//     * @param
//     * @return String
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private String getLastUpdateTime() {
//        String lastUpdate = null;
//        GarUserSynchronize userSynchronize = garUserSynchronizeMapper
//                .selectByPrimaryKey(1L);
//        if (userSynchronize == null) {
//            throw new RuntimeException(
//                    "请不要删除gar_user_synchronize表中id为1的记录!!please!!");
//        }
//        Date lastUpdateTime = userSynchronize.getUpdatedTime();
//        if (lastUpdateTime != null) {
//            lastUpdate = parseDateToString(lastUpdateTime);
//        }
//        return lastUpdate;
//    }
//
//    /**
//     * 格式化日期.
//     *
//     * @param date 日期
//     * @return String
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private String parseDateToString(final Date date) {
//        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return simple.format(date);
//    }
//
//    /**
//     * 更新同步时间.
//     *
//     * @param currentTime 同步时间
//     * @return
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private void updateTime(final Date currentTime) {
//        // 更新上次同步时间
//        GarUserSynchronize userSynchronize = new GarUserSynchronize();
//        userSynchronize.setId(1L);
//        userSynchronize.setUpdatedTime(currentTime);
//        garUserSynchronizeMapper.updateByPrimaryKey(userSynchronize);
//    }
//
//    /**
//     * 保存用户.
//     *
//     * @param list 用户列表
//     * @return
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    private void saveUsers(final List<GarUser> list) {
//        if (list != null) {
//            GarUser user;
//            Iterator<GarUser> iterator = list.iterator();
//            while (iterator.hasNext()) {
//                user = iterator.next();
//                try {
//                    garUserService.saveUser(user);
//                    LOG.info("同步用户:" + user.getUsername());
//                } catch (GarnetServiceException exception) {
//                    // 用户已存在
//                }
//            }
//        }
//    }

}