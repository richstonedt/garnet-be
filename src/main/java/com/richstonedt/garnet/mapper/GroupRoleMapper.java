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

import com.richstonedt.garnet.model.GroupRole;
import com.richstonedt.garnet.model.criteria.GroupRoleCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Mapper
public interface GroupRoleMapper extends BaseMapper<GroupRole, GroupRoleCriteria, Long> {
    GroupRole selectSingleByCriteria(GroupRoleCriteria criteria);

    int insertBatchSelective(List<GroupRole> records);

    int updateBatchByPrimaryKeySelective(List<GroupRole> records);
}
