
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

import com.richstonedt.garnet.model.ResourceDynamicProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.redis.core.mapping.RedisPersistentProperty;

import java.util.List;

public class ResourceDynamicPropertyView {

    private ResourceDynamicProperty resourceDynamicProperty;

    @ApiModelProperty(value = "资源类型配置列表")
    private List<ResourceDynamicProperty> resourceDynamicPropertyList;

    public List<ResourceDynamicProperty> getResourceDynamicPropertyList() {
        return resourceDynamicPropertyList;
    }

    public void setResourceDynamicPropertyList(List<ResourceDynamicProperty> resourceDynamicPropertyList) {
        this.resourceDynamicPropertyList = resourceDynamicPropertyList;
    }

    public ResourceDynamicProperty getResourceDynamicProperty() {
        return resourceDynamicProperty;
    }

    public void setResourceDynamicProperty(ResourceDynamicProperty resourceDynamicProperty) {
        this.resourceDynamicProperty = resourceDynamicProperty;
    }
}
