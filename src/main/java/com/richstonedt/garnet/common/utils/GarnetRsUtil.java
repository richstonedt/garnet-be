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

package com.richstonedt.garnet.common.utils;

import com.richstonedt.garnet.config.GarnetServiceErrorCodes;
import com.richstonedt.garnet.config.GarnetServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b><code>GarnetRsUtil</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2017/10/17 11:26
 *
 * @author Sun Jinpeng
 * @version 0.1.0
 * @since garnet-core-be-fe 0.1.0
 */
public class GarnetRsUtil {

    /**
     * New response entity response entity.
     *
     * @param t the t
     * @return the response entity
     * @since garnet-core-be-fe 0.1.0
     */
    public static ResponseEntity<Map<String, Object>> newResponseEntity(
            Throwable t) {

        String errorCode = "errorCode";
        Map<String, Object> errorMap = new HashMap<>(8);
        if (t instanceof GarnetServiceException) {
            GarnetServiceException e = (GarnetServiceException) t;
            if (!StringUtils.isEmpty(e.getErrorCode())) {
                errorMap.put(errorCode, e.getErrorCode());
            } else {
                errorMap.put(errorCode, GarnetServiceErrorCodes.OTHER);
            }
        } else {
            errorMap.put(errorCode, GarnetServiceErrorCodes.OTHER);
        }
        errorMap.put("errorMessage", t.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Get role list list.
     *
     * @param ids the ids
     * @return the list
     * @since garnet-core-be-fe 0.1.0
     */
    public static List<Long> parseStringToList(String ids) {
        List<Long> idList = new ArrayList<>();
        if (StringUtils.isEmpty(ids)) {
            return idList;
        }
        try {
            String comma = ",";
            if (ids.contains(comma)) {
                String[] tmpIds = ids.split(",");
                for (String id : tmpIds) {
                    if (!StringUtils.isEmpty(id)) {
                        idList.add(Long.valueOf(id));
                    }
                }
            } else {
                idList.add(Long.valueOf(ids));
            }
        } catch (NumberFormatException e) {
            String errorMessage = "The parameter is error,Please input number! ids = " + ids;
            throw new GarnetServiceException(errorMessage);
        }
        return idList;
    }
}
