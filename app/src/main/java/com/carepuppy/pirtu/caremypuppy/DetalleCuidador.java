package com.carepuppy.pirtu.caremypuppy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.carepuppy.pirtu.caremypuppy.Fragments.ComentariosFragment;
import com.carepuppy.pirtu.caremypuppy.Interfaces.OnListFragmentClickListenerComment;
import com.carepuppy.pirtu.caremypuppy.Models.Carer;
import com.carepuppy.pirtu.caremypuppy.Models.Comentario;
import com.carepuppy.pirtu.caremypuppy.Utiles.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DetalleCuidador extends AppCompatActivity implements OnListFragmentClickListenerComment{

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView iVdetAvatar;
    TextView tVdet_name,tVdet_telefono,tVdet_direccion,tVdet_descContent,tVdet_priceD,tVdet_priceN;
    ComentariosFragment comentariosFragment = null;
    ImageButton iBdet_chat;
    Intent intentChat;
    String id_Carer, id_CurrentUser;
    FirebaseAuth auth;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    RatingBar rBdet_avgCarer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cuidador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicio los componentes
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        iVdetAvatar = (ImageView) findViewById(R.id.iVcareAvatar);
        tVdet_name = (TextView) findViewById(R.id.tVdet_name);
        tVdet_telefono = (TextView) findViewById(R.id.tVdet_telefono);
        tVdet_direccion = (TextView) findViewById(R.id.tVdet_direccion);
        tVdet_descContent = (TextView) findViewById(R.id.tVdet_descContent);
        tVdet_priceD = (TextView) findViewById(R.id.tVdet_priceD);
        tVdet_priceN = (TextView) findViewById(R.id.tVdet_priceN);
        tVdet_telefono = (TextView) findViewById(R.id.tVdet_telefono);
        iBdet_chat = (ImageButton) findViewById(R.id.btn_det_chat);
        rBdet_avgCarer = (RatingBar) findViewById(R.id.rBdet_avgCarer);

//        //tomo el valor del id desde el intent

        id_Carer = getIntent().getExtras().getString("id_Carer");
        id_CurrentUser = Utils.getCurrent_userId();
         Log.d("ids", id_Carer+" "+id_CurrentUser); //TODO quitar

        //inicio el fragmet por defecto
        comentariosFragment= new ComentariosFragment();
        transicionPagina(comentariosFragment);


        //cambio de imagen del colapsing bar TODO: aqui tendré que pasar el putextra de la urlimg
        //Asyntack para el cambio de foto del local
        String imgUrl = "https://cdnb3.artstation.com/p/assets/images/images/002/240/323/large/tran-bich-phuong-dog.jpg?1459181802";
        new DownloadImageTask(collapsingToolbarLayout)
                .execute(imgUrl);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    //Actualizo los campos de la vista
    database = FirebaseDatabase.getInstance();
    myRef = database.getReference("carer");


        myRef.child(id_Carer).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Carer itemCarer = dataSnapshot.getValue(Carer.class);
                Log.d("CARER",itemCarer.toString());

                //Seteo los campos de la vista
//                iVdetAvatar = (ImageView) findViewById(R.id.iVcareAvatar);
                tVdet_name.setText(itemCarer.getName()+" "+itemCarer.getSurname());
//                tVdet_telefono.setText(itemCarer.get);
                tVdet_direccion.setText(itemCarer.getAdress()+" , "+itemCarer.getCity()+" , "+ itemCarer.getCp());
                tVdet_descContent.setText(itemCarer.getDescription());
                tVdet_priceD.setText(String.valueOf(itemCarer.getPriceD()+ " €"));
                tVdet_priceN.setText(String.valueOf(itemCarer.getPriceN()+ " €"));
                rBdet_avgCarer.setRating(itemCarer.getStars());
                tVdet_telefono.setText(String.valueOf(itemCarer.getPhone()));



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "getUser:onCancelled", databaseError.toException());
            }
        });

//Pantalla de chat desde el boton
        iBdet_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intentDetToChat = new Intent(DetalleCuidador.this,ChatActivity.class);
//                String firstName =  item.getFirstName();
//                String avatarId = item.getAvatarId();
//                String mRecipientUid = item.getmRecipientUid();
//                /*Current user (or sender) info*/
//                String mCurrentUserName = item.getmCurrentUserName();
//                String mCurrentUserUid = item.getmCurrentUserUid();
//                /*Intents*/
//                intentChatRoomToChat.putExtra("firstName",firstName);
//                intentChatRoomToChat.putExtra("avatarId",avatarId);
//                intentChatRoomToChat.putExtra("mRecipientUid",mRecipientUid);
//
//
//                startActivity(intentChatRoomToChat);
//                //creo el intent cuando hago click sobre un cadview
//                intentChat = new Intent(DetalleCuidador.this,ChatActivity.class);
//                //TODO: pasar los extras para el siguiente activity
//                startActivity(intentChat);


            }
        });


    }//fin onCreate
    @Override
    public void onListFragmentInteraction(Comentario item) {

    }

    //AsyncTask que controla el cambio de foto del local
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CollapsingToolbarLayout collapsingToolbarLayout;

        public DownloadImageTask(CollapsingToolbarLayout collapsingToolbarLayout) {
            this.collapsingToolbarLayout = collapsingToolbarLayout;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];

            Bitmap result = null;
            try {
                result = Picasso.with(getApplicationContext()).load(urldisplay).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
            //Recojo la foto buscada por la url y la transformo a tipo Bipmap para poder setearla
            BitmapDrawable ob = new BitmapDrawable(getResources(), result);
            collapsingToolbarLayout.setBackground(ob);

        }



    }

    //para cambiar los fragments
    public void transicionPagina(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentComent,fragment).commit();
    }
}
