package org.bytefire.ld27.core.entities;

import org.bytefire.ld27.core.asset.Tex;

public enum Shape {
    
    RHOMBUS(Tex.RHOMBUS),
    RAZOR(Tex.RAZOR),
    SQUARE(Tex.SQUARE),
    CIRCLE(Tex.CIRCLE);
    
    private final Tex texture;
    
    private Shape(Tex texture){
        this.texture = texture;
    }
    
    public Tex getTex(){
        return texture;
    }
}
