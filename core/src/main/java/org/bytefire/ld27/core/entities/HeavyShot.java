package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.bytefire.ld27.core.LD27;

import static java.lang.Math.*;
import org.bytefire.ld27.core.asset.Audio;
import org.bytefire.ld27.core.asset.Sprite;
import static org.bytefire.ld27.core.entities.Entity.IMMUNITY;
import org.bytefire.ld27.core.screen.AbstractScreen;
import org.bytefire.ld27.core.screen.GameScreen;

public class HeavyShot extends Entity {

    private static final float MAX_VELOCITY = 1024;
    
    private float lifeDelta;

    enum BulletFrom{ PLAYER, ALLY_HEAVY, ENEMY_HEAVY };
    BulletFrom bulletFrom = null;
    
    private final Vector2 angle;
    
    public HeavyShot(int x, int y, int r, BulletFrom bulletFrom, LD27 game){
        super(x, y, game.getSpriteHandler().getRegion(Sprite.HEAVY_SHOT), game);

        setTouchable(Touchable.disabled);

        setRotation(r);
        
        lifeDelta = 0;

        velocity.set((float) cos(toRadians(r + 90)) * MAX_VELOCITY, (float) sin(toRadians(r + 90)) * MAX_VELOCITY);
        angle = new Vector2(r, 0);
        this.bulletFrom = bulletFrom;
        
        game.getAudioHandler().play(Audio.SHOOT);
    }

    @Override
    public void act(float delta){
        
        float newx = position.x + (velocity.x * delta);
        float newy = position.y + (velocity.y * delta);


        if (newx < 0 || newx > ((AbstractScreen) game.getScreen()).getStage().getWidth()) remove();
        if (newy > ((AbstractScreen) game.getScreen()).getStage().getHeight()) remove();
        else if (newy < 128) newy = 128;
        position.x = newx;
        position.y = newy;

        setX(position.x);
        setY(position.y);

        Actor hit = ((AbstractScreen)game.getScreen()).getStage().hit(getX(), getY(), true);
        if (hit != null && hit instanceof Enemy && bulletFrom == BulletFrom.PLAYER){
            if (((Entity) hit).getLife() > IMMUNITY) hit.remove();
            remove();
            ((GameScreen)game.getScreen()).getPlayer().setPower(((GameScreen)game.getScreen()).getPlayer().getPower() - 1);
        }
        else if (hit != null && hit instanceof Base){
            if ((((Base) hit).getPlayerSide() == false && (bulletFrom == BulletFrom.PLAYER  || bulletFrom == BulletFrom.ALLY_HEAVY)) ||
                 ((Base) hit).getPlayerSide() == true && bulletFrom == BulletFrom.ENEMY_HEAVY){
                
                ((Base) hit).takeDamage(5);
                remove();
            }
        }
        else if (hit != null && hit instanceof Player && bulletFrom == BulletFrom.ENEMY_HEAVY){
            if (((Entity) hit).getLife() > IMMUNITY){
                hit.remove();
                ((GameScreen)game.getScreen()).addPlayer(); 
            }
            remove();
        }
        else if (hit != null && (hit instanceof Ally || hit instanceof AllyHeavy) && bulletFrom == BulletFrom.ENEMY_HEAVY){
            if (((Entity) hit).getLife() > IMMUNITY) hit.remove();
            remove();
        }
        else if (hit != null && (hit instanceof Enemy || hit instanceof EnemyHeavy) && bulletFrom == BulletFrom.ALLY_HEAVY){
            if (((Entity) hit).getLife() > IMMUNITY) hit.remove();
            remove();
        }

        setRotation(angle.x);

        if(position.y <= 128) remove();
        
        
        lifeDelta += delta;
        if(lifeDelta > 1) remove();
    }
}