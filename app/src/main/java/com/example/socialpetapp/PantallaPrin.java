package com.example.socialpetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.MenuItem;

import com.example.socialpetapp.Fragments.ChatFragment;
import com.example.socialpetapp.Fragments.PerfilFragment;
import com.example.socialpetapp.Fragments.usuariosFragment;
import com.example.socialpetapp.Fragments.InicioFragment;
import com.example.socialpetapp.Fragments.publications;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PantallaPrin extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String myid;
    ActionBar actionBar;
    BottomNavigationView barra_navi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_prin);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Perfil");
        firebaseAuth = FirebaseAuth.getInstance();
        barra_navi = findViewById(R.id.navigation);
        barra_navi.setOnItemReselectedListener(selectedListener);
        actionBar.setTitle("Home");
        InicioFragment fragment = new InicioFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnItemReselectedListener selectedListener = new BottomNavigationView.OnItemReselectedListener() {

        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        actionBar.setTitle("Inicio");
                        InicioFragment fragment = new InicioFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, fragment, "");
                        fragmentTransaction.commit();
                        break;
                    case R.id.users:
                        actionBar.setTitle("Usuarios");
                        usuariosFragment fragment1 = new usuariosFragment();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.content, fragment1);
                        fragmentTransaction1.commit();
                        break;
                    case R.id.add:
                        actionBar.setTitle("Añadir publicacion");
                        publications fragment2 = new publications();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.content, fragment2, "");
                        fragmentTransaction2.commit();
                        break;
                    case R.id.person:
                        actionBar.setTitle("Perfil");
                         PerfilFragment fragment3 = new PerfilFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.content, fragment3, "");
                        fragmentTransaction3.commit();
                        break;
                    case R.id.chat:
                        actionBar.setTitle("Chats");
                        ChatFragment listFragment = new ChatFragment();
                        FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction4.replace(R.id.content, listFragment, "");
                        fragmentTransaction4.commit();
                        break;
                }
        }
    };
}