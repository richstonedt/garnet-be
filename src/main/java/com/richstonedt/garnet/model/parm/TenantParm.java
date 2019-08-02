
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

package com.richstonedt.garnet.model.parm;

import com.richstonedt.garnet.model.Tenant;
import io.swagger.annotations.ApiModelProperty;

/**
 * The type Tenant parm.
 */
public class TenantParm extends BaseParm {

    private Tenant tenant;

    private Long applicationId;

    private String mode;

    @ApiModelProperty(value = "标志请求是加载列表数据还是应用树")
    private String queryOrTree;

    public String getQueryOrTree() {
        return queryOrTree;
    }

    public void setQueryOrTree(String queryOrTree) {
        this.queryOrTree = queryOrTree;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private Integer modeId; //0为SAAS 1为PAAS

    public Integer getModeId() {
        return modeId;
    }

    public void setModeId(Integer modeId) {
        this.modeId = modeId;
    }

    /**
     * Gets tenant.
     *
     * @return the tenant
     */
    public Tenant getTenant() {
        return tenant;
    }

    /**
     * Sets tenant.
     *
     * @param tenant the tenant
     */
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }


    @Override
    public Long getApplicationId() {
        return applicationId;
    }

    @Override
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
}
