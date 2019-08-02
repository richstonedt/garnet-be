
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.core.Ordered;
/**
 * <b><code>CORSConfig</code></b>
 * <p/>
 * The bean of CORSConfig
 * <p/>
 * <b>Creation Time:</b> 2016/12/4 17:15.
 *
 * @author Elvis
 */
@Configuration
public class CORSConfig {
    @Value("${cors.allowedOrigins}")
    private String allowedOrigins;

    /**
     * Build config cors configuration.
     *
     * @return the cors configuration
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        if (allowedOrigins != null && allowedOrigins.length() > 0) {
            String[] allowedOrigin = allowedOrigins.split(",");
            for (String item : allowedOrigin) {
                corsConfiguration.addAllowedOrigin(item);                // 1.allow  domain
            }
        }
        corsConfiguration.addAllowedHeader("*");                // 2.allow all Header
        corsConfiguration.addAllowedMethod("*");                // 3.allow all method(post,get etc.）
        corsConfiguration.setAllowCredentials(true);            // 4.allow cookie
        return corsConfiguration;
    }

    /**
     * Cors filter.
     *
     * @return the cors filter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);

//        茂名项目代码
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader(CorsConfiguration.ALL);
//        config.addAllowedMethod(CorsConfiguration.ALL);
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return bean;
    }

}
