package com.carepuppy.pirtu.caremypuppy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.carepuppy.pirtu.caremypuppy.Interfaces.OnListFragmentClickListenerCarer;
import com.carepuppy.pirtu.caremypuppy.Models.Carer;
import com.carepuppy.pirtu.caremypuppy.R;
import com.carepuppy.pirtu.caremypuppy.Utiles.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ItemFragmentCarer extends Fragment{

    public static final String MESSAGES_CHILD = "carer";
    private RecyclerView recyclerView;
    private OnListFragmentClickListenerCarer mListenerRecyclerCarer;
    EditText editTextSearch;
    private LinearLayoutManager mLinearLayoutManager;
    // Firebase variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    // Firebase db variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Carer, CarerViewHolder> mFirebaseAdapter;


    public static class CarerViewHolder extends RecyclerView.ViewHolder {
        ImageView iVcareAvatar;
        TextView tVcareCity,tVcareAdress,tVcareName,tVcareSurname;
        RatingBar rBList_Carer;
        //tomo un elemento, a un sólo cuidador
        public Carer mItem;

        public CarerViewHolder(View v) {
            super(v);
            iVcareAvatar = (ImageView) itemView.findViewById(R.id.iVcareAvatar);
            tVcareCity = (TextView) itemView.findViewById(R.id.tVcareCity);
            tVcareAdress = (TextView) itemView.findViewById(R.id.tVcareAdress);
            tVcareName = (TextView) itemView.findViewById(R.id.tVcareName);
            rBList_Carer = (RatingBar) itemView.findViewById(R.id.rBList_Carer);

        }

    }

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragmentCarer() {
    }

    @SuppressWarnings("unused")
    public static ItemFragmentCarer newInstance(int columnCount) {
        ItemFragmentCarer fragment = new ItemFragmentCarer();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_carer_list, container, false);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewContent); //cardList
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);

        //Boton de búsqueda
        editTextSearch = (EditText) view.findViewById(R.id.eTlocal);
        editTextSearch.setSelected(false);

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Carer, CarerViewHolder>(Carer.class,
                R.layout.fragment_item_carer,
                CarerViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {
            @Override
            protected void populateViewHolder(final CarerViewHolder viewHolder, final Carer carer, int position) {

                // mProgressBar.setVisibility(ProgressBar.INVISIBLE);


                viewHolder.tVcareName.setText(carer.getName()+" "+carer.getSurname());
                viewHolder.tVcareCity.setText(carer.getCity());
                viewHolder.tVcareAdress.setText(carer.getAdress()+" , " +String.valueOf(carer.getCp()));
                viewHolder.rBList_Carer.setRating(carer.getStars());

                //para obtener la key
//                DatabaseReference dbKey = mFirebaseAdapter.getRef(position);
//                String itemKey = dbKey.getKey();
//                Log.d("KEY","position: "+String.valueOf(position)+" - Key: "+itemKey);
                Utils.getCarerKey(mFirebaseAdapter,position);
//                Log.d("Carer",carer.toString());

                if(carer.getUrlImg().length()<1){
                    viewHolder.iVcareAvatar.setImageResource(R.drawable.avatar1);
                }else{
                    //añado fotos con Picasso
                    Picasso.with(getContext())
                            .load(carer.getUrlImg())
                            .transform(new CropCircleTransformation())
                            .placeholder(R.drawable.avatar1)
                            .error(R.drawable.avatar1)//con la url que traigo de la consulta a la API
                            .into(viewHolder.iVcareAvatar);
                }


                //Para cuando hacemos click sobre un elemento de la lista
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListenerRecyclerCarer) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an    item has been selected.

                            //Recupero el id de carer que cliceo y lo paso a la interface
                            Log.d("PRUEBA",String.valueOf(viewHolder.getAdapterPosition()));

                            int position = viewHolder.getAdapterPosition();
                            DatabaseReference dbKey = mFirebaseAdapter.getRef(position);
                            String itemKey = dbKey.getKey();

                            //Paso el id del Carer a la siguiente vista
                            mListenerRecyclerCarer.onListFragmentInteraction(carer, itemKey);


                        }
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
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
        //inicio los recycler
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mFirebaseAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentClickListenerCarer) {
            mListenerRecyclerCarer = (OnListFragmentClickListenerCarer) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerRecyclerCarer = null;
    }

        //TODO: para el tema de filtrado
//        List<Carer> filtrados = new ArrayList<Carer>();
//
//        for (Carer c: items) {
//            if(c.getName().equals("..."))
//                filtrados.add(c);
//        }
//
//        myItemRecyclerViewAdapter = new CarerItemRVAdapter(filtrados,mListenerRecyclerCarer);

    }


