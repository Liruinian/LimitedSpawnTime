package com.timlrn2016.limitedspawntime;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
	public void onEnable() {
		File folderp = new File(getDataFolder(),"\\datas");
		File filec = new File(getDataFolder(), "config.yml");
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		if (!folderp.exists()) {
			folderp.mkdir();
		}
		
		if (!filec.exists()) {
			getServer().getConsoleSender().sendMessage("§6[限制重生]未发现配置文件..正在新建一个配置文件");
			getServer().getConsoleSender().sendMessage("§6[Limited Spawn Time]No config Found! We'll create a new one for you!");
			this.saveDefaultConfig();
		}
		this.reloadConfig();
		
		if(!(getConfig().getBoolean("Enable"))) {
			getServer().getConsoleSender().sendMessage("§4[限制重生] 您未启动本插件！！ 请将配置文件中的Enable: false 改为 true");
			getServer().getConsoleSender().sendMessage("§4[Limited Spawn Time] You didn't turn this plugin on! Please edit the config, and change Enable: false to true");
			getServer().getPluginManager().disablePlugin(this);
		}else{
		Bukkit.getPluginManager().registerEvents (new PlayerSpawnListener(), this);
		Bukkit.getPluginManager().registerEvents (new PlayerDeadListener(), this);
		Bukkit.getPluginManager().registerEvents (new PlayerJoinListener(), this);
		
		this.getServer().getConsoleSender().sendMessage("§2[限制重生]成功加载限制重生插件 Made By Tim_LRN2016");
		}
	}


	public class PlayerDeadListener implements Listener {
		@EventHandler
		public void onDead(PlayerDeathEvent e) {
			if(e.getEntityType().equals(EntityType.PLAYER)) {
				Player p = Bukkit.getPlayer(e.getEntity().getName());
				if(getDeathAmount(p) <= 0) {
					SetDeathAmount(p,0);
				}else {
				minusDeathAmount(p);
				Bukkit.broadcastMessage("§4[限制重生] 玩家"+p.getName()+"损失了一个重生机会，还剩"+ getDeathAmount(p)+"个机会！");
				}
			}
		}
	}

	public class PlayerSpawnListener implements Listener {
		@EventHandler
		public void onSpawn(PlayerRespawnEvent e) {
			Player p = e.getPlayer();
			int Amount = getDeathAmount(p);
			if(Amount == 0) {
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage("§4[限制重生] 您已经用完了您的重生机会！");
				Bukkit.broadcastMessage("§4[限制重生] 玩家"+p.getName()+"用完了重生机会，已经无法与此世界交互！");
			}
		}
	}
	
	public class PlayerJoinListener implements Listener {
		@EventHandler
		public void onJoin(PlayerJoinEvent e) {
			Player p = e.getPlayer();
			int Amount = getDeathAmount(p);
			if(Amount == 0) {
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage("§4[限制重生] 您已经用完了您的重生机会！");
				Bukkit.broadcastMessage("§4[限制重生] 玩家"+p.getName()+"用完了重生机会，已经无法与此世界交互！");
			}
		}
	}
	public int getDeathAmount(Player p) {
		int deathamount = 0;
		File f = new File(getDataFolder() + "\\datas\\", p.getDisplayName() + ".yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(f);
		if (!f.exists()) {
			try {
				f.createNewFile();
				data.set("PlayerisOP",p.isOp());
				data.set("DeathAmount", getConfig().getInt("Spawntime"));
				data.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		deathamount = data.getInt("DeathAmount");
		return deathamount;
	}
	
	public void minusDeathAmount(Player p) {
		int deathamount = 0;
		File f = new File(getDataFolder() + "\\datas\\", p.getName() + ".yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(f);
		if (!f.exists()) {
			try {
				f.createNewFile();
				data.set("PlayerisOP",p.isOp());
				data.set("DeathAmount", getConfig().getInt("Spawntime"));
				data.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		deathamount = data.getInt("DeathAmount");
		deathamount = deathamount - 1;
		data.set("DeathAmount", deathamount);
		try {
			data.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public void SetDeathAmount(Player p,int i) {
		int deathamount = 0;
		File f = new File(getDataFolder() + "\\datas\\", p.getName() + ".yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(f);
		if (!f.exists()) {
			try {
				f.createNewFile();
				data.set("PlayerisOP",p.isOp());
				data.set("DeathAmount", getConfig().getInt("Spawntime"));
				data.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		deathamount = i;
		data.set("DeathAmount", deathamount);
		try {
			data.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
