package org.occideas.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.occideas.security.rest.AdminRestController;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/web/rest")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages("org.occideas");
    }
}
