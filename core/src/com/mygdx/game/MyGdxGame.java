package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture bad;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		FileHandle bad=Gdx.files.internal("badlogic.jpg");

		Mask mask;
		mask=new Mask(100,100);

		Pixmap rgba=mask.createPixmap();
		rgba.setColor(Color.WHITE);
		rgba.fill();
		//mask.setLimits(1,48,1,48);
	//	mask.disc(20f,40f,10);
		//mask.fillLine(20f,20f,20,40,10);
		//mask.setLimits();

		//mask.fillShape(10,10,40,20,30,49,-10,20);
		//mask.fillShape(40,20,140,40,30,249,30,49);
		//mask.invertLimits();

		//mask.fillLine(2,2,49,40,6);
		DotsAndLines dotsAndLines=new DotsAndLines();
		//dotsAndLines.addLines(15,15f,100,15f,80,80,70,90,20,30);
		Vector2 a=new Vector2(10,50);
		Vector2 b=new Vector2(90,50);
		Vector2 center=new Vector2(100,140);

		dotsAndLines.addABSomeCenter(a,b,center,true);
		//dotsAndLines.addBasicArc(50,50,20,0,2.3f,true);
		dotsAndLines.drawDotsAndLines(mask,6);
		//dotsAndLines.fillShape(mask);
		//mask.fillLine(200,10,10,100,10);
		mask.setPixmapAlpha(rgba);


		img = new Texture(rgba);
		rgba.dispose();

		img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		img.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(img, img.getWidth(), img.getHeight(),4*img.getWidth(),4*img.getHeight());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
