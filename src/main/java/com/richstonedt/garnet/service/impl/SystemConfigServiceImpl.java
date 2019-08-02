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

import com.richstonedt.garnet.common.utils.IdGeneratorUtil;
import com.richstonedt.garnet.common.utils.PageUtil;
import com.richstonedt.garnet.mapper.BaseMapper;
import com.richstonedt.garnet.mapper.SystemConfigMapper;
import com.richstonedt.garnet.model.SystemConfig;
import com.richstonedt.garnet.model.criteria.SystemConfigCriteria;
import com.richstonedt.garnet.model.parm.SystemConfigParm;
import com.richstonedt.garnet.model.view.SystemConfigView;
import com.richstonedt.garnet.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig, SystemConfigCriteria, Long> implements SystemConfigService {
    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public BaseMapper getBaseMapper() {
        return this.systemConfigMapper;
    }

    @Override
    public Long insertSystemConfig(SystemConfigView systemConfigView) {
        SystemConfig systemConfig = systemConfigView.getSystemConfig();
        systemConfig.setId(IdGeneratorUtil.generateId());
        this.insertSelective(systemConfig);
        return systemConfig.getId();
    }

    @Override
    public PageUtil<SystemConfig> querySystemConfigsByParms(SystemConfigParm systemConfigParm) {
        return null;
    }

    @Override
    public SystemConfig selectSystemConfigByParam(String parameter) {
        SystemConfigCriteria systemConfigCriteria = new SystemConfigCriteria();
        systemConfigCriteria.createCriteria().andParameterEqualTo(parameter);
        SystemConfig systemConfig = this.selectSingleByCriteria(systemConfigCriteria);
        return systemConfig;
    }
}