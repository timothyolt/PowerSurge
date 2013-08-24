package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.bytefire.ld27.core.LD27;

public abstract class Entity extends Image{

    protected static final float GRAVITATIONAL_ACCELERATION = 128F;
    protected static final float MAX_GRAVITY = 256F;

    protected final LD27 game;

    protected final Vector2 position;
    protected final Vector2 velocity;

    public Entity(int x, int y, TextureRegion tex, LD27 game){
        super(tex);

        this.game = game;

        setOrigin(getWidth()/2, getHeight()/2);
        setX(x);
        setY(y);

        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
    }

    @Override
    public void act(float delta){
        super.act(delta);

        float gravity = velocity.y - GRAVITATIONAL_ACCELERATION;
        if (gravity >= -MAX_GRAVITY) velocity.y = gravity;

        float newx = position.x + (velocity.x * delta);
        float newy = position.y + (velocity.y * delta);


        if (newx < 0 || newx > Gdx.graphics.getWidth()) remove();
        if (newy > Gdx.graphics.getHeight()) remove();
        else if (newy < 100) newy = 100;
        position.x = newx;
        position.y = newy;

        setX(position.x);
        setY(position.y);
    }

}
