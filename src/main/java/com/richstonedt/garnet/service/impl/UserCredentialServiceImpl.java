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
import com.richstonedt.garnet.mapper.UserCredentialMapper;
import com.richstonedt.garnet.model.User;
import com.richstonedt.garnet.model.UserCredential;
import com.richstonedt.garnet.model.criteria.UserCredentialCriteria;
import com.richstonedt.garnet.service.UserCredentialService;
import com.richstonedt.garnet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
public class UserCredentialServiceImpl extends BaseServiceImpl<UserCredential, UserCredentialCriteria, Long> implements UserCredentialService {
    @Autowired
    private UserCredentialMapper userCredentialMapper;

    @Autowired
    private UserService userService;

    @Override
    public BaseMapper getBaseMapper() {
        return this.userCredentialMapper;
    }

    @Override
    public UserCredential getCredentialByUserName(String userName) {
        User user = userService.getUserByUserName(userName);
        if (ObjectUtils.isEmpty(user)) {
            return new UserCredential();
        }

        Long userId = user.getId();
        UserCredentialCriteria userCredentialCriteria = new UserCredentialCriteria();
        userCredentialCriteria.createCriteria().andUserIdEqualTo(userId);
        UserCredential userCredential = this.selectSingleByCriteria(userCredentialCriteria);
        return userCredential;
    }
}