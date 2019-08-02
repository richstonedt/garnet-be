
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

package com.richstonedt.garnet.config.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <b><code>LogSQLConfig</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2017/9/28 16:45
 *
 * @author Sun Jinpeng
 * @version 1.0.0
 * @since gempile-core-cs 1.0.0
 */
public class LogSQLConfig<E> extends ConsoleAppender<E> {

    /**
     * The constant LOGGER_CLASS_NAME.
     * 需要过滤的日志名字
     *
     * @since gempile-core-cs 1.0.0
     */
    private static final String LOGGER_CLASS_NAME = "com.richstonedt.garnet";

    /**
     * Append
     *
     * @since gempile-core-cs 1.0.0
     */
    @Override
    protected void append(E e) {
        super.append(e);
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String method = request.getMethod();
        String url = request.getRequestURI();

        LoggingEvent le = (LoggingEvent) e;
        //如果是Mybatis中的日志，采用自定义的方式处理，否则按默认方式处理
        if (le.getLoggerName().contains(LOGGER_CLASS_NAME)) {
            SqlConfig.setSqlWithRequest(le,url+method);
        }
    }
}
