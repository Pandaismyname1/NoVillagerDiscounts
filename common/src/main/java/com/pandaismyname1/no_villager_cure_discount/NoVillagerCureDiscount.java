package com.pandaismyname1.no_villager_cure_discount;

import com.google.gson.Gson;
import com.pandaismyname1.no_villager_cure_discount.attributes.VillagerAttributes;
import dev.architectury.platform.Platform;

import java.io.File;
import java.nio.file.Path;

public class NoVillagerCureDiscount
{
	public static final String MOD_ID = "no_villager_cure_discount";
	public static Config CONFIG;

	public static void init() {
		VillagerAttributes.setup();
		if (!configExists()) {
			createConfigFile();
		}
		CONFIG = readConfigFile();
	}

	private static boolean configExists() {
		var configFolder = Platform.getConfigFolder();
		var configFilePath = Path.of(configFolder.toString(), MOD_ID + ".json");
		var fileExists = new File(configFilePath.toString()).exists();
		return fileExists;
	}

	private static Path getConfigFilePath() {
		var configFolder = Platform.getConfigFolder();
		var configFilePath = Path.of(configFolder.toString(), MOD_ID + ".json");
		return configFilePath;
	}

	private static void createConfigFile() {
		var config = new Config();
		var configFilePath = getConfigFilePath();
		var gson = new Gson();
		var configJson = gson.toJson(config);
		try {
			var file = new File(configFilePath.toString());
			var parent = file.getParentFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			if (!file.exists() && !file.createNewFile()) {
				throw new IllegalStateException("Couldn't create file: " + file);
			}
			var writer = new java.io.FileWriter(file);
			writer.write(configJson);
			writer.close();
			System.out.println("Writing config to: " + configFilePath);
		} catch (Exception e) {
			System.err.println("Failed to create config file: " + e);
		}

	}

	private static Config readConfigFile() {
		var configFilePath = getConfigFilePath();
		var gson = new Gson();
		try {
			var file = new File(configFilePath.toString());
			var reader = new java.io.FileReader(file);
			var config = gson.fromJson(reader, Config.class);
			return config;
		} catch (Exception e) {
			System.err.println("Failed to read config file: " + e);
			return new Config();
		}
	}
}
