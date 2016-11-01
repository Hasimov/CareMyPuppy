package com.carepuppy.pirtu.caremypuppy;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carepuppy.pirtu.caremypuppy.Interfaces.OnListFragmentInteractionListenerChatActivity;
import com.carepuppy.pirtu.caremypuppy.Models.MessageChatModel;
import com.carepuppy.pirtu.caremypuppy.Utiles.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class ChatActivity extends AppCompatActivity {

    Button btn_chat_send;
    EditText eTChat_textsending;
    String firstName, avatarId, mRecipientUid, mCurrentUserName,mCurrentUserUid;
    ActionBar actionbar;

    public static final String MESSAGES_CHILD = "chat_rooms";
    /*Los id de los chats están generados al sumar las claves de cada user, de este modo es fácil
    saber en que chat room están hablando*/
    public static String MESSAGES_CHILD_ITEM;

    private RecyclerView rVChatActivity;
    private OnListFragmentInteractionListenerChatActivity mListenerRecyclerChatActivity;
    private LinearLayoutManager mLinearLayoutManager;
    // Firebase variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    // Firebase db variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<MessageChatModel, MessageViewHolder> mFirebaseAdapter;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView iVuserAvatar;
        TextView tVmessageRecipient,tVmessageSender;
        //tomo un elemento, a un sólo cuidador
        public MessageChatModel mItem;

        public MessageViewHolder(View v) {
            super(v);
//          iVuserAvatar = (ImageView) itemView.findViewById(R.id.iVcareAvatar);
            tVmessageRecipient = (TextView) itemView.findViewById(R.id.recipientMessage);
            tVmessageSender = (TextView) itemView.findViewById(R.id.senderMessage);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //para ir hacia atrás en la pantalla
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //TODO como puedo volver a la página de lista de chats , no al main ppal
        eTChat_textsending = (EditText) findViewById(R.id.eTChat_textsending);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        rVChatActivity = (RecyclerView) findViewById(R.id.rv_chat); //cardList
        mLinearLayoutManager = new LinearLayoutManager(this);
        rVChatActivity.setLayoutManager(mLinearLayoutManager);

        //traigo datos del intent
        firstName =  getIntent().getExtras().getString("firstName");
        avatarId = getIntent().getExtras().getString("avatarId");
        mRecipientUid = getIntent().getExtras().getString("mRecipientUid");
            /*Current user (or sender) info*/
        mCurrentUserName = mFirebaseAuth.getCurrentUser().getDisplayName();
        mCurrentUserUid = mFirebaseAuth.getCurrentUser().getUid();

        /*Genero la key del chatroom*/
        MESSAGES_CHILD_ITEM = Utils.generateChatRoomsKey(mCurrentUserUid, mRecipientUid);
        Log.d("MESSAGE_CHIL_ITEM",MESSAGES_CHILD_ITEM);

        /*cambio el título de la bar*/
        setTitle(firstName);


        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<MessageChatModel, MessageViewHolder>(MessageChatModel.class,
                R.layout.message_item,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(MESSAGES_CHILD_ITEM)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, MessageChatModel messageModel, int position) {

                //decido donde pongo los mensajes

                if(messageModel.getSender().equals(mCurrentUserUid)){

                    viewHolder.tVmessageSender.setText(messageModel.getMessage());
                    viewHolder.tVmessageRecipient.setVisibility(View.GONE);

                }else{
                    viewHolder.tVmessageRecipient.setText(messageModel.getMessage());
                    viewHolder.tVmessageSender.setVisibility(View.GONE);

                }

                //TODO quitar
                Log.d("MENS",messageModel.getMessage());

                btn_chat_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    sendingMessage();
                    }
                });

            }
        };
        //método que nos actualiza mágicamente la lista
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int carerCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message_item.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (carerCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rVChatActivity.scrollToPosition(positionStart);
                }
            }
        });
        //inicio los recycler
        rVChatActivity.setLayoutManager(mLinearLayoutManager);
        rVChatActivity.setAdapter(mFirebaseAdapter);



    }//final onCreate


    public void sendingMessage(){

        String message = eTChat_textsending.getText().toString().trim();

        if (!message.equals("")){
            MessageChatModel messageSending = new MessageChatModel(mRecipientUid,mCurrentUserUid,message);
            Log.d("MENSAJE", messageSending.toString());

            mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(MESSAGES_CHILD_ITEM).push().setValue(messageSending);
            eTChat_textsending.setText("");

        }else{
            Toast.makeText(getBaseContext(),"Escriba un mensaje",
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        //ChatActivity.super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        startActivity(intent);
//        finish();
//        onBackPressed();
    }
}//final class
