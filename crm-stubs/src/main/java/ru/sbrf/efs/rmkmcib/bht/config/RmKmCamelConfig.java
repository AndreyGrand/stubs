package ru.sbrf.efs.rmkmcib.bht.config;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by manaev on 8/2/16.
 *
 */
@Configuration
@Import(RmKmAppConfig.class)
public class RmKmCamelConfig  extends CamelConfiguration {

    @Autowired
    RmKmAppConfig appConfig;

    @Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
        JmsComponent answer = new JmsComponent();
        answer.setConnectionFactory(appConfig.connectionFactory());
        camelContext.addComponent("activemq", answer);
    }


}
