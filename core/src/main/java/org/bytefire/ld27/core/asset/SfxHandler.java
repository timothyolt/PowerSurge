package org.bytefire.ld27.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class SfxHandler {
    
    HashMap<String, Sound> sounds;
    
    public SfxHandler(){
        sounds = new HashMap<String, Sound>();
    }
    
    public Sound get(Sfx sfx){
        if (!sounds.containsKey(sfx.file.path())) sounds.put(sfx.file.path(), Gdx.audio.newSound(sfx.file));
        return sounds.get(sfx.file.path());
    }
    
    public long play(Sfx sfx){
        return get(sfx).play();
    }
    
    public void dispose(Sfx sfx){
        get(sfx).dispose();
        sounds.remove(sfx.file.path());
    }
    
}
