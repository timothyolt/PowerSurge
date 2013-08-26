package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Tex;
import org.bytefire.ld27.core.screen.EndScreen;

public class Base extends Entity {

    private float health;
    private boolean player;

    public Base(int x, int y, boolean player, LD27 game){
        super(x, y, game.getTextureHandler().getRegion(Tex.BASE), game);
        this.player = player;
        health = 250;
        setTouchable(Touchable.enabled);
    }

    public void takeDamage(float damage){
        System.out.println(getY());
        health -= damage;
        if (health <= 0) game.setScreen(new EndScreen(player ? "GAME OVER, LOSER" : "YOUR PRIDE WILL BE YOUR DOWNFALL", game));
        //System.out.println(health);
    }

    @Override
    public void actGravity(float delta){}

    @Override
    public void actVelocity(float delta){}

}
