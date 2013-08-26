/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bytefire.ld27.core.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.asset.Tex;

public class Head extends Entity{
     
    public Head(int x, int y, boolean playerTeam, LD27 game){
        super(x, y, playerTeam ? game.getTextureHandler().getRegion(Tex.ALLY_HEAD) : game.getTextureHandler().getRegion(Tex.ENEMY_HEAD), game);
        
        setX(position.x);
        setY(position.y);
    }
    
    @Override
    public void act(float delta){
        super.act(delta);
        
        if (life > 120) remove();
    }
}
