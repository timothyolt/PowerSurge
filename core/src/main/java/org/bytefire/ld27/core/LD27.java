package org.bytefire.ld27.core;

import org.bytefire.ld27.core.screen.SplashScreen;
import com.badlogic.gdx.Game;

public class LD27 extends Game {
    float elapsed;

    public SplashScreen getSplashScreen(){
        return new SplashScreen(this);
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
