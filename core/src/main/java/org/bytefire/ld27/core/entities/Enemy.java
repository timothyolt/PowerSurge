package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;
import java.util.Random;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Tex;
import org.bytefire.ld27.core.screen.AbstractScreen;

import org.bytefire.ld27.core.screen.GameScreen;

public class Enemy extends Entity{

    private static final float MAX_VELOCITY = 128F;
    private static final float FIRE_RATE = 0.75F;

    private final Random random;

    private final GameScreen screen;
    private final Vector2 angle;
    private final TextureRegion tex;

    private float shotDelta;
    private boolean flipped;

    public Enemy(int x, int y, int r, LD27 game){
        super(x, y, game.getTextureHandler().getRegion(Tex.ENEMY), new Rectangle(23, 0, 17, 28), game);
        tex = game.getTextureHandler().getRegion(Tex.ENEMY);

        if(game.getScreen() instanceof GameScreen) screen = (GameScreen) game.getScreen();
        else screen = null;


        if (screen != null) screen.setPower2(screen.getPower2() - 25);

        random = new Random();

        setTouchable(Touchable.enabled);

        setRotation(r);

        angle = new Vector2(r, 0);

        shotDelta = 0;

        //game.getSfxHandler().play(Sfx.SHOOT);
        if (screen != null) screen.getEnemies().add(this);

    }

    @Override
    public void act(float delta){

        seek(delta);
        calcAngle(delta);

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

        super.act(delta);
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
        
        if(position.x > ((AbstractScreen) game.getScreen()).getStage().getWidth() - Tex.BASE.width -212) {
            velocity.x = 0;
            shoot(delta, 270);
        }
    }

    public void calcAngle(float delta){
        Entity target = findClosest();
        if(target == null) target = screen.getPlayer();
        if(target.position.dst(position) <= Gdx.graphics.getWidth()/2) {
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
            ((AbstractScreen) game.getScreen()).getStage().addActor(new Shot(
                (int) (position.x + origin.x), (int) (position.y + origin.y), (int) angle, false,
                game));
            shotDelta = 0;
        }
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

    @Override
    public boolean remove(){
        if (getLife() > IMMUNITY) {
            ((GameScreen) game.getScreen()).removeEnemy(this);
            return super.remove();
        }
        else return false;
    }
}
