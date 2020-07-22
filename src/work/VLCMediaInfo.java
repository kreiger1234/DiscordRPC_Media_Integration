package work;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class VLCMediaInfo{
	
	String getStatus()
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
					System.out.println("Unable to connect to vlc for media info, closing in a few seconds");
				}
				//End of connection establishment
				
				
				//Start of JSON parsing. Jackson library used
				ObjectMapper mapper=new ObjectMapper();
				try
				{
					boolean audio=false;
					boolean video=false;
					JsonNode root=mapper.readTree(json_data.toString());
					JsonNode filename=root.path("information").get("category");
					Iterator<Map.Entry<String, JsonNode>> it = filename.fields();
					while(it.hasNext())
					{
						JsonNode field=it.next().getValue();
						if(field.get("Type")!=null)
						{
							if(field.get("Type").asText().equals("Audio"))
							{
								audio=true;
							}
							else if(field.get("Type").asText().equals("Video"))
							{
								video=true;
							}
						}
					}
					if(video)
					{
						return ("Watching");
					}
					else if(audio && !video)
					{
						return ("Listening");
					}
				}
				catch(Exception e)
				{
					System.out.println("Cannot discern whether listening or watching ");
				}
				return "";
	}
	
}