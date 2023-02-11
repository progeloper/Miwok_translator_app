package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer colorsAudio;
    private MediaPlayer.OnCompletionListener completionListener = mp -> releaseMediaPlayer();
    private AudioManager caudioManager;
    private AudioManager.OnAudioFocusChangeListener caMFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case (AudioManager.AUDIOFOCUS_GAIN):
                    colorsAudio.start();
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS):
                    releaseMediaPlayer();
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                    colorsAudio.pause();
                    colorsAudio.seekTo(0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        caudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> colors = new ArrayList<>();
        colors.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        colors.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        colors.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        colors.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        colors.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        colors.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
        colors.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colors.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        ListView list = (ListView) findViewById(R.id.list);
        WordAdapter colorAdapter = new WordAdapter(this, colors, R.color.category_colors);
        list.setAdapter(colorAdapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            releaseMediaPlayer();
            int checkFocus = caudioManager.requestAudioFocus(caMFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (checkFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                colorsAudio = MediaPlayer.create(ColorsActivity.this, colorAdapter.getItem(position).getmSongResource());
                colorsAudio.start();
                colorsAudio.setOnCompletionListener(completionListener);
            }
        });
    }

    private void releaseMediaPlayer(){
        if (colorsAudio != null){
            colorsAudio.release();
            colorsAudio = null;
            caudioManager.abandonAudioFocus(caMFocusChangeListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }
}