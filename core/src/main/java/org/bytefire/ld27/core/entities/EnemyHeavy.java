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
import org.bytefire.ld27.core.entities.Shot.BulletFrom;
import org.bytefire.ld27.core.screen.AbstractScreen;

import org.bytefire.ld27.core.screen.GameScreen;

public class EnemyHeavy extends Entity{

    private static final float MAX_VELOCITY = 112F;
    private static final float FIRE_RATE = 1F;

    private final GameScreen screen;
    private final Vector2 angle;
    private final TextureRegion tex;

    Random random;

    private float lastAngle;
    private float power;
    private float shotDelta;
    private boolean flipped;
    private boolean outOfRange;

    public EnemyHeavy(int x, int y, int r, LD27 game){
        super(x, y, game.getSpriteHandler().getRegion(Sprite.ENEMY_HEAVY), new Rectangle(23, 0, 17, 28), game);
        tex = game.getSpriteHandler().getRegion(Sprite.ENEMY_HEAVY);

        if(game.getScreen() instanceof GameScreen) screen = (GameScreen) game.getScreen();
        else screen = null;

        if (screen != null) screen.setPower2(screen.getPower2() - 50);

        setTouchable(Touchable.enabled);

        random = new Random();

        setRotation(r);

        angle = new Vector2(r, 0);

        lastAngle = 90;
        power = 0;
        shotDelta = 0;
        health = 4;

        //game.getSfxHandler().play(Sfx.SHOOT);
        if (screen != null) screen.getEnemyHeavies().add(this);
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
        TextureRegion region = game.getSpriteHandler().getRegion(Sprite.HEAVY_ARM);
        if(!flipped) region.flip(true, false);
        else if(flipped && !outOfRange) region.flip(true, true);
        else region.flip(true, false);

        batch.draw(region,
            getX() + 12, getY() + 24,     //Position
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
        else if(screen.getPlayer().position.dst(position) <= Gdx.graphics.getWidth()/2){
            float dist = screen.getPlayer().position.dst(position);
            velocity.x = (float) (((screen.getPlayer().getX() - getX()) / dist) * MAX_VELOCITY);
            velocity.y = (float) (((screen.getPlayer().getY() - getY()) / dist) * MAX_VELOCITY);
        }
        else velocity.x = MAX_VELOCITY;

        if(position.x > screen.getStage().getWidth() - Sprite.BASE.width -212) {
            velocity.x = 0;
            shoot(delta, 270);
        }
    }

    public void calcAngle(float delta){
        outOfRange = false;
        Entity target = findClosest();
        if(target == null) target = screen.getPlayer();
        if(target.position.dst(position) <= Gdx.graphics.getWidth()/2) {
            long angleModifier = (random.nextInt() % 8) + 8;
            float mAngle = (float) toDegrees(atan((position.y - target.position.y) / (position.x - target.position.x)));
            //GDX angles have 0 up, not right
            if (target.position.x - position.x > 0) mAngle += 360 - 90 + angleModifier;
            else mAngle += 180 - 90 + angleModifier;
            System.out.println(mAngle);
            if((mAngle > 260 && mAngle < 310) || (mAngle > 80 && mAngle < 130)) shoot(delta, mAngle);
            else if(flipped) mAngle = 90;
            else {
                mAngle = 270;
                outOfRange = true;
            }
        }
    }

    public void shoot(float delta, float angle){
        if (shotDelta > FIRE_RATE) {
            lastAngle = angle;
            screen.midground.addActor(new HeavyShot(
                (int) (position.x + origin.x), (int) (position.y + origin.y), (int) angle, BulletFrom.ENEMY,
                game));
            shotDelta = 0;
        }
    }

    @Override
    public boolean remove(){
        if (getLife() > IMMUNITY) {
            if (screen != null) {
                screen.removeEnemyHeavy(this);
                ((AbstractScreen) game.getScreen()).getStage().addActor(new Head((int) (position.x + origin.x), (int) (position.y + origin.y), false, game));
            }
            return super.remove();
        }
        else return false;
    }

    public Entity findClosest(){
        Entity finalTarget = null;
        float targetX = 9001;
        if(screen != null) for(int i = 0; i < screen.getAllies().size(); i++){
            Ally target = (Ally) screen.getAllies().get(i);
            if(target.getX() < targetX){
                targetX = target.getX();
                finalTarget = target;
            }
            if(screen.getPlayer().getX() < targetX){
                targetX = screen.getPlayer().getX();
                finalTarget = screen.getPlayer();
            }

        }

        return finalTarget;
    }

    public void calcPower(float delta){
        if(power > 35) remove();
        else power += delta/2;
    }
}
