
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

import com.richstonedt.garnet.model.RouterGroup;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RouterGroupView {

    private RouterGroup routerGroup;

    @ApiModelProperty(value = "关联的应用ID")
    private String applicationIds;

    @ApiModelProperty(value = "关联的应用的名称")
    private List<String> applicationNames;

    @ApiModelProperty(value = "关联的应用的应用标识")
    private List<String> appCodeList;

    @ApiModelProperty(value = "关联的应用列表")
    private List<Long> applicationIdList;

    public List<Long> getApplicationIdList() {
        return applicationIdList;
    }

    public void setApplicationIdList(List<Long> applicationIdList) {
        this.applicationIdList = applicationIdList;
    }

    public List<String> getAppCodeList() {
        return appCodeList;
    }

    public void setAppCodeList(List<String> appCodeList) {
        this.appCodeList = appCodeList;
    }

    public String getApplicationIds() {
        return applicationIds;
    }

    public void setApplicationIds(String applicationIds) {
        this.applicationIds = applicationIds;
    }

    public List<String> getApplicationNames() {
        return applicationNames;
    }

    public void setApplicationNames(List<String> applicationNames) {
        this.applicationNames = applicationNames;
    }

    public RouterGroup getRouterGroup() {
        return routerGroup;
    }

    public void setRouterGroup(RouterGroup routerGroup) {
        this.routerGroup = routerGroup;
    }
}
