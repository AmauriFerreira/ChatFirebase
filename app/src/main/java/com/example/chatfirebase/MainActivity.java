package com.example.chatfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEditEmail;
    private EditText mEditPassword;
    private Button mBtntEnter;
    private TextView mTextAccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       mEditEmail = findViewById(R.id.edit_email);
       mEditPassword = findViewById(R.id.edit_password);
       mBtntEnter = findViewById(R.id.edit_enter);
       mTextAccount = findViewById(R.id.text_account);

       mBtntEnter.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick (View v){
               String email =  mEditEmail.getText().toString();
               String senha =  mEditPassword.getText().toString();

               Log.i( "Teste", email);
               Log.i( "Teste", senha);
               if (email == null || email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
                   Toast.makeText(MainActivity.this, "Senha e Email devem se preenchidos", Toast.LENGTH_SHORT).show();
                   return;
               }
               FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                                   Log.i("Teste", task.getResult().getUser().getUid());

                               Intent intent= new Intent(MainActivity.this, MenssagensActivity.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);

                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.i("Teste", e.getMessage());

                           }
                       });

           }
       });
       mTextAccount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick (View v) {
               Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
               startActivity(intent);
           }
       });

    }
}
