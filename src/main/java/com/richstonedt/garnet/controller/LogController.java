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

import com.richstonedt.garnet.common.utils.PageUtil;
import com.richstonedt.garnet.exception.GarnetServiceExceptionUtils;
import com.richstonedt.garnet.interceptory.LoginRequired;
import com.richstonedt.garnet.model.Log;
import com.richstonedt.garnet.model.Tenant;
import com.richstonedt.garnet.model.message.*;
import com.richstonedt.garnet.model.parm.LogParm;
import com.richstonedt.garnet.model.view.LogView;
import com.richstonedt.garnet.model.view.TenantView;
import com.richstonedt.garnet.service.LogService;
import io.swagger.annotations.*;
import com.richstonedt.garnet.common.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.net.URLEncoder;

/**
 * <b><code>LogController</code></b>
 * <p/>
 * Log的具体实现
 * <p/>
 * <b>Creation Time:</b> Thu May 31 14:42:54 CST 2018.
 *
 * @author maxuepeng
 * @version 1.0.0
 * @since torinosrc-rs 1.0.0
 */
@Api(value = "[Garnet]系统日志接口")
@LoginRequired
@RestController
@RequestMapping(value = "/api/v1.0")
public class LogController {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory
            .getLogger(LogController.class);

    /** The service. */
    @Autowired
    private LogService logService;

