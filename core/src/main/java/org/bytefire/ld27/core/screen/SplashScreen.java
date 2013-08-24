package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.bytefire.ld27.core.LD27;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import org.bytefire.ld27.core.asset.Tex;

public class SplashScreen extends AbstractScreen {

    private int drawWidth;
    private int drawHeight;

    public SplashScreen(LD27 game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        drawWidth = (int) stage.getWidth();
        drawHeight = (int) stage.getHeight();
        float ratioRegion = ((float) drawHeight / (float) drawWidth) * 1600;
        
        //casted calculations to int to use the correct constructor
        
        Image splash = new Image(new TextureRegion(game.getTextureHandler().getTexture(Tex.SPLASH), 0, (int) (1500 - ratioRegion), 3200, (int) (1500 + ratioRegion)));
        
        //Set image to alpha, then fade in and out
        splash.getColor().a = 0F;
        SequenceAction actions = Actions.sequence(fadeIn(0.25F), delay(2F), fadeOut(0.25F),
            new Action() {
                @Override
                public boolean act(float delta){
                    game.setScreen(new GameScreen(game));
                    return true;
                }
            });

        splash.setScale((float) drawWidth / 3200F, (float) drawWidth / 3200F);
        splash.addAction(actions);
        stage.addActor(splash);
    }
}
