package com.example.planningpokeruser.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planningpokeruser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JoinActivity extends AppCompatActivity {
    final ArrayList<String> sessionIDs = new ArrayList<>();
    final ArrayList<String> Users = new ArrayList<>();
    EditText editUsername, editSessID;
    Button btnJoin;
    private String sessionid = "", usernamesesion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();
        joinSession();
    }

    private void joinSession() {   //Join in session, check datas in database for login

        btnJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(JoinActivity.this, RoomActivity.class);

                setSessionid(editSessID.getText().toString().trim());
                setUsernamesesion(editUsername.getText().toString().trim());

                Log.d("create", "kell join:" + editSessID.getText().toString().trim());

                if (isCompletdata() && isagoodSessionID() && isagoodusername()) {

                    setToastText("Join Sucsess!");

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();

                    myRef.child("Session").child("Groups").child(sessionid).child("Users").child(getUsernamesesion()).setValue(getUsernamesesion());

                    intent.putExtra("Username", editUsername.getText().toString().trim());
                    intent.putExtra("SessionId", editSessID.getText().toString().trim());
                    startActivity(intent);
                }

            }
        });
    }


    private void init() {

        editUsername = findViewById(R.id.editUsername);
        editSessID = findViewById(R.id.editSessID);
        btnJoin = findViewById(R.id.btnJoin);

    }

    private boolean isCompletdata() {  // check if is completed and is valide with firebase all fields and
        if (editUsername.getText().toString().isEmpty() || editSessID.getText().toString().isEmpty()) {

            setToastText("Missing UserName or SessionID!");
            return false;
        } else {
            if (isagoodSessionID() && isagoodusername()) {
                Log.d("create", "kell sessionID jo");
                return true;
            }
        }
        return false;
    }

    private boolean isagoodusername() { //check typed username is busy or not in fireBase
        Log.d("create", "Users Size: " + Users.size());
        int i = 0;
        while (i < Users.size()) {
            Log.d("create", "Whiile Username: " + Users.get(i));
            if (Users.get(i).equals(getUsernamesesion())) {
                Log.d("create", "kell username egyenlo");

                setToastText("This UserName is busy!");
                // errorText.setText("This UserName is busy!");
                return false;

            }
            i++;
        }

        Log.d("create", "jo az username");
        return true;
    }

    private boolean isagoodSessionID() { //check if the entered SessonID is valid and exist in fireBase

        Log.d("create", "kell isagoodsession");

        int i = 0;
        while (i < sessionIDs.size()) {
            Log.d("create", "Whiile ID" + sessionIDs.get(i));
            if (sessionIDs.get(i).equals(getSessionid())) {
                Log.d("create", "kell session egyenlo");
                return true;
            }
            i++;
        }
        setToastText("This Session is not available!");
        Log.d("create", "kell session nincs");
        return false;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getUsernamesesion() {
        return usernamesesion;
    }

    public void setUsernamesesion(String usernamesesion) {
        this.usernamesesion = usernamesesion;
    }

    private void setToastText(String text) {
        Toast.makeText(JoinActivity.this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() { //get some data from specific session Firebase : SessionIDs,Usernames
        super.onStart();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("Session").child("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sessionIDs.clear();
                Log.d("create1", "SessionIDSnap");
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String sessionID = datas.getKey();
                    sessionIDs.add(sessionID);
                    Log.d("create1", "SessionID: " + sessionID);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}


