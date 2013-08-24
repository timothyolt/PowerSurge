package org.bytefire.ld27.core;

import org.bytefire.ld27.core.asset.TextureHandler;
import org.bytefire.ld27.core.screen.SplashScreen;
import com.badlogic.gdx.Game;
import org.bytefire.ld27.core.asset.SfxHandler;

public class LD27 extends Game {
    float elapsed;
    
    private final TextureHandler texture;
    private final SfxHandler sfx;
    
    public LD27(){
        super();
        texture = new TextureHandler();
        sfx = new SfxHandler();
    }

    public SplashScreen getSplashScreen(){
        return new SplashScreen(this);
    }
    
    public TextureHandler getTextureHandler(){
        return texture;
    }
    
    public SfxHandler getSfxHandler(){
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
