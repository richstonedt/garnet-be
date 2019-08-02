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

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午9:59:27
 * @since garnet-core-be-fe 1.0.0
 */
public class Result extends HashMap<String, Object> {

	/**
	 * The constant serialVersionUID.
	 *
	 * @since garnet-core-be-fe 1.0.0
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new Result.
	 *
	 * @since garnet-core-be-fe 1.0.0
	 */
	public Result() {
		put("code", 0);
	}

	/**
	 * Error r.
	 *
	 * @return the r
	 * @since garnet-core-be-fe 1.0.0
	 */
	public static Result error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	/**
	 * Error r.
	 *
	 * @param msg the msg
	 * @return the r
	 * @since garnet-core-be-fe 1.0.0
	 */
	public static Result error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	/**
	 * Error r.
	 *
	 * @param code the code
	 * @param msg  the msg
	 * @return the r
	 * @since garnet-core-be-fe 1.0.0
	 */
	public static Result error(int code, String msg) {
		Result result = new Result();
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	/**
	 * Ok r.
	 *
	 * @param msg the msg
	 * @return the r
	 * @since garnet-core-be-fe 1.0.0
	 */
	public static Result ok(String msg) {
		Result result = new Result();
		result.put("msg", msg);
		return result;
	}

	/**
	 * Ok r.
	 *
	 * @param map the map
	 * @return the r
	 * @since garnet-core-be-fe 1.0.0
	 */
	public static Result ok(Map<String, Object> map) {
		Result result = new Result();
		result.putAll(map);
		return result;
	}

	/**
	 * Ok r.
	 *
	 * @return the r
	 * @since garnet-core-be-fe 1.0.0
	 */
	public static Result ok() {
		return new Result();
	}

	/**
	 * Ok r.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the r
	 * @since garnet-core-be-fe 1.0.0
	 */
	@Override
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
