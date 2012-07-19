package syam.CraftIRCBridge.Listeners;

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Chatter;
import com.dthielke.herochat.Chatter.Result;
import com.dthielke.herochat.ConversationChannel;

import syam.CraftIRCBridge.CraftIRCBridge;
import syam.CraftIRCBridge.Bridge.Bridge;
import syam.CraftIRCBridge.Bridge.BridgeManager;

public class BridgePlayerListener implements Listener{
	public final static Logger log = CraftIRCBridge.log;

	private final CraftIRCBridge plugin;

	public BridgePlayerListener(final CraftIRCBridge plugin){
		this.plugin = plugin;
	}

	/* 登録するイベントはここから下に */

	@EventHandler
	public void onServerCommand(final ServerCommandEvent event) {
		if (event.getCommand().startsWith("say")) {
			String message = event.getCommand().substring(4, event.getCommand().length() - 1);
			// broadcast
			for (Bridge b : BridgeManager.bridges) {
				b.endPoint.messageOut(message, "Server", b.craftIRCTag, b.GameChannel.getName(), b.GameChannel.getNick());
			}
		}
	}

	@EventHandler
	public void onChannelChat(final ChannelChatEvent e) {
		if (e.getResult() == Result.ALLOWED) {
			String message = e.getBukkitEvent().getMessage();
			String c = null;
			String n = null;

			if (e.getChannel() instanceof ConversationChannel) {
				ConversationChannel cc = (ConversationChannel) e.getChannel();
				c = "-> ";
				for (Chatter ch : cc.getMembers()) {
					if (ch == e.getSender()) continue;
					c = c.concat(ch.getName());
				}
				n = c;
			} else {
				c = (e.getChannel().getName() == null) ? "" : e.getChannel().getName();
				n = (e.getChannel().getNick() == null) ? "" : e.getChannel().getNick();
			}

			for (Bridge b : BridgeManager.getBridges(e.getChannel())) {
				b.endPoint.messageOut(message, e.getSender().getName(), b.craftIRCTag, c, n);
			}
		}
	}
}
