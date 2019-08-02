
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

import java.util.List;

/**
 * The type Permission resource parm.
 */
public class PermissionResourceParm extends BaseParm{

    private List<Long> permissionIds;

    /**
     * Gets permission ids.
     *
     * @return the permission ids
     */
    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    /**
     * Sets permission ids.
     *
     * @param permissionIds the permission ids
     */
    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
