package work;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.*;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import java.util.List;


/* SCRAP */
public class mpc_albumart {

	public static void main(String[] args) {
		String x;
		try
		{
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
			
			x=x.replace("\\", "\\");
			System.out.println(x);
				File file=new File(x);
				System.out.print(file.exists());
				AudioFile af=AudioFileIO.read(file);
				Tag tag=af.getTag();
				String artist=tag.getFirst(FieldKey.ARTIST);
				System.out.print(artist);
				
				/*Tokenizer tokenizer=new Tokenizer();
				List<Token> tokens=tokenizer.tokenize(artist);
				for(Token token : tokens)
				{
					System.out.println(token.getSurface()+" test "+token.getAllFeatures());
				}*/
				
			//	Artwork artwork=tag.getFirstArtwork();
				//byte[] binaryimage=artwork.getBinaryData();
				//BufferedImage bi=ImageIO.read(new ByteArrayInputStream(binaryimage));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
