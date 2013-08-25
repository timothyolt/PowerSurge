package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Tex;
import org.bytefire.ld27.core.screen.AbstractScreen;

public abstract class Entity extends Image{

    protected static final float GRAVITATIONAL_ACCELERATION = 128F;
    protected static final float MAX_GRAVITY = 1024F;

    protected final LD27 game;
    protected final Rectangle bound;
            
    protected final Vector2 origin;
    protected final Vector2 position;
    protected final Vector2 velocity;

    public Entity(int x, int y, TextureRegion tex, Rectangle bound, LD27 game){
        super(tex);

        this.game = game;
        this.bound = bound;

        super.setOrigin(getWidth()/2, getHeight()/2);
        setX(x);
        setY(y);

        origin = new Vector2(getWidth()/2, getHeight()/2);
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
    }
    
    public Entity(int x, int y, TextureRegion tex, LD27 game){
        this(x, y, tex, new Rectangle(0, 0, tex.getRegionWidth(), tex.getRegionHeight()), game);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        
        actGravity(delta);
        actVelocity(delta);

        setX(position.x);
        setY(position.y);
    }
    
    @Override
    public Actor hit (float x, float y, boolean touchable) {	
        if (touchable && getTouchable() != Touchable.enabled) return null;
        return x >= bound.x + origin.x && x < bound.width + origin.x &&
               y >= bound.y + origin.y && y < bound.height + origin.y ? this : null;
    }
    
    public void actGravity(float delta){
        float gravity = velocity.y - GRAVITATIONAL_ACCELERATION;
        if (gravity >= -MAX_GRAVITY) velocity.y = gravity;
    }
    
    public void actVelocity(float delta){
        float newx = position.x + (velocity.x * delta);
        float newy = position.y + (velocity.y * delta);

        if (newx < 0 || newx > ((AbstractScreen) game.getScreen()).getStage().getWidth()) remove();
        if (newy > ((AbstractScreen) game.getScreen()).getStage().getHeight()) remove();
        else if (newy < Tex.MOON.height + origin.y - bound.y){
            if (velocity.y < 0) velocity.y = 0;
            newy = Tex.MOON.height + origin.y - bound.y;
        }
        position.x = newx;
        position.y = newy;
    }
    
    @Override
    public float getX() {
        return position.x - origin.x;
    }
    
    @Override
    public float getY() {
        return position.y - origin.y;
    }

    @Override
     public void setOriginX(float x) {
        super.setOriginX(x);
        origin.x = x;
     }
     
     @Override
     public void setOriginY(float y) {
        super.setOriginY(y);
        origin.y = y;
     }
     
     @Override
     public void setOrigin(float x, float y) {
        System.out.println(x);
        System.out.println(origin.x);

        super.setOrigin(x, y);
        origin.x = x;
        origin.y = y;
     }
        
}
