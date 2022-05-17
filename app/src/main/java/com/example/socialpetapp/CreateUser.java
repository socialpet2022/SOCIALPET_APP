package com.example.socialpetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateUser extends AppCompatActivity {
    EditText name,email,password;
    FirebaseAuth autentication;
    Button register;
ProgressDialog cargar;
    DatabaseReference mroot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        name=findViewById(R.id.txtnombre);
        email=findViewById(R.id.txtemail);
        password=findViewById(R.id.txtpass);
        register=findViewById(R.id.btncrear);
        cargar=new ProgressDialog(this);
        autentication=FirebaseAuth.getInstance();
    mroot= FirebaseDatabase.getInstance().getReference();
    }
    public void crear_user(View v){
            String nombre=name.getText().toString();
            String correo=email.getText().toString();
            String pass=password.getText().toString();
            cargar.setMessage("Por favor espere");
            cargar.show();
    if (TextUtils.isEmpty(nombre)){
                name.setError("Escribe el nombre");
                return;
            }else if(TextUtils.isEmpty(correo)){
                email.setError("Escribe el email");
                return;
            }else if(TextUtils.isEmpty(pass)){
                password.setError("Escribe la contraseña");
                return;
        }else if(!isValidEmail(correo)){
                email.setError("Correo no valido");
                return;
            }

            autentication.createUserWithEmailAndPassword(correo,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user = autentication.getCurrentUser().getUid();
                        Map<String,Object> datosusuario=new HashMap<>();
                        datosusuario.put("Nombre",nombre);
                        datosusuario.put("Email",correo);
                        datosusuario.put("Contraseña",pass);
                        datosusuario.put("image","");
                        FirebaseDatabase.getInstance().getReference("Usuarios/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(datosusuario);

                                cargar.dismiss();
                        Toast.makeText(CreateUser.this,"usuario registrado correctamente",Toast.LENGTH_LONG).show();


                        Intent intent= new Intent(CreateUser.this,fotoperfil.class);

                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(CreateUser.this,"usuario falllo o ya esta registrado",Toast.LENGTH_LONG).show();
                    }
                }
            });

    }
    private Boolean isValidEmail(CharSequence target){
        return(!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public void ini(View v){
        Intent intent= new Intent(CreateUser.this,loginUser.class);
        startActivity(intent);
        finish();
    }


}