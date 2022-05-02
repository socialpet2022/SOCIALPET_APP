package com.example.socialpetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    Button recuperar;
     EditText txtemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        recuperar=findViewById(R.id.btnrecu);
        txtemail=findViewById(R.id.txtmail);
    }
    public void recuperarcon(View v){
        validate();
    }
    public void validate(){
        String mail=txtemail.getText().toString();
        if(mail.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            txtemail.setError("Correo no valido");
            return;
        }
        sendmail(mail);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ForgotPassword.this,loginUser.class);
        startActivity(intent);
        finish();
    }
    public void sendmail(String mail){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String mailAddres=mail;
        auth.sendPasswordResetEmail(mailAddres)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this,"Correo enviado",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ForgotPassword.this,loginUser.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(ForgotPassword.this,"Correo no valido",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}