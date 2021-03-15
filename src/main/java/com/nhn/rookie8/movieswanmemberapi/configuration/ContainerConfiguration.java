package com.nhn.rookie8.movieswanmemberapi.configuration;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(WebSecurityConfiguration.class)
public class ContainerConfiguration {
    @Value("${tomcat.ajp.port}")
    int ajpPort;

    @Value("${tomcat.ajp.remoteauthentication}")
    String remoteAuthentication;

    @Value("${tomcat.ajp.enabled}")
    boolean tomcatAjpEnabled;

    @Bean
    public TomcatServletWebServerFactory servletContainer() {

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        if (tomcatAjpEnabled) {
            Connector ajpConnector = new Connector("AJP/1.3");
            ajpConnector.setPort(ajpPort);
            ajpConnector.setSecure(false);
            ajpConnector.setAllowTrace(false);
            ajpConnector.setScheme("http");
            ((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setSecretRequired(false);
            tomcat.addAdditionalTomcatConnectors(ajpConnector);
        }

        return tomcat;
    }
}
