
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

import com.richstonedt.garnet.common.utils.ExcelVOAttribute;
import com.richstonedt.garnet.model.Resource;
import com.richstonedt.garnet.model.ResourceDynamicProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * The type Resource view.
 */
public class ResourceView {

    private Resource resource;

    private Long typeId;

    @ApiModelProperty(value = "资源类型配置列表")
    private List<ResourceDynamicProperty> resourceDynamicProperties;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * Gets resource.
     *
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets resource.
     *
     * @param resource the resource
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * Gets resource dynamic properties.
     *
     * @return the resource dynamic properties
     */
    public List<ResourceDynamicProperty> getResourceDynamicProperties() {
        return resourceDynamicProperties;
    }

    /**
     * Sets resource dynamic properties.
     *
     * @param resourceDynamicProperties the resource dynamic properties
     */
    public void setResourceDynamicProperties(List<ResourceDynamicProperty> resourceDynamicProperties) {
        this.resourceDynamicProperties = resourceDynamicProperties;
    }
}
