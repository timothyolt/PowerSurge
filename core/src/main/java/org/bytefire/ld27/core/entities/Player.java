package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.screen.AbstractScreen;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.bytefire.ld27.core.asset.Tex;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.bytefire.ld27.core.screen.GameScreen;

import static com.badlogic.gdx.Input.Keys.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import static org.bytefire.ld27.core.entities.Entity.GRAVITATIONAL_ACCELERATION;
import static java.lang.Math.*;

public class Player extends Entity {

    private static final float MAX_VELOCITY = 256F;
    private static final float POWER = 64F;
    private static final float FRICTION = 16F;
    private static final float ANGUALR_POWER = 10F;
    private static final float FIRE_RATE = 0.5F;
    private static final float ALLY_SPAWN_TIME = 5;

    private static final int MAX_ALLIES = 4;

    private final Vector2 angle;
    private final TextureRegion tex;
    private final GameScreen screen;

    private float allyCoolDown;
    private float shotDelta;
    private boolean flipped;

    public Player(int x, int y, int r, LD27 game){
        super(x, y, game.getTextureHandler().getRegion(Tex.PLAYER), new Rectangle(23, 0, 17, 28), game);
        tex = game.getTextureHandler().getRegion(Tex.PLAYER);
        if (game.getScreen() instanceof GameScreen) screen = (GameScreen) game.getScreen();
        else screen = null;

        if (screen != null) screen.setPower1(screen.getPower1() - 25);
        setTouchable(Touchable.enabled);

        angle = new Vector2(r, 0);

        allyCoolDown = 0;
        shotDelta = 0;
        flipped = false;
    }

    @Override
    public void act(float delta){

        calcVelocity(delta);
        calcAngle(delta);

        float gravity = velocity.y - GRAVITATIONAL_ACCELERATION;
        if (gravity >= -MAX_GRAVITY) velocity.y = gravity;

        float newx = position.x + (velocity.x * delta);
        float newy = position.y + (velocity.y * delta);

        if (newx < 212 || newx > ((AbstractScreen) game.getScreen()).getStage().getWidth() - 212) velocity.x *= -2;
        if (newy > ((AbstractScreen) game.getScreen()).getStage().getHeight()) velocity.y *= -2;
        else if (newy < Tex.MOON.height - bound.y) {
            if (velocity.y < 0) velocity.y = 0;
            newy = Tex.MOON.height - bound.y;
        }
        position.x = newx;
        position.y = newy;

        if (screen != null) screen.centerCamera(position.x, position.y);

        setX(position.x);
        setY(position.y);

        setRotation(angle.x);

        if (velocity.x < 0 && !flipped){
            tex.flip(true, false);
            setDrawable(new TextureRegionDrawable(tex));
            flipped = true;
        }
        if (velocity.x > 0 && flipped){
            tex.flip(true, false);
            setDrawable(new TextureRegionDrawable(tex));
            flipped = false;
        }

        if(Gdx.input.isKeyPressed(Z)) spawnAlly(delta);

        allyCoolDown += delta;
        shotDelta += delta;
        life += delta;

    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha){
        super.draw(batch, parentAlpha);

        batch.draw(game.getTextureHandler().getRegion(Tex.ARM),
            getX() + 1, getY() - 3,     //Position
            16, 16,                     //Origin
            32, 32,                     //Width/Height
            1, 1,                       //Scale
            getAngleToMouse());         //Rotation
    }

    public void calcVelocity(float delta){
        if ((Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) && velocity.x > -MAX_VELOCITY) velocity.x -= POWER;
        else if (velocity.x < 0)
            if (velocity.x < - FRICTION) velocity.x += FRICTION;
            else velocity.x = 0;
        if ((Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) && velocity.x < MAX_VELOCITY) velocity.x += POWER;
        else if (velocity.x > 0)
            if (velocity.x > FRICTION) velocity.x -= FRICTION;
            else velocity.x = 0;
        if ((Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) && velocity.y > -MAX_VELOCITY) velocity.y -= POWER;
        if ((Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) && velocity.y < MAX_VELOCITY)  velocity.y += POWER * 4;
    }

