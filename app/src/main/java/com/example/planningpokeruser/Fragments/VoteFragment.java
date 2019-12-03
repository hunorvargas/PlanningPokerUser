package com.example.planningpokeruser.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planningpokeruser.Activitys.RoomActivity;
import com.example.planningpokeruser.Objects.Question;
import com.example.planningpokeruser.Objects.User;
import com.example.planningpokeruser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoteFragment extends Fragment {

    Button voteButton1,voteButton2,voteButton3,voteButton4,voteButton5,novoteButton,sendVoteButton;
    TextView questiontextView,questionDescTextView;
    private View mView;
    private String sessionID="",questionID="",vote=" ";
    private User newUser=new User();
    private Question question=new Question();
    ArrayList<Question> questions = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_vote, container, false);
        newUser.setUserName(getArguments().getString("UserName"));
        newUser.setSessionId(getArguments().getString("SessionID"));

        questionDescTextView = mView.findViewById(R.id.questionDescpTextView2);
        questiontextView = mView.findViewById(R.id.textViewQuestion2);
        init();
        getDatas();
        return mView;
    }



    private void init() {


        voteButton1 = mView.findViewById(R.id.buttonVote1);
        voteButton2 = mView.findViewById(R.id.buttonVote2);
        voteButton3 = mView.findViewById(R.id.buttonVote3);
        voteButton4 = mView.findViewById(R.id.buttonVote4);
        voteButton5 = mView.findViewById(R.id.buttonVote5);
        novoteButton=mView.findViewById(R.id.novoteButton);
        sendVoteButton=mView.findViewById(R.id.sendVoteButton);

        voteButton1.setClickable(false);
        voteButton2.setClickable(false);
        voteButton3.setClickable(false);
        voteButton4.setClickable(false);
        voteButton5.setClickable(false);
        novoteButton.setClickable(false);
        sendVoteButton.setClickable(false);

    }

    private void vote() {

        if (question.getQuestionVisibility().equals("true")) {


            voteButton1.setClickable(false);
            voteButton2.setClickable(false);
            voteButton3.setClickable(false);
            voteButton4.setClickable(false);
            voteButton5.setClickable(false);
            novoteButton.setClickable(false);
            sendVoteButton.setClickable(false);


            voteButton1.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                    setVote("1");
                    setToastText("User Voted 1");


                }
            });
            voteButton2.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                    setVote("2");
                    setToastText("User Voted 2");

                }
            });
            voteButton3.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                    setVote("3");
                    setToastText("User Voted 3");


                }
            });
            voteButton4.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                    setVote("4");
                    setToastText("User Voted 5");


                }
            });
            voteButton5.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                    setVote("5");
                    setToastText("User Voted 5");
                }
            });

            novoteButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                    setVote("No Voted");
                    setToastText("User  No Voted!");

                }
            });

            sendVoteButton.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View v) {
                    if (getVote().equals(" ")) {

                        setToastText("No vote selected");
                    } else {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference();

                        myRef.child("Session").child("Groups").child(newUser.getSessionId()).child("Questions")
                                .child(question.getID()).child("Results").child(newUser.getUserName()).setValue(getVote());
                        setToastText("Vote: " + getVote() + " Sendet!");

                        voteButton1.setClickable(false);
                        voteButton2.setClickable(false);
                        voteButton3.setClickable(false);
                        voteButton4.setClickable(false);
                        voteButton5.setClickable(false);
                        novoteButton.setClickable(false);
                        sendVoteButton.setClickable(false);
                    }


                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private void getDatas() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference  myRef = database.getReference().child("Session").child("Groups").child(newUser.getSessionId()).child("Questions");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("create1", "Questions");
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String questionID=datas.getKey();
                    question.setID(questionID);

                    Log.d("create1", "QuestionNr: " + questionID);
                    DatabaseReference  myRef2 = database.getReference().child("Session").child("Groups").
                            child(newUser.getSessionId()).child("Questions").child(questionID).child("Question");
                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String question1 = dataSnapshot.getValue(String.class);
                            question.setQuestion(question1);

                            Log.d("create1", "Question: " + question1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference  myRef3 = database.getReference().child("Session").child("Groups").
                            child(newUser.getSessionId()).child("Questions").child(questionID).child("QuestionDesc");
                    myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String questionDesc = dataSnapshot.getValue(String.class);
                            question.setQuestionDesc(questionDesc);

                            Log.d("create1", "Question: " + questionDesc);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference  myRef4 = database.getReference().child("Session").child("Groups").
                            child(newUser.getSessionId()).child("Questions").child(questionID).child("QuestionVisibility");
                    myRef4.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String questionVisibility = dataSnapshot.getValue(String.class);
                            Log.d("create1", "Question: " + questionVisibility);
                            question.setQuestionVisibility(questionVisibility);
                            questions.add(question);

                            showQuestion();
                            showQuestionDescrp();
                            vote();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showQuestionDescrp() {
        Log.d("create1", "showQuestionDesc");

        if(question.getQuestionVisibility().equals("true")){

            setQuestionDescTextView(question.getQuestionDesc());

        }
        else{
            Log.d("create1", "false");
        }
    }

    private void showQuestion() {
        Log.d("create1", "showQuestion");
        if(question.getQuestionVisibility().equals("true")){

            setQuestiontextView(question.getQuestion());
        }
        else{
            Log.d("create1", "false");
        }


    }

    public void setQuestiontextView(String questiontext) {
        this.questiontextView.setText(questiontext);
    }

    public void setQuestionDescTextView(String questiondesctext) {
        this.questionDescTextView.setText(questiondesctext);
    }

    private void setToastText(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
