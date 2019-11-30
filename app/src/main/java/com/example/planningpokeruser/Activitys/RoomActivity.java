package com.example.planningpokeruser.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planningpokeruser.Fragments.StaticsFragment;
import com.example.planningpokeruser.Fragments.VoteFragment;
import com.example.planningpokeruser.Objects.Question;
import com.example.planningpokeruser.Objects.User;
import com.example.planningpokeruser.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {



    Button voteButton1,voteButton2,voteButton3,voteButton4,voteButton5,novoteButton,sendVoteButton;
    private User newUser;
    private Question question;
    private BottomNavigationView navigationView;
    private FrameLayout frameLayout;
    private VoteFragment voteFragment;
    private StaticsFragment staticsFragment;
    ArrayList<Question> questions = new ArrayList<>();
    private String vote=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        init();

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
                    Toast.makeText(RoomActivity.this, "User Voted 5", Toast.LENGTH_SHORT).show();
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

                        myRef.child("session").child(newUser.getSessionId()).child("Users").child(newUser.getUserName()).setValue(getVote());
                        myRef.child("Session").child("Groups").child(newUser.getSessionId()).child("Questions")
                                .child(question.getID()).child("Results").child(newUser.getUserName()).setValue(getVote());
                        setToastText("Vote " + getVote() + "Sendet!");

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

    private void init() {


        frameLayout=findViewById(R.id.framelayout);
        navigationView=findViewById(R.id.navigationbottom);
        voteFragment=new VoteFragment();
        staticsFragment=new StaticsFragment();

        voteButton1 = findViewById(R.id.buttonVote1);
        voteButton2 = findViewById(R.id.buttonVote2);
        voteButton3 = findViewById(R.id.buttonVote3);
        voteButton4 = findViewById(R.id.buttonVote4);
        voteButton5 = findViewById(R.id.buttonVote5);
        novoteButton=findViewById(R.id.novoteButton);
        sendVoteButton=findViewById(R.id.sendVoteButton);

        voteButton1.setClickable(false);
        voteButton2.setClickable(false);
        voteButton3.setClickable(false);
        voteButton4.setClickable(false);
        voteButton5.setClickable(false);
        novoteButton.setClickable(false);
        sendVoteButton.setClickable(false);

        newUser=new User();
        question=new Question();

        Intent intent= getIntent();

        newUser.setUserName(intent.getStringExtra("Username"));
        newUser.setSessionId(intent.getStringExtra("SessionId"));

        setFragment(voteFragment);
        navigationViewlistener();


    }

    private void navigationViewlistener() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch ((menuItem.getItemId())){
                    case R.id.voteQuestionIcon:
                        setFragment(voteFragment);
                        voteButton1.setVisibility(View.VISIBLE);
                        voteButton2.setVisibility(View.VISIBLE);
                        voteButton3.setVisibility(View.VISIBLE);
                        voteButton4.setVisibility(View.VISIBLE);
                        voteButton5.setVisibility(View.VISIBLE);
                        novoteButton.setVisibility(View.VISIBLE);
                        sendVoteButton.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.viewStaticsIcon:
                        setFragment(staticsFragment);
                        voteButton1.setVisibility(View.GONE);
                        voteButton2.setVisibility(View.GONE);
                        voteButton3.setVisibility(View.GONE);
                        voteButton4.setVisibility(View.GONE);
                        voteButton5.setVisibility(View.GONE);
                        novoteButton.setVisibility(View.GONE);
                        sendVoteButton.setVisibility(View.GONE);
                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragments) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragments);
        fragmentTransaction.commit();
    }

    private void showQuestionDescrp() {
        Log.d("create1", "showQuestionDesc");

        if(question.getQuestionVisibility().equals("true")){

            voteFragment.setQuestionDescTextView(question.getQuestionDesc());
           // questionDescTextView.setText(question.getQuestionDesc());
        }
        else{
            Log.d("create1", "false");
        }
    }

    private void showQuestion() {
        Log.d("create1", "showQuestion");
          if(question.getQuestionVisibility().equals("true")){

              voteFragment.setQuestiontextView(question.getQuestion());
             // questiontextView.setText(question.getQuestion());
          }
          else{
              Log.d("create1", "false");
          }


    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    private void setToastText(String text){
        Toast.makeText(RoomActivity.this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {

        super.onStart();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference  myRef = database.getReference().child("Session").child("Groups").child(newUser.getSessionId()).child("Questions");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("create1", "Questions");
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String questionID=datas.getKey();
                    //questions.add(questionID);
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
                            String question1 = dataSnapshot.getValue(String.class);
                            question.setQuestionDesc(question1);

                            Log.d("create1", "Question: " + question1);
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
                            String question1 = dataSnapshot.getValue(String.class);
                            Log.d("create1", "Question: " + question1);
                            question.setQuestionVisibility(question1);
                            questions.add(question);


                            vote();
                            showQuestion();
                            showQuestionDescrp();

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
}
