package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer numbersAudio;
    private MediaPlayer.OnCompletionListener mcompletionListener = mp -> releaseMediaPlayer();

    private AudioManager maudioManager;
    private AudioManager.OnAudioFocusChangeListener amFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    numbersAudio.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    numbersAudio.pause();
                    numbersAudio.seekTo(0);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        maudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyiisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na'acha", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter numbersAdapter = new WordAdapter(NumbersActivity.this, words, R.color.category_numbers);
        ListView numbersList = (ListView) findViewById(R.id.list);
        numbersList.setAdapter(numbersAdapter);

        numbersList.setOnItemClickListener((parent, view, position, id) -> {
                releaseMediaPlayer();
                int checkFocus = maudioManager.requestAudioFocus(amFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (checkFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    numbersAudio = MediaPlayer.create(NumbersActivity.this, numbersAdapter.getItem(position).getmSongResource());
                    numbersAudio.start();

                    numbersAudio.setOnCompletionListener(mcompletionListener);
                }
        });

    }

    private void releaseMediaPlayer() {
        if (numbersAudio != null) {
            numbersAudio.release();

            numbersAudio = null;
            maudioManager.abandonAudioFocus(amFocusChangeListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}