package com.sssj.config;

import com.sssj.webservice.WebServiceRealTime;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

  @Resource
  private WebServiceRealTime webServiceRealTime;

  /**
   * 注入servlet  bean name不能dispatcherServlet 否则会覆盖dispatcherServlet
   *
   * @return
   */
  @Bean(name = "cxfServlet")
  public ServletRegistrationBean cxfServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/services/*");
  }

  @Bean(name = Bus.DEFAULT_BUS_ID)
  public SpringBus springBus() {
    return new SpringBus();
  }

  @Bean(name = "WebServiceRealTimeEndpoint")
  public Endpoint realTimeEndpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), webServiceRealTime);
    endpoint.publish("/realtime");
    return endpoint;
  }
}
