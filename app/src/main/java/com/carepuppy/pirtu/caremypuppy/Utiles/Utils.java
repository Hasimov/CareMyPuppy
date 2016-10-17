package com.carepuppy.pirtu.caremypuppy.Utiles;

import android.util.Log;

import com.carepuppy.pirtu.caremypuppy.Fragments.ItemFragmentCarer;
import com.carepuppy.pirtu.caremypuppy.Models.Carer;
import com.carepuppy.pirtu.caremypuppy.Models.MessageChatModel;
import com.carepuppy.pirtu.caremypuppy.Models.UsersChatModel;
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

    public static boolean keyExist = false;

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
}
