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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
 



public class RestTransport {


	private HttpClient httpclient = new DefaultHttpClient();
	private String session_token = null;
	private String url = "http://dm1401024591792.fun25.co.kr/api/V2/";
	private String apiKey = "6498a8ad1beb9d84d63035c5d1120c007fad6de706734db9689f8996707e0f7d";
	private ObjectMapper objectMapper = new ObjectMapper();

	public RestTransport() {
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		objectMapper.setDateFormat(df);

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

	public void list() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(url + "mysql/_table/issues");
		setApiKey(request);
		HttpResponse response = httpclient.execute(request);
		printResponse2(response);
	}

	public void delete(int id) throws ClientProtocolException, IOException {
		HttpDelete request = new HttpDelete(url + "mysql/_table/issues/" + id);
		setApiKey(request);
		HttpResponse response = httpclient.execute(request);
		printResponse(response, null);
	}



	public int insert(NoteSheet nNote) throws JsonGenerationException, JsonMappingException, IOException {
		HttpPost request = new HttpPost(url + "mysql/_table/issues");
		setApiKey(request);
		{
			List<NoteSheet> list = new LinkedList<NoteSheet>();
			list.add(nNote);
			request.setEntity(encodeBody(list));
		}
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			try {
				String s = getEntityString(entity);
				System.out.println("INSERT_RESPONSE: " + s);
				ResourceUnit ns2 = objectMapper.readValue(s.getBytes(), ResourceUnit.class);
				return ns2.getResource().get(0).getIds();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return 0;
	}

	public void update(BeanObject selectedObject) throws JsonGenerationException, JsonMappingException, IOException {
		HttpPatch request = new HttpPatch(url + "mysql/_table/issues");
		setApiKey(request);
		List<NoteSheet> list = new LinkedList<NoteSheet>();
		list.add((NoteSheet) selectedObject);
		request.setEntity(encodeBody(list));
		HttpResponse response = httpclient.execute(request);
		printResponse(response, null);
	}



	public String printResponse2(HttpResponse response) {
		System.out.println("===");

		String findValue = null;
		HttpEntity entity = response.getEntity();
		// System.out.println(response.getStatusLine());
		if (entity != null) {
			// System.out.println("--Response content length: " +
			// entity.getContentLength());
			String s = getEntityString(entity);

			try {
				ResourceUnit emp = objectMapper.readValue(s.getBytes(), ResourceUnit.class);
				System.out.println("Response = " + emp);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return findValue;
	}



	public void test() {
		try {
			delete(1);
			delete(2);
			// insert();
			// update();
			list();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<NoteSheet> getList() throws ClientProtocolException, IOException {
		ResourceUnit emp = null;
		HttpGet request = new HttpGet(url + "mysql/_table/issues");
		setApiKey(request);
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		// System.out.println(response.getStatusLine());
		if (entity != null) {
			String s = getEntityString(entity);
			try {
				emp = objectMapper.readValue(s.getBytes(), ResourceUnit.class);
				// System.out.println("Response = " + emp);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return emp.resource;
	}