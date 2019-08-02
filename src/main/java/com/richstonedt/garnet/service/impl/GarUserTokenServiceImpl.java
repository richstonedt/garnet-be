
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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
//import com.richstonedt.garnet.common.exception.GarnetServiceException;
//import com.richstonedt.garnet.common.utils.Result;
//import com.richstonedt.garnet.common.utils.TokenGenerator;
//import com.richstonedt.garnet.dao.GarUserTokenDao;
//import com.richstonedt.garnet.model.GarUser;
//import com.richstonedt.garnet.model.GarUserToken;
//import com.richstonedt.garnet.service.GarUserRoleService;
//import com.richstonedt.garnet.service.GarUserService;
import com.richstonedt.garnet.service.GarUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * The type Sys user token service.
 *
 * @since garnet-core-be-fe 1.0.0
 */
@Service("sysUserTokenService")
public class GarUserTokenServiceImpl implements GarUserTokenService {
    /**
     * The Sys user token dao.
     *
     * @since garnet-core-be-fe 1.0.0
     */
//    @Autowired
//    private GarUserTokenDao garUserTokenDao;
//
//    /**
//     * The GarUserRole service.
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Autowired
//    private GarUserRoleService garUserRoleService;
//
//    /**
//     * The GarUser service.
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Autowired
//    private GarUserService garUserService;
//    /**
//     * The constant EXPIRE. 3小时后过期
//     *
//     * @since garnet-core-be-fe 1.0.0
//     */
//    private final static int EXPIRE = 3600 * 3;
//
//    /**
//     * Query by user id sys user token entity.
//     *
//     * @param userId the user id
//     * @return the sys user token entity
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Override
//    public GarUserToken queryByUserId(Long userId) {
//        return garUserTokenDao.queryByUserId(userId);
//    }
//
//    /**
//     * Query by token sys user token entity.
//     *
//     * @param token the token
//     * @return the sys user token entity
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Override
//    public GarUserToken queryByToken(String token) {
//        return garUserTokenDao.queryByToken(token);
//    }
//
//    /**
//     * Save.
//     *
//     * @param token the token
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Override
//    public void save(GarUserToken token) {
//        garUserTokenDao.save(token);
//    }
//
//    /**
//     * Update.
//     *
//     * @param token the token
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Override
//    public void update(GarUserToken token) {
//        garUserTokenDao.update(token);
//    }
//
//    /**
//     * 生成token
//     *
//     * @param userId 用户ID
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Override
//    public Result createGarnetToken(Long userId) {
//        //生成一个token
//        String token = TokenGenerator.generateValue();
//        //当前时间
//        Date now = new Date();
//        //过期时间
//        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
//        //判断是否生成过token
//        GarUserToken tokenEntity = queryByUserId(userId);
//        if (tokenEntity == null) {
//            tokenEntity = new GarUserToken();
//            tokenEntity.setUserId(userId);
//            tokenEntity.setToken(token);
//            tokenEntity.setUpdateTime(now);
//            tokenEntity.setExpireTime(expireTime);
//            //保存token
//            save(tokenEntity);
//        } else {
//            tokenEntity.setToken(token);
//            tokenEntity.setUpdateTime(now);
//            tokenEntity.setExpireTime(expireTime);
//            //更新token
//            update(tokenEntity);
//        }
//        return Result.ok().put("garnetToken", token).put("expire", EXPIRE);
//    }
//
//    /**
//     * Create token string.
//     *
//     * @param userId the user id
//     * @return the string
//     * @since garnet-core-be-fe 1.0.0
//     */
//    @Override
//    public String createGempileToken(Long userId) {
//        GarUser garUser = garUserService.getUserById(userId);
//        List<Long> roleIdList = garUserRoleService.getRoleIdsByUserId(userId);
//        StringBuilder sb = new StringBuilder();
//        if (!CollectionUtils.isEmpty(roleIdList)) {
//            for (Long id : roleIdList) {
//                sb = sb.append(id).append(",");
//            }
//        }
//        String roleIds = "";
//        if (sb.length() >= 2) {
//            roleIds = sb.substring(0, sb.length() - 1);
//        }
//        try {
//            Algorithm algorithm = Algorithm.HMAC256("secret");
//            return JWT.create()
//                    .withClaim("uid", garUser.getUserId().toString())
//                    .withClaim("una", garUser.getUsername())
//                    .withClaim("uad", garUser.getAdmin())
//                    .withClaim("rol", roleIds)
//                    .withExpiresAt(new Date(System.currentTimeMillis() + (long) 60 * 60 * 1000 * 3))
//                    .sign(algorithm);
//        } catch (UnsupportedEncodingException e) {
//            throw new GarnetServiceException(e, "UTF-8 encoding not supported when generating token");
//        }
//    }
//
//    @Override
//    public GarUser getUserInfoByToken(String token) {
//        GarUserToken userToken = garUserTokenDao.queryByToken(token);
//        return garUserService.getUserById(userToken.getUserId());
//    }
}