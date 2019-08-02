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

package com.richstonedt.garnet;

import com.richstonedt.garnet.common.contants.GarnetContants;
import com.richstonedt.garnet.common.utils.PageUtil;
import com.richstonedt.garnet.model.Application;
import com.richstonedt.garnet.model.Resource;
import com.richstonedt.garnet.model.ResourceDynamicProperty;
import com.richstonedt.garnet.model.Tenant;
import com.richstonedt.garnet.model.criteria.ResourceDynamicPropertyCriteria;
import com.richstonedt.garnet.model.parm.ResourceDynamicPropertyParm;
import com.richstonedt.garnet.model.view.ApplicationView;
import com.richstonedt.garnet.model.view.ResourceDynamicPropertyView;
import com.richstonedt.garnet.model.view.ResourceView;
import com.richstonedt.garnet.model.view.TenantView;
import com.richstonedt.garnet.service.ApplicationService;
import com.richstonedt.garnet.service.ResourceDynamicPropertyService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceDynamicTest {

    @Autowired
    private ResourceDynamicPropertyService resourceDynamicPropertyService;


    @Before
    public void inittestData() {

        ResourceDynamicProperty resourceDynamicProperty = new ResourceDynamicProperty();
        ResourceDynamicPropertyView resourceDynamicPropertyView = new ResourceDynamicPropertyView();
        resourceDynamicProperty.setType("test_resourceDynamicProperty");
        resourceDynamicProperty.setDescription("true/false");
        resourceDynamicProperty.setFiledName("varchar00");
        resourceDynamicPropertyView.setResourceDynamicProperty(resourceDynamicProperty);
        List<ResourceDynamicProperty> resourceDynamicPropertyList = new ArrayList<>();
        resourceDynamicPropertyList.add(resourceDynamicProperty);
        resourceDynamicPropertyView.setResourceDynamicPropertyList(resourceDynamicPropertyList);
        resourceDynamicPropertyService.insertResourceDynamicProperty(resourceDynamicPropertyView);
    }

    @After
    public void dealInitTestData() {

        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria = new ResourceDynamicPropertyCriteria();
        resourceDynamicPropertyCriteria.createCriteria().andTypeEqualTo("test_resourceDynamicProperty");
        resourceDynamicPropertyService.deleteByCriteria(resourceDynamicPropertyCriteria);

    }

    @Test
    public void contextLoads() {}

    @Test
    public void test1QueryResourceDynamicPropertiesByParms() {
        //查询去重后的动态资源配置列表
        ResourceDynamicPropertyParm resourceDynamicPropertyParm = new ResourceDynamicPropertyParm();
        resourceDynamicPropertyParm.setPageNumber(1);
        resourceDynamicPropertyParm.setPageSize(1000);
        resourceDynamicPropertyParm.setUserId(GarnetContants.GARNET_USER_ID);
        PageUtil pageUtil = resourceDynamicPropertyService.queryResourceDynamicPropertySByParms(resourceDynamicPropertyParm);

        Assert.assertEquals(pageUtil.getList().size(), 4);
    }

    @Test
    public void test2InsertResourceDynamicProperty() {
        ResourceDynamicProperty resourceDynamicProperty = new ResourceDynamicProperty();
        ResourceDynamicPropertyView resourceDynamicPropertyView = new ResourceDynamicPropertyView();

        resourceDynamicProperty.setType("test_resourceDynamicProperty_insert");
        resourceDynamicProperty.setDescription("true/false");
        resourceDynamicProperty.setFiledName("varchar00");
        resourceDynamicPropertyView.setResourceDynamicProperty(resourceDynamicProperty);
        List<ResourceDynamicProperty> resourceDynamicPropertyList = new ArrayList<>();
        resourceDynamicPropertyList.add(resourceDynamicProperty);
        resourceDynamicPropertyView.setResourceDynamicPropertyList(resourceDynamicPropertyList);
        resourceDynamicPropertyService.insertResourceDynamicProperty(resourceDynamicPropertyView);

        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria = new ResourceDynamicPropertyCriteria();
        resourceDynamicPropertyCriteria.createCriteria().andTypeEqualTo("test_resourceDynamicProperty_insert");
        List<ResourceDynamicProperty> resourceDynamicPropertyList1 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria);

        Assert.assertEquals(resourceDynamicPropertyList1.size(), 1);

        resourceDynamicPropertyService.deleteByCriteria(resourceDynamicPropertyCriteria);
        List<ResourceDynamicProperty> resourceDynamicPropertyList2 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria);
        Assert.assertEquals(resourceDynamicPropertyList2.size(), 0);
    }

    @Test
    public void test3UpdateResourceDynamicProperty() {
        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria = new ResourceDynamicPropertyCriteria();
        resourceDynamicPropertyCriteria.createCriteria().andTypeEqualTo("test_resourceDynamicProperty");
        List<ResourceDynamicProperty> resourceDynamicPropertyList = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria);
        Assert.assertEquals(resourceDynamicPropertyList.size(), 1);

        ResourceDynamicProperty resourceDynamicProperty = resourceDynamicPropertyList.get(0);
        resourceDynamicProperty.setType("test_resourceDynamicProperty_update");
        ResourceDynamicPropertyView resourceDynamicPropertyView = new ResourceDynamicPropertyView();
        resourceDynamicPropertyView.setResourceDynamicProperty(resourceDynamicProperty);
        List<ResourceDynamicProperty> resourceDynamicProperties = new ArrayList<>();
        resourceDynamicProperties.add(resourceDynamicProperty);
        resourceDynamicPropertyView.setResourceDynamicPropertyList(resourceDynamicProperties);

        resourceDynamicPropertyService.updateResourceDynamicProperty(resourceDynamicPropertyView);

        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria1 = new ResourceDynamicPropertyCriteria();
        resourceDynamicPropertyCriteria1.createCriteria().andTypeEqualTo("test_resourceDynamicProperty_update");
        List<ResourceDynamicProperty> resourceDynamicPropertyList1 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria1);
        Assert.assertEquals(resourceDynamicPropertyList1.size(), 1);

        resourceDynamicPropertyService.deleteByCriteria(resourceDynamicPropertyCriteria1);
        List<ResourceDynamicProperty> resourceDynamicPropertyList2 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria1);
        Assert.assertEquals(resourceDynamicPropertyList2.size(), 0);
    }

    @Test
    public void test4DeleteResourceDynamicProperty() {
        ResourceDynamicPropertyCriteria resourceDynamicPropertyCriteria = new ResourceDynamicPropertyCriteria();
        resourceDynamicPropertyCriteria.createCriteria().andTypeEqualTo("test_resourceDynamicProperty");
        List<ResourceDynamicProperty> resourceDynamicPropertyList = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria);
        Assert.assertEquals(resourceDynamicPropertyList.size(), 1);
        ResourceDynamicPropertyView resourceDynamicPropertyView = new ResourceDynamicPropertyView();
        resourceDynamicPropertyView.setResourceDynamicProperty(resourceDynamicPropertyList.get(0));
        resourceDynamicPropertyService.deleteResourceDynamicPropertyWithType(resourceDynamicPropertyView);

        List<ResourceDynamicProperty> resourceDynamicPropertyList1 = resourceDynamicPropertyService.selectByCriteria(resourceDynamicPropertyCriteria);
        Assert.assertEquals(resourceDynamicPropertyList1.size(), 0);
    }

    @Test
    public void testSelectResourceDynamicPropertyViewById() {
        ResourceDynamicPropertyView resourceDynamicPropertyView = resourceDynamicPropertyService.selectResourceDynamicPropertyViewById(1L);
        ResourceDynamicProperty resourceDynamicProperty = resourceDynamicPropertyView.getResourceDynamicProperty();
        Assert.assertEquals(resourceDynamicProperty.getType(), "garnet_appCode");
    }
}
