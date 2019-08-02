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

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <b><code>XssAndSqlHttpServletRequestWrapper</code></b>
 * <p>
 * class_comment
 * </p>
 * <b>Create Time:</b> 2019/5/5 18:44
 *
 * @author Xuan Rui
 */
@Slf4j
public class illegalInfoHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * The Params.
     */
    private Map<String, String[]> params = new HashMap<String, String[]>();

    /**
     * Instantiates a new Illegal info http servlet request wrapper.
     *
     * @param request the request
     */
    @SuppressWarnings("unchecked")
    public illegalInfoHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap());
    }


    /**
     * 防止xss跨脚本攻击（替换，根据实际情况调整）
     */

    public static String stripXSSAndSql(String value) {
        if (value != null) {
            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
            scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid e-xpression(...) expressions
            scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }

    @Override
    public String getParameter(String name) {
        String results = super.getParameter(name);
        if (results == null)
            return null;
        else {
            return xssEncode(results);
        }
    }


    /**
     * 对数组参数进行特殊字符过滤
     */
    @Override
    public String[] getParameterValues(String name) {
        if ("content".equals(name)) {//不想过滤的参数，此处content参数是 富文本内容
            return super.getParameterValues(name);
        }
        String[] values = super.getParameterValues(name);
        if (values == null || values.length <= 0) {
            return null;
        } else {
            log.error("parameters has special word");
            String[] newValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                newValues[i] = xssEncode(HtmlUtils.htmlEscape(values[i]));//spring的HtmlUtils进行转义
            }
            log.error("already replace special word");
            return newValues;
        }
    }

    /**
     * 将容易引起xss & sql漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    private String xssEncode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        } else {
            s = stripXSSAndSql(s);
            s = stripIllegalInfo(s);
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    sb.append("＞");// 转义大于号
                    break;
                case '<':
                    sb.append("＜");// 转义小于号
                    break;
                case '\'':
                    sb.append("＇");// 转义单引号
                    break;
                case '\"':
                    sb.append("＂");// 转义双引号
                    break;
                case '&':
                    sb.append("＆");// 转义&
                    break;
                case '#':
                    sb.append("＃");// 转义#
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * Strip illegal string.
     *
     * @param value the value
     * @return the string
     */
    public String stripIllegalInfo(String value) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        if (!pattern.matcher(value).find()) {
            return value;
        } else {
            CacheManager cacheManager = CacheManager.getInstance();
            if (!cacheManager.getCacheAll().isEmpty()) {
                cacheManager.clearAll();
            }
            Workbook excel = null;
            try {
                ClassPathResource resource = new ClassPathResource("filter/illegal_info.xls");
                excel = new HSSFWorkbook(resource.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < excel.getNumberOfSheets(); numSheet++) {
                Sheet sheet = excel.getSheetAt(numSheet);
                if (sheet == null)
                    continue;
                // 循环行Row(每一列),从第行开始
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null)
                        continue;
                    Cell cell0 = row.getCell(0);  //第一列
                    if (cell0 == null)
                        continue;
                    cacheManager.putCache(String.valueOf(rowNum), cell0.getStringCellValue());
                }
            }

            Map<String, String> illegalityInfo = cacheManager.getCacheAll();
            for (String key : illegalityInfo.keySet()) {
                if (value != null && value.contains(illegalityInfo.get(key))) {
                    value.replace(illegalityInfo.get(key), "");
                }
            }
            return value;
        }
    }
}

