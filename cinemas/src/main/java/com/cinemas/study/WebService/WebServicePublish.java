package com.cinemas.study.WebService;

import javax.xml.ws.Endpoint;

/**
 * 发布服务的方法
 */
public class WebServicePublish {

	public static void main(String[] args) {
		String address = "http://127.0.0.1:7777/WebService";
		Endpoint.publish(address, new WebServiceImpl());
		System.out.println("发布服务成功！");
	}
	
}
