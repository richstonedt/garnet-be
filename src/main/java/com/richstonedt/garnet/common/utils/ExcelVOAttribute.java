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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface ExcelVOAttribute {

    /**
     * 导出到Excel中的名字. 
     */
    public abstract String name();

    /**
     * 配置列的名称,对应A,B,C,D.... 
     */
    public abstract String column();

    /**
     * 提示信息 
     */
    public abstract String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容. 
     */
    public abstract String[] combo() default {};

    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写. 
     */
    public abstract boolean isExport() default true;


    /**
     * 特殊类型，比如时间等需要转换的类型
     */
    public abstract ExcelType type() default ExcelType.COMMON;
}