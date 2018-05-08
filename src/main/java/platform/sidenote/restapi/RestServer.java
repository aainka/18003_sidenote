package platform.sidenote.restapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import platform.sidenote.OV_Task;
import platform.sidenote.TaskTreeModel;

public class RestServer {

	// private static Logger logger = Logger.getLogger(RestServer.class);

	public static void main(String[] args) throws Exception {
		// class anotation :
		// https://stackoverflow.com/questions/26003216/how-do-i-handle-http-methods-in-undertow
		System.out.println("WebServer start 8080");
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/aaa", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("SERVER ON");
	}

	static class MyHandler implements HttpHandler {
		@SuppressWarnings("restriction")
		@Override
		public void handle(HttpExchange t) throws IOException {
			File file = null;
			if (HostConfig.isLinux()) {
				file = new File("/proj7/side_note/data/aaa_web.gnote");
			} else {
				file = new File("C:/tmp/aaa_web.gnote");
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Type listType = new TypeToken<List<OV_Task>>() {
			}.getType();
			System.out.println("METHOD = " + t.getRequestMethod().toString());

			if (t.getRequestMethod().toString().equals("PATCH")) {
				debug("Recieve PUT [OK----------------------]");
				InputStream is = t.getRequestBody();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				{
					BufferedReader br = new BufferedReader(isr);
					JsonReader reader = new JsonReader(br);
					List<OV_Task> list = gson.fromJson(reader, listType);
					System.out.println("recevie count=" + list.size());
					System.out.println("recevie count=" + list.get(0).subject);
					TaskTreeModel.saveFile(list);
				}
				String response = "This is the response";
				t.sendResponseHeaders(200, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
				debug("Recieve PUT [---------------------END]");
			}

			if (t.getRequestMethod().toString().equals("GET")) {
				try {
					debug("Recieve GET [OK----------------------]");
					List<OV_Task> tasks = TaskTreeModel.loadFile();
					String response = OV_Task.encodeList(tasks);
					{
						t.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
						t.sendResponseHeaders(200, response.getBytes().length);
						OutputStream os = t.getResponseBody();
						os.write(response.getBytes());
						os.close();
					}
					debug("Recieve GET [---------------------END]");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		public void debug(String msg) {
			System.out.println("[DEBUG ]" + msg);

		}
	}
}
