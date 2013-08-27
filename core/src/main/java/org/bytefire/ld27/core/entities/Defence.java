package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.Gdx;
import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;
import java.util.Random;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Sprite;
import org.bytefire.ld27.core.screen.AbstractScreen;
import org.bytefire.ld27.core.screen.GameScreen;

public class Defence extends Entity{

    public enum DefenceState {ENEMY, NEUTRAL, ALLY};

    private static final float FIRE_RATE = 0.75F;

    private DefenceState state;
    private float shotDelta;
    private float lastAngle;
    private final GameScreen screen;
    private Random random;

    public Defence(int x, int y, LD27 game){
        super(x, y, game.getSpriteHandler().getRegion(Sprite.DEFENCE), game);

        health = 20;
        state = DefenceState.NEUTRAL;
        random = new Random();

        if(game.getScreen() instanceof GameScreen) screen = (GameScreen) game.getScreen();
        else screen = null;
    }

    @Override
    public void act(float delta){
        if (state == DefenceState.ALLY){

        }
        else if (state == DefenceState.ENEMY){

        }
    }

    @Override
    public boolean damage(float amount){
        health -= amount;
        if (health <= 0) {
            state = DefenceState.NEUTRAL;
            return true;
        }
        else return false;
    }

    public DefenceState getSide(){
        return state;
    }

    public void setSide(DefenceState state){
        
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
        return finalTarget;
    }

}
