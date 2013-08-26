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

public class Shot extends Entity {

    private static final float MAX_VELOCITY = 1024;
    private static final float DAMAGE = 1;

    private float lifeDelta;
    private GameScreen screen;

    public enum BulletFrom{ PLAYER, ALLY, ENEMY};
    BulletFrom bulletFrom;

    private final Vector2 angle;

    public Shot(int x, int y, int r, BulletFrom bulletFrom, LD27 game){
        super(x, y, game.getSpriteHandler().getRegion(Sprite.SHOT), game);

        setTouchable(Touchable.disabled);

        setRotation(r);

        lifeDelta = 0;

        if (game.getScreen() instanceof GameScreen) screen = (GameScreen) game.getScreen();
        else screen = null;

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
        if (hit != null && (hit instanceof Enemy || hit instanceof EnemyHeavy) && bulletFrom == BulletFrom.PLAYER){
            if (((Entity) hit).getLife() > IMMUNITY) ((Entity) hit).damage(DAMAGE);
            remove();
            game.getAudioHandler().play(Audio.HIT);
            if (screen != null) screen.getPlayer().setPower(((GameScreen)game.getScreen()).getPlayer().getPower() - 1);
        }
        else if (hit != null && hit instanceof Base){
            if ((((Base) hit).getPlayerSide() == false && (bulletFrom == BulletFrom.PLAYER  || bulletFrom == BulletFrom.ALLY)) ||
                 ((Base) hit).getPlayerSide() == true && bulletFrom == BulletFrom.ENEMY){

                ((Base) hit).takeDamage(5);
                remove();
            }
        }
        else if (hit != null && hit instanceof Player && bulletFrom == BulletFrom.ENEMY){
            if (((Entity) hit).getLife() > IMMUNITY){
                 ((Entity) hit).damage(DAMAGE);
                 if (screen != null) screen.addPlayer();
            }
            game.getAudioHandler().play(Audio.HIT);
            remove();
        }
        else if (hit != null && (hit instanceof Ally || hit instanceof AllyHeavy) && bulletFrom == BulletFrom.ENEMY){
            if (((Entity) hit).getLife() > IMMUNITY)  ((Entity) hit).damage(DAMAGE);
            game.getAudioHandler().play(Audio.HIT);
            remove();
        }
        else if (hit != null && (hit instanceof Enemy || hit instanceof EnemyHeavy) && bulletFrom == BulletFrom.ALLY){
            if (((Entity) hit).getLife() > IMMUNITY) ((Entity) hit).damage(DAMAGE);
            game.getAudioHandler().play(Audio.HIT);
            remove();
        }

        setRotation(angle.x);

        if(position.y <= 128) remove();


        lifeDelta += delta;
        if(lifeDelta > .5) remove();
    }
}
