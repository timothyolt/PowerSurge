package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.Gdx;
import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;
import java.util.Random;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Sprite;
import org.bytefire.ld27.core.screen.GameScreen;

public class Defence extends Entity{

    public enum DefenceState {ENEMY, NEUTRAL, ALLY};

    private static final float FIRE_RATE = 0.1F;

    private DefenceState state;
    private float shotDelta;
    private float lastAngle;
    private final GameScreen screen;
    private Random random;

    public Defence(int x, int y, LD27 game){
        super(x, y, game.getSpriteHandler().getRegion(Sprite.DEFENCE), game);

        health = 20;
        state = DefenceState.ALLY;
        random = new Random();

        if(game.getScreen() instanceof GameScreen) screen = (GameScreen) game.getScreen();
        else screen = null;
    }

    @Override
    public void actGravity(float delta){}
    @Override
    public void actVelocity(float delta){}

    @Override
    public void act(float delta){
        if (state == DefenceState.ALLY) calcAngle(delta, true);
        else if (state == DefenceState.ENEMY) calcAngle(delta, false);
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

    public void calcAngle(float delta, boolean playerSide){
        Entity target = findClosest(playerSide);
        if(target != null && target.position.dst(position) <= Gdx.graphics.getWidth() / 2) {
            long angleModifier = (random.nextInt() % 18) - 6;
            float mAngle = (float) toDegrees(atan((position.y - target.position.y) / (position.x - target.position.x)));
            //GDX angles have 0 up, not right
            if (target.position.x - position.x > 0) mAngle += 360 - 90 + angleModifier;
            else mAngle += 180 - 90 + angleModifier;
            shoot(delta, mAngle, playerSide);
        }
    }

    public void shoot(float delta, float angle, boolean playerSide){
        if (shotDelta > FIRE_RATE) {
            lastAngle = angle;
            if (screen != null) screen.getStage().addActor(new HeavyShot(
                (int) (position.x + origin.x), (int) (position.y + origin.y), (int) angle, playerSide ? Shot.BulletFrom.ALLY : Shot.BulletFrom.ENEMY,
                game));
            shotDelta = 0;
        }
    }

    public Entity findClosest(boolean playerSide){
        Entity finalTarget = null;
        if(screen != null && playerSide){
            float targetX = 9001;
            for(int i = 0; i < screen.getEnemies().size(); i++){
                Enemy target = (Enemy) screen.getEnemies().get(i);
                if(position.dst(target.getX(), target.getY()) < targetX){
                    targetX = position.dst(target.getX(), target.getY());
                    finalTarget = target;
                }
            }
            for(int i = 0; i < screen.getEnemyHeavies().size(); i++){
                EnemyHeavy target = (EnemyHeavy) screen.getEnemyHeavies().get(i);
                if(position.dst(target.getX(), target.getY())  > targetX){
                    targetX = position.dst(target.getX(), target.getY());
                    finalTarget = target;
                }
            }
        }
        if(screen != null && !playerSide){
            float targetX = 9001;
            for(int i = 0; i < screen.getEnemies().size(); i++){
                Ally target = (Ally) screen.getAllies().get(i);
                if(target.getX() < targetX){
                    targetX = target.getX();
                    finalTarget = target;
                }
            }
            for(int i = 0; i < screen.getEnemyHeavies().size(); i++){
                EnemyHeavy target = (EnemyHeavy) screen.getEnemyHeavies().get(i);
                if(target.getX() < targetX){
                    targetX = target.getX();
                    finalTarget = target;
                }
            }
        }
        return finalTarget;
    }

}
