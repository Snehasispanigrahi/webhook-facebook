package servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloServlet", urlPatterns = { "/webhook" })
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
//		doGet(request, response);
		System.out.println("request1: "+request);
		BufferedReader bufferedReader = request.getReader();
		char[] charBuffer = new char[128];
		StringBuilder stringBuilder = new StringBuilder();
		try {
			if(bufferedReader != null){
				int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
			}
		} catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }
		
		System.out.println("request now:"+stringBuilder.toString());
//		Enumeration<String> parameterNames = request.getParameterNames();
//        ServletOutputStream out = request.getOutputStream();
//        JSONObject jsonObj = (JSONObject) JSONValue.parse(request.getParameter("para"));
//        System.out.println(jsonObj.get("message"));
//        JSONObject obj = new JSONObject();
//        obj.put("message", "hello from server");
//        out.print(obj);

	}

}
