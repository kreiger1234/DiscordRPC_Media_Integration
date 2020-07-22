package work;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class test {
	public static void main(String[] args)
	{
		try
		{
			URL page=new URL("https://translate.google.com/#view=home&op=translate&sl=ja&tl=en&text=川田 まみ");
			URLConnection conn=page.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			conn.connect();
			BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line=br.readLine())!=null)
			{
				System.out.println(line);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
