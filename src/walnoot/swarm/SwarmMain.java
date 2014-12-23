package walnoot.swarm;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SwarmMain {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 1280;
		config.height = 720;
		config.foregroundFPS = 0;
		config.vSyncEnabled = false;
		config.title = "Squash";
		config.useGL20 = true;
		
		config.addIcon("icon_16.png", FileType.Internal);
		config.addIcon("icon_32.png", FileType.Internal);
		
		new LwjglApplication(new SwarmApplication(), config);
	}
}