    @ApiOperation(value = "[Garnet]创建系统日志", notes = "创建一个系统日志")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation", responseHeaders = @ResponseHeader(name = "location", description = "URL of new created resource", response = String.class) ),
            @ApiResponse(code = 409, message = "conflict"),
            @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/logs", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createTenant(
            @ApiParam(value = "access token", required = false) @RequestParam(value = "token", defaultValue = "", required = false) String token,
            @ApiParam(value = "日志", required = true) @RequestBody LogView logView,
            UriComponentsBuilder ucBuilder) {
        String error = "Failed to add entity! " + MessageDescription.OPERATION_INSERT_FAILURE;
        try {
            // 保存实体
            Long id = logService.insertLog(logView);
            // 获取刚刚保存的实体
            Log log = logService.selectByPrimaryKey(id);
            LogView logView1 = new LogView();
            logView1.setLog(log);
            // 设置http的headers
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/api/v1/logs/{id}")
                    .buildAndExpand(id).toUri());
            // 封装返回信息
            GarnetMessage<LogView> torinoSrcMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_INSERT_SUCCESS, logView1);
            return new ResponseEntity<>(torinoSrcMessage, headers, HttpStatus.CREATED);
        } catch (Throwable t) {
            error = t.getMessage();
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    @ApiOperation(value = "[Garnet]删除系统日志", notes = "通过id删除系统日志")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/logs/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteLog(
            @ApiParam(value = "系统日志id", required = true) @PathVariable(value = "id") Long id) {
        try {
            logService.deleteByPrimaryKey(id);
            // 封装返回信息
            GarnetMessage<LogView> torinoSrcMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_DELETE_SUCCESS, null);
            return new ResponseEntity<>(torinoSrcMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to delete entity! " + MessageDescription.OPERATION_DELETE_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    @ApiOperation(value = "[Garnet]删除系统日志", notes = "批量删除系统日志")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/logs", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteLogs(
            @ApiParam(value = "系统日志ids，样例 - 1,2,3", required = true) @RequestBody String ids) {
        try {
            for (String id : ids.split(",")) {
                logService.deleteByPrimaryKey(Long.parseLong(id));
            }
            // 封装返回信息
            GarnetMessage<LogView> torinoSrcMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_DELETE_SUCCESS, null);
            return new ResponseEntity<>(torinoSrcMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to delete entities! " + MessageDescription.OPERATION_DELETE_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    @ApiOperation(value = "[Garnet]获取单个系统日志", notes = "通过id获取系统日志")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/logs/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getLog(
            @ApiParam(value = "系统日志id", required = true) @PathVariable(value = "id") Long id) {
        try {
            final Log log = logService.selectByPrimaryKey(id);
            // 封装返回信息
            GarnetMessage<Log> torinoSrcMessage = MessageUtils.setMessage(MessageCode.SUCCESS, MessageStatus.SUCCESS, MessageDescription.OPERATION_QUERY_SUCCESS, log);
            return new ResponseEntity<>(torinoSrcMessage, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to get entity!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }

    @ApiOperation(value = "[Garnet]获取系统日志列表", notes = "通过查询条件获取系统日志列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getLogs(
            @ApiParam(value = "查询条件", defaultValue = "", required = false) @RequestParam(value = "searchName", defaultValue = "", required = false) String searchName,
            @RequestParam(value = "operation", defaultValue = "", required = false) String operation,
            @RequestParam(value = "message", defaultValue = "", required = false) String message,
            @RequestParam(value = "startTime",defaultValue = "", required = false) String startTime,
            @RequestParam(value = "endTime",defaultValue = "", required = false) String endTime,
            @ApiParam(value = "页数", defaultValue = "0", required = false) @RequestParam(value = "page", defaultValue = "0", required = false) Integer pageNumber,
            @ApiParam(value = "每页加载量", defaultValue = "10", required = false) @RequestParam(value = "limit", defaultValue = "10", required = false) Integer pageSize) {
        try {

            LogParm logParm = new LogParm();
            logParm.setEndTime(endTime);
            logParm.setStartTime(startTime);
            logParm.setPageSize(pageSize);
            logParm.setPageNumber(pageNumber);
            logParm.setSearchName(searchName);
            logParm.setOperation(operation);
            logParm.setMessage(message);
            PageUtil pageInfo = logService.queryLogsByParms(logParm);
            // 封装返回信息
            return new ResponseEntity<>(pageInfo, HttpStatus.OK);
        } catch (Throwable t) {
            String error = "Failed to get entities!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }

    }

    // produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    // "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
    @ApiOperation(value = "[Garnet]将数据导出到excel", notes = "查询到的数据导出到excel")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful request"),
            @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/logs/export", method = RequestMethod.GET, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
    public ResponseEntity<?> export(
            HttpServletResponse response,
            @ApiParam(value = "查询条件", defaultValue = "", required = false) @RequestParam(value = "searchName", defaultValue = "", required = false) String searchName,
            @RequestParam(value = "operation", defaultValue = "", required = false) String operation,
            @RequestParam(value = "message", defaultValue = "", required = false) String message,
            @RequestParam(value = "startTime",defaultValue = "", required = false) String startTime,
            @RequestParam(value = "endTime",defaultValue = "", required = false) String endTime
              ){
        try {

            LogParm logParm = new LogParm();
            logParm.setEndTime(endTime);
            logParm.setStartTime(startTime);
            logParm.setSearchName(searchName);
            logParm.setOperation(operation);
            logParm.setMessage(message);
            PageUtil pageInfo = logService.queryLogsByParmsWithoutLimit(logParm);
            ExcelUtils<Log> excel = new ExcelUtils<>(Log.class);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            excel.exportExcel(pageInfo.getList(),"系统日志", out, ExcelUtils.ExcelVersion.EXCEL_VERSION_07);
            ByteArrayInputStream inStream = new ByteArrayInputStream(out.toByteArray());

            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode("系统日志.xls", "UTF-8"));
            headers.add("Content-Disposition", "attachment; filename=SystemLog.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(new InputStreamResource(inStream));

            /*
            String path=this.getClass().getClassLoader().getResource("static/excelservice/").getPath();
            String path1=new Date().getTime()+".xlsx";
            File file=new File(path+path1);
            file.createNewFile();
            FileOutputStream FO=null;
            try {
                FO= new FileOutputStream(file);
            }catch (FileNotFoundException e){
                LOG.error(e.getMessage(),e);
            }
            ExcelUtils<Log> e=new ExcelUtils<>(Log.class);
            e.exportExcel(pageInfo.getList(),"日志表",FO, ExcelUtils.ExcelVersion.EXCEL_VERSION_07);
            File file1=new File(path+path1);
            if (file1.exists()) {

                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("日志表.xlsx", "UTF-8"));

                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file1);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("Download the song successfully!");
                }
                catch (Exception e1) {
                    LOG.error(e1.getMessage(),e1);
                }
                finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e2) {
                            LOG.error(e2.getMessage(),e2);
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e3) {
                            LOG.error(e3.getMessage(),e3);
                        }
                    }
                }
            }

            // 封装返回信息
            return new ResponseEntity<>( HttpStatus.OK);
            */
        } catch (Throwable t) {
            String error = "Failed to get entities!" + MessageDescription.OPERATION_QUERY_FAILURE;
            LOG.error(error, t);
            GarnetMessage<GarnetErrorResponseMessage> torinoSrcMessage = MessageUtils.setMessage(MessageCode.FAILURE, MessageStatus.ERROR, error, new GarnetErrorResponseMessage(t.toString()));
            return GarnetServiceExceptionUtils.getHttpStatusWithResponseGarnetMessage(torinoSrcMessage, t);
        }
    }


}
