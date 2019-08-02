
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

package com.richstonedt.garnet.dao;

//import com.richstonedt.garnet.model.GarUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <b><code>GarUserDao</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2017/8/29 14:51
 *
 * @author Sun Jinpeng
 * @version 1.0.0
 * @since garnet-core-be-fe 1.0.0
 */
@Mapper
public interface GarUserDao {

    /**
     * Gets user list.
     *
     * @param searchName the search name
     * @param limit      the limit
     * @param offset     the offset
     * @return the user list
     * @since garnet-core-be-fe 1.0.0
     */
//    List<GarUser> getUserList(@Param(value = "searchName") String searchName, @Param(value = "limit") Integer limit, @Param(value = "offset") Integer offset);
//
//    /**
//     * Gets user by id.
//     *
//     * @param userId the user id
//     * @return the user by id
//     * @since garnet-core-be-fe 1.0.0
//     */
//    GarUser getUserById(@Param(value = "userId") Long userId);
//
//    /**
//     * Save garUser.
//     *
//     * @param garUser the garUser
//     * @since garnet-core-be-fe 1.0.0
//     */
//    void saveUser(GarUser garUser);
//
//    /**
//     * Update garUser.
//     *
//     * @param garUser the garUser
//     * @since garnet-core-be-fe 1.0.0
//     */
//    void updateUser(GarUser garUser);
//
//    /**
//     * Delete users.
//     *
//     * @param userIds the user ids
//     * @since garnet-core-be-fe 1.0.0
//     */
//    void deleteUsers(@Param(value = "userIds") List<Long> userIds);
//
//    /**
//     * Gets all users.
//     *
//     * @return the all users
//     * @since garnet-core-be-fe 1.0.0
//     */
//    List<GarUser> getAllUsers();
//
//    /**
//     * Gets user by name.
//     *
//     * @param username the username
//     * @return the user by name
//     * @since garnet-core-be-fe 1.0.0
//     */
//    GarUser getUserByName(@Param(value = "username") String username);
//
//    /**
//     * Gets user by name and password.
//     *
//     * @param username the username
//     * @param password the password
//     * @return the user by name and password
//     * @since garnet-core-be-fe 1.0.0
//     */
//    GarUser getUserByNameAndPassword(@Param(value = "username") String username, @Param(value = "password") String password);

    /**
     * Update password.
     *
     * @param userId   the user id
     * @param password the password
     * @since garnet-core-be-fe 1.0.0
     */
    void updatePassword(@Param(value = "userId") Long userId, @Param(value = "password") String password);

    /**
     * Gets user count.
     *
     * @return the user count
     * @since garnet-core-be-fe 1.0.0
     */
    int getUserCount();
}
