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

import com.richstonedt.garnet.mapper.ApplicationTenantMapper;
import com.richstonedt.garnet.mapper.BaseMapper;
import com.richstonedt.garnet.model.ApplicationTenant;
import com.richstonedt.garnet.model.criteria.ApplicationTenantCriteria;
import com.richstonedt.garnet.service.ApplicationTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationTenantServiceImpl extends BaseServiceImpl<ApplicationTenant, ApplicationTenantCriteria, Long> implements ApplicationTenantService {

    @Autowired
    private ApplicationTenantMapper applicationTenantMapper;

    @Override
    public BaseMapper getBaseMapper() {
        return this.applicationTenantMapper;
    }
}