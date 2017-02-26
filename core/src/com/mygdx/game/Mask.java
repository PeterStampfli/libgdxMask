package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.nio.ByteBuffer;

/**
 * Created by peter on 2/26/17.
 */

public class Mask {
    private float[] alpha;        // 0 for transparent, 1 for opaque, additive, maybe outside this range
    private int width;
    private int height;

    public Mask(int width,int height){
        this.width=width;
        this.height=height;
        alpha=new float[width*height];
        int length=alpha.length;
        for (int i=0;i<length;i++){
            alpha[i]=0f;
        }
    }

    private byte byteOfFloat(float f){
        return (byte) Math.floor(MathUtils.clamp(f,0,0.99)*255);
    }

    public Texture image(float r,float g,float b){
        Pixmap pixmap=new Pixmap(width,height, Pixmap.Format.RGBA8888);
        pixmap.setColor(r,g,b,0f);
        pixmap.fill();
        ByteBuffer pixels=pixmap.getPixels();
        int size=alpha.length;
        for (int i=0;i<size;i++){
            pixels.put(i*4+3,byteOfFloat(alpha[i]));
        }
        pixels.rewind();
        Texture result=new Texture(pixmap);
        pixmap.dispose();
        return result;
    }

    public Texture image(Color color){
        return image(color.r,color.g,color.b);
    }

    public Texture imageWhite(){
        return image(1,1,1);
    }

    public void circle(float centerX,float centerY,float radius){
        int iMax,iMin,jMax,jMin;
        iMax=Math.min(width-1,MathUtils.ceil(centerX+radius));
        iMin=Math.max(0,MathUtils.floor(centerX-radius));
        jMax=Math.min(height-1,MathUtils.ceil(centerY+radius));
        jMin=Math.max(0,MathUtils.floor(centerY-radius));
        int i,j,jWidth;
        float dx,dx2,dy,dy2,dx2Plusdy2;
        float radiusSq=radius*radius;
        float d=0.5f;
        float radiusSqPlus=(radius+d)*(radius+d);
        float radiusSqMinus=(radius-d)*(radius-d);
        for (j=jMin;j<=jMax;j++){
            jWidth=j*width;
            dy=Math.abs(j-centerY);
            dy2=dy*dy;
            for (i=iMin;i<=iMax;i++){
                dx=Math.abs(i-centerX);
                dx2=dx*dx;
                dx2Plusdy2=dy2+dx2;
                if (dx2Plusdy2<radiusSqMinus){
                    alpha[i+jWidth]+=1;
                }
                else if (dx2Plusdy2<radiusSqPlus){
                    if (dx>dy){
                        d=(float) Math.sqrt(radiusSq-dy2)-dx+0.5f;
                    }
                    else {
                        d=(float) Math.sqrt(radiusSq-dx2)-dy+0.5f;
                    }
                    if (d>0) alpha[i + jWidth] += d;
                }
            }
        }
    }


}
