package com.lamesa.netfilms.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.model.modelCategoria;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.lamesa.netfilms.App.mFirebaseAnalytics;
import static com.lamesa.netfilms.App.mixpanel;
import static com.lamesa.netfilms.activity.act_main.AbrirSearch;
import static com.lamesa.netfilms.activity.act_main.mlistCategoria;
import static com.lamesa.netfilms.activity.act_main.mlistGenero;
import static com.lamesa.netfilms.otros.statics.constantes.mixCategoriaClic;
import static com.lamesa.netfilms.otros.metodos.getListaGenero;

/**
 * Created by Aws on 28/01/2018.
 */

public class adapterCategoria extends RecyclerView.Adapter<adapterCategoria.MyViewHolder> {

    private Context mContext;
    private List<modelCategoria> mListCategorias;

    //   private InterstitialAd mInterstitialAd;

    private int lastPosition = -1;


    public adapterCategoria(Context mContext, List<modelCategoria> mListCategorias) {

        this.mContext = mContext;
        this.mListCategorias = mListCategorias;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        /*
        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        //     MobileAds.initialize(mContext, "ca-app-pub-4887224789758978~2509724130");
        Amplitude.getInstance().initialize(mContext, "d261f53264579f9554bd244eef7cc2e1").enableForegroundTracking((Application) mContext.getApplicationContext());


         */

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_categoria, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        setAnimation(holder.flContenedor, position);


        holder.tvCategoria.setText(mListCategorias.get(position).getNombre());




        Glide.with(mContext)
                .load(mListCategorias.get(position).getImagen())
                //.error(R.drawable.error)
                //.placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(holder.ivCategoria);


        holder.flContenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //region MIX CategoriaClic
                JSONObject props = new JSONObject();
                try {
                    props.put("Categoria", mlistCategoria.get(position).getNombre());
                    Bundle params = new Bundle();
                    params.putString("Categoria", mlistCategoria.get(position).getNombre());



                    mFirebaseAnalytics.logEvent(mixCategoriaClic, params);
                    mixpanel.track(mixCategoriaClic, props);
                    Amplitude.getInstance().logEvent(mixCategoriaClic, props);

                } catch (JSONException e) {
                    e.printStackTrace();
                }






                AbrirSearch(mlistGenero, mlistCategoria.get(position).getNombre(), mContext);
                getListaGenero(mlistCategoria.get(position).getNombre(), mContext);
            }
        });







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
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_float_window_enter);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mListCategorias.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private FrameLayout flContenedor;
        private CircleImageView ivCategoria;
        private TextView tvCategoria;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivCategoria = itemView.findViewById(R.id.ivCategoria);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            flContenedor = itemView.findViewById(R.id.flContenedorCategoria);

        }


    }


}

