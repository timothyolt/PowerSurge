package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.util.Random;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Tex;
import org.bytefire.ld27.core.entities.Enemy;
import org.bytefire.ld27.core.entities.Player;

public class GameScreen extends AbstractScreen {

    private Player player;
    private OrthographicCamera cam;

    private static final int STAGE_WIDTH = 3416;
    private static final int STAGE_HEIGHT = 960;
    private static final int WINDOW_WIDTH = 854;
    private static final int WINDOW_HEIGHT = 480;

    private final Random rand;

    public GameScreen(LD27 game){
        super(game);
        player = null;
    rand = new Random(System.nanoTime());
    }

    @Override
    public void render(float delta){
        super.render(delta);
    }

    @Override
    public void show(){
        super.show();

        stage.setViewport(STAGE_WIDTH, STAGE_HEIGHT, true);
        cam = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
        cam.zoom = 0.25F;
        stage.setCamera(cam);

        player = new Player(STAGE_WIDTH / 2, 130, 0, game);

        Gdx.input.setInputProcessor(stage);

        addFloor();
        addBases();

        stage.addActor(player);
        stage.addActor(new Enemy(STAGE_WIDTH/2-150, 100, 0, game));
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(3416, 480, true);
    }

    public Player getPlayer(){
        return player;
    }

    public OrthographicCamera getCamera(){
        return cam;
    }

    public void centerCamera(float x, float y){
        if (x < WINDOW_WIDTH / 2)
            cam.position.x = WINDOW_WIDTH / 2;
        else if (x > stage.getWidth() - (WINDOW_WIDTH / 2))
            cam.position.x = stage.getWidth() - (WINDOW_WIDTH / 2);
        else cam.position.x = x;
        cam.position.y = y;
        cam.update();
    }

    private void addFloor(){
        int width = (int) Math.ceil(stage.getWidth() / Tex.MOON.width);
        for (int i = 0; i < width; i++){
            Image moon;
            if (rand.nextInt() % 4 == 1)
                 moon = new Image(game.getTextureHandler().getRegion(Tex.MOON_ALT));
            else moon = new Image(game.getTextureHandler().getRegion(Tex.MOON));
            moon.setX(i * Tex.MOON.width);
            moon.setY(0);
            stage.addActor(moon);
        }
    }

    private void addBases(){
        Image base1 = new Image(game.getTextureHandler().getRegion(Tex.BASE));
        base1.setX(0);
        base1.setY(Tex.BASE.height);

        Image base2 = new Image(game.getTextureHandler().getRegion(Tex.BASE));
        base2.setX(stage.getWidth() - Tex.BASE.width);
        base2.setY(Tex.BASE.height);

        stage.addActor(base1);
        stage.addActor(base2);
    }
}
