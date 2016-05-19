package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProcessSubscription", urlPatterns = { "/Subscription" })
public class ProcessSubscription extends HttpServlet {
	
	/**
	 * @param args
	 */
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("not handled");
	}
		
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int ids;
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String val = request.getParameter("page_id");
        System.out.print("val:"+val);
        if(val != null){
            ids = Integer.parseInt(val);
            out.print(ids);
        }
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
		
		System.out.println("request came:"+stringBuilder.toString());
		
	}

}
