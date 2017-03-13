package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by peter on 3/12/17.
 */

public class SpriteWithShape extends Sprite implements Shape2D {
    public Shape2D shape;

    public SpriteWithShape(Texture texture,Shape2D shape){
        super(texture);
        this.shape=shape;
    }

    @Override
    public boolean contains(float x, float y){
        if (!getBoundingRectangle().contains(x,y)) return false;
        // shift to "origin" at (0,0)
        x-=getX()+getOriginX();
        y-=getY()+getOriginY();
        float angleDeg=getRotation();
        float sinAngle= MathUtils.sinDeg(angleDeg);
        float cosAngle=MathUtils.cosDeg(angleDeg);
        // unrotate and unscale!
        // and shift to put lower left corner at (0,0)
        float unrotatedX=(cosAngle*x+sinAngle*y)/getScaleX()+getOriginX();
        float unrotatedY=(-sinAngle*x+cosAngle*y)/getScaleY()+getOriginY();
        // limit to texture/pixmap region
        if ((unrotatedX>=0)&&(unrotatedX<=getHeight())&&(unrotatedY>=0)&&(unrotatedY<=getHeight())){
            return shape.contains(unrotatedX,unrotatedY);
        }
        return false;
    }


    @Override
    public boolean contains(Vector2 point) {
        return contains(point.x,point.y);
    }


}
