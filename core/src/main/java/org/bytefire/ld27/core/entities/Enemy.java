package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.screen.EndScreen;

import org.bytefire.ld27.core.screen.GameScreen;

public class Enemy extends Entity{
    
    private static final float MAX_VELOCITY = 128F;
    
    private final Vector2 angle;
    
    public Enemy(int x, int y, int r, Shape type, LD27 game){
        super(x, y, game.getTextureHandler().getRegion(type.getTex()), game);
        
        setTouchable(Touchable.enabled);
        
        setRotation(r);
        
        angle = new Vector2(r, 0);
        
        //game.getSfxHandler().play(Sfx.SHOOT);
    }
    
    @Override
    public void act(float delta){
        
        seek(delta);
        
        super.act(delta);
    }
    
    public void seek(float delta){
        Player target = ((GameScreen)game.getScreen()).getPlayer();
        float dist = target.position.dst(position);
        if (dist < 16) game.setScreen(new EndScreen(game));
        velocity.x = (float) (((target.getX() - getX()) / dist) * MAX_VELOCITY);
        velocity.y = (float) (((target.getY() - getY()) / dist) * MAX_VELOCITY);
    }
}
