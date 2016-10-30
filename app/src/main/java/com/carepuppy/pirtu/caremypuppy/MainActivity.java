package com.carepuppy.pirtu.caremypuppy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.carepuppy.pirtu.caremypuppy.Fragments.ChatsFragment;
import com.carepuppy.pirtu.caremypuppy.Fragments.ItemFragmentCarer;
import com.carepuppy.pirtu.caremypuppy.Interfaces.OnListFragmentClickListenerCarer;
import com.carepuppy.pirtu.caremypuppy.Interfaces.OnListFragmentInteractionListenerChatRooms;
import com.carepuppy.pirtu.caremypuppy.Models.Carer;
import com.carepuppy.pirtu.caremypuppy.Models.MessageChatModel;
import com.carepuppy.pirtu.caremypuppy.Models.UsersChatModel;
import com.carepuppy.pirtu.caremypuppy.Utiles.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnListFragmentClickListenerCarer, OnListFragmentInteractionListenerChatRooms {

    ItemFragmentCarer itemFragmentCarer =null;
    Intent intentDellateCuidador, intentChatRoom, intentLogin= null;
    ChatsFragment chatsFragment = null;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String name, mail;
    TextView tVnavuser,tVnavEmail;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //componentes
        tVnavEmail  = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tVnavEmail);
        tVnavuser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tVnavuser);
//        imgMenu = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageMenu);


        //inicio el fragmet por defecto
        itemFragmentCarer = new ItemFragmentCarer();
        chatsFragment = new ChatsFragment();
        transicionPagina(itemFragmentCarer);


//Registro con Firebase
        mAuth = FirebaseAuth.getInstance();

 //       Log.d("USER",mAuth.getCurrentUser().getDisplayName);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                    intentLogin = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
                // ...
            }
        };
        // ...





        //cambio user y mail
        userInfo();

    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            if (itemFragmentCarer !=null){
                transicionPagina(itemFragmentCarer);
            }
        } else if (id == R.id.nav_message) {
            if (chatsFragment !=null){
                transicionPagina(chatsFragment);
            }

        } else if (id == R.id.nav_logout) {

            //nos deslogeamos
            FirebaseAuth.getInstance().signOut();




        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_my_puppies) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //para cambiar los fragments
    public void transicionPagina(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayoutContentMain,fragment).commit();
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

    public void userInfo(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            mail = user.getEmail();

            tVnavEmail.setText(mail);
            tVnavuser.setText(name);
            Log.d("MAIN",name+" : "+mail);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();


        }
    }

    @Override
    public void onListFragmentInteraction(UsersChatModel item) {

        Intent intentChatRoomToChat = new Intent(MainActivity.this, ChatActivity.class);
        String firstName =  item.getFirstName();
        String avatarId = item.getAvatarId();
        String mRecipientUid = item.getmRecipientUid();
        /*Intents*/
        intentChatRoomToChat.putExtra("firstName",firstName);
        intentChatRoomToChat.putExtra("avatarId",avatarId);
        intentChatRoomToChat.putExtra("mRecipientUid",mRecipientUid);
        startActivity(intentChatRoomToChat);

    }
    @Override
    public void onListFragmentInteraction(Carer item, String id_carer) {
        //lanzar el intent para abrir pantalla

        intentDellateCuidador = new Intent(MainActivity.this,DetalleCuidador.class);
        intentDellateCuidador.putExtra("id_Carer", id_carer);
//
//        //TODO: pasar los extras para el siguiente activity, este item viene vacio
//
        startActivity(intentDellateCuidador);

    }
}
