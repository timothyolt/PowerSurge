package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Sprite;
import org.bytefire.ld27.core.screen.EndScreen;

public class Base extends Entity {

    private float health;
    private boolean player;

    public Base(int x, int y, boolean player, LD27 game){
        super(x, y, game.getSpriteHandler().getRegion(Sprite.BASE), game);
        this.player = player;
        health = 250;
        setTouchable(Touchable.enabled);
    }

    public void takeDamage(float damage){
        health -= damage;
        if (health <= 0) game.setScreen(new EndScreen(player ? "YOUR BASE WAS DESTROYED \n And thus, the rebels' base was destroyed" : "YOU WIN \n Your pride will be your downfall ", game));
        //System.out.println(health);
    }

    public boolean getPlayerSide(){
        return player;
    }

    @Override
    public void actGravity(float delta){}

    @Override
    public void actVelocity(float delta){}

}
