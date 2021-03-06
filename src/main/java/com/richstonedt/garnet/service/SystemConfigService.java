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
package com.richstonedt.garnet.service;

import com.richstonedt.garnet.common.utils.PageUtil;
import com.richstonedt.garnet.model.SystemConfig;
import com.richstonedt.garnet.model.criteria.SystemConfigCriteria;
import com.richstonedt.garnet.model.parm.SystemConfigParm;
import com.richstonedt.garnet.model.view.SystemConfigView;

public interface SystemConfigService extends BaseService<SystemConfig, SystemConfigCriteria, Long> {

    Long insertSystemConfig(SystemConfigView systemConfigView);

    PageUtil<SystemConfig> querySystemConfigsByParms(SystemConfigParm systemConfigParm);

    /**
     * 通过parameter 获取 systemConfig
     * @param parameter
     * @return
     */
    SystemConfig selectSystemConfigByParam(String parameter);

}