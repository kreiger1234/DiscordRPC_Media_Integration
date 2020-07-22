package work;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class ipc2 {
	static String appid="N/A";
	static String update;
	static long starttime_minus_endtime=14729;
	static Thread thread=new Thread();
	static Timer t=new Timer(1000,null);
	static DiscordRPC lib=DiscordRPC.INSTANCE;
	MPCmediainfo mpcmi=new MPCmediainfo();
	
	void start(String arg)
	{
		appid = arg;
		DiscordEventHandlers handlers=new DiscordEventHandlers();
		handlers.ready = (user) -> System.out.println("Ready");
		lib.Discord_Initialize(appid, handlers, true, "");
		DiscordRichPresence drp=new DiscordRichPresence();
		update=new mpcdata().getHTML();
		drp.details="Listening to :-";
		drp.endTimestamp=(System.currentTimeMillis())/1000+(new mpcdata().get_secondsend()-new mpcdata().get_secondscurrent());
		if(!update.equals("nothing"))
		{
			drp.state=update;
		}
		drp.largeImageKey="img";
		drp.largeImageText="Media Player Classic ";
		lib.Discord_UpdatePresence(drp);
		lib.Discord_RunCallbacks();
		ActionListener al=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name=new mpcdata().getHTML();
					if(name.equals("nothing"))
					{
						lib.Discord_ClearPresence();
						update="nothing";
						System.out.println("Nothing");
					}
					else if(name.equals(update))
					{
					new mpcdata().getHTML();
					if(new mpcstatus().getStatus().equals("Paused") && starttime_minus_endtime!=0)
					{
						drp.details="Idling :-";
						drp.endTimestamp=System.currentTimeMillis()/1000;
						starttime_minus_endtime=new mpcdata().get_secondsend()-new mpcdata().get_secondscurrent();
					}
					else if(new mpcstatus().getStatus().equals("Paused") && starttime_minus_endtime==0)
					{
						drp.details="Finished Watching/Listening :-";
						starttime_minus_endtime=new mpcdata().get_secondsend()-new mpcdata().get_secondscurrent();
						drp.endTimestamp=System.currentTimeMillis()/1000;
					}
					else
					{
						if(mpcmi.getState().equals("Listening"))//name contains mp3
						{
							drp.details="Listening ↓ ";
							drp.endTimestamp=(System.currentTimeMillis())/1000+(new mpcdata().get_secondsend()-new mpcdata().get_secondscurrent());
						}
						else if(mpcmi.getState().equals("Watching"))
						{
							drp.details="Watching ↓ ";
							drp.endTimestamp=(System.currentTimeMillis())/1000+(new mpcdata().get_secondsend()-new mpcdata().get_secondscurrent());
						}
						else
						{
							drp.details="Listening / Watching ↓ ";
							drp.endTimestamp=(System.currentTimeMillis())/1000+(new mpcdata().get_secondsend()-new mpcdata().get_secondscurrent());
						}
					}
					lib.Discord_UpdatePresence(drp);
					System.out.println("Equals update");
					}
					else
					{
					drp.state=update=name;
					drp.details="Listening / Watching :- ";
					drp.endTimestamp=(System.currentTimeMillis())/1000+(new mpcdata().get_secondsend()-new mpcdata().get_secondscurrent());
					lib.Discord_UpdatePresence(drp);
					System.out.println("Else");
					}
					}
		};
		
		t=new Timer(3000, al);
		t.start();
		thread=new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler");
		thread.start();
	}
	void stop()
	{
		if(thread.isAlive())
		{
			thread.stop();
			lib.Discord_ClearPresence();
			System.out.println("Ran clear presence MPC");
		}
		t.stop();
	}
}
