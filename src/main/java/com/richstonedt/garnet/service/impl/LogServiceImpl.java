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

import com.richstonedt.garnet.common.contants.GarnetContants;
import com.richstonedt.garnet.common.utils.IdGeneratorUtil;
import com.richstonedt.garnet.common.utils.PageUtil;
import com.richstonedt.garnet.mapper.BaseMapper;
import com.richstonedt.garnet.mapper.LogMapper;
import com.richstonedt.garnet.model.Log;
import com.richstonedt.garnet.model.criteria.LogCriteria;
import com.richstonedt.garnet.model.parm.LogParm;
import com.richstonedt.garnet.model.view.LogView;
import com.richstonedt.garnet.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Service
@Transactional
public class LogServiceImpl extends BaseServiceImpl<Log, LogCriteria, Long> implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Value("${spring.profiles.active}")
    private String DATABASE_TYPE;

    @Override
    public BaseMapper getBaseMapper() {
        return this.logMapper;
    }


    @Override
    public PageUtil queryLogsByParms(LogParm logParm) {
        //每页加载量
        int pageSize = logParm.getPageSize();
        //当前页
        int pageNumber = logParm.getPageNumber();
        //起始位置
        int startNumber = (pageNumber - 1) * pageSize;

        if (startNumber < 0) {
            startNumber = 0;
        }
        if (pageSize < 0) {
            pageSize = 0;
        }

        LogCriteria logCriteria = new LogCriteria();

        if ("oracle".equals(DATABASE_TYPE)) {
            //查询的起始位置
            logCriteria.setStart(startNumber);
            //查询的结束位置
            int endNumber = startNumber + pageSize;
            logCriteria.setEnd(endNumber);
        } else {
            //查询的起始位置
            logCriteria.setStart(startNumber);
            //查询的记录数
            logCriteria.setEnd(pageSize);
        }

        logCriteria.setOrderByClause(GarnetContants.ORDER_BY_CREATED_TIME + " desc");
        LogCriteria.Criteria criteria = logCriteria.createCriteria();
        LogCriteria logCriteria1 = new LogCriteria();
        LogCriteria.Criteria criteria1 = logCriteria1.createCriteria();
        if(!StringUtils.isEmpty(logParm.getStartTime())) {
            long startTime = Long.parseLong(logParm.getStartTime());
            criteria.andCreatedTimeGreaterThanOrEqualTo(startTime);
        }
        if(!StringUtils.isEmpty(logParm.getEndTime())) {
            long endTime = Long.parseLong(logParm.getEndTime());
            criteria.andCreatedTimeLessThanOrEqualTo(endTime);
        }
        String searchName = logParm.getSearchName();
        String operation = logParm.getOperation();
        String message = logParm.getMessage();
        if (!StringUtils.isEmpty(searchName)) {
            criteria.andUserNameLike("%" + searchName + "%");
            criteria1.andUserNameLike("%" + searchName + "%");
        }
        if (!StringUtils.isEmpty(operation)) {
            criteria.andOperationLike("%" + operation + "%");
            criteria1.andOperationLike("%" + operation + "%");
        }
        if (!StringUtils.isEmpty(message)) {
            criteria.andMessageLike("%" + message + "%");
            criteria1.andMessageLike("%" + message + "%");
        }
        int totalCount = (int) this.countByCriteria(logCriteria1);
        int totalPage;
        if (pageSize != 0) {
            totalPage = (int) Math.ceil((double) totalCount / pageSize);
        } else {
            totalPage = 0;
        }

        return new PageUtil(this.selectByCriteria(logCriteria), totalCount, pageSize, pageNumber, totalPage);
    }
    @Override
    public PageUtil queryLogsByParmsWithoutLimit(LogParm logParm) {



        LogCriteria logCriteria = new LogCriteria();


        logCriteria.setOrderByClause(GarnetContants.ORDER_BY_CREATED_TIME + " desc");
        LogCriteria.Criteria criteria = logCriteria.createCriteria();
        LogCriteria logCriteria1 = new LogCriteria();
        LogCriteria.Criteria criteria1 = logCriteria1.createCriteria();
        if(!StringUtils.isEmpty(logParm.getStartTime())) {
            long startTime = Long.parseLong(logParm.getStartTime());
            criteria.andCreatedTimeGreaterThanOrEqualTo(startTime);
        }
        if(!StringUtils.isEmpty(logParm.getEndTime())) {
            long endTime = Long.parseLong(logParm.getEndTime());
            criteria.andCreatedTimeLessThanOrEqualTo(endTime);
        }
        String searchName = logParm.getSearchName();
        String operation = logParm.getOperation();
        String message = logParm.getMessage();
        if (!StringUtils.isEmpty(searchName)) {
            criteria.andUserNameLike("%" + searchName + "%");
            criteria1.andUserNameLike("%" + searchName + "%");
        }
        if (!StringUtils.isEmpty(operation)) {
            criteria.andOperationLike("%" + operation + "%");
            criteria1.andOperationLike("%" + operation + "%");
        }
        if (!StringUtils.isEmpty(message)) {
            criteria.andMessageLike("%" + message + "%");
            criteria1.andMessageLike("%" + message + "%");
        }
        int totalCount = (int) this.countByCriteria(logCriteria1);
        int totalPage;


        return new PageUtil(this.selectByCriteria(logCriteria));
    }

    @Override
    public Long insertLog(LogView logView) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String cookie = request.getHeader("Cookie");
        String userName =this.getCookieValue(cookie,"username");

        Log log = logView.getLog();

        log.setUserName(userName);
        log.setId(IdGeneratorUtil.generateId());
        Long currentTime = System.currentTimeMillis();
        log.setCreatedTime(currentTime);
        log.setModifiedTime(currentTime);
        this.insertSelective(log);

        return log.getId();
    }
    /**
     * 获取Cookie中存的值
     * @param cookie
     * @param name
     * @return
     */
    private String getCookieValue(String cookie, String name) {
        String n = name + "=";
        for (String s : cookie.split(";")) {
            String s1 = s.trim();
            if (s1.indexOf(n) == 0) {
                return s1.substring(n.length(), s1.length());
            }
        }
        return "";
    }
}