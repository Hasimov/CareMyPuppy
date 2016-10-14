package com.carepuppy.pirtu.caremypuppy.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.carepuppy.pirtu.caremypuppy.Interfaces.OnListFragmentClickListenerComment;
import com.carepuppy.pirtu.caremypuppy.Models.Comentario;
import com.carepuppy.pirtu.caremypuppy.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ComentariosRVAdapter extends RecyclerView.Adapter<ComentariosRVAdapter.ViewHolder> {

    private final List<Comentario> mValues;
    private final OnListFragmentClickListenerComment mListener;
    private Context context;


    public ComentariosRVAdapter(List<Comentario> items, OnListFragmentClickListenerComment listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_comentarios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //En esta sección cargamos desde el POJO los datos que iran en las vistas del cardview
        holder.mItem = mValues.get(position);
        holder.tVusernameComment.setText(mValues.get(position).getUser_name_comment());
        holder.tVCommentBody.setText(mValues.get(position).getContent_comment());
        holder.tVfechaComment.setText(String.valueOf(mValues.get(position).getDate_comment()));

        Picasso.with(context)
                .load(mValues.get(position).getUrl_img_comment())
                .placeholder(R.drawable.avatar1)
                .fit()
                //.resize(50, 0)
//                .onlyScaleDown()
//                .centerCrop()
                .error(R.drawable.dogavatar)
                .into(holder.iVavatarComment);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //Elementos del cardview para los cuidadores
        public ImageView iVavatarComment;
        public final TextView tVusernameComment;
        public final TextView tVfechaComment;
        public final TextView tVCommentBody;

        //tomo un elemento, a un sólo cuidador TODO:añadir todos los campos del comentario
        public Comentario mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            iVavatarComment = (ImageView) view.findViewById(R.id.iVavatarComment);
            tVusernameComment = (TextView) view.findViewById(R.id.tVusernameComment);
            tVfechaComment = (TextView) view.findViewById(R.id.tVfechaComment);
            tVCommentBody = (TextView) view.findViewById(R.id.tVCommentBody);


        }


    }
}