    public float getAngleToMouse(){
        Vector2 mouse = ((AbstractScreen) game.getScreen()).getStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        mouse.add(0, -16);
        float mAngle = (float) toDegrees(atan((mouse.y - position.y) / (mouse.x - position.x)));
        //GDX angles have 0 up, not right
        if (mouse.x - position.x > 0) mAngle += 360 - 90;
        else mAngle += 180 - 90;
        return mAngle;
    }

    public void calcAngle(float delta){

        if((Gdx.input.isButtonPressed(Buttons.LEFT))) {
            shoot(delta, getAngleToMouse());
        }

        /*if (angle.x >= 0) angle.x = angle.x % 360;
         * else angle.x += 360;
         * if (Gdx.input.isKeyPressed(A) && Gdx.input.isKeyPressed(W)){
         * if (angle.x > 40 && angle.x < 220) angle.x -= ANGUALR_POWER;
         * else if (angle.x != 40) angle.x += ANGUALR_POWER;
         * shoot(delta);
         * }
         * else if (Gdx.input.isKeyPressed(A) && Gdx.input.isKeyPressed(S)){
         * if (angle.x > 140 && angle.x < 320) angle.x -= ANGUALR_POWER;
         * else if (angle.x != 140) angle.x += ANGUALR_POWER;
         * shoot(delta);
         * }
         * else if (Gdx.input.isKeyPressed(D) && Gdx.input.isKeyPressed(W)){
         * if (angle.x > 140 && angle.x < 320) angle.x += ANGUALR_POWER;
         * else if (angle.x != 320) angle.x -= ANGUALR_POWER;
         * shoot(delta);
         * }
         * else if (Gdx.input.isKeyPressed(D) && Gdx.input.isKeyPressed(S)){
         * if (angle.x > 40 && angle.x < 220) angle.x += ANGUALR_POWER;
         * else if (angle.x != 220) angle.x -= ANGUALR_POWER;
         * shoot(delta);
         * }
         * else if (Gdx.input.isKeyPressed(A)){
         * if (angle.x > 90 && angle.x < 270) angle.x -= ANGUALR_POWER;
         * else if (angle.x != 90) angle.x += ANGUALR_POWER;
         * shoot(delta);
         * }
         * else if (Gdx.input.isKeyPressed(D)){
         * if (angle.x > 90 && angle.x < 270) angle.x += ANGUALR_POWER;
         * else if (angle.x != 270) angle.x -= ANGUALR_POWER;
         * shoot(delta);
         * }
         * else if (Gdx.input.isKeyPressed(W)){
         * if (angle.x > 180 && angle.x < 360) angle.x += ANGUALR_POWER;
         * else if (angle.x != 0 && angle.x != 360) angle.x -= ANGUALR_POWER;
         * shoot(delta);
         * }
         * else if (Gdx.input.isKeyPressed(S)){
         * if (angle.x < 180 && angle.x > 0) angle.x += ANGUALR_POWER;
         * else if (angle.x != 180) angle.x -= ANGUALR_POWER;
         * shoot(delta);
         * }*/
    }

    public void shoot(float delta, float angle){
        if (shotDelta > FIRE_RATE) {
            if (screen != null){
                screen.background.addActor(new Shot(
                    (int) (position.x + origin.x), (int) (position.y + 10), (int) angle, true,
                    game));
                shotDelta = 0;
                screen.setPower1(screen.getPower1() - 1);
            }
        }
    }

    public void spawnAlly(float delta){
        if(screen != null && screen.getAllies().size() < MAX_ALLIES && allyCoolDown > ALLY_SPAWN_TIME &&  screen.getPower1() >25){
            allyCoolDown = 0 ;
            screen.setPower1(screen.getPower1() - 25);
            screen.getStage().addActor(new Ally ((int)((AbstractScreen) game.getScreen()).getStage().getWidth() - (Tex.BASE.width / 2) -212, Tex.MOON.height + Tex.PLAYER.height, 0, game));
        }
        else allyCoolDown += delta;
    }

}
