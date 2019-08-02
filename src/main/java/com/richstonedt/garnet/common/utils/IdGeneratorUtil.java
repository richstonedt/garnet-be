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

package com.richstonedt.garnet.common.utils;

import java.util.Random;

/**
 * <b><code>IdGeneratorUtil</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2017/10/19 12:19
 *
 * @author Sun Jinpeng
 * @version 0.1.0
 * @since garnet-core-be-fe 0.1.0
 */
public class IdGeneratorUtil {

    /**
     * Generate Id long.
     *
     * @return the long
     * @since garnet-core-be-fe 0.1.0
     */
    public static Long generateId() {
//        long ourEpoch =Long.parseLong("1314220021721");
//        long seqId,nowMillis,result;
//        int shardId = 5;
//        seqId = new Random().nextInt(1000000000) % 1024;
//        nowMillis = (long) Math.floor(System.currentTimeMillis() * 1000);
//        result = (nowMillis - ourEpoch) << 23;
//        result = result | (shardId << 10);
//        result = result | (seqId);
//        return result;
        int i=(int)(Math.random() * 100);
        String ran = String.valueOf(i);
        if (ran.length() < 2) {
            Random random = new Random();
            ran = ran + random.nextInt(9);
        }
        String time = String.valueOf(System.currentTimeMillis()).substring(1,12);
        return Long.parseLong(time + ran);
//        return System.currentTimeMillis() + i;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i=0; i < 20; i++) {
            Thread.sleep(2000);
            System.out.println(IdGeneratorUtil.generateId());
        }
    }

}
