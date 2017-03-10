package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture bad;
	TextureRegion region;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		FileHandle bad=Gdx.files.internal("badlogic.jpg");

		Pixmap badmap=new Pixmap(bad);
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
		Vector2 a=new Vector2(10,10);
		Vector2 b=new Vector2(50,10);
		Vector2 t=new Vector2(90,90);

		//dotsAndLines.addArcABTangent(a,b,t,true);
		//dotsAndLines.addArcABSomeCenter(a,b,t,true);
		dotsAndLines.addArcABC(a,b,t);
		//dotsAndLines.addBasicArc(50,50,20,0,2.3f,true);
		dotsAndLines.drawDotsAndLines(mask,6);
		//dotsAndLines.fillShape(mask);
		//mask.fillLine(200,10,10,100,10);
		mask.setPixmapAlpha(rgba);

		//rgba=mask.cutFromPixmap(badmap,10,50);
		mask.setPixmapAlpha(rgba);

		img = new Texture(rgba);
		//img = new Texture(badmap);
		rgba.dispose();

		img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		img.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		FrameBuffer frameBuffer=new FrameBuffer(Pixmap.Format.RGBA8888,100,100,false);
		frameBuffer.bind();
		Gdx.gl.glClearColor(0, 1, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(img, 0, 0);
		//batch.draw(img, img.getWidth(), img.getHeight(),4*img.getWidth(),4*img.getHeight());
		batch.end();


		region= new TextureRegion(new Texture(ScreenUtils.getFrameBufferPixmap(0,0,100,100)));
		//byte[] bytes=ScreenUtils.getFrameBufferPixels(0,0,100,100,true);
		//frameBuffer.end();
		FrameBuffer.unbind();

		frameBuffer.dispose();


		ByteBuffer byteBuffer= ByteBuffer.allocate(20);
		byteBuffer.putInt(12345);
		byteBuffer.putInt(789222);
		Gdx.app.log("remain",""+byteBuffer.remaining());
		Gdx.app.log("limit",""+byteBuffer.limit());
		Gdx.app.log("posi",""+byteBuffer.position());

		//fileHandle.writeBytes(bytes,false);
		ByteBufferIO.write(byteBuffer,"bytes.dat",false);
		ByteBuffer input=ByteBufferIO.read("bytes.dat");
		Gdx.app.log("in limit",""+input.limit());

		Gdx.app.log("lies",""+input.getInt());
		Gdx.app.log("lies",""+input.getInt());

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		batch.draw(img, 0, 0);
		batch.draw(region, img.getWidth(), img.getHeight(),4*img.getWidth(),4*img.getHeight());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
