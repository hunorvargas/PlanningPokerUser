package com.example.planningpokeruser.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planningpokeruser.Methods.Question;
import com.example.planningpokeruser.Methods.User;
import com.example.planningpokeruser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {


    TextView questiontextView,questionDescTextView;
    Button voteButton1,voteButton2,voteButton3,voteButton4,voteButton5,novoteButton,sendVoteButton;
    private User newUser;
    private Question question;
    ArrayList<String> questions = new ArrayList<>();
    private String vote=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        init();
        addVote();

    }

    private void addVote() {
        Log.d("create", "adduser");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Log.d("create", "nem kell Onclick");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long currentSessionID=dataSnapshot.getChildrenCount();
               // newUser.setSessionId(Long.toString(currentSessionID));
                Log.d("create", "kell:"+newUser.getSessionId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("create", "kell error");
            }
        });

        vote();


        Log.d("create", "nem kell data added");
        //startActivity(new Intent(CreateActivity.this, RoomActivity.class ));

    }

    private void vote() {

        voteButton1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                setVote("1");
                Toast.makeText(RoomActivity.this, "User Voted 1", Toast.LENGTH_SHORT).show();

            }
        });
        voteButton2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                setVote("2");
                Toast.makeText(RoomActivity.this, "User Voted 2", Toast.LENGTH_SHORT).show();
            }
        });
        voteButton3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                setVote("3");
                Toast.makeText(RoomActivity.this, "User Voted 3", Toast.LENGTH_SHORT).show();

            }
        });
        voteButton4.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                setVote("4");
                Toast.makeText(RoomActivity.this, "User Voted 4", Toast.LENGTH_SHORT).show();

            }
        });
        voteButton5.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                setVote("5");
                Toast.makeText(RoomActivity.this, "User Voted 5", Toast.LENGTH_SHORT).show();
            }
        });

        novoteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                setVote("No Voted");
                Toast.makeText(RoomActivity.this, "User  No Voted!", Toast.LENGTH_SHORT).show();
            }
        });

        sendVoteButton.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                if(getVote().equals(" ")){

                    Toast.makeText(RoomActivity.this, "No vote selected", Toast.LENGTH_LONG).show();
                }
                else{

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();

                    myRef.child("session").child(newUser.getSessionId()).child("Users").child(newUser.getUserName()).setValue(getVote());

                    Toast.makeText(RoomActivity.this, "Vote "+ getVote() + "Sendet!", Toast.LENGTH_SHORT).show();

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

    private void init() {

        questiontextView=findViewById(R.id.textViewQuestion);
        questionDescTextView=findViewById(R.id.questionDescpTextView);

        voteButton1 = findViewById(R.id.buttonVote1);
        voteButton2 = findViewById(R.id.buttonVote2);
        voteButton3 = findViewById(R.id.buttonVote3);
        voteButton4 = findViewById(R.id.buttonVote4);
        voteButton5 = findViewById(R.id.buttonVote5);
        novoteButton=findViewById(R.id.novoteButton);
        sendVoteButton=findViewById(R.id.sendVoteButton);

        newUser=new User();
        question=new Question();

        Intent intent= getIntent();

        newUser.setUserName(intent.getStringExtra("Username"));
        newUser.setSessionId(intent.getStringExtra("SessionId"));


        showQuestion();
        showQuestionDescrp();
    }

    private void showQuestionDescrp() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("session").child(newUser.getSessionId()).child("Questions").child("QuestionDesc");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                question.setQuestionDesc(dataSnapshot.getValue().toString());
               // questionDescTextView.setText(question.getQuestionDesc());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showQuestion() {

//                question.setQuestion(dataSnapshot.getValue().toString());
  //              questiontextView.setText(question.getQuestion());

    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
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
                    questions.add(questionID);
                    Log.d("create1", "QuestionNr: " + questionID);
                    DatabaseReference  myRef2 = database.getReference().child("Session").child("Groups").child(newUser.getSessionId()).child(questionID).child("Question");
                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String post = dataSnapshot.getValue(String.class);
                            Log.d("create1", "Question: " + post);
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
