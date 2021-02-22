package com.lamesa.netfilms.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.eightbitlab.bottomnavigationbar.BottomBarItem;
import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.netfilms.BuildConfig;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.adapter.adapterEpisodio;
import com.lamesa.netfilms.mesa.act_add_episodio;
import com.lamesa.netfilms.mesa.act_add_film;
import com.lamesa.netfilms.model.modelEpisodio;
import com.lamesa.netfilms.otros.TinyDB;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.lamesa.netfilms.App.mFirebaseAnalytics;
import static com.lamesa.netfilms.App.mixpanel;
import static com.lamesa.netfilms.App.tbCalidadFilm;
import static com.lamesa.netfilms.App.tbImagenFilm;
import static com.lamesa.netfilms.App.tbTipoFilm;
import static com.lamesa.netfilms.otros.metodos.CargarInterAd;
import static com.lamesa.netfilms.otros.statics.constantes.NotiAbierta;
import static com.lamesa.netfilms.otros.statics.constantes.TBcalidadFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBcategoriaFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBidFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBidiomaDetail;
import static com.lamesa.netfilms.otros.statics.constantes.TBimagenFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.netfilms.otros.statics.constantes.TBnombreFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBreproductor;
import static com.lamesa.netfilms.otros.statics.constantes.TBtipoFilm;
import static com.lamesa.netfilms.otros.statics.constantes.mixAdClic;
import static com.lamesa.netfilms.otros.statics.constantes.mixFilmFavorito;
import static com.lamesa.netfilms.otros.statics.constantes.mixReproductorClic;
import static com.lamesa.netfilms.otros.statics.constantes.setDebugActivo;
import static com.lamesa.netfilms.otros.metodos.AgregarQuitarFavorito;
import static com.lamesa.netfilms.otros.metodos.DialogoEnviarReporteFilm;
import static com.lamesa.netfilms.otros.metodos.EnviarCambioFB;
import static com.lamesa.netfilms.otros.metodos.getListaEpisodios;
import static com.lamesa.netfilms.otros.metodos.getListaIdiomas;
import static com.lamesa.netfilms.otros.metodos.setDebug;
import static com.lamesa.netfilms.service.fcm.EnviarNotiTodos;
import static com.lamesa.netfilms.service.fcm.EnviarNotificacion;

public class act_film extends AppCompatActivity {



    public static ArrayList<String> mlistIdiomas = new ArrayList<>();
    public static String tbIdFilm;
    public static RecyclerView rvEpisodios;
    public static List<modelEpisodio> mlistEpisodios;
    public static adapterEpisodio mAdapterEpisodio;
    public static String tbIdioma;
    public static ImageView ivFavorito;
    public static String tbNombreFilm;
    public static String tbCategoriaFilm;
    public static EditText etFiltrarEpisodios;
    private static TextView tvIdiomaDetail;
    private static BottomNavigationBar bottomNavigationBar;
    private TinyDB tinyDB;
    private TextView tvDescrip;
    private TextView tvTituloDetail;
    private ImageView ivImagenFondoDetail;
    private ImageView ivImagenPrincipalDetail;
    private TextView tvAnoDetail;
    private TextView tvCategoriaDetail;
    private TextView tvTipoDetail;
    private TextView tvCalidadDetail;
    private TextView tvIdFilm;
    private TextView tvReproductor;
    private ImageView ivReportarFilm;
    private String textRedFilm;
    private String textPuntajeFilm;
    private String textEstadoFilm;

