
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

//import com.richstonedt.garnet.common.exception.GarnetServiceErrorCodes;
//import com.richstonedt.garnet.common.exception.GarnetServiceException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b><code>GarnetUtils</code></b>
 * <p/>
 * Garnet Restful Service Utils
 * <p/>
 * <b>Creation Time:</b> 2016年5月30日 上午10:56:08
 *
 * @author chenzechao
 * @since Garnet 1.0.0
 */
public class GarnetUtils {

    /**
     * 返回带异常信息的ResponseEntity
     *
     * @param t the t
     * @return the response entity
     * @since Garnet 1.0.0
     */
//    public static ResponseEntity<Map<String, Object>> newResponseEntity(Throwable t) {
//        Map<String, Object> errorMap = new HashMap<>();
//        if (t instanceof GarnetServiceException) {
//            GarnetServiceException e = (GarnetServiceException) t;
//            if (!StringUtils.isEmpty(e.getErrorCode())) {
//                errorMap.put("errorCode", e.getErrorCode());
//            } else {
//                errorMap.put("errorCode", GarnetServiceErrorCodes.OTHER);
//            }
//        } else if (t instanceof IllegalArgumentException) {
//            errorMap.put("errorCode", GarnetServiceErrorCodes.ILLEGAL_ARGUMENT);
//        } else {
//            errorMap.put("errorCode", GarnetServiceErrorCodes.OTHER);
//        }
//        errorMap.put("errorMessage", t.getMessage());
//        return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    /**
//     * Get role list list.
//     *
//     * @param ids the selected role ids
//     * @return the list
//     * @since garnet-core-be-fe 1.0.0
//     */
//    public static List<Long> converStringToList(String ids) {
//        List<Long> roleIds = new ArrayList<>();
//        if (!StringUtils.isEmpty(ids)) {
//            if (ids.contains(",")) {
//                String[] tmpIds = ids.split(",");
//                for (String id : tmpIds) {
//                    if (StringUtils.isNotEmpty(id)) {
//                        roleIds.add(Long.valueOf(id));
//                    }
//                }
//            } else {
//                roleIds.add(Long.valueOf(ids));
//            }
//        }
//        return roleIds;
//    }

}
