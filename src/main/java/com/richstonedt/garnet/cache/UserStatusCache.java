
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

package com.richstonedt.garnet.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 用于记录用户的登录状态  key为用户名，value为用户状态
 */
public class UserStatusCache {
    private final static Cache<String, String> cache = CacheBuilder.newBuilder().
            expireAfterAccess(20, TimeUnit.SECONDS).build();

    public static void setUserStatus(String userName, String userStatus) {
        cache.put(userName, userStatus);
    }

    public static String getUserStatus(String userName) {
        String result = cache.getIfPresent(userName);
        return result == null ? "" : result;
    }
}
