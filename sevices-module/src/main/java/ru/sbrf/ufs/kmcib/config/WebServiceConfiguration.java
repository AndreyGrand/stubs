package ru.sbrf.ufs.kmcib.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

@EnableWs
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter {
	private Logger log = LoggerFactory.getLogger(WebServiceConfiguration.class); 
	
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}
	
	@Bean(name = "ecmService")
	public Wsdl11Definition  simpleWsdl11Definition() {
		log.info("simpleWsdl11Definition creating");
		
		SimpleWsdl11Definition simpleWsdl11Definition = new SimpleWsdl11Definition();
		Resource wsdl = new ClassPathResource("/ecm/wsdl/ecmService.wsdl");
		simpleWsdl11Definition.setWsdl(wsdl);
		return simpleWsdl11Definition;
	}
}
