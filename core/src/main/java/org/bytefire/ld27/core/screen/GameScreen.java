package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.util.Random;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Tex;
import org.bytefire.ld27.core.entities.Base;
import org.bytefire.ld27.core.entities.Enemy;
import org.bytefire.ld27.core.entities.Player;

public class GameScreen extends AbstractScreen {

    private static final int STAGE_WIDTH = 3416;
    private static final int STAGE_HEIGHT = 960;
    private static final int WINDOW_WIDTH = 854;
    private static final int WINDOW_HEIGHT = 480;
    
    private static final int MAX_ENEMIES = 5;

    private final Random rand;

    private int enemyCount;
    private int coolDownTime;
    
    private float power1;
    private float power2;
    private Player player;
    private OrthographicCamera cam;
    private SpriteBatch hud;
    private BitmapFont hudFont;

    public GameScreen(LD27 game){
        super(game);
        player = null;
        rand = new Random(System.nanoTime());
        power1 = 1000;
        power2 = 1000;
        hud = new SpriteBatch();
        hudFont = new BitmapFont();
        coolDownTime = 0;
    }

    @Override
    public void render(float delta){
        super.render(delta);
        coolDownTime += delta;
        hud.begin();
        hudFont.setColor(0.5F, 0.5F, 1F, 1F);
        hudFont.draw(hud, "Power: " + Float.toString(power2), 64, 64);
        hudFont.draw(hud, "Power: " + Float.toString(power1), Gdx.graphics.getWidth() - 164, 64);
        hud.end();
        
        addEnemy(coolDownTime);
    }

    @Override
    public void show(){
        super.show();

        stage.setViewport(STAGE_WIDTH, STAGE_HEIGHT, true);
        cam = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
        cam.zoom = 0.25F;
        stage.setCamera(cam);

        Gdx.input.setInputProcessor(stage);

        addFloor();
        addBases();

        addEnemy(10);
        newPlayer();
        
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(3416, 480, true);
    }

    public Player getPlayer(){
        return player;
    }
    
    public void newPlayer(){
        player = new Player((int)stage.getWidth() - Tex.PLAYER.width, 130, 0, game);
        stage.addActor(player);
    }
    
    public void addEnemy(float delta){
        if(enemyCount < MAX_ENEMIES && delta > 10){
            coolDownTime = 0;
            enemyCount++;
            stage.addActor(new Enemy(0 + Tex.PLAYER.width, 100, 0, game));
        }
        else coolDownTime += delta;
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

    public float getPower1(){
        return power1;
    }

    public void setPower1(float power){
        power1 = power;
    }

    public float getPower2(){
        return power2;
    }

    public void setPower2(float power){
        power2 = power;
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
        Base base1 = new Base(0, Tex.BASE.height - 48, false, game);
        Base base2 = new Base((int) (stage.getWidth() - Tex.BASE.width), Tex.BASE.height - 48, true, game);

        stage.addActor(base1);
        stage.addActor(base2);
    }

    @Override
    public void dispose(){
        hud.dispose();
    }
}
