
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

package com.richstonedt.garnet.model.message;

/**
 * <b><code>MessageCode</code></b>
 * <p/>
 * MessageCode的具体实现
 * <p/>
 * <b>Creation Time:</b> Thu Oct 01 18:45:41 GMT+08:00 2017.
 *
 * @author lvxin
 * @version 1.0.0
 * @since torinosrc-common 1.0.0
 */
public class MessageCode {

    /**
     * 操作成功
     */
    public static final String SUCCESS = "1000";

    /**
     * 用户登陆成功
     */
    public static final String LOGIN_SUCCESS = "2000";

    /**
     * 用户登陆失败
     */
    public static final String LOGIN_FAILURE = "2009";

    /**
     * 用户名已存在
     */
    public static final String LOGIN_USERNAME_EXIST = "2100";

    /**
     * 用户名不存在
     */
    public static final String LOGIN_USERNAME_NOT_EXIST = "2101";

    /**
     * 操作失败
     */
    public static final String FAILURE = "9000";
}
