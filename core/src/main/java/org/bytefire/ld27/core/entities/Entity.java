package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Sprite;
import org.bytefire.ld27.core.screen.AbstractScreen;

public abstract class Entity extends Image{

    protected static final float GRAVITATIONAL_ACCELERATION = 128F;
    protected static final float MAX_GRAVITY = 256F;
    protected static final float IMMUNITY = 0.5F;

    protected final LD27 game;
    protected final Rectangle bound;

    protected final Vector2 origin;
    protected final Vector2 position;
    protected final Vector2 velocity;

    protected float life;
    protected float health;

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
        life = 0;
        health = 1;
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

        life += delta;
    }

//    @Override
//    public Actor hit (float x, float y, boolean touchable) {
//        System.out.println("bound.x: " + bound.x);
//        System.out.println("bound.y: " + bound.y);
//        System.out.println("origin.x: " + origin.x);
//        System.out.println("origin.y: " + origin.y);
//        System.out.println("hit x: " + x);
//        if (touchable && getTouchable() != Touchable.enabled) return null;
//        return x >= bound.x + origin.x && x < bound.width + origin.x &&
//               y >= bound.y + origin.y && y < bound.height + origin.y ? this : null;
//    }

    public void actGravity(float delta){
        float gravity = velocity.y - GRAVITATIONAL_ACCELERATION;
        if (gravity >= -MAX_GRAVITY) velocity.y = gravity;
    }

    public void actVelocity(float delta){
        float newx = position.x + (velocity.x * delta);
        float newy = position.y + (velocity.y * delta);

        if (newx < 0 || newx > ((AbstractScreen) game.getScreen()).getStage().getWidth()) remove();
        if (newy > ((AbstractScreen) game.getScreen()).getStage().getHeight()) remove();
        else if (newy < Sprite.MOON.height - bound.y){
            if (velocity.y < 0) velocity.y = 0;
            newy = Sprite.MOON.height - bound.y;
        }
        position.x = newx;
        position.y = newy;
    }

//    @Override
//    public float getX() {
//        return position.x - origin.x;
//    }

//    @Override
//    public float getY() {
//        return position.y - origin.y;
//    }

    public Vector2 getOrigin() {
        return origin;
    }

//    @Override
//     public void setOriginX(float x) {
//        super.setOriginX(x);
//        origin.x = x;
//     }
//
//     @Override
//     public void setOriginY(float y) {
//        super.setOriginY(y);
//        origin.y = y;
//     }
//
//     @Override
//     public void setOrigin(float x, float y) {
//        super.setOrigin(x, y);
//        origin.x = x;
//        origin.y = y;
//     }

    public float getLife(){
        return life;
    }

    public boolean damage(float amount){
        health -= amount;
        if (health <= 0) {
            remove();
            return true;
        }
        else return false;
    }

    public float getHealth(){
        return health;
    }

}
