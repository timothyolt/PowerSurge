package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.bytefire.ld27.core.LD27;

public class EndScreen extends AbstractScreen {

    private String message;

    public EndScreen(String message, LD27 game) {
        super(game);
        this.message = message;
    }

    @Override
    public void show() {
        super.show();

        Label text = new Label(message, new Label.LabelStyle(new BitmapFont(Gdx.files.internal("hud.fnt"), false), new Color(1F, 1F, 1F, 1F)));
        //text.set
        stage.addActor(text);
        stage.addAction(fadeIn(0.25F));
        text.setX((stage.getWidth() / 2) - (text.getWidth() / 2));
        text.setY(stage.getHeight() / 2);
    }
}
