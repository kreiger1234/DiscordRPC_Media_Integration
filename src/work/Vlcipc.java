package work;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class Vlcipc {
	static String appid="N/A";
	static DiscordRPC lib=DiscordRPC.INSTANCE;
	static VLCData vlc=new VLCData();
	static String oldstate="-";  //For storing what the previous file name was before the 3 sec update.
	static Thread thread=new Thread();
	static Timer t;
	static boolean ran_finished=false;
	static boolean ran_idle=false;
	static VLCMediaInfo vlcmi=new VLCMediaInfo();
	
	void start(String arg)
	{
		appid = arg;
		DiscordEventHandlers handlers=new DiscordEventHandlers();
		handlers.ready = (user) -> System.out.println("Ready");
		lib.Discord_Initialize(appid, handlers, true, "");
		DiscordRichPresence drp=new DiscordRichPresence();
		drp.largeImageKey="vlcimg";
		drp.largeImageText="VLC Media Player";
		drp.details="Watching/Listening :- ";
		lib.Discord_UpdatePresence(drp);
		lib.Discord_RunCallbacks();
		oldstate="-";
		ActionListener al=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!vlc.update().equals("vlcoff"))//If vlc is not off.
				{
				if(!vlc.state.equals("paused") && !vlc.state.equals("stopped")) //If state is  NOT paused
				{
					if(oldstate.equals(vlc.name)) //If the previous file name is the same as new filename
					{
						if(ran_finished || ran_idle) // IN case finished is ran, details are set to finished, which will not be reset to filename here. So oldstate is set to default to facilitate the running of other setting parameters for setting the detaisl like listeningo rw atching
						{
							oldstate="-";
						}
						drp.endTimestamp=(System.currentTimeMillis()/1000)+(vlc.duration-vlc.currenttime); //Current epoch time + remaining time
						// The action that is, Listening or watching is not updated here in case hte old file name is same as new file name
						// The state i.e. the filename of file that is being played is also not updated here in case the old file name is same as new file name;
						lib.Discord_UpdatePresence(drp);
						System.out.println("Equals Update");
						ran_finished=false;
						ran_idle=false;
					}
					else //in case old file name is not same as new file name.
					{
						if(vlcmi.getStatus().equals("Listening"))
						{
							drp.state=vlc.name;
							drp.endTimestamp=(System.currentTimeMillis()/1000)+(vlc.duration-vlc.currenttime); //Current epoch time + remaining time
							drp.details="Listening ↓";
							lib.Discord_UpdatePresence(drp);
							oldstate=vlc.name; //Set oldstate to the current state as the state will now be updated after 3 seconds.
							System.out.println("State changed (Listening)");
						}
						else if(vlcmi.getStatus().equals("Watching"))
						{
							drp.state=vlc.name;
							drp.endTimestamp=(System.currentTimeMillis()/1000)+(vlc.duration-vlc.currenttime); //Current epoch time + remaining time
							drp.details="Watching ↓";
							lib.Discord_UpdatePresence(drp);
							oldstate=vlc.name; //Set oldstate to the current state as the state will now be updated after 3 seconds.
							System.out.println("State changed (Watching)");
						}
					}
				}
				else if(vlc.state.equals("paused")) // no need to check for the start - end time being > 0 as vlc will never go in paused state otherwise;
				{
					drp.details="Idling ↓";
					drp.endTimestamp=System.currentTimeMillis()/1000;
					lib.Discord_UpdatePresence(drp);
					System.out.println("Idle state ");
					ran_idle=true;
				}
				else if(vlc.state.equals("stopped") && oldstate.equals("-")) // Same condition as above comment. If oldstate is empty, that means vlc was just started, and has not played anything.
				{
					lib.Discord_ClearPresence();
					System.out.println("First initialization");
				}
				else if(vlc.state.equals("stopped") && !oldstate.equals("-"))
				{
					drp.details="Finished watching/listening ↓";
					drp.endTimestamp=System.currentTimeMillis()/1000;
					lib.Discord_UpdatePresence(drp);
					System.out.println("Finished state");
					ran_finished=true;
				}
				}
				else // Vlc is off
				{
					lib.Discord_ClearPresence();
					System.out.println("Vlc has been shut off");
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
		}
		if(t.isRunning())
		{
			t.stop();
		}
		lib.Discord_ClearPresence();
		System.out.println("Ran clear presence VLC");
	}
	
	/*
	 * What to implement. Stop the program as soon as it detects VLC has closed down.
	 * Need to imeplement above on the double. It'll otherwise throw a bunch of errors in case vlc closes down and it runs an update method.
	 */
}
