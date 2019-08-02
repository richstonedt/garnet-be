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

import com.richstonedt.garnet.model.SystemConfig;
import com.richstonedt.garnet.model.view.SystemConfigView;
import com.richstonedt.garnet.service.SystemConfigService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemConfigTest {
    @Autowired
    private SystemConfigService systemConfigService;

    @Test
    public void test1InsertSystemConfig() {
        SystemConfigView systemConfigView = new SystemConfigView();
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setParameter("mode");
        systemConfig.setValue("paas");
        systemConfigView.setSystemConfig(systemConfig);
        Long id = systemConfigService.insertSystemConfig(systemConfigView);

        SystemConfig systemConfig1 = systemConfigService.selectByPrimaryKey(id);
        Assert.assertEquals(systemConfig1.getValue(), "paas");
        systemConfigService.deleteByPrimaryKey(id);
    }

    @Test
    public void test2SelectSystemConfig() {
        SystemConfig systemConfig = systemConfigService.selectSystemConfigByParam("mode");
        Assert.assertEquals(systemConfig.getValue(), "all");
    }

}
