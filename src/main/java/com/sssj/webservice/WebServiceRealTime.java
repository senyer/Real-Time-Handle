package com.sssj.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.IOException;

/**
 * 接收实时数据接口webservice
 */
@WebService(targetNamespace="http://webservice.zy.com/")
public interface WebServiceRealTime {
	
    
    /**
	  * 接收所有实时数据
	  * @param data
	  */
     @WebMethod(action ="http://webservice.zy.com/realtime" )
	 public String realtime(
             @WebParam(name = "data",
                     targetNamespace = "http://webservice.zy.com/")
                     String data,
             @WebParam(name = "type",
                     targetNamespace = "http://webservice.zy.com/")
                     String type) throws IOException;


}
