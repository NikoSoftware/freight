package net.xiaomotou.freight.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

/**
 * 发送请求 httpclient封装
 *
 * @author lishangzhi
 * E-mail:1669852599@qq.com
 * @version v1.0
 * Time：2015年8月1日 上午9:46:07
 */
@SuppressWarnings("deprecation")
public class HttpUtils {

    private static DefaultHttpClient client = new DefaultHttpClient();

    /**
     * 发送Get请求
     *
     * @param url      请求的地址
     * @param headers  请求的头部信息
     * @param params   请求的参数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static UrlResult sendGet(String url, Map<String, String> headers, Map<String, String> params,
                                    boolean duan) throws ClientProtocolException, IOException {
        url = url + (null == params ? "" : assemblyParameter(params));
        HttpGet hp = new HttpGet(url);
        if (null != headers)
            hp.setHeaders(assemblyHeader(headers));
        HttpResponse response = client.execute(hp);
        if (duan)
            hp.abort();
        HttpEntity entity = response.getEntity();
        UrlResult result = new UrlResult();
        result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));
        result.setStatusCode(response.getStatusLine().getStatusCode());
        result.setHeaders(response.getAllHeaders());
        result.setHttpEntity(entity);
        return result;
    }

    public static UrlResult sendGet(String url, Map<String, String> headers, Map<String, String> params)
            throws ClientProtocolException, IOException {
        return sendGet(url, headers, params,false);
    }

    /**
     * 发送Post请求
     *
     * @param url      请求的地址
     * @param headers  请求的头部信息
     * @param params   请求的参数
     * @param encoding 字符编码
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static UrlResult sendPost(String url, Map<String, String> headers, Map<String, String> params, String encoding)
            throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String temp : params.keySet()) {
            list.add(new BasicNameValuePair(temp, params.get(temp)));
        }
        post.setEntity(new UrlEncodedFormEntity(list, encoding));

        if (null != headers)
            post.setHeaders(assemblyHeader(headers));
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();

        UrlResult result = new UrlResult();
        result.setStatusCode(response.getStatusLine().getStatusCode());
        result.setHeaders(response.getAllHeaders());
        result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));
        result.setHttpEntity(entity);
        return result;
    }

    /**
     * 组装头部信息
     *
     * @param headers
     * @return
     */
    public static Header[] assemblyHeader(Map<String, String> headers) {
        Header[] allHeader = new BasicHeader[headers.size()];
        int i = 0;
        for (String str : headers.keySet()) {
            allHeader[i] = new BasicHeader(str, headers.get(str));
            i++;
        }
        return allHeader;
    }

    /**
     * 组装Cookie
     *
     * @param cookies
     * @return
     */
    public static String assemblyCookie(List<Cookie> cookies) {
        StringBuffer sbu = new StringBuffer();
        for (Cookie cookie : cookies) {
            sbu.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        if (sbu.length() > 0)
            sbu.deleteCharAt(sbu.length() - 1);
        return sbu.toString();
    }

    /**
     * 组装请求参数
     *
     * @param parameters
     * @return
     */
    public static String assemblyParameter(Map<String, String> parameters) {
        String para = "?";
        for (String str : parameters.keySet()) {
            para += str + "=" + parameters.get(str) + "&";
        }
        return para.substring(0, para.length() - 1);
    }

    // TODO demo
    public static void main(String[] args) {
        Map<String, String> param = new HashMap<String, String>();
        try {
            UrlResult result = HttpUtils.sendGet("http://www.baidu.com", param, param);
            // HttpUtils.u
            String str = result.getHtml(result, "utf-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}