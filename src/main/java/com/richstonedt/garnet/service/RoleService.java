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
import com.richstonedt.garnet.model.Role;
import com.richstonedt.garnet.model.criteria.RoleCriteria;
import com.richstonedt.garnet.model.parm.RoleParm;
import com.richstonedt.garnet.model.view.RoleView;

import java.util.List;

public interface RoleService extends BaseService<Role, RoleCriteria, Long> {

    /**
     * 新增角色
     * @param roleView
     * @return
     */
    Long insertRole(RoleView roleView);

    /**
     * 更新角色
     * @param roleView
     */
    void updateRole(RoleView roleView);

    /**
     * 删除角色及其关联外键
     * @param roleView
     */
    void deleteRole(RoleView roleView);

    PageUtil queryRolesByParms(RoleParm roleParm);

    /**
     * 当用户点击删除时，将其状态设为禁用
     * @param role
     */
    void updateStatusById(Role role);

    /**
     * 获取单个角色及其绑定的groupIds、permissionIds
     * @param id
     * @return
     */
    RoleView selectRoleWithGroupAndPermission(Long id);

    /**
     * 通过租户id查询角色列表
     * @param roleParm
     * @return
     */
    List<Role> queryRolesByTenantId(RoleParm roleParm);

    /**
     * 查询角色列表
     * @return
     */
    List<Role> queryRoles();

    /**
     * 根据应用id查询角色列表
     * @param roleParm
     * @return
     */
    List<Role> queryRolesByApplicationId(RoleParm roleParm);

    /**
     * 根据应用id或租户id查询角色列表
     * @param roleParm
     * @return
     */
    List<Role> queryRolesByParams(RoleParm roleParm);

    /**
     * 查询角色列表
     * @param roleParm
     * @return
     */
    PageUtil getRolesByParams(RoleParm roleParm);

}