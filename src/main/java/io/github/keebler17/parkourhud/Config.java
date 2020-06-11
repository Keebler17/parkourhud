package io.github.keebler17.parkourhud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Config { // everything in here needs cleanup
	
	public static int getInteger(String key) {
		ParkourHUD.configFile = new File(ParkourHUD.configFile.getPath());

		try {
			BufferedReader reader = new BufferedReader(new FileReader(ParkourHUD.configFile));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] config = line.split("=");

				if (config[0].equals(key)) {
					reader.close();
					return Integer.valueOf(config[1]);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static void setInteger(String key, int value) {

		try {
			List<String> lines = Files.readAllLines(ParkourHUD.configFile.toPath());
			int lineNum = 0;
			for(String line : lines) {
				if(line.split("=")[0].equals(key)) {
					break;
				}
				lineNum++;
			}
			lines.set(lineNum, key + "=" + String.valueOf(value));
			
			Files.write(ParkourHUD.configFile.toPath(), lines);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static String getString(String key) {
		ParkourHUD.configFile = new File(ParkourHUD.configFile.getPath());

		try {
			BufferedReader reader = new BufferedReader(new FileReader(ParkourHUD.configFile));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] config = line.split("=");

				if (config[0].equals(key)) {
					reader.close();
					return config[1];
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
