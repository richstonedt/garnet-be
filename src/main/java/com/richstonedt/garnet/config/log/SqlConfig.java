
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

import ch.qos.logback.classic.spi.LoggingEvent;

import java.util.*;

/**
 * <b><code>SqlConfig</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2017/10/10 17:49
 *
 * @author Sun Jinpeng
 * @version 0.1.0
 * @since garnet-core-be-fe 0.1.0
 */
class SqlConfig {

    /**
     * The constant sqlWithRequest.
     *
     * @since gempile-model 0.1.0
     */
    private static Map<String, Map<String, List<String>>> sqlWithRequest = new HashMap<>();

    /**
     * Return the SqlWithRequest
     *
     * @return property value of sqlWithRequest
     * @since gempile-model 0.1.0
     */
    static Map<String, List<String>> getSqlWithRequest(String key) {
        if (sqlWithRequest.keySet().contains(key)) {
            Map<String, List<String>> result = sqlWithRequest.get(key);
            sqlWithRequest.remove(key);
            return result;
        } else {
            return null;
        }
    }

    /**
     * Set the SqlWithRequest
     *
     * @since gempile-model 0.1.0
     */
    static void setSqlWithRequest(LoggingEvent le,String key) {
        Set<String> keys = sqlWithRequest.keySet();
        if (!keys.contains(key)) {
            Map<String, List<String>> sqlWithPackageName = new HashMap<>();
            List<String> sql = new ArrayList<>();
            sql.add(le.getMessage());
            sqlWithPackageName.put(le.getLoggerName(), sql);
            sqlWithRequest.put(key, sqlWithPackageName);
        } else {
            Map<String, List<String>> sqlWithPackageName = sqlWithRequest.get(key);
            Set<String> sqlKeys = sqlWithPackageName.keySet();
            if (sqlKeys.contains(le.getLoggerName())) {
                List<String> sql = sqlWithPackageName.get(le.getLoggerName());
                sql.add(le.getMessage());
            } else {
                List<String> sql = new ArrayList<>();
                sql.add(le.getMessage());
                sqlWithPackageName.put(le.getLoggerName(), sql);
            }
        }
    }
}
