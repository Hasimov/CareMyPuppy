package com.carepuppy.pirtu.caremypuppy.Utiles;

import android.util.Log;
import android.widget.Toast;

import com.carepuppy.pirtu.caremypuppy.Fragments.ItemFragmentCarer;
import com.carepuppy.pirtu.caremypuppy.Models.Carer;
import com.carepuppy.pirtu.caremypuppy.Models.MessageChatModel;
import com.carepuppy.pirtu.caremypuppy.Models.UsersChatModel;
import com.carepuppy.pirtu.caremypuppy.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pirtu on 30/09/2016.
 */
public class Utils {

    public static boolean keyExist;

    /**
     * Tomando como referencia la instancia de Firebase rescata el id del usuario según la posición
     * en el listview
     * @param mFirebaseAdapter
     * @param position
     * @return id del usuario
     */
    public static String getCarerKey (FirebaseRecyclerAdapter<Carer, ItemFragmentCarer.CarerViewHolder> mFirebaseAdapter, int position){

        DatabaseReference dbKey = mFirebaseAdapter.getRef(position);
        String itemKey = dbKey.getKey();
        Log.d("KEY","position: "+String.valueOf(position)+" - Key: "+itemKey);
        return itemKey;
    }

    public static String generateChatRoomsKey(String recipient_id, String sender_id){

        String key;
        if(recipient_id.compareTo(sender_id)>0){
            key = recipient_id+"-"+sender_id;
            Log.d("ChatKey", "recipient mayor que sender "+key);
        }else{
            key = sender_id+"-"+recipient_id;
            Log.d("ChatKey", "sender mayor que recipient "+key);
        }
        return key;
    }

    //obtener id del usuario actual
    public static String getCurrent_userId(){

        String current_userId="";
        // Firebase instance variables
        FirebaseAuth mFirebaseAuth;
        FirebaseUser mFirebaseUser;
        // Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        current_userId = mFirebaseAuth.getCurrentUser().getUid();//id del usuario actual

        return current_userId;
    }

    //obtener info del user actual
    public static FirebaseUser getCurrentUserInfo(){


        // Firebase instance variables
        FirebaseAuth mFirebaseAuth;
        FirebaseUser mFirebaseUser;
        // Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        return mFirebaseUser;
    }

    //comprobar que existe el id_chat_room, si existe me devuelve un true
    public static boolean idChatRoomChecker(String idCurrentUser, String idRecipient){


        final String keyChatRoom = generateChatRoomsKey(idCurrentUser,idRecipient);
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
                .getReference("chat_rooms_info").child(idCurrentUser);

       mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                   UsersChatModel usersChatModel = postSnapshot.getValue(UsersChatModel.class);
                   String keyItemChat = postSnapshot.getKey();

                   if (keyChatRoom.equals(keyItemChat)){
                       Log.d("KEYCHAT son iguales",keyItemChat);
                       keyExist = true;
                   }else{
                       Log.d("KEYCHAT no son iguales",keyItemChat);
                   }
               }
           }
           @Override
           public void onCancelled(DatabaseError databaseError) {
           }
       });

        /*if (keyExist){
            Log.d("KEY", "is true");
        }
        else {
            Log.d("KEY", "is false");

        }*/
        return keyExist;

    }//fin idChatChecker

    /*Genera a partir de los ids el ChatRoom y ChatRoomInfo*/
    public static void generateIdChatMetadata(UsersChatModel userChatModel){

        //TODO
        String welcomeMessageText = "Gracias por usar el Chat de Care my Puppy, comience a hablar con" +
                " el cuidador cuando quiera!! wuau!!";
        MessageChatModel welcomeMessage = new MessageChatModel();
        welcomeMessage.setMessage(welcomeMessageText);
        welcomeMessage.setRecipient(userChatModel.getmCurrentUserUid());
        welcomeMessage.setSender(userChatModel.getmRecipientUid());

        DatabaseReference mFirebaseDatabaseReference;
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        /*si no existe el chat room se pasa a crearlo con este método*/
        String idNewChatRoom = generateChatRoomsKey(userChatModel.getmRecipientUid(),userChatModel.getmCurrentUserUid());

        //introducimos los datos de cabecera del chatRoom nuevo. hay que generar el chat tanto en la raiz de uno como del otro user
        mFirebaseDatabaseReference.child("chat_rooms_info").child(userChatModel.getmCurrentUserUid()).child(idNewChatRoom).setValue(userChatModel);
        //cambio los roles para el chatRoom del usuario actual

        //Genero el chatRoom Info para el chat del carer, cambio los roles
        UsersChatModel chatModelCarer = new UsersChatModel();
        chatModelCarer.setmCurrentUserUid(userChatModel.getmRecipientUid());
        chatModelCarer.setmRecipientUid(userChatModel.getmCurrentUserUid());
        chatModelCarer.setFirstName(getCurrentUserInfo().getDisplayName());
        chatModelCarer.setmCurrentUserName(userChatModel.getFirstName());
        chatModelCarer.setAvatarId("http://carerAvatar");//TODO: incluir foto de user
        chatModelCarer.setCreatedAt(userChatModel.getCreatedAt());
        chatModelCarer.setmCurrentUserCreatedAt("");





        Log.d("KEYCHAT_user",userChatModel.toString());
        Log.d("KEYCHAT_carer",chatModelCarer.toString());
        /*el chat Info para el carer*/
        mFirebaseDatabaseReference.child("chat_rooms_info").child(chatModelCarer.getmCurrentUserUid()).child(idNewChatRoom).setValue(chatModelCarer);

        //creamos el chatRoom con mensaje de bienvenida
        mFirebaseDatabaseReference.child("chat_rooms").child(idNewChatRoom).push().setValue(welcomeMessage);




    }
}
