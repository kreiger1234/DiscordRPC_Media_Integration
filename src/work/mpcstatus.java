package work;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

public class mpcstatus {
	public String getStatus()
	{
		String x="";
		try
		{
			URL page=new URL("http://localhost:13579/status.html");
			URLConnection conn=page.openConnection();
			BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			x=br.readLine();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(x.contains("Playing"))
		{
			return "Playing";
		}
		else if(x.contains("Paused") || x.contains("N/A") || x.contains("n/a"))
		{
			return "Paused";
		}
		else
		{
			return "";
		}
	}
}
