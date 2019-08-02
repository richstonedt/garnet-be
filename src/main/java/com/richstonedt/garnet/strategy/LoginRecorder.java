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
package com.richstonedt.garnet.strategy;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
@Component
public class LoginRecorder {
    /**
     * 连续登录失败次数上限
     */
    public static Integer LIMIT_LOGIN_FAIL_TIMES = 3;

    /**
     * 禁止登录时间间隔
     */
    public static  int DELAY_MINUTE = 5;

    public static final Cache<String, Integer> recordMap = CacheBuilder.newBuilder().expireAfterWrite(DELAY_MINUTE, TimeUnit.MINUTES).maximumSize(1000).build();


    /**
     * 将{userName}的登录失败次数+1
     *
     * @param userName
     */
    public static void doRecord(String userName) {
        Integer failTimes = recordMap.getIfPresent(userName);
        if (failTimes == null) {
            recordMap.put(userName, 1);
        } else {
            recordMap.put(userName, ++failTimes);
        }
    }

    /**
     * 登录失败次数是否超过规定
     *
     * @param userName
     * @return true or false
     */
    public static Boolean isForbidden(String userName) {
        Integer failTimes = recordMap.getIfPresent(userName);
        return failTimes != null && failTimes >= LIMIT_LOGIN_FAIL_TIMES ? true : false;
    }

    /**
     * 重置{userName}的登录失败次数
     *
     * @param userName
     */
    public static void remove(String userName) {
        recordMap.put(userName,0);
    }
}
