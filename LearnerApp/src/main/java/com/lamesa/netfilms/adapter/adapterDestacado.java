package com.lamesa.netfilms.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.activity.act_film;
import com.lamesa.netfilms.model.modelDestacado;
import com.lamesa.netfilms.otros.TinyDB;

import java.util.List;

import static com.lamesa.netfilms.otros.statics.constantes.TBidFilm;


/**
 * Created by Aws on 28/01/2018.
 */

public class adapterDestacado extends RecyclerView.Adapter<adapterDestacado.MyViewHolder> {

    private Context mContext;
    private List<modelDestacado> mListDestacados;

    //   private InterstitialAd mInterstitialAd;

    private int lastPosition = -1;


    public adapterDestacado(Context mContext, List<modelDestacado> mListDestacados) {

        this.mContext = mContext;
        this.mListDestacados = mListDestacados;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        /*
        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        //     MobileAds.initialize(mContext, "ca-app-pub-4887224789758978~2509724130");
        Amplitude.getInstance().initialize(mContext, "d261f53264579f9554bd244eef7cc2e1").enableForegroundTracking((Application) mContext.getApplicationContext());
d

         */

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_destacado2, parent, false);
        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        setAnimation(holder.cardView, position);


        if(mListDestacados!=null && mListDestacados.get(position).getNombre() != null && mListDestacados.get(position).getId() != null && mListDestacados.get(position).getImagen() != null) {

            holder.tvTitulo.setText(mListDestacados.get(position).getNombre().toUpperCase());


            Glide.with(mContext)
                    .load(mListDestacados.get(position).getImagen())
                    //.error(R.drawable.error)
                    //.placeholder(R.drawable.placeholder)
                    .into(holder.imgFondo);

            Glide.with(mContext)
                    .load(mListDestacados.get(position).getImagen())
                    //.error(R.drawable.error)
                    //.placeholder(R.drawable.placeholder)
                    .into(holder.imgPrincipal);


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //guardar id para ser cargado en activity de detalles
                    final TinyDB tinyDB = new TinyDB(mContext);
                    tinyDB.putString(TBidFilm, mListDestacados.get(position).getId());
                    mContext.startActivity(new Intent(mContext, act_film.class));


                }
            });


            // eliminar de favoritos

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    return false;


                }
            });

        }


    }


    /*

    private void EliminarFavorito(int idfavorito) {

        //login aunth
        // get el usuario
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser u = mAuth.getCurrentUser();


        if (u != null) {


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference clientesRef = ref.child("lofiradio").child("usuario").child(u.getUid()).child("favoritos").child("canciones").child(String.valueOf(idfavorito));


            ((DatabaseReference) clientesRef).child("idfavorito").removeValue();
            ((DatabaseReference) clientesRef).child("LinkYT").removeValue();
            ((DatabaseReference) clientesRef).child("NombreCancionSonando").removeValue();


            Toast.makeText(mContext, mContext.getString(R.string.cancionelimfavoritos), Toast.LENGTH_SHORT).show();


        }
    }


     */


    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.enter_ios_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mListDestacados.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private CardView   cardView;
        private ImageView imgFondo;
        private ImageView imgPrincipal;
        private TextView tvTitulo;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardviewDestacado);
            imgFondo = itemView.findViewById(R.id.imgFondoDestacado);
            imgPrincipal = itemView.findViewById(R.id.imgPrincipalDestacado);
            tvTitulo = itemView.findViewById(R.id.tvTituloDestacado);


        }
    }


}

