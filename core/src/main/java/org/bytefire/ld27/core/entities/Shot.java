package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.bytefire.ld27.core.LD27;

import static java.lang.Math.*;
import org.bytefire.ld27.core.asset.Sfx;
import org.bytefire.ld27.core.asset.Tex;
import org.bytefire.ld27.core.screen.AbstractScreen;

public class Shot extends Entity {
    
    private static final float MAX_VELOCITY = 1024;
    
    private final Vector2 angle;
    
    public Shot(int x, int y, int r, LD27 game){
        super(x, y, game.getTextureHandler().getRegion(Tex.SHOT), game);
        
        setTouchable(Touchable.disabled);
        
        setRotation(r);
        
        velocity.set((float) cos(toRadians(r + 90)) * MAX_VELOCITY, (float) sin(toRadians(r + 90)) * MAX_VELOCITY);
        angle = new Vector2(r, 0);
        
        game.getSfxHandler().play(Sfx.SHOOT);
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
        
        Actor hit = ((AbstractScreen)game.getScreen()).getStage().hit(getX(), getX(), true);
        if (hit != null && hit instanceof Enemy){
            hit.remove();
            remove();
        }
        
        setRotation(angle.x);
        
        if(position.y <= 128) remove();
    }
}
