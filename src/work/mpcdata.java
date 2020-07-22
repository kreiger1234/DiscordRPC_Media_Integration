package work;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.math.*;

public class mpcdata {
	static long secondsend;
	static long secondcurrent;
public String getHTML()
{
	String x="";
	String name="";
	try
	{
		URL page=new URL("http://localhost:13579/info.html");
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		URLConnection conn=page.openConnection();
		BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
		x=br.readLine();
		while(true)
		{
			String y=br.readLine(); //X
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
			}
			x=x+y;
		}
		int sindex=x.indexOf("&bull;")+7;
		int endindex=x.indexOf("&bull;", sindex+2);
		int tstart=endindex+7;
		int tend=x.indexOf("/",tstart);
		int totstart=tend+1;
		int totend=x.indexOf(" ",totstart);
		String time="";
		String end="";
		name=x.substring(sindex, endindex);
		time=x.substring(tstart,tend);
		end=x.substring(totstart,totend);
		String[] sec_end= end.split(":");
		String[] current_sec=time.split(":");
		secondsend=((Integer.parseInt(sec_end[0])*3600)+(Integer.parseInt(sec_end[1])*60)+(Integer.parseInt(sec_end[2])));
		secondcurrent=((Integer.parseInt(current_sec[0])*3600)+(Integer.parseInt(current_sec[1])*60)+(Integer.parseInt(current_sec[2])));
	}
	catch(Exception e)
	{
		name="nothing";
	}
	return name;
}
public long get_secondsend()
{
	return secondsend;
}
public long get_secondscurrent()
{
	return secondcurrent;
}

}
