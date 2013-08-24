package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.bytefire.ld27.core.LD27;

public class EndScreen extends AbstractScreen {

    private int drawWidth;
    private int drawHeight;

    public EndScreen(LD27 game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        drawWidth = (int) stage.getWidth();
        drawHeight = (int) stage.getHeight();
        
        Label text = new Label("GAME OVER, LOSER", new Label.LabelStyle(new BitmapFont(), new Color(1F, 1F, 1F, 1F)));
        //text.set
        stage.addActor(text);
        stage.addAction(fadeIn(0.25F));
        text.setX((stage.getWidth() / 2) - (text.getWidth() / 2));
        text.setY(stage.getHeight() / 2);
    }
}