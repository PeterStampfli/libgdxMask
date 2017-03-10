package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.nio.ByteBuffer;

/**
 * Created by peter on 3/10/17.
 */

public class ByteBufferIO {

    static void write(ByteBuffer byteBuffer, FileHandle fileHandle,boolean append){
        int nBytes=byteBuffer.position()!=0?byteBuffer.position():byteBuffer.capacity();
        byte[] bytes=new byte[nBytes];
        byteBuffer.rewind();
        byteBuffer.get(bytes);
        fileHandle.writeBytes(bytes,append);
        Gdx.app.log("***",""+nBytes);
    }

    static void write(ByteBuffer byteBuffer,String fileName,boolean append){
        ByteBufferIO.write(byteBuffer,Gdx.files.local(fileName),append);
    }

    static ByteBuffer read(FileHandle fileHandle){
        byte[] bytes=fileHandle.readBytes();
        return ByteBuffer.wrap(bytes);
    }

    static ByteBuffer read(String fileName){
        return ByteBufferIO.read(Gdx.files.local(fileName));
    }
}