    public static void ElegirIdioma(final Context mContext) {

        final TinyDB tinyDB = new TinyDB(mContext);
        System.out.println("OBTENER EPISODIO: ElegirIdioma");

        if (mlistIdiomas.size() != 0 && mlistIdiomas != null && bottomNavigationBar != null) {


            bottomNavigationBar.setVisibility(View.VISIBLE);

            for (String idioma : mlistIdiomas) {
                if (idioma.toLowerCase().contains("latino")) {
                    BottomBarItem item_historial = new BottomBarItem(R.drawable.ic_jugar, R.string.latino);
                    bottomNavigationBar.addTab(item_historial);
                    tinyDB.putString(TBidiomaDetail, "latino");
                    tbIdioma = "latino";
                } else if (idioma.toLowerCase().contains("castellano")) {
                    BottomBarItem item_inicio = new BottomBarItem(R.drawable.ic_jugar, R.string.catellano);
                    bottomNavigationBar.addTab(item_inicio);
                    tbIdioma = "castellano";
                    tinyDB.putString(TBidiomaDetail, "castellano");

                } else if (idioma.toLowerCase().contains("subtitulado")) {
                    BottomBarItem item_favoritos = new BottomBarItem(R.drawable.ic_jugar, R.string.subtitulado);
                    bottomNavigationBar.addTab(item_favoritos);
                    tinyDB.putString(TBidiomaDetail, "subtitulado");

                    tbIdioma = "subtitulado";

                }
            }


            getListaEpisodios(tbIdFilm, mlistIdiomas.get(0), mContext);

            tbIdioma = mlistIdiomas.get(0);


            for (String idioma : mlistIdiomas) {
                System.out.println("IDIOMA " + idioma);
            }


            bottomNavigationBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // getListaEpisodios(tbIdFilm, tinyDB.getString(TBidiomaDetail), mContext);
                    //   WaitDialog.show((AppCompatActivity) mContext, "sadasdasd lista...").setCancelable(true);

                }
            });


            bottomNavigationBar.setOnSelectListener(new BottomNavigationBar.OnSelectListener() {
                @Override
                public void onSelect(int position) {
                    // Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();

                    switch (position) {

                        case 0:
                            tinyDB.putString(TBidiomaDetail, "castellano");
                            getListaEpisodios(tbIdFilm, tinyDB.getString(TBidiomaDetail), mContext);
                            bottomNavigationBar.setActivated(true);
                            tbIdioma = "castellano";
                            WaitDialog.show((AppCompatActivity) mContext, "Cargando lista...").setCancelable(true);


                            break;
                        case 1:
                            tinyDB.putString(TBidiomaDetail, "latino");
                            getListaEpisodios(tbIdFilm, tinyDB.getString(TBidiomaDetail), mContext);
                            tbIdioma = "latino";
                            Toast.makeText(mContext, tinyDB.getString(TBidiomaDetail), Toast.LENGTH_SHORT).show();
                            System.out.println("OBTENER EPISODIO: Click Latino");

                            bottomNavigationBar.setActivated(true);
                            WaitDialog.show((AppCompatActivity) mContext, "Cargando lista...").setCancelable(true);
                            break;
                        case 2:
                            tinyDB.putString(TBidiomaDetail, "subtitulado");
                            getListaEpisodios(tbIdFilm, tinyDB.getString(TBidiomaDetail), mContext);
                            tbIdioma = "subtitulado";

                            bottomNavigationBar.setActivated(true);
                            WaitDialog.show((AppCompatActivity) mContext, "Cargando lista...").setCancelable(true);

                            break;


                        default:
                            //   WaitDialog.show((AppCompatActivity) mContext, "sadasdasd lista...").setCancelable(true);

                            break;

                    }

                }
            });


            bottomNavigationBar.setOnReselectListener(new BottomNavigationBar.OnReselectListener() {
                @Override
                public void onReselect(int position) {
                    switch (position) {


                        case 0:
                            tinyDB.putString("castellano", TBidiomaDetail);
                            getListaEpisodios(tbIdFilm, tinyDB.getString(TBidiomaDetail), mContext);
                            bottomNavigationBar.setActivated(true);
                            break;
                        case 1:
                            tinyDB.putString("latino", TBidiomaDetail);
                            System.out.println("OBTENER EPISODIO: ReClick Latino");

                            getListaEpisodios(tbIdFilm, tinyDB.getString(TBidiomaDetail), mContext);
                            bottomNavigationBar.setActivated(true);

                            break;
                        case 2:
                            tinyDB.putString("subtitulado", TBidiomaDetail);
                            getListaEpisodios(tbIdFilm, tinyDB.getString(TBidiomaDetail), mContext);
                            bottomNavigationBar.setActivated(true);
                            break;


                    }
                }
            });


        } else {
            //   Toast.makeText(mContext, "Sin episodios disponibles", Toast.LENGTH_LONG).show();
            bottomNavigationBar.setVisibility(View.GONE);
            TipDialog.show((AppCompatActivity) mContext, "Sin episodios disponibles.", TipDialog.TYPE.ERROR).setCancelable(true).setTipTime(50000);

        }
    }

    public static void FiltrarLista(String text, @NotNull List<modelEpisodio> ListaParaFiltrar, Context mContext) {

        if (ListaParaFiltrar.size() > 5) {

            setDebug("act_main", "FiltrarLista", "Filtrando lista", "d", setDebugActivo);
            List<modelEpisodio> listaFiltrada = new ArrayList<>();


            //  System.out.println("HOLAAAAAAAAAAAAAAAAAAA "+ListaParaFiltrar.size());


            setDebug("act_main", "FiltrarLista", "Filtrando lista ListaParaFiltrar tamaño == " + ListaParaFiltrar.size(), "d", setDebugActivo);

            if (ListaParaFiltrar != null && !ListaParaFiltrar.isEmpty()) {

//            setDebug("act_main","FiltrarLista","Filtrando lista ListaParaFiltrar nombre == "+ListaParaFiltrar.get(4).getNombre(),"d",setDebugActivo);
//


                for (int i = 0; ListaParaFiltrar.size() > i; i++) {


                    if (ListaParaFiltrar.get(i).getIdEpisodio() != "" && ListaParaFiltrar.get(i).getNombre() != null && ListaParaFiltrar.get(i).getIdEpisodio() != "" && ListaParaFiltrar.get(i).getNombre() != null) {
                        if (ListaParaFiltrar.get(i).getIdEpisodio().toLowerCase().contains(text.toLowerCase()) || ListaParaFiltrar.get(i).getNombre().toLowerCase().contains(text.toLowerCase())) {
                            listaFiltrada.add(ListaParaFiltrar.get(i));
                        }
                    }

                }


            }


            mAdapterEpisodio = new adapterEpisodio(mContext, listaFiltrada);
            rvEpisodios.setAdapter(mAdapterEpisodio);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        WaitDialog.show(this,"Cargando...");
        MobileAds.initialize(this,
                "ca-app-pub-3040756318290255~8298479348");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

         CargarAdFilm(); // solo en releaseeeeeeeeeeeeeeeeeeeeeeeeeee

        //guardar id para ser cargado en activity de detalles

        tinyDB = new TinyDB(this);
        tbIdFilm = tinyDB.getString(TBidFilm);
        tbIdioma = tinyDB.getString(TBidiomaDetail);
        tbNombreFilm = tinyDB.getString(TBnombreFilm);
        tbCategoriaFilm = tinyDB.getString(TBcategoriaFilm);
        tbTipoFilm = tinyDB.getString(TBtipoFilm);
        tbImagenFilm = tinyDB.getString(TBimagenFilm);
        tbCalidadFilm = tinyDB.getString(TBcalidadFilm);

        System.out.println("OBTENER EPISODIO: ID FILM = " + tbIdFilm);


        vistas();
        CheckFavorito(this);
        CargarRecycler();


        getListaIdiomas(tbIdFilm, this);
        getInfoFilm(tbIdFilm, this);
        mesa(act_film.this);



    }

    @Override
    public void onBackPressed() {
        finish();
        // saber si act film se abrio desde una nptificacion y por ende act main no se ha creado y evitar que se cierre la app al dar onback
        if (NotiAbierta){
            startActivity(new Intent(this, act_main.class));
            NotiAbierta = false;
        }
    }



    private void mesa(Context mContext) {

        ImageView ivAddEpisodio = findViewById(R.id.ivAddEpisodio);
        ImageView ivEditarFilm = findViewById(R.id.ivEditarFilm);
        ImageView ivEnviarNotif = findViewById(R.id.ivEnviarNotif);



        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.ivAddEpisodio:

                        Intent intent = new Intent(mContext, act_add_episodio.class);
                        intent.putExtra("idFilm", tbIdFilm);
                        intent.putExtra("idioma", tbIdioma);
                        mContext.startActivity(intent);
                        break;


                    case R.id.ivEditarFilm:


                        Intent i = new Intent(act_film.this, act_add_film.class);
                        i.putExtra("idFilm", tbIdFilm);
                        i.putExtra("idioma", tbIdioma);
                        i.putExtra("nombreFilm", tbNombreFilm);
                        i.putExtra("categoriaFilm", tbCategoriaFilm);
                        i.putExtra("tipoFilm", tbTipoFilm);
                        i.putExtra("imagenFilm", tbImagenFilm);
                        i.putExtra("calidadFilm", tbCalidadFilm);
                        i.putExtra("redFilm", textRedFilm);
                        i.putExtra("anoFilm", tvAnoDetail.getText().toString());
                        i.putExtra("sipnosisFilm", tvDescrip.getText().toString());
                        i.putExtra("puntajeFilm", textPuntajeFilm);
                        i.putExtra("estadoFilm", textEstadoFilm);
                        i.putExtra("nuevoFilm", false);


                        startActivity(i);

                        break;



                    case R.id.ivEnviarNotif:


                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                        DialogSettings.theme = DialogSettings.THEME.DARK;

                        //对于未实例化的布局：
                        MessageDialog.show(act_film.this, "ENVIAR NOTIFICACION", "Notificar Film agregado, destacado, episodio nuevo o mensaje personalizado", "CERRAR")
                                .setBackgroundColor(act_film.this.getResources().getColor(R.color.fondo_blank)).setCustomView(R.layout.layout_send_notifi, new MessageDialog.OnBindView() {
                                    @Override
                                    public void onBind(MessageDialog dialog, View v) {


// Set value


                                        Spinner spinTipoNotificacion = v.findViewById(R.id.spinTipoNotificacion);
                                        EditText etTipoNotif = v.findViewById(R.id.etTipoNotif);
                                        EditText etIdFilm = v.findViewById(R.id.etIdFilm);
                                        EditText etTitulo = v.findViewById(R.id.etTitulo);
                                        EditText etMensaje = v.findViewById(R.id.etMensaje);

                                        MaterialButton btnProbarNotif = v.findViewById(R.id.btnProbarNotif);
                                        MaterialButton btnEnviarNotif = v.findViewById(R.id.btnEnviarNotif);


                                        etIdFilm.setText(tinyDB.getString(TBidFilm));
                                        etTitulo.setText(tinyDB.getString(TBnombreFilm));

                                        View.OnClickListener listener2 = new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                switch (v.getId()){

                                                    case R.id.btnEnviarNotif:

                                                        EnviarNotiTodos(mContext,etIdFilm.getText().toString(),etTitulo.getText().toString(),etMensaje.getText().toString(),etTipoNotif.getText().toString());
                                                        EnviarCambioFB(mContext);

                                                        break;

                                                    case R.id.btnProbarNotif:

                                                        EnviarNotificacion(mContext,etIdFilm.getText().toString(),etTitulo.getText().toString(),etMensaje.getText().toString(),etTipoNotif.getText().toString());
                                                        TipDialog.show((AppCompatActivity) mContext,"Notificación de prueba.", TipDialog.TYPE.SUCCESS).setTipTime(5000);

                                                        break;

                                                }
                                            }
                                        };

                                        btnProbarNotif.setOnClickListener(listener2);
                                        btnEnviarNotif.setOnClickListener(listener2);



                                        //enviar items a spinner

                                        final List<String> list = new ArrayList<String>();

                                        list.add("Destacado");
                                        list.add("Estreno");
                                        list.add("Agregado");
                                        list.add("Episodio");
                                        list.add("Mensaje");
                                        list.add("Video funcionando");
                                        list.add("Temporada");



                                        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(act_film.this,
                                                android.R.layout.simple_list_item_1, list);
                                        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinTipoNotificacion.setAdapter(adp1);



                                        spinTipoNotificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                        {
                                            @Override
                                            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                                                // TODO Auto-generated method stub
                                               // Toast.makeText(act_film.this, list.get(position), Toast.LENGTH_SHORT).show();
                                                etTipoNotif.setText("Nuevo "+list.get(position).toLowerCase());
                                                switch (position){
                                                    case 0:
                                                        etMensaje.setText("Nuevo "+list.get(position).toLowerCase()+" en PPLUS");
                                                        break;

                                                    case 1:
                                                        etMensaje.setText("Nuevo "+list.get(position).toLowerCase()+" disponible en PPLUS");
                                                        break;

                                                    case 2:
                                                        etMensaje.setText("Film "+list.get(position).toLowerCase()+" a PPLUS");
                                                        break;

                                                    case 3:
                                                      etMensaje.setText("Nuevo "+list.get(position).toLowerCase()+" de "+"'"+etTitulo.getText().toString()+"'");
                                                        break;
                                                    case 4:
                                                        etMensaje.setText("");
                                                        etTitulo.setText("");
                                                        etIdFilm.setText("");
                                                        break;
                                                    case 5:
                                                        etMensaje.setText("Se ha solucionado los problemas de video"+" en "+"'"+etTitulo.getText().toString()+"'");
                                                        break;
                                                    case 6:
                                                        etMensaje.setText("Nueva temporada de "+"'"+ etTitulo.getText().toString()+"'");
                                                        break;
                                                }

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> arg0) {
                                                // TODO Auto-generated method stub
                                            }
                                        });



                                    }
                                });

                        break;


                }


            }
        };

        ivEditarFilm.setOnClickListener(listener);
        ivEnviarNotif.setOnClickListener(listener);
        ivAddEpisodio.setOnClickListener(listener);
    }

    private void vistas() {

        //vistas
        tvDescrip = findViewById(R.id.tvDescripDetail);
        tvDescrip.setMovementMethod(new ScrollingMovementMethod());
        tvTituloDetail = findViewById(R.id.tvTituloDestacado);
        ivImagenFondoDetail = findViewById(R.id.ivImagenFondoDetail);
        ivImagenPrincipalDetail = findViewById(R.id.imgPrincipalDestacado);
        tvAnoDetail = findViewById(R.id.tvAnoDetail);
        tvCategoriaDetail = findViewById(R.id.tvCategoriaDetail);
        tvIdiomaDetail = findViewById(R.id.tvIdiomaDetail);
        tvTipoDetail = findViewById(R.id.tvTipoDetail);
        tvCalidadDetail = findViewById(R.id.tvCalidadDetail);
        ivFavorito = findViewById(R.id.ivFavorito);
        tvIdFilm = findViewById(R.id.tvIdFilm);
        tvIdFilm.setText(String.valueOf(tbIdFilm));
        tvReproductor = findViewById(R.id.tvReproductor);
        ivReportarFilm = findViewById(R.id.ivReportarFilm);
        etFiltrarEpisodios = findViewById(R.id.etFiltrarEpisodios);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bnIdioma);
        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //region Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                // refrescar activity



                WaitDialog.show(act_film.this, "Actualizando datos...").setCancelable(true);


                // finalizar refresh
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 2 seconds)
                        swipeContainer.setRefreshing(false);
                        WaitDialog.dismiss();
                        try{
                            recreate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000); // Delay in millis
            }
        });
        //endregion
        //region Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //endregion


        //listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {



                    case R.id.tvDescripDetail:

                        TipDialog.show(act_film.this, tvDescrip.getText().toString(), TipDialog.TYPE.WARNING).setCancelable(true).setTipTime(50000);

                        break;

                    case R.id.ivFavorito:


                        if (ivFavorito.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.learn_ic_dislike).getConstantState()) {
                            AgregarQuitarFavorito(act_film.this, true, tbIdFilm);
                            //  Toast.makeText(act_film.this, "no tenia like", Toast.LENGTH_SHORT).show();

                            //region enviar MIX favorito
                            JSONObject props = new JSONObject();
                            try {
                                props.put("NombreFilm", tbNombreFilm);
                                props.put("IdFilm", tbIdFilm);
                                props.put("Calidad", tbCalidadFilm);
                                props.put("Tipo", tbTipoFilm);
                                props.put("Imagen", tbImagenFilm);
                                //para FB
                                Bundle params = new Bundle();
                                params.putString("NombreFilm", tbNombreFilm);
                                params.putString("IdFilm", tbIdFilm);
                                params.putString("Calidad", tbCalidadFilm);
                                params.putString("Tipo", tbTipoFilm);
                                params.putString("Imagen", tbImagenFilm);


                                mFirebaseAnalytics.logEvent(mixFilmFavorito, params);
                                mixpanel.track(mixFilmFavorito, props);
                                Amplitude.getInstance().logEvent(mixFilmFavorito, props);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //endregion

                        } else if (ivFavorito.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.learn_ic_like).getConstantState()) {
                            AgregarQuitarFavorito(act_film.this, false, tbIdFilm);
                            //  Toast.makeText(act_film.this, "tenia like", Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case R.id.tvReproductor:


                        List<String> opcionMenu = new ArrayList<>();
                        opcionMenu.add("Interno vía web");
                        opcionMenu.add("Interno PelisPlusHD (con video flotante) ");
                        opcionMenu.add("Externo");

//您自己的Adapter

                        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
                        DialogSettings.theme = DialogSettings.THEME.DARK;


                        BaseAdapter baseAdapter = new ArrayAdapter(act_film.this, com.kongzue.dialog.R.layout.item_bottom_menu_material, opcionMenu);


                        BottomMenu.show(act_film.this, baseAdapter, new OnMenuItemClickListener() {
                            @Override
                            public void onClick(String text, int index) {
                                //注意此处的 text 返回为自定义 Adapter.getItem(position).toString()，如需获取自定义Object，请尝试 datas.get(index)
                                //   Toast.makeText(act_main.this, "MAIN", Toast.LENGTH_SHORT).show();
                                String reproductor = tinyDB.getString(TBreproductor);
                                switch (index) {

                                    case 0:


                                        tinyDB.putString(TBreproductor, "web");


                                        tvReproductor.setText("Cambiar reproductor: Web");


                                        //region MIX ReproductorClic para estadisticas
                                        JSONObject props = new JSONObject();
                                        try {
                                            props.put("Reproductor", "web");
                                            Bundle params = new Bundle();
                                            params.putString("Reproductor", "web");


                                            mFirebaseAnalytics.logEvent(mixReproductorClic, params);

                                            mixpanel.track(mixReproductorClic, props);
                                            Amplitude.getInstance().logEvent(mixReproductorClic, props);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        //endregion

                                        break;

                                    case 1:

                                        tinyDB.putString(TBreproductor, "interno");
                                        tvReproductor.setText("Cambiar reproductor: PelisPlusHD");

//region MIX ReproductorClic para estadisticas
                                        JSONObject props2 = new JSONObject();
                                        try {
                                            props2.put("Reproductor", "interno");
                                            Bundle params = new Bundle();
                                            params.putString("Reproductor", "interno");


                                            mFirebaseAnalytics.logEvent(mixReproductorClic, params);

                                            mixpanel.track(mixReproductorClic, props2);
                                            Amplitude.getInstance().logEvent(mixReproductorClic, props2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        //endregion
                                        break;

                                    case 2:

                                        tinyDB.putString(TBreproductor, "externo");

                                        tvReproductor.setText("Cambiar reproductor: Externo");

//region MIX ReproductorClic para estadisticas
                                        JSONObject props3 = new JSONObject();
                                        try {
                                            props3.put("Reproductor", "externo");

                                            Bundle params = new Bundle();
                                            params.putString("Reproductor", "externo");


                                            mFirebaseAnalytics.logEvent(mixReproductorClic, params);
                                            mixpanel.track(mixReproductorClic, props3);
                                            Amplitude.getInstance().logEvent(mixReproductorClic, props3);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        //endregion
                                        break;

                                    default:

                                        tinyDB.putString(TBreproductor, "interno");
                                }

                            }
                        }).setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER).setFontColor(Color.GRAY)).setTitle("Si tiene problemas con el reproductor intente cambiandolo:");


                        break;

//您自己的Adapter


                    case R.id.ivReportarFilm:

                        DialogoEnviarReporteFilm(act_film.this, tinyDB.getString(TBidFilm), tinyDB.getString(TBnombreFilm));

                        break;



                }


            }
        };


        //enviar listener
        tvDescrip.setOnClickListener(listener);
        ivFavorito.setOnClickListener(listener);
        tvReproductor.setOnClickListener(listener);
        ivReportarFilm.setOnClickListener(listener);


        //declarar reproductor
        String reproductor = tinyDB.getString(TBreproductor);
        if (reproductor.contains("interno")) {
            tvReproductor.setText("Cambiar reproductor: PelisPlusHD");
        } else if (reproductor.contains("web")) {
            tvReproductor.setText("Cambiar reproductor: Web");
        } else if (reproductor.contains("externo")) {
            tvReproductor.setText("Cambiar reproductor: Externo");
        } else {
            tvReproductor.setText("Cambiar reproductor: PelisPlusHD");
        }


        //filtrar busqueda
        etFiltrarEpisodios.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mlistEpisodios != null && !mlistEpisodios.isEmpty()) {

                    FiltrarLista(s.toString(), mlistEpisodios, act_film.this);
                }

            }
        });


    }

    private void CargarAdFilm() {


        AdView mAdView = findViewById(R.id.adViewFilm);
        AdView mAdView2 = findViewById(R.id.adViewFilm2);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("830648A2D5D5AF09D0FAED08D38E2353").build();
        AdRequest adRequest2 = new AdRequest.Builder().addTestDevice("830648A2D5D5AF09D0FAED08D38E2353").build();
        // listener
        AdListener listener =  new AdListener() {

            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub


                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                // TODO Auto-generated method stub

                super.onAdClosed();

            }

            @Override
            public void onAdOpened() {
                // TODO Auto-generated method stub
                //region MIX mixAdClic para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("TipoAd", "Banner");
                    //para FB
                    Bundle params = new Bundle();
                    params.putString("TipoAd", "Banner");


                    mFirebaseAnalytics.logEvent(mixAdClic, params);
                    mixpanel.track(mixAdClic, props);
                    Amplitude.getInstance().logEvent(mixAdClic, props);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion
                super.onAdOpened();
            }

            @Override
            public void onAdLeftApplication() {
                // TODO Auto-generated method stub


                super.onAdLeftApplication();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // TODO Auto-generated method stub


                super.onAdFailedToLoad(errorCode);
            }

        };

        mAdView.loadAd(adRequest);
        mAdView2.loadAd(adRequest2);
        mAdView.setAdListener(listener);
        mAdView2.setAdListener(listener);




        Random numRandom = new Random();
        int numPosibilidad = numRandom.nextInt(6);

        //  Toast.makeText(this, String.valueOf(numPosibilidad), Toast.LENGTH_SHORT).show();


        String idAd = "ca-app-pub-3040756318290255/5727395558";

        if (BuildConfig.APPLICATION_ID.toLowerCase().contains("mesa")) {
            idAd = "ca-app-pub-3040756318290255/4041240372";
        }



        CargarInterAd(act_film.this, idAd, 9);



        /*
        InterstitialAd mInterstitialAd = new InterstitialAd(act_film.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (numPosibilidad == 3) {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }


         */

    }

    private void CheckFavorito(Context mContext) {

        ArrayList<String> ListaDeFavoritos = tinyDB.getListString(TBlistFavoritos);
        System.out.println("IDFILM " + tbIdFilm);

        if (ListaDeFavoritos != null && !ListaDeFavoritos.isEmpty()) {
            for (String favorito : ListaDeFavoritos) {
                String id = tbIdFilm;
                if (favorito.toLowerCase().contains(id.toLowerCase())) {
                    ivFavorito = findViewById(R.id.ivFavorito);
                    ivFavorito.setImageDrawable(mContext.getResources().getDrawable(R.drawable.learn_ic_like));
                    break;
                } else {
                    ivFavorito = findViewById(R.id.ivFavorito);
                    ivFavorito.setImageDrawable(mContext.getResources().getDrawable(R.drawable.learn_ic_dislike));
                }

            }
        }


    }

    private void CargarRecycler() {
        //region LISTA EPISODIOS
        rvEpisodios = findViewById(R.id.rvEpisodios);
        rvEpisodios.setHasFixedSize(true);
        rvEpisodios.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvEpisodios.setItemAnimator(new DefaultItemAnimator());
        mlistEpisodios = new ArrayList<>();
        mAdapterEpisodio = new adapterEpisodio(this, mlistEpisodios);
        rvEpisodios.setAdapter(mAdapterEpisodio);

        //endregion
    }

    private void getInfoFilm(String idFilm, final Context mContext) {


        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("data").child(idFilm).child("info");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.child("ano").exists()) {
                    String ano = dataSnapshot.child("ano").getValue().toString();
                    tvAnoDetail.setText(ano);
                } else {
                    // dataSnapshot.child("ano").getRef().setValue(0000);
                }


                if (dataSnapshot.child("calidad").exists()) {
                    String calidad = dataSnapshot.child("calidad").getValue().toString();
                    tvCalidadDetail.setText(calidad);
                    tbCalidadFilm = calidad;
                } else {
                    //  dataSnapshot.child("calidad").getRef().setValue("...");
                }

                if (dataSnapshot.child("categoria").exists()) {
                    String categoria = dataSnapshot.child("categoria").getValue().toString();
                    tvCategoriaDetail.setText(categoria);
                    tbCategoriaFilm = categoria;
                } else {
                    //  dataSnapshot.child("categoria").getRef().setValue("...");
                }

                if (dataSnapshot.child("descrip").exists()) {
                    String descrip = dataSnapshot.child("descrip").getValue().toString();
                    tvDescrip.setText(descrip);
                } else {
                    //  dataSnapshot.child("descrip").getRef().setValue("...");
                }

                if (dataSnapshot.child("imagen").exists()) {
                    String imagen = dataSnapshot.child("imagen").getValue().toString();
                    tbImagenFilm = imagen;

                    if (!((Activity) mContext).isFinishing()) {

                        Glide.with(mContext)
                                .load(imagen)
                                //.error(R.drawable.error)
                                //.placeholder(R.drawable.placeholder)
                                .into(ivImagenFondoDetail);
                        Glide.with(mContext)
                                .load(imagen)
                                .error(R.drawable.ic_alert)
                                //.placeholder(R.drawable.placeholder)
                                .into(ivImagenPrincipalDetail);
                    }
                }

                if (dataSnapshot.child("nombre").exists()) {
                    String nombre = dataSnapshot.child("nombre").getValue().toString();
                    tvTituloDetail.setText(nombre);
                    tbNombreFilm = nombre;


                } else {
                    //  dataSnapshot.child("nombre").getRef().setValue("...");
                }

                if (dataSnapshot.child("tipo").exists()) {
                    String tipo = dataSnapshot.child("tipo").getValue().toString();
                    tvTipoDetail.setText(tipo);
                    tbTipoFilm = tipo;

                } else {
                    // dataSnapshot.child("tipo").getRef().setValue("...");
                }


                if (dataSnapshot.child("red").exists()) {
                    String red = dataSnapshot.child("red").getValue().toString();
                    textRedFilm = red;

                }


                if (dataSnapshot.child("puntaje").exists()) {
                    String puntaje = dataSnapshot.child("puntaje").getValue().toString();
                    textPuntajeFilm = puntaje;

                }


                if (dataSnapshot.child("estado").exists()) {
                    String estado = dataSnapshot.child("estado").getValue().toString();
                    textEstadoFilm = estado;

                }



                WaitDialog.dismiss();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
            }
        });


    }


}
