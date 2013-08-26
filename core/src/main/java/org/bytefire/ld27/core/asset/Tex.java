package org.bytefire.ld27.core.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum Tex {

    SPLASH(Gdx.files.internal("studio.png"), 0, 0, 0),
    PLAYER(Gdx.files.internal("level.png"), 32, 32, 64),
    ENEMY(Gdx.files.internal("level.png"), 32, 32, 65),
    ARM(Gdx.files.internal("level.png"), 32, 32, 80),
    SHOT(Gdx.files.internal("level.png"), 16, 16, 384),
    MOON(Gdx.files.internal("level.png"), 128, 128, 2),
    MOON_ALT(Gdx.files.internal("level.png"), 128, 128, 3),
    BASE(Gdx.files.internal("level.png"), 256, 128, 0);

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
