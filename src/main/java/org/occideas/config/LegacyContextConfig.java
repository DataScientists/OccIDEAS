package org.occideas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@ImportResource("applicationContext.xml")
public class LegacyContextConfig {
}
