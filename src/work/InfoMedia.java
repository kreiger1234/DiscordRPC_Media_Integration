package work;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.abercap.mediainfo.api.MediaInfo;



public class InfoMedia {
	
	String getInfo(String file_loc)
	{
		try
		{
		/*String x;
		URL page=new URL("http://localhost:13579/status.html");
		URLConnection conn=page.openConnection();
		BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
		x=br.readLine();
		if(x.contains("MPC-BE"))
		{
			x=x.substring(x.lastIndexOf(",")+3, x.lastIndexOf("\'"));
			//System.out.println(x);
		}
		else
		{
			x=x.substring(x.lastIndexOf(",")+3, x.lastIndexOf("\""));
			//System.out.println(x);
		}
		
		x=x.replace("\\", "\\");*/
		File f=new File(file_loc);
		MediaInfo mi=new MediaInfo();
		mi.open(f);
		boolean audio=false,video=false;
		if(mi.inform().contains("Video"))
		{
			video=true;
		}
		else if(mi.inform().contains("Audio"))
		{
			audio=true;
		}
		mi.close();
		if(video)
		{
			return "Watching";
		}
		else if(audio && !video)
		{
			return "Listening";
		}
		}
		catch(Exception e)
		{
			System.out.println("Media info cannot get data, probably player is closed. This error will fade in a few seconds");
		}
		return "";
	}
}
