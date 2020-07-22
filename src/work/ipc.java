package work;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import javax.swing.Timer;

public class ipc {
	static String appid="N/A";
	static String update;
	static long starttime_minus_endtime=14729;
	static boolean mpc=false;
	static boolean vlc=false;
	static ipc2 mpc_rpc=new ipc2();
	static Vlcipc vlc_rpc=new Vlcipc();
	static boolean mpc_started=false;
	static boolean vlc_started=false;
	public static void main(String[] args)
	{
		System.out.println(System.getProperty("file.encoding"));
		if(args.length != 0) {
			appid=args[0];
		}
		if( appid.equals("N/A")) {
			System.out.println("Please supply your Client_ID as a parameter");
			System.exit(1);
		}
		ActionListener al=new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				try
				{
					SocketAddress sr=new InetSocketAddress("localhost", 13579);
					Socket socket=new Socket();
					socket.connect(sr, 100);
					socket.close();
					mpc=true;
				}
				catch(Exception e)
				{
					mpc=false;
				}
				try
				{
					SocketAddress sr=new InetSocketAddress("localhost", 8080);
					Socket socket=new Socket();
					socket.connect(sr, 100);
					socket.close();
					vlc=true;
				}
				catch(Exception e)
				{
					vlc=false;
				}
				if(mpc)
				{
					System.out.println("MPC is alive");
				}
				if(mpc && !mpc_started && !vlc_started)
				{
					System.out.println("MPC Thread starting ");
					mpc_rpc.start(args[0]);
					mpc_started=true;
				}
				if(!mpc || vlc)
				{
					System.out.println("Killing MPC Thread");
					mpc_rpc.stop();
					mpc_started=false;
				}
				if(vlc && !vlc_started)
				{
					System.out.println("Vlc thread starting");
					vlc_rpc.start(args[0]);
					vlc_started=true;
				}
				if(!vlc && vlc_started)
				{
					System.out.println("Killing vlc thread ");
					vlc_rpc.stop();
					vlc_started=false;
				}
				System.out.println("MPC = "+mpc+"VLC = "+vlc);
			}
		};
		Timer t=new Timer(10000,al);
		t.start();
		new Thread(()-> {
			while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
		}).start();;
	}
}
/**/