package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.nio.ByteBuffer;

/**
 * Created by peter on 2/26/17.
 */

// flip the y-axis !!!
// to compensate for inverted y-axis in pixmaps

public class Mask {
    public byte[] alpha;
    public int width;
    public int height;
    private int iMin, iMax, jMin, jMax;


    public Mask(int width,int height){
        this.width=width;
        this.height=height;
        alpha=new byte[width*height];
        clear();
        setLimits();                                            // transparent border
    }

    private int flipY(int y){
        return height-1-y;
    }

    private float flipY(float y){
        return height-1-y;
    }

    private void flipY(Vector2 point){
        point.y=height-1-point.y;
    }

    public Pixmap createPixmap(){
        return new Pixmap(width,height, Pixmap.Format.RGBA8888);
    }

    public void setPixmapAlpha(Pixmap pixmap){
        ByteBuffer pixels=pixmap.getPixels();
        int length=alpha.length;
        int index=3;
        for (int i=0;i<length;i++){
            pixels.put(index,alpha[i]);
            index+=4;
        }
        pixels.rewind();
    }

    public Pixmap cutFromPixmap(Pixmap input,int offsetX,int offsetY){
        Pixmap result=createPixmap();
        result.drawPixmap(input,-offsetX,-offsetY);
        setPixmapAlpha(result);
        return result;
    }

    public Vector2 getCenter(){
        int surface=0;
        int centerX=0;
        int centerY=0;
        int a;
        int i,j;
        int index=0;
        for (j=0;j<height;j++){
            for (i  = 0; i < width; i++) {
                a = alpha[index];
                index++;
                if (a<0) a+=256;
                surface += a;
                centerX += a * i;
                centerY += a * j;
            }
        }
        surface=Math.max(surface,1);
        return new Vector2((float)centerX/(float)surface,(float)centerY/(float)surface);
    }

    public Mask clear(){
        int length=alpha.length;
        for (int i=0;i<length;i++){
            alpha[i]=0;
        }
        return this;
    }

    public void setLimits(int iMin,int iMax,int jMin,int jMax){
        jMin=flipY(jMin);
        jMax=flipY(jMax);
        this.iMin = MathUtils.clamp(Math.min(iMin,iMax),0,width-1);
        this.iMax = MathUtils.clamp(Math.max(iMin,iMax),0,width-1);
        this.jMin = MathUtils.clamp(Math.min(jMin,jMax),0,height-1);
        this.jMax = MathUtils.clamp(Math.max(jMin,jMax),0,height-1);
    }

    public void setLimits(){
        setLimits(Math.min(1,width-2),Math.max(width-2,1),Math.min(1,height-2),Math.max(1,height-2));
    }

    public void noLimits(){
        setLimits(0,width-1,0,height-1);
    }

    // fill rect area
    public void invertWithinLimits(){
        int i,j,jWidth;
        for (j=jMin;j<=jMax;j++) {
            jWidth = j * width;
            for (i = iMin; i <= iMax; i++) {
                alpha[i + jWidth] = (byte) (255-alpha[i + jWidth]);
            }
        }
    }

    // fill rect area
    public void fillLimits(){
        int i,j,jWidth;
        for (j=jMin;j<=jMax;j++) {
            jWidth = j * width;
            for (i = iMin; i <= iMax; i++) {
                alpha[i + jWidth] = (byte) 255;
            }
        }
    }

    public byte maxByteFloat(byte b,float f){
        int iB=b;
        if (iB<0) iB+=256;
        return (byte) Math.max(iB,MathUtils.clamp(Math.floor(f*256),0,255));
    }

    // smooth full disc, flip center y value
    public void circle(float centerX,float centerY,float radius){
        centerY=flipY(centerY);
        int iMax,iMin,jMax,jMin;
        iMax=Math.min(this.iMax,MathUtils.ceil(centerX+radius));
        iMin=Math.max(this.iMin,MathUtils.floor(centerX-radius));
        jMax=Math.min(this.jMax,MathUtils.ceil(centerY+radius));
        jMin=Math.max(this.jMin,MathUtils.floor(centerY-radius));
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
                    alpha[i+jWidth]=(byte) 255;
                }
                else if (dx2Plusdy2<radiusSqPlus){
                    if (dx>dy){
                        d=(float) Math.sqrt(radiusSq-dy2)-dx+0.5f;
                    }
                    else {
                        d=(float) Math.sqrt(radiusSq-dx2)-dy+0.5f;
                    }
                    if (d>0) {
                        alpha[i+jWidth]=maxByteFloat(alpha[i+jWidth],d);
                    }
                }
            }
        }
    }

    public void circle(Vector2 center,float radius){
        circle(center.x,center.y,radius);
    }

    // convex shapes
    private class Line{
        float pointX,pointY;
        float slope;
        boolean isHorizontal;
        boolean increasing;

        // flip y-value, this inverts orientation
        Line(float ax,float ay,float bx,float by) {
            ay=flipY(ay);
            by=flipY(by);
            pointX = ax;
            pointY = ay;
            isHorizontal = (Math.abs(bx - ax) > Math.abs(by - ay));
            if (isHorizontal){
                increasing = (bx > ax);
                slope = (by - ay) / (bx - ax);
            }
            else {
                increasing=(by > ay);
                slope=(bx-ax)/(by-ay);
            }
        }

        public float distance(int i,int j){
            float linePosition,d;
            if (isHorizontal){
                linePosition=pointY+slope*(i-pointX);
                d=increasing?linePosition-j:j-linePosition;
            }
            else {
                linePosition=pointX+slope*(j-pointY);
                d=increasing?i-linePosition:linePosition-i;
            }
            return d+0.5f;
        }
    }

    public void fillShape(float... coordinates){
        int length=coordinates.length-2;
        Array<Line> lines=new Array<Line>();
        for (int i=0;i<length;i+=2) {
            lines.add(new Line(coordinates[i],coordinates[i+1],coordinates[i+2],coordinates[i+3]));
        }
        lines.add(new Line(coordinates[length],coordinates[length+1],coordinates[0],coordinates[1]));
        int i,j,jWidth;
        float d;
        length=lines.size;
        for (j=jMin;j<=jMax;j++){
            jWidth=j*width;
            for (i=iMin;i<=iMax;i++){
                d=1f;
                for (Line line:lines){
                    d=Math.min(d,line.distance(i,j));
                    if (d<0) {
                        break;
                    }
                }
                if (d>0) {
                    alpha[i+jWidth]=maxByteFloat(alpha[i+jWidth],d);
                }
            }
        }

    }

    public void fillShape(Array<Vector2> points){
        fillShape(Vector2Array.toFloats(points));
    }

    public void fillLine(float x1,float y1,float x2,float y2,float halfWidth){
        float length=(float) Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
        float ex=(x2-x1)/length*halfWidth;
        float ey=(y2-y1)/length*halfWidth;
        fillShape(x1+ey,y1-ex,x2+ey,y2-ex,x2-ey,y2+ex,x1-ey,y1+ex);
    }

    public void fillLine(Vector2 a,Vector2 b,float halfWidth){
        fillLine(a.x,a.y,b.x,b.y,halfWidth);
    }



}


