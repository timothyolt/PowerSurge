package org.bytefire.ld27.core.screen;

import com.badlogic.gdx.Gdx;
import org.bytefire.ld27.core.LD27;
import org.bytefire.ld27.core.entities.Enemy;
import org.bytefire.ld27.core.entities.Player;
import org.bytefire.ld27.core.entities.Shape;

public class GameScreen extends AbstractScreen {
    
    private Player player;
    
    public GameScreen(LD27 game){
        super(game);
        player = null;
    }
    
    @Override
    public void render(float delta){
        super.render(delta);
    }
    
    @Override
    public void show(){
        super.show();

        player = new Player(320, 480, 0, game);
        
        Gdx.input.setInputProcessor(stage);
        
        stage.addActor(player);
        stage.addActor(new Enemy(100, 100, 0, Shape.RHOMBUS, game));
    }
    
    public Player getPlayer(){
        return player;
    }
}
