package cn.jzcscw.commons.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

@Slf4j
public class HttpClientUtil {
    public static String doPostJson(CloseableHttpClient httpClient, String url, Map<String, String> headers, Object data) {
        log.debug("https doPostJson {} data:{}", url, JSONUtil.toJsonStr(data));
        StringBuilder sb = new StringBuilder();
        CloseableHttpResponse resp = null;
        try {
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(2000).setConnectTimeout(2000).build();
            String json = JSONUtil.toJsonStr(data);
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            stringEntity.setContentEncoding("UTF-8");

            HttpPost post = new HttpPost(url);
            post.addHeader("Accept", "application/json");
            addHeader(post, headers);
            post.setConfig(requestConfig);
            post.setEntity(stringEntity);

            resp = httpClient.execute(post);
            InputStream in = resp.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.debug("https doPostJson resp->" + sb.toString());
        } catch (IOException e) {
            log.error("exception={}", e);
        } finally {
            try {
                if (resp != null) {
                    resp.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static void addHeader(HttpRequest request, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                request.addHeader(key, headers.get(key));
            }
        }
    }
}
