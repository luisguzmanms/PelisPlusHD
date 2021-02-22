package com.lamesa.netfilms.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lamesa.netfilms.R;
import com.lamesa.netfilms.mesa.act_episodio;
import com.lamesa.netfilms.model.modelEpisodio;
import com.lamesa.netfilms.otros.TinyDB;

import java.util.List;

import static com.lamesa.netfilms.activity.act_film.mlistEpisodios;
import static com.lamesa.netfilms.activity.act_film.tbIdFilm;
import static com.lamesa.netfilms.activity.act_film.tbIdioma;
import static com.lamesa.netfilms.activity.act_film.tbNombreFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBidEpisodio;
import static com.lamesa.netfilms.otros.statics.constantes.TBidFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBnombreEpisodio;
import static com.lamesa.netfilms.otros.metodos.DialogoEnviarReporte;
import static com.lamesa.netfilms.otros.metodos.MarcarDesmarcarVisto;
import static com.lamesa.netfilms.otros.metodos.getUrlEpisodio;

/**
 * Created by Aws on 28/01/2018.
 */

public class adapterEpisodio extends RecyclerView.Adapter<adapterEpisodio.MyViewHolder> {

    private Context mContext;
    private List<modelEpisodio> mListEspisodios;

    //   private InterstitialAd mInterstitialAd;

    private int lastPosition = -1;
    private TinyDB tinyDB;


    public adapterEpisodio(Context mContext, List<modelEpisodio> mListEspisodios) {

        this.mContext = mContext;
        this.mListEspisodios = mListEspisodios;

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
        view = mInflater.inflate(R.layout.item_episodio, parent, false);


        tinyDB = new TinyDB(mContext);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        List<Integer> ListaHistorial = tinyDB.getListInt("historial-" + tbIdFilm);

        for (int historial : ListaHistorial) {

            if (historial == position) {
                holder.ivVisto.setVisibility(View.VISIBLE);
                // setAnimation(holder.ivVisto, position);
                // break;
            }

        }


        setAnimation(holder.contenedorEpisodio, position);


        holder.tvTitulo.setText(mListEspisodios.get(position).getNombre());


        holder.ivVisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarcarDesmarcarVisto(mContext, tbIdFilm, position, false);
            }
        });


        // reportar episodio
        holder.ivReportarEp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoEnviarReporte(mContext, tbIdFilm, mlistEpisodios.get(position).getIdEpisodio(), mlistEpisodios.get(position).getNombre(), tbNombreFilm);
            }
        });


        holder.contenedorEpisodio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//guardar id para ser cargado en activity de detalles
                final TinyDB tinyDB = new TinyDB(mContext);

                tinyDB.putString(TBidFilm, mListEspisodios.get(position).getId());
                tinyDB.putString(TBnombreEpisodio, mListEspisodios.get(position).getNombre());
                tinyDB.putString(TBidEpisodio, mListEspisodios.get(position).getIdEpisodio());
                MarcarDesmarcarVisto(mContext, tbIdFilm, position, true);
                getUrlEpisodio(mContext, tbIdFilm, tbIdioma, mlistEpisodios.get(position).getIdEpisodio());
                System.out.println("OBTENER EPISODIO: CLICK EPISODIO");


            }
        });


        // eliminar de favoritos

        holder.contenedorEpisodio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {





                /*
                FullScreenDialog show = FullScreenDialog
                        .show((AppCompatActivity) mContext, R.layout.dialog_edit_epi, new FullScreenDialog.OnBindView() {
                            @Override
                            public void onBind(FullScreenDialog dialog, View rootView) {
                                getUrlEpisodioEditar(mContext, mListEspisodios.get(position).getId(), tbIdioma, mListEspisodios.get(position).getIdEpisodio());


                                EditText etNombreEpisodio = rootView.findViewById(R.id.etNombreEpisodio);
                                EditText etAddLink = rootView.findViewById(R.id.etAddLink);


                                MaterialButton btnEnviarCambios = rootView.findViewById(R.id.btnEnviarCambios);
                                btnEnviarCambios.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EnviarCambioEpisodio(mContext, mlistEpisodios.get(position).getId(), tbIdioma, mlistEpisodios.get(position).getIdEpisodio(), etAddLink.getText().toString(), etNombreEpisodio.getText().toString());
                                        etAddLink.setText("");
                                    }
                                });

                                etNombreEpisodio.setText(mListEspisodios.get(position).getNombre());
                                etNombreEpisodio.setHint(mListEspisodios.get(position).getNombre());


                                lvUrls = rootView.findViewById(R.id.lvUrls);
                                lvUrls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int positionn, long id) {

                                        if (mlistUrls != null && !mlistUrls.isEmpty()) {

                                            InputDialog.show((AppCompatActivity) mContext, "Link", "Link de " + mlistEpisodios.get(position).getNombre(), "ABRIR", "CERRAR").setOtherButton("ELIMINAR").setInputText(mlistUrls.get(positionn))
                                                    .setCancelable(false)
                                                    .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                                                        @Override
                                                        public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                                            Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();


                                                            String url = inputStr;
                                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                                            i.setData(Uri.parse(url));
                                                            mContext.startActivity(i);

                                                            return false;
                                                        }
                                                    }).setOnOtherButtonClickListener(new OnInputDialogButtonClickListener() {
                                                @Override
                                                public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                                    Toast.makeText(mContext, "asdasdasd", Toast.LENGTH_SHORT).show();
                                                    EliminarLinkExistente(mContext, mlistEpisodios.get(position).getId(), tbIdioma, mlistEpisodios.get(position).getIdEpisodio(), mlistUrls.get(positionn));
                                                    return false;
                                                }
                                            });
                                        }

                                    }
                                });
                                lvUrls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position2, long id) {
                                        MessageDialog.show((AppCompatActivity) mContext, "¿ELIMINAR ESTE LINK?", "Se eliminara este link de FB", "SI", "CANCELAR")
                                                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                                    @Override
                                                    public boolean onClick(BaseDialog baseDialog, View v) {
                                                        return false;
                                                    }
                                                })
                                                .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                                                    @Override
                                                    public boolean onClick(BaseDialog baseDialog, View v) {
                                                        return true;                    //位于“取消”位置的按钮点击后无法关闭对话框
                                                    }
                                                });
                                        return false;
                                    }
                                });

                            }
                        }).setCancelable(true).setCancelButton("CERRAR").setTitle("EDICIÓN EPISODIO");

*/

                return true;


            }
        });


        holder.ivEditEpisodio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, act_episodio.class);
                i.putExtra("idFilm", mlistEpisodios.get(position).getId());
                i.putExtra("idioma", tbIdioma);
                i.putExtra("idEpisodio", mlistEpisodios.get(position).getIdEpisodio());
                i.putExtra("nombreEpisodio", mlistEpisodios.get(position).getNombre());
                mContext.startActivity(i);

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


    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.enter_ios_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mListEspisodios.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout contenedorEpisodio;
        private TextView tvTitulo;
        private ImageView ivVisto;
        private ImageView ivReportarEp;
        private ImageView ivEditEpisodio;


        public MyViewHolder(View itemView) {
            super(itemView);
            ivEditEpisodio = itemView.findViewById(R.id.ivEditEpisodio);
            ivReportarEp = itemView.findViewById(R.id.ivReportarEp);
            ivVisto = itemView.findViewById(R.id.ivVisto);
            contenedorEpisodio = itemView.findViewById(R.id.contenedorEpisodio);
            tvTitulo = itemView.findViewById(R.id.tvTituloEpisodio);


        }
    }


}

