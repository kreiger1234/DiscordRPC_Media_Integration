package work;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

public class MPCmediainfo {
	
	static String state="";
	
	String getState()
	{
		String x="";
		String name="";
		try
		{
			URL page=new URL("http://localhost:13579/variables.html");
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			URLConnection conn=page.openConnection();
			BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line=br.readLine())!=null)
			{
				/*String y=br.readLine(); //X
				if(x.contains("MPC-BE"))
				{
					if(y==null)
					{
						break;
					}
				}
				if(x.contains("MPC-HC")) // Does not work unless I do this X & Y SHIT. I GOT NO GOD DAMN IDEA WHY ! Just leave this part as it is.
				{
					if(y.isEmpty())
					{
						break;
					}
				}*/
				x+=line;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		x=x.substring(x.indexOf("\"filepath\"")+11, x.indexOf("</p>",x.indexOf("\"filepath\"")));
		
		state=new InfoMedia().getInfo(x);
		return state;
	}
}
