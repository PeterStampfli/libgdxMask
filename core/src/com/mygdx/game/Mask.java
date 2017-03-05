package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

/**
 * Created by peter on 2/26/17.
 */

public class Mask {
    public float[] alpha;        // 0 for transparent, 1 for opaque
    public int width;
    public int height;
    private int iMin, iMax, jMin, jMax;
    // for shapes
    private FloatArray verticesX,verticesY;
    private Array<Line> lines;

    public Mask(int width,int height){
        this.width=width;
        this.height=height;
        setLimits(1,1,width-2,height-2);
        alpha=new float[width*height];
        clear();
        iMin=0;
        iMax=width-1;
        jMin=0;
        jMax=height-1;
        verticesX=new FloatArray();
        verticesY=new FloatArray();
        lines=new Array<Line>();
    }

    public Mask clear(){
        int length=alpha.length;
        for (int i=0;i<length;i++){
            alpha[i]=0f;
        }
        return this;
    }

    public void setLimits(int iMin,int iMax,int jMin,int jMax){
        this.iMin =MathUtils.clamp(iMin,0,width-1);
        this.iMax =MathUtils.clamp(iMax,0,width-1);
        this.jMin =MathUtils.clamp(jMin,0,height-1);
        this.jMax =MathUtils.clamp(jMax,0,height-1);
    }

    public void setLimits(){
        setLimits(Math.min(1,width-2),Math.max(width-2,1),Math.min(1,height-2),Math.max(1,height-2));
    }

    public Mask invert(){
        int length=alpha.length;
        for (int i=0;i<length;i++){
            alpha[i]=1f-alpha[i];
        }
        return this;
    }

    // fill rect area
    public void fillLimits(){
        int i,j,jWidth;
        for (j=jMin;j<=jMax;j++) {
            jWidth = j * width;
            for (i = iMin; i <= iMax; i++) {
                alpha[i + jWidth] = 1;
            }
        }
    }

    // smooth full disc
    public void disc(float centerX,float centerY,float radius){
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
                    alpha[i+jWidth]=1;
                }
                else if (dx2Plusdy2<radiusSqPlus){
                    if (dx>dy){
                        d=(float) Math.sqrt(radiusSq-dy2)-dx+0.5f;
                    }
                    else {
                        d=(float) Math.sqrt(radiusSq-dx2)-dy+0.5f;
                    }
                    if (d>0) {
                        alpha[i+jWidth]=Math.max(alpha[i+jWidth],d);
                    }
                }
            }
        }
    }

    // convex shapes
    private class Line{
        float pointX,pointY;
        float slope;
        boolean isHorizontal;
        boolean increasing;

        Line(float x1,float y1,float x2,float y2) {
            pointX = x1;
            pointY = y1;
            isHorizontal = (Math.abs(x2 - x1) > Math.abs(y2 - y1));
            if (isHorizontal){
                increasing = (x2 > x1);
                slope = (y2 - y1) / (x2 - x1);
            }
            else {
                increasing=(y2>y1);
                slope=(x2-x1)/(y2-y1);
            }
        }

        public float distance(int i,int j){
            float linePosition,d;
            if (isHorizontal){
                linePosition=pointY+slope*(i-pointX);
                d=increasing?j-linePosition:linePosition-j;
            }
            else {
                linePosition=pointX+slope*(j-pointY);
                d=increasing?linePosition-i:i-linePosition;
            }
            return d+0.5f;
        }
    }

    // a smooth convex shape
    public Mask resetShapeVertices(){
        verticesX.clear();
        verticesY.clear();
        return  this;
    }

    public Mask addShapeVertex(float x,float y){
        verticesX.add(x);
        verticesY.add(y);
        return this;
    }

    public Mask makeShapeLines(){
        lines.clear();
        int length=verticesX.size;
        for (int i=0;i<length-1;i++){
            lines.add(new Line(verticesX.get(i),verticesY.get(i),verticesX.get(i+1),verticesY.get(i+1)));
        }
        lines.add(new Line(verticesX.get(length-1),verticesY.get(length-1),verticesX.get(0),verticesY.get(0)));
        return this;
    }

    public void fillShape(){
        int iMin=this.iMax+1;
        int iMax=this.iMin-1;
        int jMin=this.jMax+1;
        int jMax=this.jMin-1;
        int length=verticesX.size;
        for (int i=0;i<length;i++){
            iMin=Math.min(iMin,MathUtils.floor(verticesX.get(i)));
            iMax=Math.max(iMax,MathUtils.ceil(verticesX.get(i)));
            jMin=Math.min(jMin,MathUtils.floor(verticesY.get(i)));
            jMax=Math.max(jMax,MathUtils.ceil(verticesY.get(i)));
        }
        iMin=Math.max(iMin,this.iMin);
        iMax=Math.min(iMax,this.iMax);
        jMin=Math.max(jMin,this.jMin);
        jMax=Math.min(jMax,this.jMax);
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
                    alpha[i+jWidth]=Math.max(alpha[i+jWidth],d);
                }
            }
        }
    }

    public void fillShape(float... coordinates){
        resetShapeVertices();
        int length=coordinates.length;
        for (int i=0;i<length;i+=2){
            addShapeVertex(coordinates[i],coordinates[i+1]);
        }
        makeShapeLines();
        fillShape();
    }

    public void fillShape(Array<Vector2> points){
        resetShapeVertices();
        for (Vector2 point:points){
            addShapeVertex(point.x,point.y);
        }
        makeShapeLines();
        fillShape();
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


