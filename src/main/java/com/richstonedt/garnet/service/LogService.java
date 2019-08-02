
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
import com.richstonedt.garnet.model.Log;
import com.richstonedt.garnet.model.criteria.LogCriteria;
import com.richstonedt.garnet.model.parm.LogParm;
import com.richstonedt.garnet.model.view.LogView;

public interface LogService extends BaseService<Log, LogCriteria, Long> {

    /**
     * 查询系统日志列表
     * @param logParm
     * @return
     */
    PageUtil queryLogsByParms(LogParm logParm);
    PageUtil queryLogsByParmsWithoutLimit(LogParm logParm);
    Long insertLog(LogView logView);

}