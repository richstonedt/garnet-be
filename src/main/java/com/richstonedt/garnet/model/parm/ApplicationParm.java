
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

import com.richstonedt.garnet.model.Application;
import io.swagger.annotations.ApiModelProperty;

/**
 * The type Application parm.
 */
public class ApplicationParm extends BaseParm{

    private Application application;

    private String mode;

    private Integer modeId;

    private Long routerGroupId;

    @ApiModelProperty(value = "资源路径")
    private String path;

    @ApiModelProperty(value = "标志请求是加载列表数据还是应用树")
    private String queryOrTree;

    public String getQueryOrTree() {
        return queryOrTree;
    }

    public void setQueryOrTree(String queryOrTree) {
        this.queryOrTree = queryOrTree;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getRouterGroupId() {
        return routerGroupId;
    }

    public void setRouterGroupId(Long routerGroupId) {
        this.routerGroupId = routerGroupId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getModeId() {
        return modeId;
    }

    public void setModeId(Integer modeId) {
        this.modeId = modeId;
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

}
