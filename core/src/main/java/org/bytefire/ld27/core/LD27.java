package org.bytefire.ld27.core;

import org.bytefire.ld27.core.asset.SpriteHandler;
import org.bytefire.ld27.core.screen.SplashScreen;
import com.badlogic.gdx.Game;
import org.bytefire.ld27.core.asset.AudioHandler;
import org.bytefire.ld27.core.screen.AbstractScreen;
import org.bytefire.ld27.core.screen.GameScreen;

public class LD27 extends Game {

    private static final boolean DEV_MODE = false;

    private final SpriteHandler texture;
    private final AudioHandler sfx;

    public LD27(){
        super();
        texture = new SpriteHandler();
        sfx = new AudioHandler();
    }

    public AbstractScreen getSplashScreen(){
        if (DEV_MODE) return new GameScreen(this);
        else return new SplashScreen(this);
    }

    public SpriteHandler getSpriteHandler(){
        return texture;
    }

    public AudioHandler getAudioHandler(){
        return sfx;
    }

    @Override
    public void create () {
        setScreen(getSplashScreen());
    }

    @Override
    public void resize (int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void pause () {
        super.pause();
    }

    @Override
    public void resume () {
        super.resume();
    }

    @Override
    public void dispose () {
        super.dispose();
    }
}
