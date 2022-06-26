package com.androidkotlin.schoolspostnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesionActivity extends AppCompatActivity {

    EditText EmailEditText, PasswordEditText;
    Button iniciar_sesion;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        EmailEditText = findViewById(R.id.EmailEditText);
        PasswordEditText = findViewById(R.id.PasswordEditText);
        iniciar_sesion = findViewById(R.id.iniciar_sesion);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(IniciarSesionActivity.this);
        dialog = new Dialog(IniciarSesionActivity.this);

        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = EmailEditText.getText().toString();
                String Password = PasswordEditText.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    EmailEditText.setError("Email no valido");
                    EmailEditText.setFocusable(true);
                }else if (Password.length()<6){
                    PasswordEditText.setError("Password debe ser mayor o igual a 6 digitos");
                    PasswordEditText.setFocusable(true);
                }else {
                    LOGINUSUARIO(Email,Password);
                }
            }
        });
    }

    private void LOGINUSUARIO(String Email, String Password) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            startActivity(new Intent(IniciarSesionActivity.this,HomeActivity.class));
                            assert user != null;
                            Toast.makeText(IniciarSesionActivity.this,"!Bienvenid@ "+user.getEmail(),Toast.LENGTH_SHORT).show();
                            finish();

                        }else {
                            progressDialog.dismiss();
                            Dialog_No_Inicio();
                            //Toast.makeText(IniciarSesionActivity.this,"Algo se salio de control vuelve a intentar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(IniciarSesionActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void Dialog_No_Inicio() {

        Button ok_no_sesion;

        dialog.setContentView(R.layout.no_se_inicio_sesion);

        ok_no_sesion = dialog.findViewById(R.id.ok_no_sesion);

        ok_no_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }

}