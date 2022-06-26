package com.androidkotlin.schoolspostnetwork;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Button CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        CerrarSesion = findViewById(R.id.CerrarSesion);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarSesion();
            }
        });

    }

    @Override
    protected void onStart() {
        VerificarInicioSesion();
        super.onStart();
    }

    private void VerificarInicioSesion(){
        if (firebaseUser != null){
            Toast.makeText(this, "Se incio sesion correctamente", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();
        }
    }

    private void CerrarSesion(){
        firebaseAuth.signOut();
        Toast.makeText(this,"Has cerrado sesion correctamente", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeActivity.this,MainActivity.class));
    }

}