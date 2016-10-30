package com.carepuppy.pirtu.caremypuppy;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView tVname;
    EditText eTemail,eTpass;
    String email, pass, uid;
    Intent intentReg, intentLog;
    Button btnRegistro,btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declaración de componentes
        tVname = (TextView) findViewById(R.id.tVName);
        eTemail = (EditText) findViewById(R.id.eTemail);
        eTpass = (EditText) findViewById(R.id.eTpass);
        btnRegistro = (Button) findViewById(R.id.buttonRegistro);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        //cambiar a una fuente custom
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lobster.otf");
        tVname.setTypeface(custom_font);
        eTemail.setTypeface(custom_font);
        eTpass.setTypeface(custom_font);

        //Los intent para lanzar las pantallas
        intentLog = new Intent(LoginActivity.this,MainActivity.class);
        intentReg = new Intent(LoginActivity.this,RegisterActivity.class);

        // Para comprobar el login
        mAuth = FirebaseAuth.getInstance();
        //compruebo el cmbio en el estado del logeo de un usuario
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("LOGIN", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("LOGIN", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        //Clickear botón de registro desde el login
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intentReg);
                //No quiero que se cierre la pantalla de login, así podemos volver atrás
//                finish();

            }
        });

        //cuando pulsamos sobre el boton de login ...

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Cambiar credenciales
//                email = "juan@gmail.com";
//                pass = "123456";
//                uid = "ehkWuIWJmSgGhf3OgpAX1F3N0JJ3";

                email = String.valueOf(eTemail.getText());
                pass = String.valueOf(eTpass.getText());


                if (!(email.length()<1)&&!(pass.length()<1)) {

                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Log.d("LOGIN", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message_item to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("LOGIN", "signInWithEmail", task.getException());
                                Toast.makeText(LoginActivity.this, "Error de Login, compruebe sus credenciales",
                                        Toast.LENGTH_LONG).show();
                            } else {

                                //si nos logeamos con éxito
                                startActivity(intentLog);
                                //finish();

                            }

                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this, "Error de Login, No deje campos vacios",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
