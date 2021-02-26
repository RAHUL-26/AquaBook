package com.rahulkumaryadav.pdd1;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateInfoFragment extends Fragment {


    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    private CircleImageView circleImageView;
    private Button changePhotoBtn, removeBtn, updateBtn,doneBtn;
    private EditText nameField, emailField,password;
    private Dialog loadingDialog,passwordDialog;
    private String name,email,photo;
    private Uri imageUri;
    private boolean updatePhoto = false;
    private String firstName,lastName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        circleImageView = view.findViewById(R.id.profile_image);
        changePhotoBtn = view.findViewById(R.id.change_photo_btn);
        removeBtn = view.findViewById(R.id.remove_photo_btn);
        updateBtn = view.findViewById(R.id.update);
        nameField = view.findViewById(R.id.name);
        emailField = view.findViewById(R.id.email);


        ////////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.button_round));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ////////loading dialog

        ////////password dialog
        passwordDialog = new Dialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(true);
        passwordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        password=passwordDialog.findViewById(R.id.password);
        doneBtn=passwordDialog.findViewById(R.id.done_btn);
        ////////password dialog

        name = getArguments().getString("Name");
        email = getArguments().getString("Email");
        photo = getArguments().getString("Photo");

        Glide.with(getContext()).load(photo).into(circleImageView);
        nameField.setText(name);
        emailField.setText(email);

//////////////getting first and last name
//        String newName=nameField.getText().toString().trim();
//        int i;
//        for(i=0;i<newName.length();i++){
//            if (newName.charAt(i)==' '){
//                break;
//            }
//        }
//        firstName=newName.substring(0,i);
//        lastName=newName.substring(i+1);
//        //////////////getting first and last name

        changePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri=null;
                updatePhoto=true;
                Glide.with(getContext()).load(R.drawable.profile_pic).into(circleImageView);
            }
        });


        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });

        return view;
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(nameField.getText())) {
            if (!TextUtils.isEmpty(emailField.getText())) {
                updateBtn.setEnabled(true);


            } else {
                updateBtn.setEnabled(false);
               // updateBtn.setTextColor(Color.parseColor("#7000CCCB") );
            }
        } else {
            updateBtn.setEnabled(false);
           // updateBtn.setTextColor(Color.parseColor("#7000CCCB"));
        }
    }

    private void checkEmailAndPassword() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (emailField.getText().toString().matches(emailPattern)) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (emailField.getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim())){
                ////same email
                loadingDialog.show();
                updatePhoto(user);
            }else{
                /////update email
                passwordDialog.show();
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loadingDialog.show();
                        String userPassword=password.getText().toString();

                        passwordDialog.dismiss();
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), userPassword);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            user.updateEmail(emailField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        updatePhoto(user);
                                                    }else{
                                                        loadingDialog.dismiss();
                                                        String error=task.getException().getMessage();
                                                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            loadingDialog.dismiss();
                                            String error=task.getException().getMessage();
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }

        } else {

            emailField.setError("Invalid email!");

        }

    }

    private void updatePhoto(FirebaseUser user){
        ////////updating Photo
        if(updatePhoto){

            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("profile/"+user.getUid() + ".jpg");

            if (imageUri!=null){

                Glide.with(getContext()).asBitmap().load(imageUri).circleCrop().into(new ImageViewTarget<Bitmap>(circleImageView) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){

                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()){
                                                imageUri=task.getResult();
                                                DBQueries.profile=task.getResult().toString();
                                                Glide.with(getContext()).load(DBQueries.profile).into(circleImageView);

                                                String newName=nameField.getText().toString().trim();
                                                int i;
                                                for(i=0;i<newName.length();i++){
                                                    if (newName.charAt(i)==' '){
                                                        break;
                                                    }
                                                }
                                                firstName=newName.substring(0,i);
                                                if (i<newName.length()) {
                                                    lastName = newName.substring(i + 1);
                                                }else
                                                {
                                                    lastName="";
                                                }
                                                Map<String,Object> updateData = new HashMap<>();
                                                updateData.put("Email",emailField.getText().toString());
                                                updateData.put("First Name ",firstName);
                                                updateData.put("Last Name",lastName);
                                                updateData.put("Profile",DBQueries.profile);

                                                updateFields(user,updateData);

                                            }else{
                                                loadingDialog.dismiss();
                                                DBQueries.profile="";
                                                String error=task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else{
                                    loadingDialog.dismiss();
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        circleImageView.setImageResource(R.drawable.profile_pic);
                    }
                });

            }else{///////////remove photo
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            DBQueries.profile="";

                            String newName=nameField.getText().toString().trim();
                            int i;
                            for(i=0;i<newName.length();i++){
                                if (newName.charAt(i)==' '){
                                    break;
                                }
                            }
                            firstName=newName.substring(0,i);
                            if (i<newName.length()) {
                                lastName = newName.substring(i + 1);
                            }else
                            {
                                lastName="";
                            }
                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("Email",emailField.getText().toString());
                            updateData.put("First Name ",firstName);
                            updateData.put("Last Name",lastName);
                            updateData.put("Profile","");

                            updateFields(user,updateData);
                        }else{
                            loadingDialog.dismiss();
                            String error=task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else {
            String newName=nameField.getText().toString().trim();
            int i;
            for(i=0;i<newName.length();i++){
                if (newName.charAt(i)==' '){
                    break;
                }
            }
            firstName=newName.substring(0,i);
            if (i<newName.length()) {
                lastName = newName.substring(i + 1);
            }else
            {
                lastName="";
            }
            Map<String,Object> updateData = new HashMap<>();
            updateData.put("First Name ",firstName);
            updateData.put("Last Name",lastName);
            updateFields(user,updateData);
        }

        ////////updating Photo
    }

    private void updateFields(FirebaseUser user,Map<String,Object> updateData){

        String newName=nameField.getText().toString().trim();
        int i;
        for(i=0;i<newName.length();i++){
            if (newName.charAt(i)==' '){
                break;
            }
        }
        firstName=newName.substring(0,i);
        if (i<newName.length()) {
            lastName = newName.substring(i + 1);
        }else
        {
            lastName="";
        }

        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid()).update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (updateData.size()>2){
                                DBQueries.email=emailField.getText().toString().trim();
                                DBQueries.firstName=firstName;
                                DBQueries.lastName=lastName;
                                Toast.makeText(getContext(), "Email Updated!", Toast.LENGTH_SHORT).show();
                            }else{
                                DBQueries.firstName=firstName;
                                DBQueries.lastName=lastName;
                                String s= String.valueOf(updateData.size());
                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                            }
                            getActivity().finish();

                        }else{

                            String error=task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    imageUri= data.getData();
                    updatePhoto=true;
                    Glide.with(getContext()).load(imageUri).into(circleImageView);
                } else {
                    Toast.makeText(getContext(), "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);

            } else {
                Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}