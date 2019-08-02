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
package com.richstonedt.garnet.config;

import com.richstonedt.garnet.filter.UserFilter;
import com.richstonedt.garnet.filter.illegalFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <b><code>WebConfiguration</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2019/5/6 15:49
 *
 * @author Xuan Rui
 */
@Configuration
public class WebConfiguration {
    @Bean
    public FilterRegistrationBean illegalFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new illegalFilter());
        List<String> urlList = new ArrayList<>();
        urlList.add("/api/v1.0/*");
        registration.setUrlPatterns(urlList);
        registration.setName("illegalFilter");
        registration.setOrder(1);
        return registration;
    }


    @Bean
    public FilterRegistrationBean userFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new UserFilter());
        List<String> urlList = new ArrayList<>();
        urlList.add("/api/v1.0/*");
        registration.setUrlPatterns(urlList);
        registration.setName("userFilter");
        registration.setOrder(2);
        return registration;
    }
}
