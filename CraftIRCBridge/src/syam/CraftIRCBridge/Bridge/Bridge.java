package syam.CraftIRCBridge.Bridge;

import syam.CraftIRCBridge.CraftIRCBridge;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.Herochat;

public class Bridge {
    public String craftIRCTag = "";
    public BridgeEndPoint endPoint = null;
    public Channel GameChannel = null;
    public boolean getAll = false;

    public Bridge(String craftIRCTag, String GameChannel) {
        this.craftIRCTag = craftIRCTag;

        if (GameChannel.equals("*")) {
            this.getAll = true;
            this.GameChannel = null;
        } else {
            this.GameChannel = Herochat.getChannelManager().getChannel(GameChannel);
        }

        endPoint = new BridgeEndPoint(this.GameChannel);
        CraftIRCBridge.craftIRC.registerEndPoint(craftIRCTag, endPoint);
    }
}
