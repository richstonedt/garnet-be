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
package com.richstonedt.garnet.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName="UserFilter",urlPatterns="/api/v1/*")
public class UserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {


    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

//        HttpServletRequest hreq = (HttpServletRequest) servletRequest;
//
//        HttpServletResponse hresponse = (HttpServletResponse) servletResponse;

//
//        //获取uri
//        String uri = hreq.getRequestURI();
////
////
//        Cookie[] cookies = hreq.getCookies();//这样便可以获取一个cookie数组
//
//        for(Cookie cookie : cookies){
//
//            if("cookiename2".equals(cookie.getName())){
//
//                System.out.println(cookie.getName()+", value:"+cookie.getValue());
//            }
//
//        }
//
//        Cookie cookie = new Cookie("cookiename2","myvalue");
//        cookie.setHttpOnly(true);
//        hresponse.addCookie(cookie);


               filterChain.doFilter(servletRequest, servletResponse);

//        if(uri.contains("login")){
//
//            //图像上传的请求，不做处理
//            filterChain.doFilter(servletRequest, servletResponse);
//        }else{
//            String reqMethod = hreq.getMethod();
//            if("POST".equals(reqMethod)){
//
//                PrintWriter out = null;
//                HttpServletResponse response = (HttpServletResponse) servletResponse;
//                response.setCharacterEncoding("UTF-8");
//                response.setContentType("application/json; charset=utf-8");
//
//                ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(hreq);
////                String body =  HttpHelper.getBodyString(requestWrapper);
////
//                //如果是POST请求则需要获取 param 参数
//                String param = URLDecoder.decode(body,"utf-8");
//                //String param = hreq.getParameter("param");
//                //json串 转换为Map
//                //System.out.println("json str: "+param);
//
//                if(param!=null& param.contains("=")){
//                    param = param.split("=")[1];
//                }
//                Map paramMap = GsonUtil.GsonToMaps(param);
//
//                //System.out.println("get data: "+paramMap.get("auditContent").toString());
//                filterChain.doFilter(requestWrapper, servletResponse);
//
//            }else{
//                //get请求直接放行
//                filterChain.doFilter(servletRequest, servletResponse);
//            }
//        }
//
//
//
//
//        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
