package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Tex;
import org.bytefire.ld27.core.screen.EndScreen;

import org.bytefire.ld27.core.screen.GameScreen;

public class Enemy extends Entity{
    
    private static final float MAX_VELOCITY = 128F;
    
    private final Vector2 angle;
    private final TextureRegion tex;
    
    private boolean flipped;
    
    public Enemy(int x, int y, int r, LD27 game){
        super(x, y, game.getTextureHandler().getRegion(Tex.PLAYER), new Rectangle(23, 4, 17, 28), game);
        tex = game.getTextureHandler().getRegion(Tex.PLAYER);
        
        setTouchable(Touchable.enabled);
        
        setRotation(r);
        
        angle = new Vector2(r, 0);
        
        //game.getSfxHandler().play(Sfx.SHOOT);
    }
    
    @Override
    public void act(float delta){
        
        seek(delta);
        
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
        
        super.act(delta);
    }
    
    public void seek(float delta){
        Player target = ((GameScreen)game.getScreen()).getPlayer();
        float dist = target.position.dst(position);
        velocity.x = (float) (((target.getX() - getX()) / dist) * MAX_VELOCITY);
        velocity.y = (float) (((target.getY() - getY()) / dist) * MAX_VELOCITY);
    }
}
