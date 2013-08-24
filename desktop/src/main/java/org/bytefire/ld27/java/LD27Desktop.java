package org.bytefire.ld27.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;   

import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.util.XMLConfig;

public class LD27Desktop {
    public static void main (String[] args) {
        LD27 game = new LD27();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        XMLConfig storedConfig = new XMLConfig(ClassLoader.getSystemResourceAsStream("config.xml"));

        config.useGL20 = true;
        config.width = storedConfig.getWidth();
        config.height = storedConfig.getHeight();
        LwjglApplication app = new LwjglApplication(game, config);
    }
}
