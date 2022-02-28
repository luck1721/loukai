package com.example.demo.bll.service.impl;

import com.example.demo.bll.config.HttpResult;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lk
 * @date 2021/2/20
 */
@Service
public class HttpAPIService {

	@Autowired
	private CloseableHttpClient httpClient;

	@Autowired
	private RequestConfig config;


	/**
	 * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url) throws Exception {
		// 声明 http get 请求
		HttpGet httpGet = new HttpGet(url);

		// 装载配置信息
		httpGet.setConfig(config);

		// 发起请求
		CloseableHttpResponse response = this.httpClient.execute(httpGet);

		// 判断状态码是否为200
		if (response.getStatusLine().getStatusCode() == 200) {
			// 返回响应体的内容
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		}
		return null;
	}

	/**
	 * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url, Map<String, Object> map) throws Exception {
		URIBuilder uriBuilder = new URIBuilder(url);

		if (map != null) {
			// 遍历map,拼接请求参数
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}

		// 调用不带参数的get请求
		return this.doGet(uriBuilder.build().toString());

	}

	/**
	 * 带参数的post请求
	 *
	 * @param url
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url, Map<String, Object> map) throws Exception {
		// 声明httpPost请求
		HttpPost httpPost = new HttpPost(url);
		// 加入配置信息
		httpPost.setConfig(config);

		// 判断map是否为空，不为空则进行遍历，封装from表单对象
		if (map != null) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			// 构造from表单对象
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

			// 把表单放到post里
			httpPost.setEntity(urlEncodedFormEntity);
		}

		// 发起请求
		CloseableHttpResponse response = this.httpClient.execute(httpPost);
		return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
				response.getEntity(), "UTF-8"));
	}


	//发送post请求,参数为json格式
	public  Map<String, Object> sendPostJson(String url, String jsonMag, String[] herderName, String[] headerValue) throws IOException {
		// 声明httpPost请求
		HttpPost httpPost = new HttpPost(url);
		// 加入配置信息
		httpPost.setConfig(config);
		//添加header信息
		httpPost.addHeader("Content-Type", "application/json");//设置httpPost的请求头中的MIME类型为json
		for (int i = 0; i < herderName.length; i++) {
			httpPost.addHeader(herderName[i], headerValue[i]);
		}
		//设置请求参数
		StringEntity se = new StringEntity(jsonMag, "utf-8");//使用json格式来设置body使用StringEntity类
		httpPost.setEntity(se);
		LocalTime startTime = LocalTime.now();//响应开始时间
		HttpResponse response = this.httpClient.execute(httpPost);//访问接口，并且获取返回值
		LocalTime endTime = LocalTime.now();//响应结束时间
		int code = response.getStatusLine().getStatusCode();
		if (code != 200) {
			System.out.println(url + response);
		}
		Map<String, Object> codeAndEntity = new HashMap<>();
		codeAndEntity.put("code", code);
		String a = EntityUtils.toString(response.getEntity(), "UTF-8");
		codeAndEntity.put("entity", a);
		codeAndEntity.put("responseTime", Duration.between(startTime, endTime).toMillis());
		System.out.println("code" +code);
		return codeAndEntity;
	}

	/**
	 * 不带参数post请求
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url) throws Exception {
		return this.doPost(url, null);
	}
}
