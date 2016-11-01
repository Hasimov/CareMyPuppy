package com.carepuppy.pirtu.caremypuppy.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.carepuppy.pirtu.caremypuppy.Adapters.ComentariosRVAdapter;
import com.carepuppy.pirtu.caremypuppy.Interfaces.OnListFragmentClickListenerComment;
import com.carepuppy.pirtu.caremypuppy.Models.Comentario;
import com.carepuppy.pirtu.caremypuppy.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ComentariosFragment extends Fragment {

    private int mColumnCount = 1;

    private RecyclerView recyclerViewComment;
    private RecyclerView.Adapter myItemRecyclerViewAdapterComment;
    private RecyclerView.LayoutManager myManager;
    private List<Comentario> items;
    private OnListFragmentClickListenerComment mListenerRecyclerComment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ComentariosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comentarios_list, container, false);


        //creo el recyclerView
        recyclerViewComment = (RecyclerView) view.findViewById(R.id.recyclerViewContentComment);//cardList
        recyclerViewComment.setHasFixedSize(true);
        myManager = new LinearLayoutManager(getActivity());
        recyclerViewComment.setLayoutManager(myManager);
        //para que no inicia la vista en el listview
        recyclerViewComment.setFocusable(false);

        //añado a la lista TODO quitar después
        addList();


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentClickListenerComment) {
            mListenerRecyclerComment = (OnListFragmentClickListenerComment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerRecyclerComment = null;
    }

    //relleno la lista con los cuidadores ejemplo, Aqui tengo que añadir la consulta a la API
    public void addList() {

        items = new ArrayList<Comentario>();
        items.add(new Comentario("https://upload.wikimedia.org/wikipedia/commons/0/0a/Paw_(Animal_Rights_symbol).png", "Paco Sánchez Pérez", "me encantó la experiencia volvería a repetir", 3.5,"02/04/2016"));
        items.add(new Comentario("http://www.iconshock.com/img_vista/FLAT/communications/jpg/dog_icon.jpg", "Marta Amapola Martín", "me encantó la experiencia volvería a repetir", 3.5, "02/04/2016"));
        items.add(new Comentario("https://upload.wikimedia.org/wikipedia/commons/0/0a/Paw_(Animal_Rights_symbol).png", "Lucía Solaris Vázquez", "me encantó la experiencia volvería a repetir", 3.5, "04/04/2016"));
        items.add(new Comentario("http://www.iconshock.com/img_vista/FLAT/communications/jpg/dog_icon.jpg", "Ana Martín Martín", "me encantó la experiencia volvería a repetir", 3.5, "13/05/2016"));
        items.add(new Comentario("https://upload.wikimedia.org/wikipedia/commons/0/0a/Paw_(Animal_Rights_symbol).png", "Laura García Del Rio", "me encantó la experiencia volvería a repetir", 3.5, "02/03/2016"));
        items.add(new Comentario("http://www.iconshock.com/img_vista/FLAT/communications/jpg/dog_icon.jpg", "Sonia Pérez García", "me encantó la experiencia volvería a repetir", 3.5, "02/02/2016"));
        items.add(new Comentario("http://www.iconshock.com/img_vista/FLAT/communications/jpg/dog_icon.jpg", "Luis Felipe Saez Esteves", "me encantó la experiencia volvería a repetir", 3.5, "12/12/2016"));
        items.add(new Comentario("https://upload.wikimedia.org/wikipedia/commons/0/0a/Paw_(Animal_Rights_symbol).png", "Pablo Escobar Martinez", "me encantó la experiencia volvería a repetir", 3.5, "22/11/2016"));
        items.add(new Comentario("http://www.iconshock.com/img_vista/FLAT/communications/jpg/dog_icon.jpg", "Jethro Tull Martín", "me encantó la experiencia volvería a repetir", 3.5, "26/12/2016"));
        items.add(new Comentario("http://www.iconshock.com/img_vista/FLAT/communications/jpg/dog_icon.jpg", "Eric Clapton Clapton", "me encantó la experiencia volvería a repetir", 3.5, "31/12/2016"));

        //Construyo el adaptador con los datos que ya lleva items
        myItemRecyclerViewAdapterComment = new ComentariosRVAdapter(items, mListenerRecyclerComment);
        //pongo el adaptador al ReclycerView
        recyclerViewComment.setAdapter(myItemRecyclerViewAdapterComment);


//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnListFragmentInteractionListener {
//        void onListFragmentInteraction(DummyItem item);
//    }
    }
}