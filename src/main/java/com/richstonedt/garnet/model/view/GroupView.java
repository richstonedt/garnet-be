
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

import com.richstonedt.garnet.model.Group;
import com.richstonedt.garnet.model.GroupRole;
import com.richstonedt.garnet.model.GroupUser;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * The type Group view.
 */
public class GroupView {

    private Group group;

    private List<GroupUser> groupUsers;

    private List<GroupRole> groupRoles;

    @ApiModelProperty(value = "选择关联的用户ID")
    private List<Long> userIds;

    @ApiModelProperty(value = "选择关联的角色ID")
    private List<Long> roleIds;

    @ApiModelProperty(value = "类型（应用、租户、租户+应用）")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    /**
     * Gets group.
     *
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets group.
     *
     * @param group the group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Gets group users.
     *
     * @return the group users
     */
    public List<GroupUser> getGroupUsers() {
        return groupUsers;
    }

    /**
     * Sets group users.
     *
     * @param groupUsers the group users
     */
    public void setGroupUsers(List<GroupUser> groupUsers) {
        this.groupUsers = groupUsers;
    }

    /**
     * Gets group roles.
     *
     * @return the group roles
     */
    public List<GroupRole> getGroupRoles() {
        return groupRoles;
    }

    /**
     * Sets group roles.
     *
     * @param groupRoles the group roles
     */
    public void setGroupRoles(List<GroupRole> groupRoles) {
        this.groupRoles = groupRoles;
    }

}
