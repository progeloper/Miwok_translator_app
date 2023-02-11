package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    private MediaPlayer phraseAudio;
    private MediaPlayer.OnCompletionListener pCompletionListener = mp -> releaseMediaPlayer();
    private AudioManager paudioManager;
    private AudioManager.OnAudioFocusChangeListener paudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case (AudioManager.AUDIOFOCUS_GAIN):
                    phraseAudio.start();
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS):
                    releaseMediaPlayer();
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                    phraseAudio.pause();
                    phraseAudio.seekTo(0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        paudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> phrases = new ArrayList<>();
        phrases.add(new Word("where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        phrases.add(new Word("what is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        phrases.add(new Word("my name is....", "oyaaset....", R.raw.phrase_my_name_is));
        phrases.add(new Word("how are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        phrases.add(new Word("I'm feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        phrases.add(new Word("are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        phrases.add(new Word("yes I'm coming", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        phrases.add(new Word("I'm coming.", "әәnәm", R.raw.phrase_im_coming));
        phrases.add(new Word("Let's go.", "yoowutis", R.raw.phrase_lets_go));
        phrases.add(new Word("come here", "әnni'nem", R.raw.phrase_come_here));

        ListView list = (ListView) findViewById(R.id.list);
        WordAdapter phraseAdapter = new WordAdapter(this, phrases, R.color.category_phrases);
        list.setAdapter(phraseAdapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
                releaseMediaPlayer();
                int checkFocus = paudioManager.requestAudioFocus(paudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (checkFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    phraseAudio = MediaPlayer.create(PhrasesActivity.this, phraseAdapter.getItem(position).getmSongResource());
                    phraseAudio.start();
                    phraseAudio.setOnCompletionListener(pCompletionListener);
                }
        });
    }

    private void releaseMediaPlayer(){
        if(phraseAudio != null){
            phraseAudio.release();
            phraseAudio = null;
            paudioManager.abandonAudioFocus(paudioFocusChangeListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }
}