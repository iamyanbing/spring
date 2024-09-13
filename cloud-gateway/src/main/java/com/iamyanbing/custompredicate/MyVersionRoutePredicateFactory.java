package com.iamyanbing.custompredicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class MyVersionRoutePredicateFactory extends AbstractRoutePredicateFactory<MyVersionRoutePredicateFactory.MyConfig> {


    public MyVersionRoutePredicateFactory() {
        super(MyConfig.class);
    }

    /**
     * 把 application.yml 配置文件中的值，拿过来，放入 MyConfig 对象中
     *
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {

        // 这里的 version 要和 MyConfig 类的属性名一样
        return Arrays.asList(new String[]{"version", "tag"});
    }

    /**
     * 处理逻辑
     *
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(MyConfig config) {

        return (ServerWebExchange serverWebExchange) -> {
            // 获取客户端请求参数中的 version 值。该值是客户端请求服务端时，传递到服务端。
            String version = serverWebExchange.getRequest().getQueryParams().getFirst("version");
            String tag = serverWebExchange.getRequest().getQueryParams().getFirst("tag");
            System.out.println("version:" + version);
            System.out.println("tag:" + tag);
            // 比较 application.yml 中配置的值和客户端请求的值
            if (version != null && version.equals(config.getVersion())
                    && tag != null && tag.equals(config.getTag())) {
                return true;
            } else {
                return false;
            }
        };
    }

    public static class MyConfig {
        private String version;

        private String tag;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
