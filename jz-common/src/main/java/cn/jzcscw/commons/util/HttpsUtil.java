package cn.jzcscw.commons.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpsUtil {

    private static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
                    (chain, authType) ->
                            true
            ).build();
            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext);

            return HttpClients.custom().setSSLSocketFactory(sslFactory).build();
        } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
            log.error("exception={}", e);
        }

        return HttpClients.createDefault();
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doGet(String url, Map<String, String> headers) {
        log.debug("https get->" + url);
        CloseableHttpClient client = createSSLClientDefault();
        StringBuilder sb = new StringBuilder();
        try {
            HttpGet get = new HttpGet(url);
            // ?????????????????????????????????
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(2000).setConnectTimeout(2000).build();
            get.setConfig(requestConfig);
            addHeader(get, headers);
            CloseableHttpResponse resp = client.execute(get);
            log.debug("resp={}", resp);
            InputStream in = resp.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.debug("https get resp->" + sb.toString());
        } catch (IOException e) {
            log.error("exception={}", e);
        }
        return sb.toString();
    }

    public static void doPostImage(String url, Map<String, String> headers, Map<String, Object> param, OutputStream outputStream) {
        log.debug("https doPostImage->" + url);
        CloseableHttpClient client = createSSLClientDefault();
        try {
            HttpPost post = new HttpPost(url);
            // ?????????????????????????????????
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(2000).setConnectTimeout(2000).build();
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            post.setConfig(requestConfig);
            addHeader(post, headers);
            String json = JSONUtil.toJsonPrettyStr(param);
            log.debug("json=->{}", json);
            StringEntity stringEntity = new StringEntity(json);
            post.setEntity(stringEntity);
            CloseableHttpResponse resp = client.execute(post);
            log.debug("resp={}", resp);
            InputStream in = resp.getEntity().getContent();
            byte[] bytes = new byte[1024];
            while (in.read(bytes) > 0) {
                byte[] outBytes = Base64.getEncoder().encode(bytes);
                outputStream.write(outBytes);
            }
        } catch (IOException e) {
            log.error("exception={}", e);
        }
    }

    public static InputStream doPostFile(String url, Map<String, Object> param) {
        return doPostFile(url, null, param);
    }

    public static InputStream doPostFile(String url, Map<String, String> headers, Map<String, Object> param) {
        log.debug("https doPostFile->" + url);
        CloseableHttpClient client = createSSLClientDefault();
        try {
            HttpPost post = new HttpPost(url);
            // ?????????????????????????????????
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(2000).setConnectTimeout(2000).build();
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            post.setConfig(requestConfig);
            addHeader(post, headers);
            String json = JSONUtil.toJsonPrettyStr(param);
            log.debug("json=->{}", json);
            StringEntity stringEntity = new StringEntity(json);
            post.setEntity(stringEntity);
            CloseableHttpResponse resp = client.execute(post);
            log.debug("resp={}", resp);
            InputStream in = resp.getEntity().getContent();
            return in;
        } catch (IOException e) {
            log.error("exception={}", e);
        }
        return null;
    }

    public static String doPost(String url, Map<String, String> param) {
        return doPost(url, null, param);
    }

    public static String doPost(String url, Map<String, String> headers, Map<String, String> param) {
        log.debug("https doPost->" + url);
        CloseableHttpClient client = createSSLClientDefault();
        StringBuilder sb = new StringBuilder();
        try {
            // ?????????????????????????????????
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(2000).setConnectTimeout(2000).build();
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            addHeader(post, headers);

            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String keys : param.keySet()) {
                    paramList.add(new BasicNameValuePair(keys, param.get(keys)));
                }
                post.setEntity(new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8));
            }

            CloseableHttpResponse resp = client.execute(post);
            InputStream in = resp.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.debug("https post resp->" + sb.toString());
        } catch (IOException e) {
            log.error("exception={}", e);
        }
        return sb.toString();
    }

    public static String doPostJson(String url, Map<String, String> jsonMap) {
        return doPostJson(url, null, jsonMap);
    }

    public static String doPostJson(String url, Map<String, String> headers, Map<String, String> jsonMap) {
        log.debug("https doPostJson->" + url);
        CloseableHttpClient client = createSSLClientDefault();
        StringBuilder sb = new StringBuilder();
        try {
            // ?????????????????????????????????
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(2000).setConnectTimeout(2000).build();
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            addHeader(post, headers);
            post.setConfig(requestConfig);
            String json = JSONUtil.toJsonPrettyStr(jsonMap);
            log.debug("json=->{}", json);
            StringEntity stringEntity = new StringEntity(json);
            post.setEntity(stringEntity);
            CloseableHttpResponse resp = client.execute(post);
            InputStream in = resp.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.debug("https doPostJson resp->" + sb.toString());
        } catch (IOException e) {
            log.error("exception={}", e);
        }
        return sb.toString();
    }

    public static String doPostJsonStr(String url, String jsonStr) {
        return doPostJsonStr(url, null, jsonStr);
    }

    public static String doPostJsonStr(String url, Map<String, String> headers, String jsonStr) {
        log.debug("https doPostJson->" + url);
        CloseableHttpClient client = createSSLClientDefault();
        StringBuilder sb = new StringBuilder();
        try {
            // ?????????????????????????????????
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(2000).setConnectTimeout(2000).build();
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            addHeader(post, headers);
            post.setConfig(requestConfig);
            log.debug("json=->{}", jsonStr);
            StringEntity stringEntity = new StringEntity(jsonStr);
            post.setEntity(stringEntity);
            CloseableHttpResponse resp = client.execute(post);
            InputStream in = resp.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.debug("https doPostJson resp->" + sb.toString());
        } catch (IOException e) {
            log.error("exception={}", e);
        }
        return sb.toString();
    }

    public static String doPostXml(String url, String xmlStr) {
        return doPostXml(url, null, xmlStr);
    }

    public static String doPostXml(String url, Map<String, String> headers, String xmlStr) {
        log.debug("https doPostXml->{},{}", url, xmlStr);
        CloseableHttpClient client = createSSLClientDefault();
        StringBuilder sb = new StringBuilder();
        try {
            // ?????????????????????????????????
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(2000).setConnectTimeout(2000).build();
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "text/xml");
            addHeader(post, headers);
            post.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(xmlStr, "UTF-8");
            post.setEntity(stringEntity);
            CloseableHttpResponse resp = client.execute(post);
            InputStream in = resp.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            log.debug("https doPostXml resp->{}", sb.toString());
        } catch (IOException e) {
            log.error("doPostXml={}", e);
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
