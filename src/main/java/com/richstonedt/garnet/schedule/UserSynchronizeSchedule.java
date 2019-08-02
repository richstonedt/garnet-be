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

package com.richstonedt.garnet.schedule;

import com.richstonedt.garnet.service.GarUserSynchronizeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;

/**
 * <b><code>UserSynchronizeSchedule</code></b>
 * <p>
 * 同步用户定时任务.
 * <p>
 * <b>Creation Time:</b> 2018/9/6 17:43.
 *
 * @author liuruojing
 * @since garnet-core-be-fe 0.1.0
 */
//@Component
public class UserSynchronizeSchedule {
    /**
    *
    *log.
    *
    *@since ${PROJECT_NAME} 0.1.0
    */
//    private static final Logger LOG = LoggerFactory
//            .getLogger(UserSynchronizeSchedule.class);
//
//    /**
//    *
//    *userSynchronizeService.
//    *
//    *@since ${PROJECT_NAME} 0.1.0
//    */
//    @Autowired
//    private GarUserSynchronizeService userSynchronizeService;
//
//    /**
//     * 用户同步定时任务，一分钟执行一次.
//     *
//     * @param
//     * @return
//     * @author liuruojing
//     * @since ${PROJECT_NAME} 0.1.0
//     */
//    @Scheduled(fixedDelay = 1000 * 60)
//    public final void userSynchronize() {
//        try {
//            userSynchronizeService.userSynchronize();
//        } catch (Exception e) {
//            //
//        }
//    }
}
