package work;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VLCData {
	public static int duration=-1;
	public static String name="";
	public static int currenttime=-1;
	public static String state="";
	String update()
	{
		//For establishing connection and taking fresh information out of the vlc http api.
		StringBuilder json_data=new StringBuilder();
		try
		{
			URL url=new URL("http://localhost:8080/requests/status.json");
			String encoding=new String(Base64.getEncoder().encode((":12345").getBytes()));
			HttpURLConnection huc=(HttpURLConnection)url.openConnection();
			huc.setRequestMethod("GET");
			huc.setDoOutput(true);
			huc.setRequestProperty("Authorization", "Basic "+encoding);
			//System.out.println(encoding);
			BufferedReader br=new BufferedReader(new InputStreamReader(huc.getInputStream()));
			String line;
			while((line=br.readLine())!=null)
			{
				json_data.append(line);
			}
		}
		catch(Exception e)
		{
			return "vlcoff";
		}
		//End of connection establishment
		
		
		//Start of JSON parsing. Jackson library used
		ObjectMapper mapper=new ObjectMapper();
		try
		{
			JsonNode root=mapper.readTree(json_data.toString());
			JsonNode total_duration=root.path("length"); // Cannot change order, its important
			JsonNode vlcstate=root.path("state");
			JsonNode current_time=root.path("time");
			state=vlcstate.asText();
			duration=total_duration.asInt();
			currenttime=current_time.asInt();
			JsonNode filename=root.path("information").get("category").get("meta").get("filename");
			name=filename.asText();
		}
		catch(Exception e)
		{
			System.out.println("No data fetched ");
		}
		//end of json parsing and string storage.
		return "work";
	}
}
