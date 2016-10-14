package com.carepuppy.pirtu.caremypuppy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carepuppy.pirtu.caremypuppy.ChatActivity;
import com.carepuppy.pirtu.caremypuppy.Interfaces.OnListFragmentInteractionListenerChatRooms;
import com.carepuppy.pirtu.caremypuppy.Models.UsersChatModel;
import com.carepuppy.pirtu.caremypuppy.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ChatsFragment extends Fragment {

    public static final String MESSAGES_CHILD = "chat_rooms_info";
    private RecyclerView rVChat_rooms;
    private LinearLayoutManager mLinearLayoutManagerChatRooms;
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    String current_userId;
    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<UsersChatModel, ChatRoomsViewHolder> mFirebaseAdapter;


    public static class ChatRoomsViewHolder extends RecyclerView.ViewHolder {
        ImageView iVuserAvatar;
        TextView tVuserName,tVlastMessageDate;

        //tomo un elemento, a un sólo cuidador
        public UsersChatModel mItem;

        public ChatRoomsViewHolder(View itemView) {
            super(itemView);
            iVuserAvatar = (ImageView) itemView.findViewById(R.id.iVavatar_chat_rooms);
            tVuserName = (TextView) itemView.findViewById(R.id.tVusername_chat_rooms);
            tVlastMessageDate = (TextView) itemView.findViewById(R.id.tVfecha_chat_rooms);

        }

    }
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListenerChatRooms mListenerRecyclerChatRooms;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_chat_rooms_list, container, false);

        // Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        current_userId = mFirebaseAuth.getCurrentUser().getUid();//id del usuario actual

        //Creo el RecyclerView del Chat Rooms
        rVChat_rooms = (RecyclerView) view.findViewById(R.id.rVchatRooms_list);
        mLinearLayoutManagerChatRooms = new LinearLayoutManager(getContext());
        rVChat_rooms.setLayoutManager(mLinearLayoutManagerChatRooms);
        Log.d("USERID", current_userId);

        // Conexión con Firebase
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<UsersChatModel, ChatRoomsViewHolder>(UsersChatModel.class,
                R.layout.fragment_chat_rooms,
                ChatRoomsViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(current_userId)) {
            @Override
            protected void populateViewHolder(final ChatRoomsViewHolder viewHolderChatRooms, final UsersChatModel userChatModel, int position) {

                //Seteo las vistas para cada item leido en Firebase
                viewHolderChatRooms.tVuserName.setText(userChatModel.getFirstName());
                viewHolderChatRooms.tVlastMessageDate.setText(userChatModel.getCreatedAt());
                viewHolderChatRooms.iVuserAvatar.setImageResource(R.drawable.avatar1);
                Log.d("CHATMODEL",userChatModel.toString());

                //Elemento click para el cambio de vista
                viewHolderChatRooms.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Recupero el id de carer que cliceo y lo paso a la interface

                        int position = viewHolderChatRooms.getAdapterPosition();
                        DatabaseReference dbKey = mFirebaseAdapter.getRef(position);
                        mListenerRecyclerChatRooms.onListFragmentInteraction(userChatModel);

                    }
                });


            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManagerChatRooms.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message_item.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rVChat_rooms.scrollToPosition(positionStart);
                }
            }
        });

        //inicio los recycler
        rVChat_rooms.setLayoutManager(mLinearLayoutManagerChatRooms);
        rVChat_rooms.setAdapter(mFirebaseAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListenerChatRooms) {
            mListenerRecyclerChatRooms = (OnListFragmentInteractionListenerChatRooms) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListenerChatRooms");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerRecyclerChatRooms = null;
    }

}

