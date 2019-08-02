
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
 * <b><code>MessageDescription</code></b>
 * <p/>
 * MessageDescription的具体实现
 * <p/>
 * <b>Creation Time:</b> Thu Oct 01 18:45:41 GMT+08:00 2017.
 *
 * @author lvxin
 * @version 1.0.0
 * @since torinosrc-common 1.0.0
 */
public class MessageDescription {

    /**
     * 操作成功
     */
    public static final String OPERATION_SUCCESS = "操作成功";

    /**
     * 操作失败
     */
    public static final String OPERATION_FAILURE = "操作失败";

    /**
     * 数据插入操作成功
     */
    public static final String OPERATION_INSERT_SUCCESS = "数据插入操作成功";

    /**
     * 数据插入操作失败
     */
    public static final String OPERATION_INSERT_FAILURE = "数据插入操作失败";

    /**
     * 数据删除操作成功
     */
    public static final String OPERATION_DELETE_SUCCESS = "数据删除操作成功";

    /**
     * 数据删除操作失败
     */
    public static final String OPERATION_DELETE_FAILURE = "数据删除操作失败";

    /**
     * 数据查询操作成功
     */
    public static final String OPERATION_QUERY_SUCCESS = "数据查询操作成功";

    /**
     * 数据查询操作失败
     */
    public static final String OPERATION_QUERY_FAILURE = "数据查询操作失败";

    /**
     * 数据更新操作成功
     */
    public static final String OPERATION_UPDATE_SUCCESS = "数据更新操作成功";

    /**
     * 数据更新操作失败
     */
    public static final String OPERATION_UPDATE_FAILURE = "数据更新操作失败";

    /**
     * 登陆成功
     */
    public static final String LOGIN_SUCCESS = "用户登陆成功";

    /**
     * 登陆失败
     */
    public static final String LOGIN_FAILURE = "用户登陆失败";

    /**
     * 登陆密码错误
     */
//    public static final String LOGIN_WRONG_PASSWORD = "用户密码错误";

    /**
     * 用户名已存在
     */
    public static final String LOGIN_USERNAME_EXIST = "用户名已存在";

    /**
     * 用户名不存在
     */
    public static final String LOGIN_USERNAME_NOT_EXIST = "用户名不存在";
}
