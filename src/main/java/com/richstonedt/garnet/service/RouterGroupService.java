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
import com.richstonedt.garnet.model.RouterGroup;
import com.richstonedt.garnet.model.criteria.RouterGroupCriteria;
import com.richstonedt.garnet.model.parm.RouterGroupParm;
import com.richstonedt.garnet.model.view.RouterGroupView;

public interface RouterGroupService extends BaseService<RouterGroup, RouterGroupCriteria, Long> {

    /**
     * 新增应用组
     * @param routerGroupView
     * @return
     */
    String insertRouterGroup(RouterGroupView routerGroupView);

    /**
     * 更新应用组
     * @param routerGroupView
     */
    void updateRouterGroup(RouterGroupView routerGroupView);

    /**
     * 删除应用组
     * @param routerGroupView
     */
    void deleteRouterGroup(RouterGroupView routerGroupView);

    /**
     * 查询应用组
     * @param routerGroupParm
     * @return
     */
    PageUtil queryRouterGroupByParms(RouterGroupParm routerGroupParm);

    /**
     * 获取单个router白名单及其绑定的applications
     * @param id
     * @return
     */
    RouterGroupView selectRouterByIdWithApp(Long id);

    /**
     * 通过appCode查询其所在的应用组名
     * @param appCode
     * @return
     */
    String getGroupNameByAppCode(String appCode);

    /**
     * 判断应用组名是否已经存在
     * @param groupName
     * @return
     */
    boolean isGroupNameUsed(Long id, String groupName);

    PageUtil getRouterGroupsByParams(RouterGroupParm routerGroupParm);
}