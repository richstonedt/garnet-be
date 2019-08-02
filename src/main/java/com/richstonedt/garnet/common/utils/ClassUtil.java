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

import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.slf4j.Logger;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.*;

/**
 * <b><code>ClassUtil</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b>2017/12/7 12:52
 *
 * @author PanXin
 * @version 1.0.0
 * @since garnet-core-be-fe  1.0.0
 */
public class ClassUtil {
    private static final Logger LOG = LoggerFactory
            .getLogger(ClassUtil.class);
    /**
     * Gets class list from package.
     *
     * @param clazz the clazz
     * @return the class list from package
     * @since garnet-core-be-fe 0.1.0
     */
    public static List<Class<?>> getClassListFromPackage(Class clazz) {
        try {
            List<Class<?>> clazzList = new ArrayList<>();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String pkgName = clazz.getPackage().getName();
            String strFile = pkgName.replaceAll("\\.", "/");
            Enumeration<URL> urls = loader.getResources(strFile);
            // TODO 复杂度待修改
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (!ObjectUtils.isEmpty(url)) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String pkgPath = url.getPath();
                        File[] files = filterClassFiles(pkgPath);
                        if (!ObjectUtils.isEmpty(files)) {
                            for (File file : files) {
                                String fileName = file.getName();
                                if (file.isFile()) {
                                    // .class 文件的情况
                                    String clazzName = getClassName(pkgName, fileName);
                                    clazz = Class.forName(clazzName);
                                    if (!ObjectUtils.isEmpty(clazz)) {
                                        clazzList.add(clazz);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return clazzList;
        } catch (Exception e) {
            LOG.error("异常",e);

        }
        return null;
    }

    /**
     * Filter class files file [ ].
     *
     * @param pkgPath the pkg path
     * @return the file [ ]
     * @since garnet-core-be-fe 0.1.0
     */
    private static File[] filterClassFiles(String pkgPath) {
        if (pkgPath == null) {
            return null;
        }
        // 接收 .class 文件 或 类文件夹
        return new File(pkgPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
    }

    /**
     * Gets class name.
     *
     * @param pkgName  the pkg name
     * @param fileName the file name
     * @return the class name
     * @since garnet-core-be-fe 0.1.0
     */
    private static String getClassName(String pkgName, String fileName) {
        int endIndex = fileName.lastIndexOf('.');
        String clazz = null;
        if (endIndex >= 0) {
            clazz = fileName.substring(0, endIndex);
        }
        String clazzName = null;
        if (clazz != null) {
            clazzName = pkgName + "." + clazz;
        }
        return clazzName;
    }
}
