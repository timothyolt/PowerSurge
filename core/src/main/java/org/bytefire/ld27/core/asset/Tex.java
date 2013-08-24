package org.bytefire.ld27.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum Tex {
    
    SPLASH(Gdx.files.internal("studio.png"), 0, 0, 0),
    PLAYER(Gdx.files.internal("warmup.png"), 16, 16, 0),
    SHOT(Gdx.files.internal("warmup.png"), 16, 16, 1),
    RHOMBUS(Gdx.files.internal("warmup.png"), 16, 16, 2),
    RAZOR(Gdx.files.internal("warmup.png"), 16, 16, 3),
    SQUARE(Gdx.files.internal("warmup.png"), 16, 16, 4),
    CIRCLE(Gdx.files.internal("warmup.png"), 16, 16, 5);
    
    public final FileHandle file;
    public final int width;
    public final int height;
    public final int id;
    
    private Tex(FileHandle file, int width, int height, int id){
        this.file = file;
        this.width = width;
        this.height = height;
        this.id = id;
    }
}
