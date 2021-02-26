package com.rahulkumaryadav.pdd1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static boolean setSignUpFragment = false;

    int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frameLayout=findViewById(R.id.register_frameLayout);
        if(setSignUpFragment)
        {
            setSignUpFragment = false;
            setFragment(new SignupFragment());
        }
        else
        {
            setFragment(new SignInFragment());
        }
    }
    private void setFragment(Fragment fragment )
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        SignInFragment.disableCloseBtn=false;
        SignupFragment.disableCloseBtn=false;

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.register_frameLayout);
        if(!(currentFragment instanceof SignInFragment)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_left);
            fragmentTransaction.replace(R.id.register_frameLayout, new SignInFragment()).commit();
        }
        else{
             Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
             startActivity(intent);
             finish();
        }

        }
    }
