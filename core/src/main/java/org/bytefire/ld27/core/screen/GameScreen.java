package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.util.ArrayList;
import java.util.Random;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Sprite;
import org.bytefire.ld27.core.entities.Ally;
import org.bytefire.ld27.core.entities.Base;
import org.bytefire.ld27.core.entities.Enemy;
import org.bytefire.ld27.core.entities.Player;

public class GameScreen extends AbstractScreen {

    private static final int STAGE_WIDTH = 3416;
    private static final int STAGE_HEIGHT = 960;
    private static final int WINDOW_WIDTH = 854;
    private static final int WINDOW_HEIGHT = 480;
    private static final float STARFIELD_SCALE = 0.5F;
    private static final int STARFIELD_PADDING = 4;

    private static final int POWER_RATE = 3;
    private static final int MAX_ENEMIES = 10;
    private static final float RESPAWN_TIME = 4;
    private static final float RESPAWN_CHANCE = 5;

    private final Random rand;
    private final Group starfield;

    private float coolDownTime;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Ally> allyList;
    private float power1;
    private float power2;
    private Player player;
    private OrthographicCamera cam;
    private SpriteBatch hud;
    private BitmapFont hudFont;

    public final Group background;
    public final Group midground;
    public final Group foreground;

    public GameScreen(LD27 game){
        super(game);
        enemyList = new ArrayList<Enemy>();
        allyList = new ArrayList<Ally>();
        player = null;
        rand = new Random(System.nanoTime());
        power1 = 1000;
        power2 = 1000;
        hud = new SpriteBatch();
        hudFont = new BitmapFont();
        coolDownTime = 0;

        background = new Group();
        midground = new Group();
        foreground = new Group();

        starfield = new Group();
    }

    @Override
    public void render(float delta){
        super.render(delta);
        hud.begin();
        hudFont.setColor(0.5F, 0.5F, 1F, 1F);
        hudFont.draw(hud, "Power: " + Float.toString(power2), 64, 64);
        hudFont.draw(hud, "Power: " + Float.toString(power1), WINDOW_WIDTH - 164, 64);
        hud.end();

        calcPower(delta);
        addEnemy(delta);
    }

    @Override
    public void show(){
        super.show();

        stage.setViewport(STAGE_WIDTH, STAGE_HEIGHT, true);
        cam = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
        cam.zoom = 0.25F;
        stage.setCamera(cam);

        Gdx.input.setInputProcessor(stage);

        addStars();
        addFloor();
        addBases();
        addEnemy(10);
        addPlayer();

        stage.addActor(background);
        stage.addActor(midground);
        stage.addActor(foreground);
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(3416, 480, true);
    }

    public void calcPower(float delta){
        power1 += delta * POWER_RATE;
        power2 += delta * POWER_RATE;

        if (power1 < 0) game.setScreen(new EndScreen("GAME OVER, LOSER", game));
        if (power2 < 0) game.setScreen(new EndScreen("YOUR PRIDE WILL BE YOUR DOWNFALL", game));
    }

    public Player getPlayer(){
        return player;
    }

    public void addPlayer(){
        player = new Player((int)stage.getWidth() - (Sprite.BASE.width / 2) - 212, Sprite.MOON.height + Sprite.PLAYER.height, 0, game);
        midground.addActor(player);
    }

    public void addEnemy(float delta){
        if(getEnemies().size() < MAX_ENEMIES && rand.nextInt() % RESPAWN_CHANCE == 1 && coolDownTime > RESPAWN_TIME && power2 > 25){
            coolDownTime = 0;
            midground.addActor(new Enemy(Sprite.BASE.width / 2 + 212, Sprite.MOON.height + Sprite.PLAYER.height, 0, game));
        }
        else
        coolDownTime += delta;
    }

    public void removeEnemy(Enemy enemy){
        getEnemies().remove(enemy);
    }

    public void removeAlly(Ally ally){
        getAllies().remove(ally);
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
        if (y > stage.getHeight() - (WINDOW_HEIGHT / 2)) cam.position.y = stage.getHeight() - (WINDOW_HEIGHT / 2);
        else cam.position.y = y;

        starfield.setX((cam.position.x * STARFIELD_SCALE) - (Sprite.STARS.width * STARFIELD_PADDING / 2));
        starfield.setY((cam.position.y * STARFIELD_SCALE) - (Sprite.STARS.height * STARFIELD_PADDING / 2));

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

    private void addStars(){
        int width = (int) Math.ceil(stage.getWidth() / Sprite.STARS.width * STARFIELD_SCALE) + STARFIELD_PADDING;
        int height = (int) Math.ceil(stage.getHeight() / Sprite.STARS.height * STARFIELD_SCALE) + STARFIELD_PADDING;
        for (int x = 0; x < width; x++) for (int y = Sprite.MOON.height / Sprite.STARS.height; y < height; y++){
            Image stars = new Image(game.getSpriteHandler().getRegion(Sprite.STARS));
            stars.setX(x * Sprite.STARS.width);
            stars.setY(y * Sprite.STARS.height);
            starfield.addActor(stars);
            if (rand.nextInt() % 8 == 1){
                Image bigStar = new Image(game.getSpriteHandler().getRegion(Sprite.STARS_ALT));
                bigStar.setColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1F);
                bigStar.setX(x * Sprite.STARS.width);
                bigStar.setY(y * Sprite.STARS.height);
                starfield.addActor(bigStar);
            }
        }
        background.addActor(starfield);
    }

    private void addFloor(){
        int width = (int) Math.ceil(stage.getWidth() / Sprite.MOON.width);
        for (int i = 0; i < width; i++){
            Image moon;
            if (rand.nextInt() % 4 == 1)
                 moon = new Image(game.getSpriteHandler().getRegion(Sprite.MOON_ALT));
            else moon = new Image(game.getSpriteHandler().getRegion(Sprite.MOON));
            moon.setX(i * Sprite.MOON.width);
            moon.setY(0);
            background.addActor(moon);

            Image dark;
            dark = new Image(game.getSpriteHandler().getRegion(Sprite.MOON));
            dark.setColor(Color.BLACK);
            dark.setX(i * Sprite.MOON.width);
            dark.setY(-1 * Sprite.MOON.width);
            background.addActor(dark);
        }
    }

    private void addBases(){
        Base base1 = new Base(0, Sprite.BASE.height - 48, false, game);
        Base base2 = new Base((int) (stage.getWidth() - Sprite.BASE.width), Sprite.BASE.height - 48, true, game);

        background.addActor(base1);
        background.addActor(base2);
    }

    @Override
    public void dispose(){
        hud.dispose();
    }

    public ArrayList getEnemies(){
        return enemyList;
    }

    public ArrayList getAllies(){
        return allyList;
    }

}
