package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;

import java.nio.ByteBuffer;

/**
 * Created by peter on 3/1/17.
 */

public class RGBAPixmap extends Pixmap {

    public RGBAPixmap(int width,int height){
        super(width,height, Pixmap.Format.RGBA8888);
    }

    static public RGBAPixmap create(FileHandle fileHandle){
        Pixmap input=new Pixmap(fileHandle);
        RGBAPixmap rgbaPixmap=new RGBAPixmap(input.getWidth(),input.getHeight());
        rgbaPixmap.drawPixmap(input,0,0);
        input.dispose();
        return rgbaPixmap;
    }

    static public RGBAPixmap create(String name){
        return RGBAPixmap.create(Gdx.files.internal(name));
    }

    public Mask createMask(){
        return new Mask(this.getWidth(),this.getHeight());
    }

    private byte byteOfFloat(float f){
        return (byte) Math.floor(MathUtils.clamp(f,0,0.99)*256);
    }

    public void setAlpha(Mask mask){
        ByteBuffer pixels=this.getPixels();
        Gdx.app.log("",""+pixels.toString());

        int size=mask.alpha.length;
        Gdx.app.log("s",""+size);

        for (int i=0;i<size;i++){
            pixels.put(i*4+3,byteOfFloat(mask.alpha[i]));
        }
        pixels.rewind();

    }




}
