package com.example.planningpokeruser.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planningpokeruser.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoteFragment extends Fragment {


    TextView questiontextView,questionDescTextView;
    private View mView;
    String sessionID="",questionID="";


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_vote, container, false);

        questionDescTextView = mView.findViewById(R.id.questionDescpTextView2);
        questiontextView = mView.findViewById(R.id.textViewQuestion2);

        return mView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public void setQuestiontextView(String questiontext) {
        this.questiontextView.setText(questiontext);
    }

    public void setQuestionDescTextView(String questiondesctext) {
        this.questionDescTextView.setText(questiondesctext);
    }
}
