package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MyServlet", urlPatterns = { "/webhook" })
public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String challenge = "hub.challenge";
		String token = "hub.verify_token";
		String value = req.getParameter(challenge);
		ServletOutputStream out = resp.getOutputStream();
		System.out.println("challenge: "+challenge);
		System.out.println("token: "+token);
//		PrintWriter out = resp.getWriter();
		if(token == "abc123")
			out.println(value);
		
		out.flush();
		out.close();
	}

}
