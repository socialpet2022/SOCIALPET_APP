package com.example.socialpetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.socialpetapp.Fragments.BuscarFragment;
import com.example.socialpetapp.Fragments.InicioFragment;
import com.example.socialpetapp.Fragments.NotificacionesFragment;
import com.example.socialpetapp.Fragments.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PantallaPrin extends AppCompatActivity {
BottomNavigationView bottomNavigationView;
Fragment selectorFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_prin);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        selectorFragment=new InicioFragment();
                        break;
                    case R.id.search:
                        selectorFragment=new BuscarFragment();
                        break;
                    case R.id.add:
                        selectorFragment=null;
                        startActivity(new Intent(PantallaPrin.this,publications.class));
                        break;
                    case R.id.person:
                        selectorFragment=new PerfilFragment();
                        break;
                    case R.id.heart:
                        selectorFragment=new NotificacionesFragment();
                        break;
                }
                if(selectorFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectorFragment).commit();
                }

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new InicioFragment()).commit();
    }
}