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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * <b><code>PropertiesUtils</code></b>
 * <p>
 * 读取配置文件工具类.
 * <p>
 * <b>Creation Time:</b> 2018/9/5 10:35.
 *
 * @author liuruojing
 * @since garnet-core-be-fe 0.1.0
 */
public class PropertiesUtils {
    /**
     * Description.
     *
     * @param fileUrl properties文件相对类加载器的路径
     * @param paramName 参数名称
     * @return String
     * @author liuruojing
     * @since ${PROJECT_NAME} 0.1.0
     */
    public static String read(String fileUrl,String paramName) throws IOException {
        if(fileUrl==null||"".equals(fileUrl)||paramName==null||"".equals(paramName))
            throw new NullPointerException("Parameters are not allowed to be null");
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileUrl);
        // 使用properties对象加载输入流
        try {
            properties.load(in);
            String paramValue = properties.getProperty(paramName);
            return paramValue;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}