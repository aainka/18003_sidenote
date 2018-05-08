package platform.sidenote.restapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import platform.sidenote.OV_Task;

public class RestClient {

	private HttpClient httpclient = new DefaultHttpClient();
	private String session_token = null;
	// private String url = "http://dm1401024591792.fun25.co.kr/api/V2/";
	private String url = "http://localhost:8000/aaa";
	private String apiKey = "6498a8ad1beb9d84d63035c5d1120c007fad6de706734db9689f8996707e0f7d";
//	private ObjectMapper objectMapper = new ObjectMapper();

	public RestClient() {
	//	objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	//	objectMapper.setDateFormat(df);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	}

	private void test() {
		try {
			// login("xx", "yy");
			List<OV_Task> list = new LinkedList<OV_Task>();
			list.add(new OV_Task("hongkill"));
			list.add(new OV_Task("Donals"));
			insert(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setApiKey(HttpRequestBase request) {
		request.addHeader("X-DreamFactory-Api-Key", apiKey);
		if (session_token != null) {
			request.addHeader("X-DreamFactory-Session-Token", session_token);
		}
	}

	public void login(String userId, String passwd) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url + "user/session");
		setApiKey(httpPost);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("app_name", "admin"));
		nameValuePairs.add(new BasicNameValuePair("email", "aainka@naver.com"));
		nameValuePairs.add(new BasicNameValuePair("password", "root123"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
		httpPost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpPost);
		session_token = printResponse(response, "session_token");
	}

	public int insert(List<OV_Task> tasks) throws JsonGenerationException, JsonMappingException, IOException {
		HttpPost request = new HttpPost(url + "/");
		request.setEntity(encodeBody(tasks));
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String s = getEntityString(entity);
			System.out.println("INSERT_RESPONSE: " + s);
			// ResourceUnit ns2 = objectMapper.readValue(s.getBytes(), ResourceUnit.class);
			// return ns2.getResource().get(0).getIds();
		}
		return 0;
	}
	
	public List<OV_Task> list() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(url + "/");
		setApiKey(request);
		HttpResponse response = httpclient.execute(request);
		System.out.println("===");

		List<OV_Task> list = null;
		HttpEntity entity = response.getEntity();
		// System.out.println(response.getStatusLine());
		if (entity != null) {
			// System.out.println("--Response content length: " +
			// entity.getContentLength());
			String s = getEntityString(entity);
			  list = OV_Task.decode(s);
//			try {
//				ResourceUnit emp = objectMapper.readValue(s.getBytes(), ResourceUnit.class);
//				System.out.println("Response = " + emp);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

		}
		return list;
	}
	 
	
	public String printResponse(HttpResponse response, String findKey) {
		String findValue = null;
		HttpEntity entity = response.getEntity();
		// System.out.println(response.getStatusLine());
		if (entity != null) {
			// System.out.println("Response content length: " +
			// entity.getContentLength());
			String s = getEntityString(entity);
			try {
				if (findKey != null) {
					JSONParser parser = new JSONParser();
					JSONObject jsonObject = (JSONObject) parser.parse(s);
					findValue = jsonObject.get(findKey).toString();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return findValue;
	}

	private String getEntityString(HttpEntity entity) {
		String sContent = null;
		// System.out.println("Response content length: " +
		// entity.getContentLength());
		BufferedReader rd;
		try {
			rd = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line = "";
			sContent = line;
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				sContent += line;
			}
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ans=" + sContent);
		return sContent;
	}

	public StringEntity encodeBody(List<OV_Task> tasks)
			throws JsonGenerationException, JsonMappingException, IOException {
		StringWriter writer = new StringWriter();
		OV_Task.encode(writer, tasks);
		// objectMapper.writeValue(writer, list);
		String body = writer.toString();
		StringEntity entity = new StringEntity(body, "UTF-8");
		entity.setContentType("application/json; charset=utf-8");
		return entity;
	}

	public static void main(String arg[]) {
		new RestClient().test();
	}

}
