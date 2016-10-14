package com.carepuppy.pirtu.caremypuppy.Utiles;

import android.util.Log;

import com.carepuppy.pirtu.caremypuppy.Fragments.ItemFragmentCarer;
import com.carepuppy.pirtu.caremypuppy.Models.Carer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by pirtu on 30/09/2016.
 */
public class Utils {

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
}
