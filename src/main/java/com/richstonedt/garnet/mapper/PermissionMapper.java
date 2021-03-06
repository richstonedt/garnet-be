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
package com.richstonedt.garnet.mapper;

import com.richstonedt.garnet.model.Permission;
import com.richstonedt.garnet.model.criteria.PermissionCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PermissionMapper extends BaseMapper<Permission, PermissionCriteria, Long> {
    Permission selectSingleByCriteria(PermissionCriteria criteria);

    int insertBatchSelective(List<Permission> records);

    int updateBatchByPrimaryKeySelective(List<Permission> records);
}
