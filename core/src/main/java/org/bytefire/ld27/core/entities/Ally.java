package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;
import java.util.Random;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Sprite;
import static org.bytefire.ld27.core.entities.Entity.IMMUNITY;
import org.bytefire.ld27.core.screen.AbstractScreen;

import org.bytefire.ld27.core.screen.GameScreen;

public class Ally extends Entity{

    private static final float MAX_VELOCITY = 128F;
    private static final float FIRE_RATE = 0.75F;

    private final GameScreen screen;
    private final Vector2 angle;
    private final TextureRegion tex;

    private Random random;

    private float lastAngle;
    private float power;
    private float shotDelta;
    private boolean flipped;

    public Ally(int x, int y, int r, LD27 game){
        super(x, y, game.getSpriteHandler().getRegion(Sprite.PLAYER), new Rectangle(23, 0, 17, 28), game);
        tex = game.getSpriteHandler().getRegion(Sprite.PLAYER);

        if(game.getScreen() instanceof GameScreen) screen = (GameScreen) game.getScreen();
        else screen = null;

        if (screen != null) screen.setPower2(screen.getPower2() - 25);

        setTouchable(Touchable.enabled);

        setRotation(r);

        random = new Random();

        angle = new Vector2(r, 0);

        lastAngle = 90;
        power = 0;
        shotDelta = 0;
        health = 3;

        //game.getAudioHandler().play(Sfx.SHOOT);
        if (screen != null) screen.getAllies().add(this);
    }

    @Override
    public void act(float delta){

        seek(delta);
        calcAngle(delta);
        calcPower(delta);

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
        
        shotDelta += delta;
        power += delta;

        super.act(delta);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha){
        super.draw(batch, parentAlpha);

        batch.draw(game.getSpriteHandler().getRegion(Sprite.ARM),
            getX() + 1, getY() - 3,     //Position
            16, 16,                     //Origin
            32, 32,                     //Width/Height
            1, 1,                       //Scale
            lastAngle);         //Rotation
    }

    public void seek(float delta){
        Entity target = findClosest();
        if(target != null && target.position.dst(position) <= Gdx.graphics.getWidth()/2){
            float dist = target.position.dst(position);
            velocity.x = (float) (((target.getX() - getX()) / dist) * MAX_VELOCITY);
            velocity.y = (float) (((target.getY() - getY()) / dist) * MAX_VELOCITY);
        }
        else velocity.x = -MAX_VELOCITY;
        if(position.x < Sprite.BASE.width + 212) {
            velocity.x = 0;
            shoot(delta, 90);
        }
    }

    public void calcAngle(float delta){
        Entity target = findClosest();
        if(target != null && target.position.dst(position) <= Gdx.graphics.getWidth() * .6) {
            long angleModifier = (random.nextInt() % 18) - 6;
            float mAngle = (float) toDegrees(atan((position.y - target.position.y) / (position.x - target.position.x)));
            //GDX angles have 0 up, not right
            if (target.position.x - position.x > 0) mAngle += 360 - 90 + angleModifier;
            else mAngle += 180 - 90 + angleModifier;
            shoot(delta, mAngle);
        }
    }

    public void shoot(float delta, float angle){
        if (shotDelta > FIRE_RATE) {
            lastAngle = angle;
            ((AbstractScreen) game.getScreen()).getStage().addActor(new Shot(
                (int) (position.x + origin.x), (int) (position.y + origin.y), (int) angle, Shot.BulletFrom.ALLY,
                game));
            shotDelta = 0;
        }
    }

    @Override
    public boolean remove(){
        if (getLife() > IMMUNITY) {
            if (screen != null) {
                screen.removeAlly(this);
                ((AbstractScreen) game.getScreen()).getStage().addActor(new Head((int) (position.x + origin.x), (int) (position.y + origin.y), true, game));
            }
            return super.remove();
        }
        else return false;
    }

    public Entity findClosest(){
        Entity finalTarget = null;
        float targetX = 0;
        if(screen != null) for(int i = 0; i < screen.getEnemies().size(); i++){
            Enemy target = (Enemy) screen.getEnemies().get(i);
            if(target.getX() > targetX){
                targetX = target.getX();
                finalTarget = target;
            }
        }
        if(screen != null) for(int i = 0; i < screen.getEnemyHeavies().size(); i++){
            EnemyHeavy target = (EnemyHeavy) screen.getEnemyHeavies().get(i);
            if(target.getX() > targetX){
                targetX = target.getX();
                finalTarget = target;
            }
        }
        return finalTarget;
    }

    public void calcPower(float delta){
        if(power > 20) remove();
        else power += delta/2;
    }
}
