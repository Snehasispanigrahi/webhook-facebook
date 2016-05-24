package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProcessSubscription", urlPatterns = { "/Subscription" })
public class ProcessSubscription extends HttpServlet {
	
	/**
	 * @param args
	 */
	
	static String app_Secret = "b6e08769dc3d3421f9677855ba852013";
	static String app_Id = "521073994746729";
	static String app_AccessToken;
	
	static{
//		https://graph.facebook.com//oauth/access_token?client_id=<app-id>&client_secret=<app-secret>&grant_type=client_credentials
//		https://graph.facebook.com//oauth/access_token?client_id=521073994746729&client_secret=b6e08769dc3d3421f9677855ba852013&grant_type=client_credentials
		String url = "https://graph.facebook.com//oauth/access_token?client_id="+ app_Id +"&client_secret=" + app_Secret + "&grant_type=client_credentials";
		try {
			URL urldemo = new URL(url);
			URLConnection uc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println("app accessToken : "+inputLine);
			app_AccessToken = inputLine;
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* GET /oauth/access_token?  
	    grant_type=fb_exchange_token&amp;           
	    client_id={app-id}&amp;
	    client_secret={app-secret}&amp;
	    fb_exchange_token={short-lived-token}  */
	    /* app-id:521073994746729
	    secret:b6e08769dc3d3421f9677855ba852013 */
		
		String pageId = request.getParameter("page_id");
        String page_acessToken = request.getParameter("access_token");
        System.out.print("page access token:"+page_acessToken);System.out.print("pageId:"+pageId);
		
        if(app_AccessToken != null && page_acessToken != null){
//        	https://graph.facebook.com/debug_token?input_token=<page-short-live-access-token>&access_token=<app-accesstoken>
        	try {
				String url = "https://graph.facebook.com/debug_token?input_token="+ page_acessToken +"&access_token=" + app_AccessToken;
				URL urldemo = new URL(url);
				URLConnection uc = urldemo.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null)
					System.out.println("debug the page access token"+inputLine);
				in.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }

/*        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();*/
      /*  
        if(page_acessToken != null){
//        	https://graph.facebook.com/oauth/client_code?access_token=...&amp;client_secret=...&amp;redirect_uri=...&amp;client_id=...
        	String url = "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&amp;client_id="+ app_Id +"&amp;client_secret=" + app_Secret + "&amp;fb_exchange_token=" + page_acessToken;
			URL urldemo = new URL(url);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();        
		}*/
	}

}
