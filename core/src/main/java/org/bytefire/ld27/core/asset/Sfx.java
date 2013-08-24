package org.bytefire.ld27.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum Sfx {
    
    SHOOT(Gdx.files.internal("shoot.wav"));
    
    public final FileHandle file;
    
    private Sfx(FileHandle file){
        this.file = file;
    }
}
