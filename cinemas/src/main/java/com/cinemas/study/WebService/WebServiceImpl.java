package com.cinemas.study.WebService;

import javax.xml.ws.Endpoint;

@javax.jws.WebService(serviceName = "WebServiceImpl", name="WebServiceImpl")
public class WebServiceImpl implements WebService {

	/**
	 * 提供访问天气的服务。
	 */
	@Override
	public String getWeather(String city) {
		String info = city + "\t天气多云转晴！";
		return info;
	}
	
	public static void main(String[] args) {
		String address = "http://127.0.0.1:1234/WebService";
		Endpoint.publish(address, new WebServiceImpl());
		System.out.println("发布服务成功！");
	}
	
	/**
	 * 运行main方法，并使用浏览器访问address?wsdl出现webservice文档，证明服务发布成功。
	 * 1.生成客户端的类结构
	 * 	·打开cmd，进入D盘（d:），创建wsclient文件夹（mkdir wsclient）
	 * 	·wsimport -d wsclient -s wsclient http://127.0.0.1:4444/weatherserve?wsdl
	 * 	·然后找到该目录，将完整的包拷贝到项目的java目录下
	 * 
	 */
}
