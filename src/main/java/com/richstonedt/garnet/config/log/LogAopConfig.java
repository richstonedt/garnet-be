
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

package com.richstonedt.garnet.config.log;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.richstonedt.garnet.common.utils.Result;
//import com.richstonedt.garnet.model.GarLog;
//import com.richstonedt.garnet.model.GarLogOperation;
//import com.richstonedt.garnet.model.view.model.GarUserLogin;
//import com.richstonedt.garnet.service.GarLogOperationService;
//import com.richstonedt.garnet.service.GarLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <b><code>LogAop</code></b>
 * <p> 对controller 中所有的方法进行切面
 * class_comment
 * </p>
 * <b>Create Time:</b> 2017/9/20 12:13
 *
 * @author Sun Jinpeng
 * @version 0.1.0
 * @since gempile-core-cs 0.1.0
 */
@Aspect
@Component
public class LogAopConfig {

    /**
     * The Operation service.
     *
     * @since gempile-core-cs 1.0.0
     */
//    @Autowired
//    private GarLogOperationService operationService;
//
//    /**
//     * The Log service.
//     *
//     * @since gempile-core-cs 1.0.0
//     */
//    @Autowired
//    private GarLogService garLogService;
//
//    /**
//     * The Operations.
//     *
//     * @since gempile-core-cs 1.0.0
//     */
//    private static List<GarLogOperation> operations;
//
//    /**
//     * Login aspect.
//     *
//     * @param point       the point
//     * @param returnValue the return value
//     * @since gempile-core-cs 1.0.0
//     */
//    @AfterReturning(pointcut = "execution(* com.richstonedt.garnet.controller.GarLoginController.loginByToken(..))",
//            returning = "returnValue")
//    public void loginByTokenAspect(JoinPoint point, Object returnValue) {
//
//        Result result = (Result) returnValue;
//        Object garnetToken = result.get("garnetToken");
//        if (garnetToken == null) {
//            return;
//        }
//        // 用户登录成功
//        String userName = (String)result.get("username");
//        GarLog log = getLog();
//        log.setUserName(userName);
//        log.setOperation(userName + "登录系统");
//        log.setCreatedTime(new Date());
//        garLogService.saveLog(log);
//    }
//    @AfterReturning(pointcut = "execution(* com.richstonedt.garnet.controller.GarLoginController.login(..))",
//            returning = "returnValue")
//    public void loginAspect(JoinPoint point, Object returnValue) {
//        Object[] args = point.getArgs();
//        Result result = (Result) returnValue;
//        GarUserLogin user = (GarUserLogin) args[1];
//        Object garnetToken = result.get("garnetToken");
//        if (garnetToken == null) {
//            return;
//    }
//    // 用户登录成功
//        String userName = user.getUsername();
//        GarLog log = getLog();
//        log.setUserName(userName);
//        log.setOperation(userName + "登录系统");
//        log.setCreatedTime(new Date());
//        garLogService.saveLog(log);
//    }
//    /**
//     * GarUserRole aspect.
//     *
//     * @since gempile-core-cs 1.0.0
//     */
//    /*@After("execution(* com.richstonedt.garnet.controller..*(..))")
//    public void authorityAspect(JoinPoint point) {
//        String loginName = "login";
//        String methodName = point.getSignature().getName();
//        // 登录方法由上面单独拦截处理
//        if (!loginName.equals(methodName)) {
//            garLogService.saveLog(getLog());
//        }
//    }*/
//
//    /**
//     * Get log.
//     *
//     * @since gempile-core-cs 0.1.0
//     */
//    public GarLog getLog() {
//        // get HttpServletRequest
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        //get log info
//        String gempileToken = request.getHeader("gempileToken");
//        String userName = "";
//        if (!StringUtils.isEmpty(gempileToken)) {
//            try {
//                userName = JWT.decode(gempileToken).getClaim("una").asString();
//            } catch (JWTDecodeException e) {
//                userName = "";
//            }
//        }
//        String method = request.getMethod();
//        String url = request.getRequestURI();
//        String operation = getOperation(method, url);
//        String IP = request.getHeader("X-Real-IP");
//        //set log info
//        GarLog log = new GarLog();
//        log.setUserName(userName);
//        log.setMethod(method);
//        log.setOperation(operation);
//        log.setUrl(url);
//        log.setIp(IP);
//        //get this request SQL
//        Map<String, List<String>> sourceSql = SqlConfig.getSqlWithRequest(url + method);
//        String finalSql = decorateSql(sourceSql);
//        log.setSqlText(finalSql);
//        return log;
//    }
//
//    /**
//     * Gets operation.
//     *
//     * @param method the method
//     * @param url    the url
//     * @return the operation
//     */
//    private String getOperation(String method, String url) {
//        if (CollectionUtils.isEmpty(operations)) {
//            operations = operationService.getAllOperations();
//        }
//        List<GarLogOperation> matchedOperations = new ArrayList<>();
//        for (GarLogOperation operation : operations) {
//            if (url.contains(operation.getUrl()) && method.toUpperCase().equals(operation.getMethod().toUpperCase())) {
//                matchedOperations.add(operation);
//            }
//        }
//        String operation;
//        if (matchedOperations.size() == 1) {
//            operation = matchedOperations.get(0).getOperation();
//        } else if (matchedOperations.size() == 2) {
//            String url1 = matchedOperations.get(0).getUrl();
//            String url2 = matchedOperations.get(1).getUrl();
//            String operation1 = matchedOperations.get(0).getOperation();
//            String operation2 = matchedOperations.get(1).getOperation();
//            operation = url1.contains(url2) ? operation1 : operation2;
//        } else {
//            operation = "没有匹配到具体操作";
//        }
//        return operation;
//    }
//
//    /**
//     * Decorate sql string.
//     *
//     * @param maps the list
//     * @return the string
//     * @since gempile-core-cs 1.0.0
//     */
//    private String decorateSql(Map<String, List<String>> maps) {
//        List<String> resultList = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(maps)) {
//            Set<Map.Entry<String, List<String>>> entrySets = maps.entrySet();
//            for (Map.Entry<String, List<String>> entrySet : entrySets) {
//                List<String> value = entrySet.getValue();
//                List<String> tmpList = new ArrayList<>();
//                for (int i = 0; i < value.size(); i++) {
//                    if ((i + 1) % 3 == 0) {
//                        resultList.add(formatOneSql(tmpList));
//                        tmpList.clear();
//                    } else {
//                        if ((value.get(i).contains("Preparing") || value.get(i).contains("Parameters") ||
//                                (value.get(i).contains("gar_log") && value.get(i).toUpperCase().contains("SELECT"))) && !value.get(i).contains("gar_log_operation")) {
//                            tmpList.add(value.get(i));
//                        }
//                    }
//                }
//            }
//        }
//        StringBuilder result = new StringBuilder();
//        for (String str : resultList) {
//            result.append(str);
//        }
//        return result.toString();
//    }
//
//    /**
//     * Format one sql string.
//     *
//     * @param list the sql
//     * @return the string
//     * @since gempile-core-cs 1.0.0
//     */
//    private String formatOneSql(List<String> list) {
//        if (CollectionUtils.isEmpty(list) || list.size() != 2) {
//            return "";
//        }
//        String prepareSql = list.get(0);
//        if (!prepareSql.contains(";")) {
//            prepareSql += ";";
//        }
//        String parametersSql = list.get(1);
//        prepareSql = prepareSql.replace("==>  Preparing:", "");
//        parametersSql = parametersSql.replace("==> Parameters:", "");
//        List<String> parametersList = new ArrayList<>();
//        if (parametersSql.contains(",")) {
//            String tmpArray[] = parametersSql.split(",");
//            for (String value : tmpArray) {
//                parametersList.add(removeStr(value));
//            }
//        } else {
//            parametersList.add(removeStr(parametersSql));
//        }
//        for (String parameter : parametersList) {
//            prepareSql = prepareSql.replaceFirst("\\?", parameter);
//        }
//        return prepareSql;
//    }
//
//    /**
//     * Remove str string.
//     *
//     * @param name the name
//     * @return the string
//     * @since gempile-core-cs 1.0.0
//     */
//    private String removeStr(String name) {
//        int start = 0;
//        if (name.contains("(")) {
//            start = name.indexOf("(");
//        }
//        if (start != 0) {
//            return name.substring(0, start);
//        } else {
//            return name;
//        }
//    }
}
