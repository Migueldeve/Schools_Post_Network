package com.androidkotlin.schoolspostnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegistrarActivity extends AppCompatActivity {

    EditText Email, Password, Nombres, Apellidos, Edad, Telefono, Direccion;
    Button registrar_usuarios;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        Nombres = findViewById(R.id.Nombres);
        Apellidos = findViewById(R.id.Apellidos);
        Edad = findViewById(R.id.Edad);
        Telefono = findViewById(R.id.Telefono);
        Direccion = findViewById(R.id.Direccion);
        registrar_usuarios = findViewById(R.id.registrar_usuarios);

        firebaseAuth = FirebaseAuth.getInstance();

        registrar_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("Email no valido");
                    Email.setFocusable(true);
                }else if (Password.length()<6){
                    Password.setError("Password debe ser mayor a 6 digitos");
                    Password.setFocusable(true);
                }else {
                    REGISTRAR(email,password);
                }
            }
        });

    }

    private void REGISTRAR(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            assert user != null;
                            String uid = user.getUid();
                            String email = Email.getText().toString();
                            String password = Password.getText().toString();
                            String nombres = Nombres.getText().toString();
                            String apellidos = Apellidos.getText().toString();
                            String edad = Edad.getText().toString();
                            String telefono = Telefono.getText().toString();
                            String direccion = Direccion.getText().toString();

                            HashMap<Object,String> DatosUsuario = new HashMap<>();

                            DatosUsuario.put("uid", uid );
                            DatosUsuario.put("email",email);
                            DatosUsuario.put("password",password);
                            DatosUsuario.put("nombres",nombres);
                            DatosUsuario.put("apellidos",apellidos);
                            DatosUsuario.put("edad",edad);
                            DatosUsuario.put("telefono",telefono);
                            DatosUsuario.put("direccion",direccion);
                            DatosUsuario.put("imagen","");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("USUARIOS_DE_APP");

                            reference.child(uid).setValue(DatosUsuario);
                            Toast.makeText(RegistrarActivity.this,"Registro exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrarActivity.this,HomeActivity.class));
                        }else {
                            Toast.makeText(RegistrarActivity.this,"Algo salio mal vuelve a intentar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}