package com.wyf.config;

import org.springframework.context.annotation.Configuration;

/**
 * 解决cookie根域名设置问题
 * @author Declan
 */
@Configuration
public class CookieConfig {
    /**
     * 解决问题：
     * There was an unexpected error (type=Internal Server Error, status=500).
     * An invalid domain [.xxx.com] was specified for this cookie
     *
     * @return
     */
    //@Bean
    //public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
    //    return (factory) -> factory.addContextCustomizers(
    //            (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
    //}
}
