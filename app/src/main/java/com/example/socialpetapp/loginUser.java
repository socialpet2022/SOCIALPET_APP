package com.example.socialpetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class loginUser extends AppCompatActivity {
    EditText email,password;
    TextView forgot;
    FirebaseAuth autentication;
    Button login,login_google;

    private static final String TAG = "GOOGLEAUTH";
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        email=findViewById(R.id.txtcorreo);
        password=findViewById(R.id.txtpassword);
        login=findViewById(R.id.buttonlogin);
        autentication=FirebaseAuth.getInstance();
        login_google=findViewById(R.id.btnlogingoogle);
        forgot=findViewById(R.id.forgotpass);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(loginUser.this,gso);



    }

    public void loginuser(View v){

        String correo=email.getText().toString();
        String pass=password.getText().toString();
        if(TextUtils.isEmpty(correo)){
            email.setError("Escribe el email");
            return;
        }else if(TextUtils.isEmpty(pass)){
            password.setError("Escribe la contraseña");
            return;
        }else if(!isValidEmail(correo)){
            email.setError("Correo no valido");
            return;
        }

        autentication.signInWithEmailAndPassword(correo,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(loginUser.this,"usuario ha iniciado sesion",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(loginUser.this,PantallaPrin.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(loginUser.this,"usuario falllo o no esta registrado",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private Boolean isValidEmail(CharSequence target){
        return(!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public void l_google(View v){
    resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent() ));

    }
   ActivityResultLauncher <Intent> resultLauncher=  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
       @Override
       public void onActivityResult(ActivityResult result) {
           if(result.getResultCode()== Activity.RESULT_OK){
               Intent intent=result.getData();
               Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
               try {
                   // Google Sign In was successful, authenticate with Firebase
                   GoogleSignInAccount account = task.getResult(ApiException.class);
                   //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                   firebaseAuthWithGoogle(account.getIdToken());
               } catch (ApiException e) {
                   // Google Sign In failed, update UI appropriately
                   Log.w(TAG, "Google sign in failed", e);

                   // ...
               }
           }
       }
   });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        autentication.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = autentication.getCurrentUser();
                            Intent i = new Intent(loginUser.this,PantallaPrin.class);
                            startActivity(i);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            //  Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //  Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // updateUI(null);

                            Toast.makeText(loginUser.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    public void register(View v){
        Intent intent=new Intent(loginUser.this,CreateUser.class);
        startActivity(intent);
        finish();
    }
    public void forgotpassword(View v){
        Intent intent=new Intent(loginUser.this,ForgotPassword.class);
        startActivity(intent);
        finish();
    }


    }
