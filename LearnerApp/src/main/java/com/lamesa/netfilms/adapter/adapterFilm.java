package com.lamesa.netfilms.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.activity.act_film;
import com.lamesa.netfilms.model.modelFilm;
import com.lamesa.netfilms.otros.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lamesa.netfilms.App.mFirebaseAnalytics;
import static com.lamesa.netfilms.App.mixpanel;
import static com.lamesa.netfilms.otros.statics.constantes.TBcalidadFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBidFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBimagenFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBnombreFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBtipoFilm;
import static com.lamesa.netfilms.otros.statics.constantes.mixFilmAbierto;
import static com.lamesa.netfilms.otros.statics.constantes.mixFilmFavorito;

/**
 * Created by Aws on 28/01/2018.
 */

public class adapterFilm extends RecyclerView.Adapter<adapterFilm.MyViewHolder> {

    private Context mContext;
    private List<modelFilm> mListFilms;

    //   private InterstitialAd mInterstitialAd;

    private int lastPosition = -1;
    private TinyDB tinyDB;


    public adapterFilm(Context mContext, List<modelFilm> mListFilms) {

        this.mContext = mContext;
        this.mListFilms = mListFilms;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        /*
        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        //     MobileAds.initialize(mContext, "ca-app-pub-4887224789758978~2509724130");
        Amplitude.getInstance().initialize(mContext, "d261f53264579f9554bd244eef7cc2e1").enableForegroundTracking((Application) mContext.getApplicationContext());


         */


        tinyDB = new TinyDB(mContext);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_film, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        setAnimation(holder.contenidoFilm, position);

        if(mListFilms!=null && !mListFilms.isEmpty()) {

            if (mListFilms.get(position).getNombre() != null && mListFilms.get(position).getTipo() != null && mListFilms.get(position).getCalidad() != null && mListFilms.get(position).getImagen() != null && mListFilms.get(position).getId() != null) {

                holder.tvTitulo.setText(mListFilms.get(position).getNombre());
                holder.tvTipo.setText(mListFilms.get(position).getTipo());
                holder.tvCalidad.setText(mListFilms.get(position).getCalidad());


                Glide.with(mContext)
                        .load(mListFilms.get(position).getImagen())
                     //   .error(R.drawable.ic_alert)
                        //.placeholder(R.drawable.placeholder)
                        .transition(DrawableTransitionOptions.withCrossFade(200))
                        .into(holder.ivImagen);


                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //region MIX mixFilmFavorito para estadisticas
                        JSONObject props = new JSONObject();
                        try {
                            props.put("NombreFilm", mListFilms.get(position).getNombre());
                            props.put("IdFilm", mListFilms.get(position).getId());
                            props.put("Calidad", mListFilms.get(position).getCalidad());
                            props.put("Tipo", mListFilms.get(position).getTipo());
                            props.put("Imagen", mListFilms.get(position).getImagen());
                            //para FB
                            Bundle params = new Bundle();
                            params.putString("NombreFilm", mListFilms.get(position).getNombre());
                            params.putString("IdFilm", mListFilms.get(position).getId());
                            params.putString("Calidad", mListFilms.get(position).getCalidad());
                            params.putString("Tipo",  mListFilms.get(position).getTipo());
                            params.putString("Imagen",  mListFilms.get(position).getImagen());


                            mFirebaseAnalytics.logEvent(mixFilmFavorito, params);
                            mixpanel.track(mixFilmAbierto, props);
                            Amplitude.getInstance().logEvent(mixFilmAbierto, props);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //endregion


                        //region GUARDAR EN TINY DB
                        tinyDB.putString(TBnombreFilm, mListFilms.get(position).getNombre());
                        tinyDB.putString(TBidFilm, mListFilms.get(position).getId());
                        tinyDB.putString(TBcalidadFilm, mListFilms.get(position).getCalidad());
                        tinyDB.putString(TBtipoFilm, mListFilms.get(position).getTipo());
                        tinyDB.putString(TBimagenFilm, mListFilms.get(position).getImagen());
                        //endregion


                        //region guardar id para ser cargado en activity de detalles

                        tinyDB.putString(TBidFilm, mListFilms.get(position).getId());
                        mContext.startActivity(new Intent(mContext, act_film.class));
                        //endregion

                    }
                });


                // eliminar de favoritos

                holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        /*
                        DialogSettings.theme = DialogSettings.THEME.DARK;
                        DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;

                        MessageDialog.show((AppCompatActivity) mContext, "提示", "这个窗口附带自定义布局", "知道了")
                                .setCustomView(R.layout.layout_full_login, new MessageDialog.OnBindView() {
                                    @Override
                                    public void onBind(MessageDialog dialog, View v) {
                                        //绑定布局事件，可使用v.findViewById(...)来获取子组件
                                        v.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                            }
                                        });
                                    }
                                });

//对于已实例化的布局：


                         */

                        return false;


                    }
                });


            }
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
        if(mListFilms!=null) {
            return mListFilms.size();
        }
        return 0;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private RelativeLayout contenidoFilm;
        private CardView cardView;
        private ImageView ivImagen;
        private TextView tvTitulo;
        private TextView tvCalidad;
        private TextView tvTipo;

        public MyViewHolder(View itemView) {
            super(itemView);

            contenidoFilm = itemView.findViewById(R.id.rlFilm);
            cardView = itemView.findViewById(R.id.cardviewFilm);
            ivImagen = itemView.findViewById(R.id.ivImagenPrincipalFilm);
            tvTitulo = itemView.findViewById(R.id.tvTituloFilm);
            tvCalidad = itemView.findViewById(R.id.tvCalidadFilm);
            tvTipo = itemView.findViewById(R.id.tvTipoFilm);


        }
    }


}

