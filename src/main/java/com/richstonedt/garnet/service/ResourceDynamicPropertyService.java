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
import com.richstonedt.garnet.model.ResourceDynamicProperty;
import com.richstonedt.garnet.model.criteria.ResourceDynamicPropertyCriteria;
import com.richstonedt.garnet.model.parm.ResourceDynamicPropertyParm;
import com.richstonedt.garnet.model.view.ResourceDynamicPropertyView;

import javax.validation.constraints.Max;
import java.util.List;

public interface ResourceDynamicPropertyService extends BaseService<ResourceDynamicProperty, ResourceDynamicPropertyCriteria, Long> {

    /**
     * 新增资源配置类型
     * @param resourceDynamicPropertyView
     */
    void insertResourceDynamicProperty(ResourceDynamicPropertyView resourceDynamicPropertyView);

    /**
     * 更新资源配置类型
     * @param resourceDynamicPropertyView
     */
    void updateResourceDynamicProperty(ResourceDynamicPropertyView resourceDynamicPropertyView);

    /**
     * 查询资源配置类型
     * @param resourceDynamicPropertyParm
     * @return
     */
    PageUtil queryResourceDynamicPropertySByParms(ResourceDynamicPropertyParm resourceDynamicPropertyParm);

    /**
     * 通过type删除资源配置
     * @param resourceDynamicPropertyView
     */
    void deleteResourceDynamicPropertyWithType(ResourceDynamicPropertyView resourceDynamicPropertyView);

    /**
     * 通过id查询资源配置
     * @param id
     * @return
     */
    ResourceDynamicPropertyView selectResourceDynamicPropertyViewById(Long id);

    /**
     * 通过type查询资源配置
     * @param type
     * @return
     */
    ResourceDynamicPropertyView selectResourceDynamicPropertyViewByType(String type);

    /**
     * 检查type是否已被使用
     * @param id
     * @param type
     * @return
     */
    boolean isTypeUsed(Long id, String type);

    /**
     * 检查名称是否已被使用
     * @param id
     * @param name
     * @return
     */
    boolean isResourceDyPropNameUsed(Long id, String name);

    /**
     * 根据租户id和应用id返回资源配置列表
     * @param resourceDynamicPropertyParm
     * @return
     */
    List<ResourceDynamicProperty> getResourceDynamicPropertyByTIdAndAId(ResourceDynamicPropertyParm resourceDynamicPropertyParm);

    PageUtil getResourceDynamicPropertiesByParams(ResourceDynamicPropertyParm resourceDynamicPropertyParm);
}