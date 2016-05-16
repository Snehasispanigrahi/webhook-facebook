package servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;

@WebServlet(name = "MyServlet", urlPatterns = { "/webhook" })
public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String challenge = "hub.challenge";
		String token = "hub.verify_token";
		String challengeValue = req.getParameter(challenge);
		String tokenValue = req.getParameter(token);
		ServletOutputStream out = resp.getOutputStream();
//		PrintWriter out = resp.getWriter();
		if(tokenValue.equals("abc123")){
			out.write(challengeValue.getBytes());
			System.out.println("challenge: "+challengeValue);
		}
		out.flush();
		out.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("request1: "+request.toString());
		
		Enumeration<String> parameterNames = request.getParameterNames();
		
		do {
			System.out.println("para name: "+parameterNames.nextElement());
		} while(parameterNames.hasMoreElements());
		
		
//        ServletOutputStream out = request.getOutputStream();
//        JSONObject jsonObj = (JSONObject) JSONValue.parse(request.getParameter("para"));
//        System.out.println(jsonObj.get("message"));
//        JSONObject obj = new JSONObject();
//        obj.put("message", "hello from server");
//        out.print(obj);

	}

}
