
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
//import com.richstonedt.garnet.model.GarLog;
//import com.richstonedt.garnet.service.GarLogService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b><code>LogController</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2017/9/29 14:54
 *
 * @author Sun Jinpeng
 * @version 0.1.0
 * @since garnet -core-be-fe 0.1.0
 */
@RestController
@RequestMapping(value = "/v1.0")
@Api(tags = "[Garnet]查询日志接口")
public class GarLogController {

    /**
     * The Log
     *
     * @since garnet-core-be-fe 0.1.0
     */
//    private static Logger LOG = LoggerFactory.getLogger(GarLogController.class);
//
//    /**
//     * The Log service.
//     *
//     * @since garnet-core-be-fe 0.1.0
//     */
//    @Autowired
//    private GarLogService logService;
//
//    /**
//     * Gets user roles.
//     *
//     * @param page  the page
//     * @param limit the limit
//     * @return the user roles
//     * @since garnet -core-be-fe 0.1.0
//     */
//    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]查询Log列表", notes = "Get log list")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> getAllLogs(
//            @ApiParam(value = "用户名") @RequestParam(value = "searchName", required = false) String searchName,
//            @ApiParam(value = "IP") @RequestParam(value = "ip", required = false) String ip,
//            @ApiParam(value = "开始时间，结束时间 用逗号隔开: 1514736000000,1519833600000")
//            @RequestParam(value = "timeSlots", required = false) String timeSlots,
//            @ApiParam(value = "当前页数", required = true) @RequestParam(value = "page") int page,
//            @ApiParam(value = "每页数量,查询全部请设为 -1", required = true) @RequestParam(value = "limit") int limit) {
//        try {
//            List<GarLog> results = logService.getLogs(searchName, ip, timeSlots, page, limit);
//            PageUtils pageUtils = new PageUtils(results, results.size(), limit, page);
//            return new ResponseEntity<>(pageUtils, HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get log list :", t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
//
//    /**
//     * Gets one log.
//     *
//     * @param id the id
//     * @return the one log
//     * @since garnet -core-be-fe 0.1.0
//     */
//    @RequestMapping(value = "/log/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ApiOperation(value = "[Garnet]查询log详情", notes = "Get detail log")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful query"),
//            @ApiResponse(code = 500, message = "internal server error")})
//    public ResponseEntity<?> getOneLog(@PathVariable(value = "id") Integer id) {
//        try {
//            GarLog log = logService.getLogById(id);
//            return new ResponseEntity<>(log, HttpStatus.OK);
//        } catch (Throwable t) {
//            LOG.error("Failed to Get log :" + id, t);
//            return GarnetUtils.newResponseEntity(t);
//        }
//    }
}