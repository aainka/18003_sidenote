package platform.sidenote.restapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import platform.sidenote.OV_Task;

public class RestServer {

	public static void main(String[] args) throws Exception {
		// class anotation :
		// https://stackoverflow.com/questions/26003216/how-do-i-handle-http-methods-in-undertow
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/aaa", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("SERVER ON");
	}

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			File file = new File("C:/tmp/aaa_web.gson");
			Type listType = new TypeToken<List<OV_Task>>() {
			}.getType();
			System.out.println("METHOD=" + t.getRequestMethod().toString());
			if (t.getRequestMethod().toString().equals("PUT")) {
				InputStream is = t.getRequestBody();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				JsonReader reader = new JsonReader(br);
				List<OV_Task> list = gson.fromJson(reader, listType);
				System.out.println("recevie count=" + list.size());
				System.out.println("recevie count=" + list.get(0).subject);
				String response = "This is the response";
				t.sendResponseHeaders(200, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}

			if (t.getRequestMethod().toString().equals("GET")) {
				
				// File Read
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				JsonReader reader = new JsonReader(br);
			 	List<OV_Task> list = gson.fromJson(reader, listType);
			 	
			 	// Build Result
				StringWriter writer = new StringWriter();
				System.out.println("Read.size="+list.size());
			    OV_Task.encode(writer, list);
			    
			    String encoding = "UTF-8";
			    t.getResponseHeaders().set("Content-Type", "text/html; charset="+encoding);
				String response = writer.toString();
				t.sendResponseHeaders(200, response.getBytes().length);
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}

		}
	}
}
