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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    TextView tVregRegistro;
    EditText eTname, eTregemail,eTregPass1,eTregPass2;
    String name, email, password1,password2;
    Button btnRegWuau;
    private FirebaseAuth mAuth;
    private FirebaseUser newuser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Intent backToLoginIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tVregRegistro = (TextView) findViewById(R.id.tVregRegistro);
        btnRegWuau = (Button) findViewById(R.id.btnReg);
        eTname = (EditText) findViewById(R.id.eTregNombre);
        eTregemail = (EditText) findViewById(R.id.eTregemail);
        eTregPass1 = (EditText) findViewById(R.id.eTregPass1);
        eTregPass2 = (EditText) findViewById(R.id.eTregPass2);
        backToLoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);

        //cambiar a una fuente custom
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Lobster.otf");
        tVregRegistro.setTypeface(custom_font);

        //Registro con Firebase
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("SIGN", "onAuthStateChanged:signed_in:");
                } else {
                    // User is signed out
                    Log.d("SIGN", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        //Registro para un usuario desde la pantalla Registro

        //Rescato los valores de los TextView

        btnRegWuau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = String.valueOf(eTregemail.getText());
                password1 = String.valueOf(eTregPass1.getText());
                password2 = String.valueOf(eTregPass2.getText());

                Log.d("TAG", "datos: " + email + " : " + password1 + " : "+password2);

                //comprobaciones,
                //que la contraseña tiene 6 o más caractéres
                if (password1.length()>=6&&password2.length()>=6){

                    //que las contraseñas coinciden
                    if (password1.equals(password2)) {

                     /*Cuando un usuario inicia sesión se pasa la dirección de correo electrónico y
                     la contraseña del usuario asignInWithEmailAndPassword:*/
                        mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Log.d("REG", "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message_item to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w("REG", "signInWithEmail", task.getException());
                                    Toast.makeText(RegisterActivity.this, "El registro ha fallado, vuelva a intentarlo más tarde",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registrado con éxito",
                                            Toast.LENGTH_SHORT).show();
                                    //ahora devuelvo a la pantalla de login
                                    //Actualizo los campos del usuario, antes tengo que logearme con el nuevo usuario para poder completarlo
                                    mAuth.signInWithEmailAndPassword(email, password1).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            newuser = mAuth.getCurrentUser();

                                            Log.d("LOGIN", "signInWithEmail:onComplete:" + task.isSuccessful());

                                            // If sign in fails, display a message_item to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Log.w("LOGIN", "signInWithEmail", task.getException());

                                            } else {
                                                //si nos logeamos con éxito
                                                //Actualizo el nombre
                                                name = String.valueOf(eTname.getText());

                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name)
                                                        //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                                        .build();
                                                newuser.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d("USER", "User profile updated.");
                                                                    Log.d("USER",name+" "+ newuser.getDisplayName()+" "+newuser.getUid()+" "+newuser.getEmail());
                                                                    //nos deslogueamos
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    startActivity(backToLoginIntent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }

                                        }
                                    });//actulizar campos
                                }

                            }
                        });
                    }else {
                        Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(RegisterActivity.this, "Las contraseñas deben tener más de 6 caracteres", Toast.LENGTH_SHORT).show();
                }

            }
        });


    } // ... onCreate

    @Override
    public void onStart() {
        super.onStart();
        //Para que no se quede la sesion anterior abierta
        mAuth.removeAuthStateListener(mAuthListener);
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
