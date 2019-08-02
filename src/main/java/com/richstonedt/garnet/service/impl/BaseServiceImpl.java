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
package com.richstonedt.garnet.service.impl;

import com.richstonedt.garnet.mapper.BaseMapper;
import com.richstonedt.garnet.service.BaseService;
import java.io.Serializable;
import java.util.List;

public abstract class BaseServiceImpl<T, E, PK extends Serializable> implements BaseService<T, E, PK> {

    public abstract BaseMapper<T, E, PK> getBaseMapper();

    @Override
    public long countByCriteria(E criteria) {
        return getBaseMapper().countByCriteria(criteria);
    }

    @Override
    public int deleteByCriteria(E criteria) {
        return getBaseMapper().deleteByCriteria(criteria);
    }

    @Override
    public int deleteByPrimaryKey(PK id) {
        return getBaseMapper().deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(T record) {
        return getBaseMapper().insertSelective(record);
    }

    @Override
    public int insertBatchSelective(List<T> records) {
        return getBaseMapper().insertBatchSelective(records);
    }

    @Override
    public List<T> selectByCriteria(E criteria) {
        return getBaseMapper().selectByCriteria(criteria);
    }

    @Override
    public T selectByPrimaryKey(PK id) {
        return getBaseMapper().selectByPrimaryKey(id);
    }

    @Override
    public T selectSingleByCriteria(E criteria) {
        return getBaseMapper().selectSingleByCriteria(criteria);
    }

    @Override
    public int updateByCriteriaSelective(T record, E criteria) {
        return getBaseMapper().updateByCriteriaSelective(record,criteria);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return getBaseMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateBatchByPrimaryKeySelective(List<T> records) {
        return getBaseMapper().updateBatchByPrimaryKeySelective(records);
    }
}
