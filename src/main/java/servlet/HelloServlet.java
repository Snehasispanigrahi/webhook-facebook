package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet(name = "HelloServlet", urlPatterns = { "/webhook" })
public class HelloServlet extends HttpServlet {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String jsonRequest = "{\"entry\": [{\"changes\": [{\"field\": \"leadgen\", \"value\": {\"ad_id\": 0, \"form_id\": 945245845529885, \"leadgen_id\": 974394229281713, \"created_time\": 1463468994, \"page_id\": 1376204942612961, \"adgroup_id\": 0}}], \"id\": \"1376204942612961\", \"time\": 1463468994}], \"object\": \"page\"}";
		System.out.println("request: "+jsonRequest);
		JsonParser  jsonParser = new JsonParser();
		JsonElement jsonElt = jsonParser.parse(jsonRequest);
		
		if(jsonElt.isJsonObject()){
			JsonObject jsonObj = (JsonObject) jsonElt;
			JsonArray entry = (JsonArray) jsonObj.get("entry");
			jsonElt = jsonObj.get("object");
			
			System.out.println("object: "+jsonElt.toString());
			
			
			GsonBuilder builder = new GsonBuilder();
//			builder.setDateFormat("yyyy-MM-dd hh:mm:ss");
			Gson gson = builder.create();
			fbEntry[] leadList = gson.fromJson(entry, fbEntry[].class);
			System.out.println("leadList :"+leadList);
			if(leadList == null || leadList.length == 0)
				System.out.println("No leads!");
			else{
				System.out.println("Leads="+leadList.length);
				
				for(fbEntry lead : leadList){
					String externalSourceData = gson.toJson(lead);
					System.out.println("lead request view: "+externalSourceData);
					
					fbEntry.fbChanges[] changes = lead.changes;
					
					for (int i = 0; i < changes.length; i++) {
						fbEntry.fbChanges.fbValues value = changes[i].value;
						String leadgen_id = value.getLeadgen_id();
						
						System.out.println("Pull this complete udpate: "+leadgen_id);
					}
				}
			}
		}else{
			System.out.println("Error: JSON is not an array");
//			JsonObject jObj = (JsonObject) jsonParser.parse(jsonRequest);
//			JsonArray jAry = (JsonArray) jsonParser.parse(jsonRequest);
//			System.out.println("jObj: "+jObj);
//			System.out.println("object: "+jObj.get("object"));
//			System.out.println("Entry: "+jObj.get("entry"));
//			System.out.println("Entry: "+jObj.get("entry"));
			
//			Gson gson = new Gson();
//			CFError error = gson.fromJson(response, CFError.class);
//			System.out.println(error.getStatus()+","+error.getStatus_code()+","+error.getMessage());
//			throw new HomeBuy360Exception("Error Details="+response, HomeBuy360Exception.EXCEPTION);
		}
	}
	
//	private static class fbRequest{
//		private fbEntry[] entry;
//		private String object;
		
		private static class fbEntry{
			private fbChanges[] changes;
			private String id;
			private String time;
			
			private static class fbChanges{
				private String field;
				private fbValues value;
				
				private static class fbValues{
					private String ad_id;
					private String form_id;
					private String leadgen_id;
					private String created_time;
					private String page_id;
					private String adgroup_id;
					public String getLeadgen_id() {
						return leadgen_id;
					}
					public void setLeadgen_id(String leadgen_id) {
						this.leadgen_id = leadgen_id;
					}
				}
			}
		}

/*		public fbEntry[] getEntry() {
			return entry;
		}

		public void setEntry(fbEntry[] entry) {
			this.entry = entry;
		}

		public String getObject() {
			return object;
		}

		public void setObject(String object) {
			this.object = object;
		}
		
	}*/
	
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
		
		String jsonRequest = stringBuilder.toString();
		
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElt = jsonParser.parse(jsonRequest);
		
		if(jsonElt.isJsonObject()){
			JsonObject jsonObj = (JsonObject) jsonElt;
			JsonArray entry = (JsonArray) jsonObj.get("entry");
			jsonElt = jsonObj.get("object");
			
			System.out.println("object: "+jsonElt.toString());
			
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			fbEntry[] leadList = gson.fromJson(entry, fbEntry[].class);
			System.out.println("leadList :"+leadList);
			if(leadList == null || leadList.length == 0)
				System.out.println("No leads!");
			else{
				System.out.println("Leads="+leadList.length);
				
				for(fbEntry lead : leadList){
					String externalSourceData = gson.toJson(lead);
					System.out.println("lead request view: "+externalSourceData);
					
					fbEntry.fbChanges[] changes = lead.changes;
					
					for (int i = 0; i < changes.length; i++) {
						fbEntry.fbChanges.fbValues value = changes[i].value;
						String leadgen_id = value.getLeadgen_id();
//						before processing should we check the access token's life? If its expired only way to get is by logging in again.
//						Page access tokens last up to 60 days (5184000 seconds), but more importantly, they last as long as the user access token that was used to acquire them. So they will be invalidated as soon as the user that you got them from:
//
//							1.logs out of FB.
//							2.changes password.
//							3.deauthorizes your application.
//							Basically, when you lose the user's token, you will lose the page's token. Instead, you should retrieve page access tokens once per user access token. If you throw out a user access token, throw out the page token. You should not be trying to store page access tokens for any significant period of time. Instead you should get them as needed and forget them when a user's session dies.
//
//							To get a new page access token:
//
//							https://graph.facebook.com/PAGEID?fields=access_token&access_token=USER_ACCESS_TOKEN
						System.out.println("Pull this complete udpate: "+leadgen_id);
						String access_token = "EAAHZA6f5by2kBAFQRnvVn60nlQckPapZBIrZBkLTKqXoK1xcp2uYZCO2Ne2zuZB13wkGxfK6SKyEnfZAtsfDSkiY5GG5tyWG3SnXJrABWFyauDZCwVOwi9EXZCoubduvXWk3ukZBtjjVGf92nezfeSckOPDspiH2t4dZCLf0KgeLLnN1ZCk6MSnWZCCy";
						String url = "https://graph.facebook.com/v2.6/" +leadgen_id + "?access_token=" + access_token ;
						URL urldemo = new URL(url);
						URLConnection yc = urldemo.openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
						String inputLine;
						while ((inputLine = in.readLine()) != null)
							System.out.println(inputLine);
						in.close();
						
					}
				}
			}
		}else{
			System.out.println("Error: JSON is not an object");
		}
		
		
		
	}

}
