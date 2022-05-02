package com.example.socialpetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inicio extends AppCompatActivity {
    Button iniuser,createuse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        iniuser=findViewById(R.id.btnini);
        createuse=findViewById(R.id.btncrearuser);
    }
    public void iniciar_sesion(View v){
        Intent i = new Intent(Inicio.this,loginUser.class);
        startActivity(i);
        finish();
    }
    public void crear_usuario(View v){
        Intent i = new Intent(Inicio.this,CreateUser.class);
        startActivity(i);
        finish();
    }
}