package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer familyAudio;
    private final MediaPlayer.OnCompletionListener mcompletionListener = mp -> releaseMediaPlayer();
    private AudioManager faudioManager;
    private final AudioManager.OnAudioFocusChangeListener faudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case (AudioManager.AUDIOFOCUS_GAIN):
                    familyAudio.start();
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS):
                    releaseMediaPlayer();
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                    familyAudio.pause();
                    familyAudio.seekTo(0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        faudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolitti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        ListView familyList = (ListView) findViewById(R.id.list);
        WordAdapter familyAdapter = new WordAdapter(this, words, R.color.category_family);
        familyList.setAdapter(familyAdapter);

        familyList.setOnItemClickListener((parent, view, position, id) -> {
            releaseMediaPlayer();
            int checkFocus = faudioManager.requestAudioFocus(faudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (checkFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                familyAudio = MediaPlayer.create(FamilyActivity.this, familyAdapter.getItem(position).getmSongResource());
                familyAudio.start();
                familyAudio.setOnCompletionListener(mcompletionListener);
            }

        });
    }

    private void releaseMediaPlayer(){
        if (familyAudio != null){
            familyAudio.release();
            familyAudio = null;
            faudioManager.abandonAudioFocus(faudioFocusChangeListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }
}