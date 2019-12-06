package com.example.planningpokeruser.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planningpokeruser.Fragments.StaticsFragment;
import com.example.planningpokeruser.Fragments.VoteFragment;
import com.example.planningpokeruser.Objects.User;
import com.example.planningpokeruser.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RoomActivity extends AppCompatActivity {

    private User newUser;
    private BottomNavigationView navigationView;
    private FrameLayout frameLayout;
    private VoteFragment voteFragment;
    private StaticsFragment staticsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        init();

    }


    private void init() {


        frameLayout=findViewById(R.id.framelayout);
        navigationView=findViewById(R.id.navigationbottom);
        voteFragment=new VoteFragment();
        staticsFragment=new StaticsFragment();

        newUser=new User();

        Intent intent= getIntent();

        newUser.setUserName(intent.getStringExtra("Username"));
        newUser.setSessionId(intent.getStringExtra("SessionId"));

        Bundle bundle = new Bundle();
        bundle.putString("UserName", newUser.getUserName());
        bundle.putString("SessionID", newUser.getSessionId());

        voteFragment.setArguments(bundle);

        setFragment(voteFragment);
        voteFragment.setNewUser(newUser);
        navigationViewlistener();

    }

    private void navigationViewlistener() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch ((menuItem.getItemId())){
                    case R.id.voteQuestionIcon:
                        setFragment(voteFragment);
                        return true;
                    case R.id.viewStaticsIcon:
                        staticsFragment.setSessionID(newUser.getSessionId());
                        setFragment(staticsFragment);
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

}
