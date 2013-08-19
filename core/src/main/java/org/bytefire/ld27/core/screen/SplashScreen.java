package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.bytefire.ld27.core.LD27;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen extends AbstractScreen {

    private Texture tex_splash;
    private TextureRegion reg_splash;
    private int drawWidth;
    private int drawHeight;

    public SplashScreen(LD27 game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        tex_splash = new Texture(Gdx.files.internal("studio.png"));
        tex_splash.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        drawWidth = (int) stage.getWidth();
        drawHeight = (int) stage.getHeight();
        float ratioRegion = ((float) drawHeight / (float) drawWidth) * 1600;

        //casted calculations to int to use the correct constructor
        reg_splash = new TextureRegion(tex_splash, 0, (int) (1500 - ratioRegion), 3200, (int) (1500 + ratioRegion));

        Image splash = new Image(reg_splash);

        //Set image to alpha, then fade in and out
        splash.getColor().a = 0F;
        SequenceAction actions = Actions.sequence(fadeIn(0.25F), delay(2F), fadeOut(0.25F));

        splash.setScale((float) drawWidth / 3200F, (float) drawWidth / 3200F);
        splash.addAction(actions);
        stage.addActor(splash);
    }

    @Override
    public void dispose() {
        super.dispose();
        tex_splash.dispose();
    }
}
