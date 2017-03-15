package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by peter on 3/15/17.
 */

public class Pieces implements AbstractPiece {
    public Array<Piece> pieceArray;

    public Pieces(){
        pieceArray=new Array<Piece>();
    }

    public void add(Piece piece){
        pieceArray.add(piece);
    }

    @Override
    public void render(SpriteBatch batch) {
        int length=pieceArray.size;
        for (int i=length-1;i>=0;i--){
            pieceArray.get(i).render(batch);                  // rendering from bottom to top
        }
    }

    @Override
    public boolean contains(Vector2 position) {
        int length=pieceArray.size;
        for (int i=0;i<length;i++){
            if (pieceArray.get(i).contains(position)){
                pieceArray.insert(0,pieceArray.removeIndex(i));   // put piece to top
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchBegin(Vector2 position) {
        if (pieceArray.size>0){
            return pieceArray.get(0).touchBegin(position);
        }
        return false;
    }

    @Override
    public boolean touchDrag(Vector2 position, Vector2 deltaPosition) {
        if (pieceArray.size>0){
            return pieceArray.get(0).touchDrag(position,deltaPosition);
        }
        return false;
    }

    @Override
    public boolean touchEnd() {
        if (pieceArray.size>0){
            return pieceArray.get(0).touchEnd();
        }
        return false;
    }
}
