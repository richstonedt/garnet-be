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

import java.io.Serializable;
import java.util.List;

public interface BaseService<T, E, PK extends Serializable> {
    long countByCriteria(E criteria);

    int deleteByCriteria(E criteria);

    int deleteByPrimaryKey(PK id);

    int insertSelective(T record);

    int insertBatchSelective(List<T> records);

    List<T> selectByCriteria(E criteria);

    T selectByPrimaryKey(PK id);

    T selectSingleByCriteria(E criteria);

    int updateByCriteriaSelective(T record, E criteria);

    int updateByPrimaryKeySelective(T record);

    int updateBatchByPrimaryKeySelective(List<T> records);
}
