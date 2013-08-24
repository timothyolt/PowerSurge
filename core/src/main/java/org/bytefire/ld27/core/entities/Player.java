package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.screen.AbstractScreen;

import static com.badlogic.gdx.Input.Keys.*;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.bytefire.ld27.core.asset.Tex;
import static org.bytefire.ld27.core.entities.Entity.GRAVITATIONAL_ACCELERATION;

public class Player extends Entity {
    
    private static final float MAX_VELOCITY = 256F;
    private static final float POWER = 64F;
    private static final float FRICTION = 16F;
    private static final float ANGUALR_POWER = 10F;
    private static final float FIRE_RATE = 0.1F;
    
    private final Vector2 angle;
    
    private float shotDelta;
    
    public Player(int x, int y, int r, LD27 game){
        super(x, y, game.getTextureHandler().getRegion(Tex.PLAYER), game);
        
        setTouchable(Touchable.enabled);
        
        angle = new Vector2(r, 0);
        
        shotDelta = 0;
    }
    
    @Override
    public void act(float delta){
        
        calcVelocity(delta);
        calcAngle(delta);
        
        float gravity = velocity.y - GRAVITATIONAL_ACCELERATION;
        if (gravity >= -MAX_GRAVITY) velocity.y = gravity;
        
        float newx = position.x + (velocity.x * delta);
        float newy = position.y + (velocity.y * delta);
        
        if (newx > 0 && newx < Gdx.graphics.getWidth() - getWidth()) position.x = newx;
        else velocity.x *= -2;
        if (newy > 0 + getHeight() && newy < Gdx.graphics.getHeight()) position.y = newy;
        else velocity.y *= -2;
        
        setX(position.x);
        setY(position.y);
        
        setRotation(angle.x);
        
        shotDelta += delta;
    }
    
    public void calcVelocity(float delta){
        if (Gdx.input.isKeyPressed(LEFT) && velocity.x > -MAX_VELOCITY) velocity.x -= POWER;
        else if (velocity.x < 0)
            if (velocity.x < - FRICTION) velocity.x += FRICTION;
            else velocity.x = 0;
        if (Gdx.input.isKeyPressed(RIGHT) && velocity.x < MAX_VELOCITY) velocity.x += POWER;
        else if (velocity.x > 0)
            if (velocity.x > FRICTION) velocity.x -= FRICTION;
            else velocity.x = 0;
        if (Gdx.input.isKeyPressed(DOWN) && velocity.y > -MAX_VELOCITY) velocity.y -= POWER;
        else if (velocity.y < 0)  
            if (velocity.y < FRICTION) velocity.y += FRICTION;
            else velocity.y = 0;
        if (Gdx.input.isKeyPressed(UP) && velocity.y < MAX_VELOCITY)  velocity.y += POWER;
        else if (velocity.y > 0) 
            if (velocity.y > - FRICTION) velocity.y -= FRICTION;
            else velocity.y = 0;
    }
    
    public void calcAngle(float delta){
        if (angle.x >= 0) angle.x = angle.x % 360;
        else angle.x += 360;
        if (Gdx.input.isKeyPressed(A) && Gdx.input.isKeyPressed(W)){
            if (angle.x > 40 && angle.x < 220) angle.x -= ANGUALR_POWER;
            else if (angle.x != 40) angle.x += ANGUALR_POWER;
            shoot(delta);
        }
        else if (Gdx.input.isKeyPressed(A) && Gdx.input.isKeyPressed(S)){
            if (angle.x > 140 && angle.x < 320) angle.x -= ANGUALR_POWER;
            else if (angle.x != 140) angle.x += ANGUALR_POWER;
            shoot(delta);
        }
        else if (Gdx.input.isKeyPressed(D) && Gdx.input.isKeyPressed(W)){
            if (angle.x > 140 && angle.x < 320) angle.x += ANGUALR_POWER;
            else if (angle.x != 320) angle.x -= ANGUALR_POWER;
            shoot(delta);
        }
        else if (Gdx.input.isKeyPressed(D) && Gdx.input.isKeyPressed(S)){
            if (angle.x > 40 && angle.x < 220) angle.x += ANGUALR_POWER;
            else if (angle.x != 220) angle.x -= ANGUALR_POWER;
            shoot(delta);
        }
        else if (Gdx.input.isKeyPressed(A)){
            if (angle.x > 90 && angle.x < 270) angle.x -= ANGUALR_POWER;
            else if (angle.x != 90) angle.x += ANGUALR_POWER;
            shoot(delta);
        }
        else if (Gdx.input.isKeyPressed(D)){
            if (angle.x > 90 && angle.x < 270) angle.x += ANGUALR_POWER;
            else if (angle.x != 270) angle.x -= ANGUALR_POWER;
            shoot(delta);
        }
        else if (Gdx.input.isKeyPressed(W)){
            if (angle.x > 180 && angle.x < 360) angle.x += ANGUALR_POWER;
            else if (angle.x != 0 && angle.x != 360) angle.x -= ANGUALR_POWER;
            shoot(delta);
        }
        else if (Gdx.input.isKeyPressed(S)){
            if (angle.x < 180 && angle.x > 0) angle.x += ANGUALR_POWER;
            else if (angle.x != 180) angle.x -= ANGUALR_POWER;
            shoot(delta);
        }
    }
    
    public void shoot(float delta){
        if (shotDelta > FIRE_RATE) {
            ((AbstractScreen) game.getScreen()).getStage().addActor(new Shot(
                (int) position.x, (int) position.y, (int) angle.x,
                game));
            shotDelta = 0;
        }
    }
    
}
