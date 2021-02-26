package com.rahulkumaryadav.pdd1;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetPasswordFragment extends Fragment {

private EditText resetEmail;
private Button resetPasswordBtn;
private FirebaseAuth firebaseAuth;
private ProgressBar resetPasswordProgressBar;
private TextView resetPasswordSuccessTv;
private ViewGroup successTv;
    public resetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reset_password, container, false);
           resetEmail=view.findViewById(R.id.forgotPasswordEmail);
           resetPasswordBtn=view.findViewById(R.id.passwordResetBtn);
           firebaseAuth=FirebaseAuth.getInstance();
           resetPasswordProgressBar=view.findViewById(R.id.forgot_password_progressBar);
           resetPasswordSuccessTv=view.findViewById(R.id.reset_passwordSuccess_tv);
           successTv=view.findViewById(R.id.fogotPasswordContainer);
         return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resetEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               resetPasswordProgressBar.setVisibility(View.VISIBLE);
                resetPasswordSuccessTv.setVisibility(View.INVISIBLE);
                resetPasswordBtn.setEnabled(false);
                firebaseAuth.sendPasswordResetEmail(resetEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            resetPasswordProgressBar.setVisibility(View.INVISIBLE);
                            TransitionManager.beginDelayedTransition(successTv);
                            resetPasswordSuccessTv.setVisibility(View.VISIBLE);
                            //Toast.makeText(getActivity(), "Email sent Successfully!", Toast.LENGTH_SHORT).show();
                        }else{
                            String error=task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                        resetPasswordProgressBar.setVisibility(View.INVISIBLE);

                      //  resetPasswordBtn.setEnabled(true);
                    }
                });
            }
        });

    }
    private void checkInputs(){
        if(TextUtils.isEmpty(resetEmail.getText())){
            resetPasswordBtn.setEnabled(false);
        }else{
            resetPasswordBtn.setEnabled(true);
        }
    }
}