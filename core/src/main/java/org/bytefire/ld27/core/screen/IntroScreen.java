package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.bytefire.ld27.core.LD27;

public class IntroScreen extends AbstractScreen{
    private static final String text =
        "Unit ID: " + Integer.toString((int) Math.random()) + "\n" +
        "Unit Mission: Power Surge Enemy Base " + "\n" +
        "Battery Life: . . . " + "\n" +
        "10 seconds";

    private final BitmapFont font;
    private final SpriteBatch draw;

    private float delta;

    public IntroScreen(LD27 game){
        super(game);

        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setColor(1F, 1F, 1F, 1F);
        font.scale(0.25F);
        draw = new SpriteBatch();

        delta = 0;
    }

    @Override
    public void render(float delta){
        super.render(delta);

        this.delta += delta;
        float localDelta = this.delta;

        if (localDelta * 8 > text.length()) localDelta = text.length() / 8;
        if (this.delta * 8 > text.length() + 4) game.setScreen(new GameScreen(game));

        draw.begin();
        font.drawWrapped(draw,
            text.substring(0, (int) (localDelta * 8)) + "",
            0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), HAlignment.CENTER);
        draw.end();
    }

}
