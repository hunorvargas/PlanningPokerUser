package com.example.planningpokeruser.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.planningpokeruser.Objects.Question;
import com.example.planningpokeruser.Objects.User;
import com.example.planningpokeruser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoteFragment extends Fragment {

    final ArrayList<String> votedUsers = new ArrayList<>();
    Button voteButton1, voteButton2, voteButton3, voteButton4, voteButton5, novoteButton, sendVoteButton;
    TextView questiontextView, questionDescTextView;
    ArrayList<Question> questions = new ArrayList<>();
    private View mView;
    private String vote = " ", currentDateTime, maxUserVoteNumber;
    private User newUser = new User();

    private Question question1;
    private Date expireDate, currentDate;
    private boolean noDateTime = false;

// Init get datas from  antoher fragment intit, and get datas from Firebase
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_vote, container, false);
        newUser.setUserName(getArguments().getString("UserName"));
        newUser.setSessionId(getArguments().getString("SessionID"));

        init();
        getDatas();
        return mView;
    }


    private void init() {

        questionDescTextView = mView.findViewById(R.id.questionDescpTextView2);
        questiontextView = mView.findViewById(R.id.textViewQuestion2);

        voteButton1 = mView.findViewById(R.id.buttonVote1);
        voteButton2 = mView.findViewById(R.id.buttonVote2);
        voteButton3 = mView.findViewById(R.id.buttonVote3);
        voteButton4 = mView.findViewById(R.id.buttonVote4);
        voteButton5 = mView.findViewById(R.id.buttonVote5);
        novoteButton = mView.findViewById(R.id.novoteButton);
        sendVoteButton = mView.findViewById(R.id.sendVoteButton);

        voteButton1.setClickable(false);
        voteButton2.setClickable(false);
        voteButton3.setClickable(false);
        voteButton4.setClickable(false);
        voteButton5.setClickable(false);
        novoteButton.setClickable(false);
        sendVoteButton.setClickable(false);
        // Current Date

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy/HH:mm");
        String formattedDate = df.format(c.getTime());
        setCurrentDateTime(formattedDate);
        Log.d("create1", "Current Date: " + formattedDate);
    }

    private void vote() {  // Check question visibility and Date before sending the choosed vote
        Log.d("create1", "Vote : " + question1.getQuestionVisibility() + " " + expireDate);
        if (question1.getQuestionVisibility().equals("true") && noDateTime) {
            Log.d("create1", "NoDateTime : ");
            getVotefromButton();
        } else {
            Log.d("create1", "CurrentDate : " + currentDate + " QuestionVisibilty" + question1.getQuestionVisibility());
            if (question1.getQuestionVisibility().equals("true"))
                if (expireDate.after(currentDate)) {
                    Log.d("create1", "Van DateTIme : ");
                    getVotefromButton();
                }
        }

        sendVoteButton.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                if (getVote().equals(" ")) {

                    setToastText("No vote selected");
                } else {
                    // add selected vote in firebase
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();

                    myRef.child("Session").child("Groups").child(newUser.getSessionId()).child("Questions")
                            .child(question1.getID()).child("Results").child(newUser.getUserName()).setValue(getVote());
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }


    public void getVotefromButton() { // get the vote from button
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
    }

    private void showQuestion() { // show question and question desc. if the question is visiible and the time and date is correct
        Log.d("create1", "showQuestionDesc");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
        try {
            expireDate = formatter.parse(question1.getQuestionTime());
            currentDate = formatter.parse(currentDateTime);
        } catch (ParseException e) {
            e.printStackTrace();

            Log.d("create1", e.toString());
        }
        try {
            expireDate.after(currentDate);
        } catch (NullPointerException e) {
            noDateTime = true;
        }

        if (question1.getQuestionVisibility().equals("true")) {
            if (noDateTime) {
                setQuestionDescTextView(question1.getQuestionDesc());
                setQuestiontextView(question1.getQuestion());
                checkVoteNumbers();

            } else if (expireDate.after(currentDate)) {

                setQuestionDescTextView(question1.getQuestionDesc());
                setQuestiontextView(question1.getQuestion());
                checkVoteNumbers();
            }


        } else {
            Log.d("create1", "false");
        }
    }

    private void checkVoteNumbers() {
        // get max Vote Number and check Results number
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("Session").child("Groups").child(newUser.getSessionId()).child("Questions")
                .child(question1.getID()).child("Results").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("create", "Results Snap");
                votedUsers.clear();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String username = datas.getKey();
                    if (username.equals("MaxUserVoteNumber")) {
                        maxUserVoteNumber = datas.getValue().toString();
                        Log.d("create", "MaxUserNum: " + maxUserVoteNumber);
                    }
                    votedUsers.add(username);
                    Log.d("create", "UsersName: " + username);
                }
                int votedUserSize = votedUsers.size() - 1;
                votedUsers.clear();
                Log.d("create", "VotedUsers: " + votedUserSize);
                int MaxVoteUserSize = parseInt(maxUserVoteNumber);
                if (MaxVoteUserSize > votedUserSize) {
                    Log.d("create", "MaxVote > votedUser");
                    vote();
                } else {
                    Log.d("create", "Nem nagyobb ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDatas() {
        // Get Datas From firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Session").child("Groups").child(newUser.getSessionId()).child("Questions");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questions.clear();
                final Question question = new Question();
                Log.d("create1", "Questions");
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    final String questionID = datas.getKey();
                    question.setID(questionID);

                    Log.d("create1", "QuestionNr: " + questionID);
                    DatabaseReference myRef2 = database.getReference().child("Session").child("Groups").
                            child(newUser.getSessionId()).child("Questions").child(questionID).child("Question");
                    myRef2.addValueEventListener(new ValueEventListener() {
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

                    DatabaseReference myRef3 = database.getReference().child("Session").child("Groups").
                            child(newUser.getSessionId()).child("Questions").child(questionID).child("QuestionDesc");
                    myRef3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String questionDesc = dataSnapshot.getValue(String.class);
                            question.setQuestionDesc(questionDesc);

                            Log.d("create1", "QuestionDesc: " + questionDesc);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference myRef6 = database.getReference().child("Session").child("Groups").
                            child(newUser.getSessionId()).child("Questions").child(questionID).child("QuestionTime");
                    myRef6.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String questionTime = dataSnapshot.getValue(String.class);
                            question.setQuestionTime(questionTime);

                            Log.d("create1", "QuestionTime: " + questionTime);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference myRef4 = database.getReference().child("Session").child("Groups").
                            child(newUser.getSessionId()).child("Questions").child(questionID).child("QuestionVisibility");
                    myRef4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String questionVisibility = dataSnapshot.getValue(String.class);
                            Log.d("create1", "QuestionVisibility: " + questionVisibility);
                            question.setQuestionVisibility(questionVisibility);
                            question.setID(questionID);
                            questions.add(question);

                            if (!question.getQuestionTime().equals(" ") && question.getQuestionVisibility().equals("true")){

                                question1=new Question(question.getQuestion(),question.getID(),question.getQuestionDesc(),question.getQuestionVisibility(),question.getQuestionTime());
                                showQuestion();
                                checkVoteNumbers();
                                Log.d("create1", "QuestionTIme != ");
                            }
                            else
                                if (question.getQuestionTime().equals(" ") && question.getQuestionVisibility().equals("true")){
                                     question1=new Question(question.getQuestion(),question.getID(),question.getQuestionDesc(),question.getQuestionVisibility(),question.getQuestionTime());
                                     showQuestion();
                                     checkVoteNumbers();
                                     Log.d("create1", "QuestionTIme = ");
                            }
                            Log.d("create1", "ChekVoteNumbers");
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

    public void setQuestiontextView(String questiontext) {
        this.questiontextView.setText(questiontext);
    }

    public void setQuestionDescTextView(String questiondesctext) {
        this.questionDescTextView.setText(questiondesctext);
    }

    private void setToastText(String text) {
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

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}
