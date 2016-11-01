package com.carepuppy.pirtu.caremypuppy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.carepuppy.pirtu.caremypuppy.Models.UsersChatModel;
import com.carepuppy.pirtu.caremypuppy.Utiles.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DetalleCuidador extends AppCompatActivity implements OnListFragmentClickListenerComment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView iVdetAvatar;
    TextView tVdet_name, tVdet_telefono, tVdet_direccion, tVdet_descContent, tVdet_priceD, tVdet_priceN;
    ComentariosFragment comentariosFragment = null;
    Intent intentChat;
    String id_Carer, id_CurrentUser;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RatingBar rBdet_avgCarer;
    Context context;
    static Carer itemCarer = new Carer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cuidador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicio los componentes
        context = getApplicationContext();
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        iVdetAvatar = (ImageView) findViewById(R.id.iVdet_Avatar);
        tVdet_name = (TextView) findViewById(R.id.tVdet_name);
        tVdet_telefono = (TextView) findViewById(R.id.tVdet_telefono);
        tVdet_direccion = (TextView) findViewById(R.id.tVdet_direccion);
        tVdet_descContent = (TextView) findViewById(R.id.tVdet_descContent);
        tVdet_priceD = (TextView) findViewById(R.id.tVdet_priceD);
        tVdet_priceN = (TextView) findViewById(R.id.tVdet_priceN);
        rBdet_avgCarer = (RatingBar) findViewById(R.id.rBdet_avgCarer);

        //tomo el valor del id desde el intent
        id_Carer = getIntent().getExtras().getString("id_Carer");
        id_CurrentUser = Utils.getCurrent_userId();
        Log.d("ids", id_Carer + " " + id_CurrentUser); //TODO quitar

        //inicio el fragmet por defecto
        comentariosFragment = new ComentariosFragment();
        transicionPagina(comentariosFragment);


        //cambio de imagen del colapsing bar TODO: aqui tendré que pasar el putextra de la urlimg
        //Asyntack para el cambio de foto del local
        String imgUrl = "https://cdnb3.artstation.com/p/assets/images/images/002/240/323/large" +
                "/tran-bich-phuong-dog.jpg?1459181802";
        new DownloadImageTask(collapsingToolbarLayout)
                .execute(imgUrl);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                /*Acción ir a ActivityChat*/
                //1.Compruebo que el chat aún no existe
                final String keyChatRoom = Utils.generateChatRoomsKey(id_CurrentUser, id_Carer);
                Log.d("KEYCHAT generado", keyChatRoom);

                DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
                        .getReference("chat_rooms_info").child(id_CurrentUser);

                mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean keyExist = false;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            UsersChatModel usersChatModel = postSnapshot.getValue(UsersChatModel.class);
                            String keyItemChat = postSnapshot.getKey();
                            if (keyChatRoom.equals(keyItemChat)) {
                                Log.d("KEYCHAT son iguales", keyItemChat);
                                keyExist = true;
                            } else {
                                Log.d("KEYCHAT no son iguales", keyItemChat);

                            }
                        }

                        //aquí lanzo el intent si existe el chat o si no lo creo y lo lanzo
                        Intent intentDetalleToChat = new Intent(DetalleCuidador.this, ChatActivity.class);
                        String firstName = itemCarer.getName();
                        String avatarId = itemCarer.getUrlImg();
                        String mRecipientUid = id_Carer;
                            /*Intents*/
                        intentDetalleToChat.putExtra("firstName", firstName);
                        intentDetalleToChat.putExtra("avatarId", avatarId);
                        intentDetalleToChat.putExtra("mRecipientUid", mRecipientUid);

                        if (keyExist) {
                            /*Si existe el chat lanzo directamente el intent*/
                            startActivity(intentDetalleToChat);

                        } else {
                            /*Si no existe el chat lo creo y luego  lanzo  el intent*/
                            UsersChatModel usersChatModel = new UsersChatModel();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String currentName = user.getDisplayName();
                            String unixTime = String.valueOf(System.currentTimeMillis() / 1000L);

                            usersChatModel.setAvatarId(itemCarer.getUrlImg());//TODO: URL avatar
                            usersChatModel.setCreatedAt(unixTime);
                            usersChatModel.setFirstName(itemCarer.getName());
                            usersChatModel.setmCurrentUserName(currentName);
                            usersChatModel.setmCurrentUserCreatedAt(unixTime);
                            usersChatModel.setmRecipientUid(id_Carer);
                            usersChatModel.setmCurrentUserUid(id_CurrentUser);
                            Utils.generateIdChatMetadata(usersChatModel);

                            /*Lanzo el nuevo intent*/
                            startActivity(intentDetalleToChat);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

        });

        //Obtengo los datos del Carer a partir del id
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("carer");

        myRef.child(id_Carer).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                itemCarer = dataSnapshot.getValue(Carer.class);

                //Seteo los campos de la vista
//                iVdetAvatar = (ImageView) findViewById(R.id.iVcareAvatar);
                tVdet_name.setText(itemCarer.getName() + " " + itemCarer.getSurname());
                tVdet_direccion.setText(itemCarer.getAdress() + " , " + itemCarer.getCity() + " , " + itemCarer.getCp());
                tVdet_descContent.setText(itemCarer.getDescription());
                tVdet_priceD.setText(String.valueOf(itemCarer.getPriceD() + " €"));
                tVdet_priceN.setText(String.valueOf(itemCarer.getPriceN() + " €"));
                rBdet_avgCarer.setRating(itemCarer.getStars());
                tVdet_telefono.setText(String.valueOf(itemCarer.getPhone()));
                if(itemCarer.getUrlImg().length()<1){
                    iVdetAvatar.setImageResource(R.drawable.avatar1);
                }else{
                    //añado fotos con Picasso
                    Picasso.with(context)
                            .load(itemCarer.getUrlImg())
                            .placeholder(R.drawable.avatar1)
                            .error(R.drawable.avatar1)//con la url que traigo de la consulta a la API
                            .into(iVdetAvatar);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "getUser:onCancelled", databaseError.toException());
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
                .replace(R.id.fragmentComent, fragment).commit();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetalleCuidador.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
