package syam.CraftIRCBridge;

import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import syam.CraftIRCBridge.Bridge.BridgeManager;
import syam.CraftIRCBridge.Listeners.BridgePlayerListener;

import com.ensifera.animosity.craftirc.CraftIRC;

public class CraftIRCBridge extends JavaPlugin{
	// Logger
	public final static Logger log = Logger.getLogger("Minecraft");
	public final static String logPrefix = "[IRCBridge] ";
	public final static String msgPrefix = "&6[IRCBridge] &f";

	// ** Listener **
	private final BridgePlayerListener playerListener = new BridgePlayerListener(this);

	// ** Private classes **
	private ConfigurationManager config;

	// Variable

	// ** Hookup Plugins **
	public static CraftIRC craftIRC;

	// Instance
	private static CraftIRCBridge instance;

	/**
	 * プラグイン起動処理
	 */
	public void onEnable(){
		instance = this;
		PluginManager pm = getServer().getPluginManager();
		config = new ConfigurationManager(this);

		// loadconfig
		try{
			config.loadConfig(true);
		}catch (Exception ex){
			log.warning(logPrefix+"an error occured while trying to load the config file.");
			ex.printStackTrace();
		}

		// Hookup Plugins
		// CraftIRC
		Plugin p = pm.getPlugin("CraftIRC");
		if (p == null){
			log.warning(logPrefix+"Cannot find CraftIRC! Disabling plugin.");
			getPluginLoader().disablePlugin(this); return;
		}else{
			craftIRC = (CraftIRC) p;
		}

		// Herochat
		p = pm.getPlugin("Herochat");
		if (p == null){
			log.warning(logPrefix+"Cannot find Herochat! Disabling plugin.");
			getPluginLoader().disablePlugin(this); return;
		}

		// bridges setup
		setupBridges();

		// Regist Listeners
		pm.registerEvents(playerListener, this);

		// メッセージ表示
		PluginDescriptionFile pdfFile=this.getDescription();
		log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is enabled!");
	}

	public void setupBridges(){
		Set<String> bridges = config.bridges;
		if (bridges != null){
			for (String bridge : bridges){
				BridgeManager.add(bridge, getConfig().getString("Bridges." + bridge));
				log.info(logPrefix +  bridge + " added!");
			}
		}else{
			log.warning(logPrefix + "No Bridges Loaded! Configure config.yml file!");
		}
	}

	/**
	 * プラグイン停止処理
	 */
	public void onDisable(){
		// メッセージ表示
		PluginDescriptionFile pdfFile=this.getDescription();
		log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is disabled!");
	}

	/**
	 * 設定マネージャを返す
	 * @return ConfigurationManager
	 */
	public ConfigurationManager getConfigs() {
		return config;
	}

	/**
	 * インスタンスを返す
	 * @return プラグインインスタンス
	 */
	public static CraftIRCBridge getInstance() {
    	return instance;
    }
}
