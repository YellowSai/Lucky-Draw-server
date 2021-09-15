package cn.marsLottery.server.configure;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers((tomcatConnector) -> {
            ProtocolHandler protocolHandler = tomcatConnector.getProtocolHandler();
            if (protocolHandler instanceof Http11NioProtocol) {
                Http11NioProtocol http11NioProtocol = (Http11NioProtocol) protocolHandler;
                http11NioProtocol.setKeepAliveTimeout(120000);
                http11NioProtocol.setConnectionTimeout(90000);
                http11NioProtocol.setMaxKeepAliveRequests(1000);
                http11NioProtocol.setMinSpareThreads(200);
                http11NioProtocol.setMaxHttpHeaderSize(64 * 1024);
            }
        });
    }
}
