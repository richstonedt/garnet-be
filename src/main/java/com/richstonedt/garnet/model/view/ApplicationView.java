
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

package com.richstonedt.garnet.model.view;

import com.richstonedt.garnet.model.Application;
import com.richstonedt.garnet.model.ApplicationTenant;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * The type Application view.
 */
public class ApplicationView {

    private Application application;

    private List<ApplicationTenant> applicationTenants;

    @ApiModelProperty(value = "租户ID列表")
    private List<Long> tenantIdList;

    @ApiModelProperty(value = "租户名称列表")
    private List<String> tenantNameList;

    @ApiModelProperty(value = "租户ID,用','隔开")
    private String tenantIds;

    @ApiModelProperty(value = "登录用户Id")
    private Long loginUserId;

    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

    /**
     * Gets application.
     *
     * @return the application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Sets application.
     *
     * @param application the application
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * Gets application tenants.
     *
     * @return the application tenants
     */
    public List<ApplicationTenant> getApplicationTenants() {
        return applicationTenants;
    }

    /**
     * Sets application tenants.
     *
     * @param applicationTenants the application tenants
     */
    public void setApplicationTenants(List<ApplicationTenant> applicationTenants) {
        this.applicationTenants = applicationTenants;
    }

    public List<Long> getTenantIdList() {
        return tenantIdList;
    }

    public void setTenantIdList(List<Long> tenantIdList) {
        this.tenantIdList = tenantIdList;
    }

    public List<String> getTenantNameList() {
        return tenantNameList;
    }

    public void setTenantNameList(List<String> tenantNameList) {
        this.tenantNameList = tenantNameList;
    }

    public String getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(String tenantIds) {
        this.tenantIds = tenantIds;
    }
}
