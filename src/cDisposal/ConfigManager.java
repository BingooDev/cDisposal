package cDisposal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	private Main pl = Main.getPlugin(Main.class);

	// Files and file configs here
	public FileConfiguration languagecfg;
	public File languagefile;
	public File savefile;
	// ---------------------------

	public void setup() {
		if (!pl.getDataFolder().exists()) {
			pl.getDataFolder().mkdir();
		}

		languagefile = new File(pl.getDataFolder(), "language.yml");

		if (!languagefile.exists()) {
				InputStream stream = pl.getResource("language.yml");
				File dest = new File(pl.getDataFolder(), "language.yml");
				copy(stream, dest);
			
		}

		languagecfg = YamlConfiguration.loadConfiguration(languagefile);
	}
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public FileConfiguration getLanguage() {
		return languagecfg;
	}

	public void saveLanguage() {
		try {
			languagecfg.save(languagefile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("§4Could not save the language.yml file!");
		}
	}

	public void reloadLanguage() {
		languagecfg = YamlConfiguration.loadConfiguration(languagefile);
	}
}
