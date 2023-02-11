package com.example.android.miwok;

import android.media.MediaPlayer;

public class Word {
    private String mdefaultText;
    private String mmiwokText;
    private int mSongResource;
    private int mimageResource = NO_IMAGE_PROVIDED;
    private static int NO_IMAGE_PROVIDED  = -1;

    public Word(String def, String miwok, int icon, int song){
        mdefaultText = def;
        mmiwokText = miwok;
        mimageResource = icon;
        mSongResource = song;
    }

    public Word(String def, String miwok, int song){
        mdefaultText = def;
        mmiwokText = miwok;
        mSongResource = song;
    }

    public boolean hasImage(){
        return mimageResource != -1;
    }

    public String getDefaultText(){
        return mdefaultText;
    }

    public String getMiwokText(){
        return mmiwokText;
    }

    public int getImageResource(){
        return mimageResource;
    }

    public int getmSongResource(){
        return mSongResource;
    }
}
