package com.cinemas.study.WebService;

import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingType;

/**
 * 什么是WebService?
 * ·如果说java 是跨平台的语言，那么WebService就是跨语言的技术。
 * ·WebService可以让多个进程之间实现数据交换，即进程间通讯。
 * ·无论某个进程运行在互联网的任何地点，无论进程是用什么语言写成的，都可以互相访问并传输数据。
 * ·WebService是基于HTTP和XML的技术。
 * 
 * WebService应用：
 * ·在不同的终端设备上如手机、pad实时查询天气预报、股票行情等。
 * ·网上支付-支付宝
 * ·银联提供统一的WebService，实现银行之间的转账付款
 */
@javax.jws.WebService(serviceName = "WebService", name="WebService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
//定义SOAP的协议(正因为这种协议，A，B两地才能通讯，这种协议可以穿过防火墙，因为底层就是xml技术。叫做简单对象访问协议)
@SOAPBinding(style=SOAPBinding.Style.RPC)
public interface WebService {

	@WebMethod//表明getWeather方法是要发布出去的方法
	String getWeather(String city);
	
}
