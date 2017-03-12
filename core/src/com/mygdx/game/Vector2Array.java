package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by peter on 3/12/17.
 */

// to go from to simple array use Array.toArray and new Array(array)

public class Vector2Array {

    static public float[] toFloats(Array<Vector2> vectors){
        float[] result=new float[2*vectors.size];
        int i=0;
        for (Vector2 vector:vectors){
            result[i++]=vector.x;
            result[i++]=vector.y;
        }
        return result;
    }
}
