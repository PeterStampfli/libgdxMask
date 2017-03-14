package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.mygdx.game.UserInteraction.FullOrientation.LANDSCAPE;
import static com.mygdx.game.UserInteraction.FullOrientation.PORTRAIT;
import static com.mygdx.game.UserInteraction.FullOrientation.REVERSE_LANDSCAPE;
import static com.mygdx.game.UserInteraction.FullOrientation.REVERSE_PORTRAIT;


public class UserInteraction {
    private boolean hasAccelerometer=true;            // variables for mouse simmulation of accelerometer
    private FullOrientation orientation;               // the current orientation of the userInteraction
    private Camera camera;
    private Vector3 spacePositionOfTouch=new Vector3();    // x,y-components give touch position
    private float mouseAccelerometerMax =5f;
    private float mouseAccelerometerScale;
    private Vector2 mouseAccelerometer=new Vector2();
    private int screenWidth2=0;
    private int screenHeight2=0;
    private long numberOfRenderCalls=0;

    public enum FullOrientation {
        PORTRAIT, LANDSCAPE, REVERSE_PORTRAIT, REVERSE_LANDSCAPE
    }

    public UserInteraction(Camera theCamera){
        camera=theCamera;
        hasAccelerometer= Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
    }

    /**
     * set the camera for unprojecting touch position
     * @param theCamera Camera object
     */
    public  void setCamera(Camera theCamera){
        camera=theCamera;
    }

    /**
     * set the acceleration for mouse replacement of accelerometer at border of screen
     * smaller values for less sensitivity and more precision
     */
    public void setMouseAccelerometerMax(float a){ mouseAccelerometerMax=a;}

    /**
     * find the full orientation of the userInteraction
     *
     * @return the orientation
     */
    public FullOrientation getOrientation(){
        FullOrientation orientation=PORTRAIT;
        Input.Orientation nativeOrientation=Gdx.input.getNativeOrientation();
        int rotation=Gdx.input.getRotation();

        if(nativeOrientation==Input.Orientation.Landscape){ // tablets
            switch (rotation){
                case 0:
                    orientation= LANDSCAPE;
                    break;
                case 90:
                    orientation= REVERSE_PORTRAIT;
                    break;
                case 180:
                    orientation= REVERSE_LANDSCAPE;
                    break;
                case 270:
                    orientation= PORTRAIT;
                    break;
            }
        }
        else {                                               // phones: native orientation=portrait
            switch (rotation){
                case 0:
                    orientation= PORTRAIT;
                    break;
                case 90:
                    orientation= LANDSCAPE;
                    break;
                case 180:
                    orientation= REVERSE_PORTRAIT;
                    break;
                case 270:
                    orientation= REVERSE_LANDSCAPE;
                    break;
            }
        }
        return orientation;
    }

    /**
     * update orientation and mouse accelerometer scales at resize
     */
    public void resize(int width,int height){
        mouseAccelerometerScale =2* mouseAccelerometerMax /Math.min(width, height);
        screenWidth2=width/2;
        screenHeight2=height/2;
        orientation=getOrientation();
    }

    /**
     * touch position on screen, limited to screen (for mouse)
     */
    public int getXLimited(){
        return MathUtils.clamp(Gdx.input.getX(),0,screenWidth2+screenWidth2);
    }

    public int getYLimited(){
        return MathUtils.clamp(Gdx.input.getY(),0,screenHeight2+screenHeight2);
    }
    /**
     * read touch position,limit to screen if its a mouse and unproject
     */
    public void readTouch(Vector2 vector){
        spacePositionOfTouch.set(getXLimited(),getYLimited(),0f);
 //       L.log("Spacex "+spacePositionOfTouch);
        camera.unproject(spacePositionOfTouch);
        vector.set(spacePositionOfTouch.x,spacePositionOfTouch.y);
    }


    /**
     * read the accelerometer if there is one, use current userInteraction orientation to get transform
     * readings to screen axis values
     *
     * if there is no accelerometer, read the touch position if the right mouse button is pressed
     *
     */
    public void readAccelerometer(Vector2 vector){
        if (hasAccelerometer){
            switch (orientation) {
                case LANDSCAPE:
                    vector.set(-Gdx.input.getAccelerometerY(),Gdx.input.getAccelerometerX());
                    break;
                case PORTRAIT:
                    vector.set(Gdx.input.getAccelerometerX(),Gdx.input.getAccelerometerY());
                    break;
                case REVERSE_LANDSCAPE:
                    vector.set(Gdx.input.getAccelerometerY(),-Gdx.input.getAccelerometerX());
                    break;
                case REVERSE_PORTRAIT:
                    vector.set(-Gdx.input.getAccelerometerX(),-Gdx.input.getAccelerometerY());
                    break;
            }
        }
        else {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                // if the right mouse button is pressed update the replacement accelerometer values
                //  screen origin is top left corner
                mouseAccelerometer.x = mouseAccelerometerScale *(Gdx.input.getX()-screenWidth2);
                mouseAccelerometer.y =-mouseAccelerometerScale *(Gdx.input.getY()-screenHeight2);
                mouseAccelerometer.clamp(0.0f,mouseAccelerometerMax);
            }
            vector.set(mouseAccelerometer);
        }
    }
}
