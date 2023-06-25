package com.iamyanbing.config;

import com.iamyanbing.util.CustomErrorHandler;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.Header;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


@Configuration
public class RestTemplateConfig {

    @Autowired
    private RestTemplateBuilder builder;

    /**
     * 声明 RestTemplate
     * 底层系统默认
     * @return
     */
    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        //在使用RestTemplate进行远程接口服务调用的时候，当请求的服务出现异常：超时、服务不存在等情况的时候（响应状态非200、而是400、500HTTP状态码），就会抛出异常
        //setErrorHandler作用是当响应状态码不为200时不抛出异常，通过ResponseEntity对象中响应状态码自定义处理
        //restTemplate.setErrorHandler(new CustomErrorHandler());
        return builder
                // 设置http请求连接超时时间
                .setConnectTimeout(Duration.ofMillis(12000))
                // 设置http请求读数据超时时间
                .setReadTimeout(Duration.ofMillis(12000))
                // 自定义异常处理
                .errorHandler(new CustomErrorHandler())
                .build();
    }

    /**
     * 声明 RestTemplate
     * 底层使用Httpclient
     * @return
     */
    @Bean
    public RestTemplate restTemplateUseHttpclient() {
        return new RestTemplate(httpRequestFactory());
    }

    /**
     * 声明 RestTemplate
     * 底层使用OkHttp
     */
    @Bean
    public RestTemplate restTemplateOkhttp() {
        ClientHttpRequestFactory factory = okhttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {

        return new HttpComponentsClientHttpRequestFactory(httpClient());

    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //设置整个连接池最大连接数 根据自己的场景决定
        connectionManager.setMaxTotal(100);
        //同路由的并发数,路由是对maxTotal的细分。
        //单host（可以理解为单域名）最大并发数，在依赖的外部系统数量较少时，可以配置和maxTotal一致。
        connectionManager.setDefaultMaxPerRoute(50);
        RequestConfig requestConfig = RequestConfig.custom()
                //服务器返回数据(response)的时间，超过该时间抛出read timeout
                .setSocketTimeout(12000)
                //连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
                .setConnectTimeout(5000)
                //从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .setConnectionRequestTimeout(1000)
                .build();
        //headers
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        headers.add(new BasicHeader("Content-type", "application/json;charset=UTF-8"));

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setDefaultHeaders(headers)
                // 保持长连接配置，需要在头添加Keep-Alive
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                //重试。默认没有开启；默认次数是3次
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
                .build();
    }




    /**
     * 工厂
     * @return
     */
    private ClientHttpRequestFactory okhttpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory(okHttpConfigClient());
    }

    /**
     * 客户端
     * @return
     */
    private OkHttpClient okHttpConfigClient() {
        return new OkHttpClient().newBuilder()
                .connectionPool(pool())
                //设置连接超时时间及单位
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(10L, TimeUnit.SECONDS)
                .writeTimeout(10L, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    /**
     * 连接池
     * @return
     */
    private ConnectionPool pool() {
        //设置连接池参数，最大空闲连接数30，空闲连接存活时间10分钟
        return new ConnectionPool(30, 10L, TimeUnit.MINUTES);
    }

}
