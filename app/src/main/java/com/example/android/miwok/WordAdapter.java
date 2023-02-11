package com.example.android.miwok;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    private static String LOG_TAG = WordAdapter.class.getSimpleName();
    private int mColorResourceId;

    public WordAdapter(Activity context, ArrayList<Word> words, int backgroundColor){
        super(context, 0, words);
        mColorResourceId = backgroundColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Word currentWord = getItem(position);

        LinearLayout textLayout = listItemView.findViewById(R.id.textLayout);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textLayout.setBackgroundColor(color);

        TextView miwokTextView = listItemView.findViewById(R.id.miwokTran);
        miwokTextView.setText(currentWord.getMiwokText());

        TextView englishTextView = listItemView.findViewById(R.id.englishTran);
        englishTextView.setText(currentWord.getDefaultText());

        ImageView iconView = listItemView.findViewById(R.id.icon);
        if(currentWord.hasImage()){
            iconView.setImageResource(currentWord.getImageResource());
        }
        else
            iconView.setVisibility(View.GONE);

        return listItemView;
    }

}
