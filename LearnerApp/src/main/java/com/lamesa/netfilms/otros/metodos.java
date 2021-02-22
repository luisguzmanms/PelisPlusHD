package com.lamesa.netfilms.otros;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.amplitude.api.Amplitude;
import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Logger;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.netfilms.BuildConfig;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.activity.act_main;
import com.lamesa.netfilms.activity.act_webview;
import com.lamesa.netfilms.adapter.adapterEpisodio;
import com.lamesa.netfilms.model.modelCategoria;
import com.lamesa.netfilms.model.modelDestacado;
import com.lamesa.netfilms.model.modelEpisodio;
import com.lamesa.netfilms.model.modelFilm;
import com.lamesa.netfilms.otros.statics.Animacion;
import com.opencsv.CSVReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lamesa.netfilms.App.mFirebaseAnalytics;
import static com.lamesa.netfilms.App.mixpanel;
import static com.lamesa.netfilms.activity.act_film.ElegirIdioma;
import static com.lamesa.netfilms.activity.act_film.etFiltrarEpisodios;
import static com.lamesa.netfilms.activity.act_film.ivFavorito;
import static com.lamesa.netfilms.activity.act_film.mAdapterEpisodio;
import static com.lamesa.netfilms.activity.act_film.mlistEpisodios;
import static com.lamesa.netfilms.activity.act_film.mlistIdiomas;
import static com.lamesa.netfilms.activity.act_film.rvEpisodios;
import static com.lamesa.netfilms.activity.act_film.tbIdioma;
import static com.lamesa.netfilms.activity.act_film.tbNombreFilm;
import static com.lamesa.netfilms.activity.act_main.animacionCargandoTop;
import static com.lamesa.netfilms.activity.act_main.contenidoCargando;
import static com.lamesa.netfilms.activity.act_main.contenidoHome;
import static com.lamesa.netfilms.activity.act_main.contenidoSearch;
import static com.lamesa.netfilms.activity.act_main.mAdapterBusqueda;
import static com.lamesa.netfilms.activity.act_main.mAdapterCategoria;
import static com.lamesa.netfilms.activity.act_main.mAdapterDestacado;
import static com.lamesa.netfilms.activity.act_main.mAdapterEstrenos;
import static com.lamesa.netfilms.activity.act_main.mAdapterNetflix;
import static com.lamesa.netfilms.activity.act_main.mAdapterPelicula;
import static com.lamesa.netfilms.activity.act_main.mAdapterSerie;
import static com.lamesa.netfilms.activity.act_main.mlistCategoria;
import static com.lamesa.netfilms.activity.act_main.mlistContenido;
import static com.lamesa.netfilms.activity.act_main.mlistDestacado;
import static com.lamesa.netfilms.activity.act_main.mlistEstrenos;
import static com.lamesa.netfilms.activity.act_main.mlistFavoritos;
import static com.lamesa.netfilms.activity.act_main.mlistGenero;
import static com.lamesa.netfilms.activity.act_main.mlistNetflix;
import static com.lamesa.netfilms.activity.act_main.mlistPeliculas;
import static com.lamesa.netfilms.activity.act_main.mlistSeries;
import static com.lamesa.netfilms.activity.act_main.mlistUrls;
import static com.lamesa.netfilms.mesa.act_add_episodio.lvEpisodios;
import static com.lamesa.netfilms.mesa.act_episodio.lvUrls;
import static com.lamesa.netfilms.otros.statics.constantes.TBfechaCambiosData;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistContenido;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistEstrenos;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistNetflix;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistPeliculas;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistSeries;
import static com.lamesa.netfilms.otros.statics.constantes.TBnombreEpisodio;
import static com.lamesa.netfilms.otros.statics.constantes.TBreproductor;
import static com.lamesa.netfilms.otros.statics.constantes.TBurlEpisodio;
import static com.lamesa.netfilms.otros.statics.constantes.mixActualizarApp;
import static com.lamesa.netfilms.otros.statics.constantes.mixCompartirApp;
import static com.lamesa.netfilms.otros.statics.constantes.mixContenidoCargado;
import static com.lamesa.netfilms.otros.statics.constantes.mixEpisodioClic;
import static com.lamesa.netfilms.otros.statics.constantes.setDebugActivo;
import static com.lamesa.netfilms.otros.fire.EnviarSolicitud;
import static com.lamesa.netfilms.otros.fire.EnviarSugerencia;
import static com.lamesa.netfilms.otros.fire.ReportarEpisodio;
import static com.lamesa.netfilms.otros.fire.ReportarFilm;


public class metodos {


    public static FirebaseUser user;
    public static ArrayList<String> listEpisodiosListView;
    private static FirebaseAuth mAuth;
    private static PermissionListener permissionlistener;
    private String CLASS = "metodos";

    public static boolean LoginDeUsuario() {
        // NECESARIO PARA COMPROBAR EL INICIO DE SESION DE UN USUARIO
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void CargarHome(Context mContext) {
        if(contenidoHome.getVisibility()== GONE) {
            contenidoSearch.startAnimation(Animacion.alpha_out(mContext));
            contenidoSearch.setVisibility(GONE);
        }
        contenidoHome.setVisibility(VISIBLE);
    }

    public static void MostrarCargando(Context mContext) {

        if (contenidoCargando != null && contenidoCargando.getVisibility() == VISIBLE) {
            contenidoCargando.setVisibility(GONE);
        }

        if (mlistContenido == null || mlistContenido.size() == 0) {
            if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                WaitDialog.show((AppCompatActivity) mContext, "Lista vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                CargarHome(mContext);
            }
        }
    }

    public static void getListaDestacados(Context mContext) {


        if (mlistDestacado == null) {
            mlistDestacado = new ArrayList<>();
        }

        animacionCargandoTop(true);

        Query database = FirebaseDatabase.getInstance().getReference().child("destacados");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistDestacado.removeAll(mlistDestacado);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {

                    String idDestacado = (String) snapshot.getValue();
                    getDestacado(mContext, String.valueOf(idDestacado), mlistDestacado);
                    System.out.println("CAMBIO :: ");

                }


                for (int i = 0; mlistDestacado.size() > i; i++) {
                    System.out.println("LOGNET :: " + mlistDestacado.get(i).getId());
                    System.out.println("LOGNET2 :: " + mlistDestacado.get(i).getImagen());
                    System.out.println("LOGNET3 :: " + mlistDestacado.get(i).getNombre());

                }

                Collections.shuffle(mlistDestacado);


                if (mlistDestacado.isEmpty()) {
                    //TipDialog.show((AppCompatActivity) mContext,"No hay destacados disponibles.", TipDialog.TYPE.WARNING).setCancelable(true);
                }

                mAdapterDestacado.notifyDataSetChanged();

                animacionCargandoTop(false);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }

    public static void getListaSeries(Context mContext) {


        // mlistSeries = new ArrayList<>();

        //  mlistSeries = new ArrayList<>();
        TinyDB tinyDB = new TinyDB(mContext);


        List<modelFilm> tbListSeries = tinyDB.getListObject(TBlistSeries, modelFilm.class);

        //   contenidoCargando.setVisibility(VISIBLE);


        if (tbListSeries.size() == 0) {


            Query database = FirebaseDatabase.getInstance().getReference().child("data");


            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mlistSeries.removeAll(mlistSeries);
                    tbListSeries.removeAll(tbListSeries);

                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {


                        if (snapshot.child("info").child("tipo").exists()) {

                            String tipo = (String) snapshot.child("info").child("tipo").getValue();

                            if (tipo.toLowerCase().contains("serie")) {
                                modelFilm serie = snapshot.child("info").getValue(modelFilm.class);
                                mlistSeries.add(serie);
                            }
                        }


                    }


                    //Guardar lista en temporal para empezar a cargar este y no los otros
                    tinyDB.putListObject(TBlistSeries, mlistSeries);
                    //Guardar lista en temporal para empezar a cargar este y no los otros


                    System.out.println("CARGADO DESDE SERVIDOR");

                    Collections.shuffle(mlistSeries);


                    if (mAdapterSerie != null) {
                        mAdapterSerie.notifyDataSetChanged();
                    }


                    //     contenidoCargando.setVisibility(GONE);

                    if (mlistSeries == null || mlistSeries.size() == 0) {
                        if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                            WaitDialog.show((AppCompatActivity) mContext, "Lista de series vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                            CargarHome(mContext);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


        } else {

            System.out.println("INFA: YA HAY LISTA EN TEMPORTAL, NO SE RELLENARA DESDE EL SERVIDOR");

            //region ACTUALIZAR INFO LISTA YA
            //remover y volver a cargar para evitar informacion duplicada
            mlistSeries.removeAll(mlistSeries);
            mlistSeries.addAll(tbListSeries);
            //aleatorizar lista
            Collections.shuffle(mlistSeries);
            if (mAdapterSerie != null) {
                mAdapterSerie.notifyDataSetChanged();
            }
            //endregion


            System.out.println("CARGADO DESDE CACHE");

            //   MostrarCargando(mContext);

            for (int i = 0; tbListSeries.size() > i; i++) {
                System.out.println("INFA: tbListContenido ya almacenado" + tbListSeries.get(i).getNombre());
            }

        }


    }

    public static void getDestacado(Context mContext, String idDestacado, List listaAGuardar) {


        Query database = FirebaseDatabase.getInstance().getReference().child("data").child(String.valueOf(idDestacado));


        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.child("info").exists()) {
                    modelDestacado infoDestacado = dataSnapshot.child("info").getValue(modelDestacado.class);
                    listaAGuardar.add(infoDestacado);
                    Collections.shuffle(listaAGuardar);
                    mAdapterDestacado.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    public static void getListaPeliculas(Context mContext) {


        //  mlistPeliculas = new ArrayList<>();

        WaitDialog.show((AppCompatActivity) mContext, "Cargando peliculas..").setCancelable(true);


        //  mlistPeliculas = new ArrayList<>();
        //  contenidoCargando.setVisibility(View.VISIBLE);


        TinyDB tinyDB = new TinyDB(mContext);

        List<modelFilm> tbListPeliculas = tinyDB.getListObject(TBlistPeliculas, modelFilm.class);


        if (tbListPeliculas.size() == 0) {

            Query database = FirebaseDatabase.getInstance().getReference().child("data");


            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mlistPeliculas.removeAll(mlistPeliculas);
                    tbListPeliculas.removeAll(tbListPeliculas);

                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {


                        if (snapshot.child("info").child("tipo").exists()) {

                            String tipo = (String) snapshot.child("info").child("tipo").getValue();

                            if (tipo.toLowerCase().contains("pelicula")) {
                                modelFilm pelicula = snapshot.child("info").getValue(modelFilm.class);
                                mlistPeliculas.add(pelicula);
                            }
                        }


                    }


                    //Guardar lista en temporal para empezar a cargar este y no los otros
                    tinyDB.putListObject(TBlistPeliculas, mlistPeliculas);

                    System.out.println("LISTA PELICULAS CARGADO DESDE SERVIDOR");


                    Collections.shuffle(mlistPeliculas);


                    if (mAdapterPelicula != null) {
                        mAdapterPelicula.notifyDataSetChanged();
                    }


                    //   contenidoCargando.setVisibility(GONE);

                    if (mlistPeliculas == null || mlistPeliculas.size() == 0) {
                        if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                            WaitDialog.show((AppCompatActivity) mContext, "Lista vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                            CargarHome(mContext);
                        }
                    }


                    WaitDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


        } else {


            System.out.println("INFA: LISTA PELICULAS  YA HAY LISTA EN TEMPORTAL, NO SE RELLENARA DESDE EL SERVIDOR");

            //region ACTUALIZAR INFO LISTA YA
            //remover y volver a cargar para evitar informacion duplicada
            mlistPeliculas.removeAll(mlistPeliculas);

            mlistPeliculas.addAll(tbListPeliculas);
            //aleatorizar lista
            Collections.shuffle(mlistPeliculas);
            if (mAdapterPelicula != null) {
                mAdapterPelicula.notifyDataSetChanged();
            }
            //endregion


            System.out.println("LISTA PELICULAS CARGADO DESDE CACHE");

            //  MostrarCargando(mContext);

            for (int i = 0; tbListPeliculas.size() > i; i++) {
                System.out.println("INFA: LISTA PELICULAS  tbListContenido ya almacenado" + tbListPeliculas.get(i).getNombre());
            }


            WaitDialog.dismiss();


        }


    }

    public static void getListaDeFirebase(Context mContext) {


        TinyDB tinyDB = new TinyDB(mContext);


        WaitDialog.show((AppCompatActivity) mContext, "Cargando contenido con la conexion a Internet, si es la primera vez, podria tardar algunos minutos, por favor sea paciente.").setCancelable(false);
        //   contenidoCargando.setVisibility(VISIBLE);


        if (mlistSeries == null) {
            mlistSeries = new ArrayList<>();
        }

        if (mlistContenido == null) {
            mlistContenido = new ArrayList<>();
        }

        if (mlistPeliculas == null) {
            mlistPeliculas = new ArrayList<>();
        }

        if (mlistNetflix == null) {
            mlistNetflix = new ArrayList<>();
        }

        if (mlistEstrenos == null) {
            mlistEstrenos = new ArrayList<>();
        }

        List<modelFilm> tbListSeries = tinyDB.getListObject(TBlistSeries, modelFilm.class);
        List<modelFilm> tbListPeliculas = tinyDB.getListObject(TBlistPeliculas, modelFilm.class);
        List<modelFilm> tbListContenido = tinyDB.getListObject(TBlistContenido, modelFilm.class);
        List<modelFilm> tbListNetflix = tinyDB.getListObject(TBlistNetflix, modelFilm.class);
        List<modelFilm> tbListEstrenos = tinyDB.getListObject(TBlistEstrenos, modelFilm.class);


        //   contenidoCargando.setVisibility(VISIBLE);


        //verificar cual esta vacia y rellenarla
        if (tbListSeries.isEmpty() || tbListPeliculas.isEmpty() || tbListContenido.isEmpty() || tbListNetflix.isEmpty() || tbListEstrenos.isEmpty()) {


            Toast.makeText(mContext, "Cargando contenido desde Internet, esto podria tardar un momento.", Toast.LENGTH_LONG).show();

            Query database = FirebaseDatabase.getInstance().getReference().child("data");


            //   TipDialog.show((AppCompatActivity) mContext,"Cargando contenido con la conexion a Internet, si es la primera vez, podria tardar algunos minutos, por favor sea paciente.", TipDialog.TYPE.WARNING).setCancelable(false).setTipTime(90000);


            mixpanel.timeEvent(mixContenidoCargado);

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mlistSeries.removeAll(mlistSeries);
                    mlistContenido.removeAll(mlistContenido);
                    mlistPeliculas.removeAll(mlistPeliculas);
                    mlistNetflix.removeAll(mlistNetflix);
                    mlistEstrenos.removeAll(mlistEstrenos);

                    tbListSeries.removeAll(tbListSeries);
                    tbListContenido.removeAll(tbListContenido);
                    tbListPeliculas.removeAll(tbListPeliculas);
                    tbListNetflix.removeAll(tbListNetflix);
                    tbListEstrenos.removeAll(tbListEstrenos);

                    tinyDB.putListObject(TBlistPeliculas, tbListPeliculas);
                    tinyDB.putListObject(TBlistSeries, tbListSeries);
                    tinyDB.putListObject(TBlistContenido, tbListContenido);
                    tinyDB.putListObject(TBlistNetflix, tbListNetflix);
                    tinyDB.putListObject(TBlistEstrenos, tbListEstrenos);

                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {


                        //enlistar series
                        if (snapshot.child("info").child("tipo").exists()) {

                            String tipo = (String) snapshot.child("info").child("tipo").getValue();

                            if (tipo.toLowerCase().contains("serie")) {
                                modelFilm serie = snapshot.child("info").getValue(modelFilm.class);
                                mlistSeries.add(serie);
                            }
                        }


                        // enlistar contenido
                        if (snapshot.child("info").exists()) {


                            modelFilm film = snapshot.child("info").getValue(modelFilm.class);
                            //mlistContenido.add(film);
                            mlistContenido.add(film);


                        }


                        //enlistar peliculas
                        if (snapshot.child("info").child("tipo").exists()) {

                            String tipo = (String) snapshot.child("info").child("tipo").getValue();

                            if (tipo.toLowerCase().contains("pelicula")) {
                                modelFilm pelicula = snapshot.child("info").getValue(modelFilm.class);
                                mlistPeliculas.add(pelicula);
                            }
                        }



                        //enlistar netflix
                        if (snapshot.child("info").child("red").exists()) {

                            String red = (String) snapshot.child("info").child("red").getValue();

                            if (red.toLowerCase().contains("netflix")) {
                                modelFilm filmNetflix = snapshot.child("info").getValue(modelFilm.class);
                                mlistNetflix.add(filmNetflix);
                            }
                        } else {
                            snapshot.getRef().child("info").child("red").setValue("N/A");
                        }


                        //enlistar estrenos
                        if (snapshot.child("info").child("ano").exists()) {

                            String ano = snapshot.child("info").child("ano").getValue().toString();

                            if (ano.toLowerCase().contains("2020")) {
                                modelFilm filmEstreno = snapshot.child("info").getValue(modelFilm.class);
                                mlistEstrenos.add(filmEstreno);
                            }

                        } else {
                            snapshot.getRef().child("info").child("ano").setValue("N/A");
                        }

                    }


                    tinyDB.putListObject(TBlistPeliculas, mlistPeliculas);
                    tinyDB.putListObject(TBlistSeries, mlistSeries);
                    tinyDB.putListObject(TBlistContenido, mlistContenido);
                    tinyDB.putListObject(TBlistNetflix, mlistNetflix);
                    tinyDB.putListObject(TBlistEstrenos, mlistEstrenos);


                    Log.i(getClass().getName(), "SE HAN CARGADO TODAS LAS LISTAS DESDE EL SERVIDOR");
                    System.out.println("INFA: " + getClass().getSimpleName() + " SE HAN CARGADO TODAS LAS LISTAS DESDE EL SERVIDOR");


                    //region aleatorizar - organizar listas
                    Collections.shuffle(mlistPeliculas);
                    Collections.shuffle(mlistSeries);
                    Collections.shuffle(mlistContenido);
                    Collections.shuffle(mlistNetflix);
                    Collections.shuffle(mlistEstrenos);
                    /*
                    Collections.sort(mlistEstrenos, new Comparator<modelFilm>() {
                        public int compare(modelFilm o1, modelFilm o2) {
                            return extractInt(o1.getFechaActualizado()) - extractInt(o2.getFechaActualizado());
                        }

                        int extractInt(String s) {
                            if (s != null) {
                                String num = s.replaceAll("\\D", "").replace("2020","").replace("/","").substring(2,3);
                                System.out.println("SORT LISTA "+num);
                                return num.isEmpty() ? 0 : Integer.parseInt(num);
                            }
                            return 0;
                        }
                    });
                     */

                    //region comprobar adaptador y actualizar
                    if (mAdapterSerie != null) {
                        mAdapterSerie.notifyDataSetChanged();
                        WaitDialog.dismiss();
                        TipDialog.dismiss();
                    }

                    if (mAdapterBusqueda != null) {
                        mAdapterBusqueda.notifyDataSetChanged();
                        WaitDialog.dismiss();
                        TipDialog.dismiss();
                    }

                    if (mAdapterPelicula != null) {
                        mAdapterPelicula.notifyDataSetChanged();
                        WaitDialog.dismiss();
                        TipDialog.dismiss();
                    }

                    if (mAdapterNetflix != null) {
                        mAdapterNetflix.notifyDataSetChanged();
                        WaitDialog.dismiss();
                        TipDialog.dismiss();
                    }

                    if (mAdapterEstrenos != null) {
                        mAdapterEstrenos.notifyDataSetChanged();
                        WaitDialog.dismiss();
                        TipDialog.dismiss();
                    }

                    //endregion

                    /*
                    contenidoCargando.setVisibility(GONE);
                    WaitDialog.dismiss();
                     */


                    //region comprobar listas
                    if (mlistSeries == null || mlistSeries.size() == 0) {
                        //   if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                        TipDialog.show((AppCompatActivity) mContext, "Lista de series vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                        //CargarHome();

                    }

                    if (mlistPeliculas == null || mlistPeliculas.size() == 0) {
                        //   if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                        TipDialog.show((AppCompatActivity) mContext, "Lista de peliculas vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                        // CargarHome();


                    }


                    if (mlistContenido == null || mlistContenido.size() == 0) {
                        //   if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                        TipDialog.show((AppCompatActivity) mContext, "Lista de contenido vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                        // CargarHome();

                    }

                    if (mlistNetflix == null || mlistNetflix.size() == 0) {
                        //   if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                        TipDialog.show((AppCompatActivity) mContext, "Lista de netflix vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                        // CargarHome();
                    }

                    if (mlistEstrenos == null || mlistEstrenos.size() == 0) {
                        //   if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                        TipDialog.show((AppCompatActivity) mContext, "Lista de estrenos vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                        // CargarHome();

                    }

                    //endregion


                    mixpanel.track(mixContenidoCargado);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    TipDialog.dismiss();
                    WaitDialog.dismiss();
                    TipDialog.show((AppCompatActivity) mContext, "Error al cargar los datos, por favor verifique su conexión a internet o vuelva abrir la app", TipDialog.TYPE.ERROR).setCancelable(true).setTipTime(20000);
                }


            });


        } else {

            System.out.println("INFA: YA HAY LISTA EN TEMPORTAL, NO SE RELLENARA DESDE EL SERVIDOR");

            //region ACTUALIZAR INFO LISTA YA
            //remover y volver a cargar para evitar informacion duplicada
            mlistSeries.removeAll(mlistSeries);
            mlistSeries.addAll(tbListSeries);
            mlistPeliculas.removeAll(mlistPeliculas);
            mlistPeliculas.addAll(tbListPeliculas);
            mlistContenido.removeAll(mlistContenido);
            mlistContenido.addAll(tbListContenido);
            mlistNetflix.removeAll(mlistNetflix);
            mlistNetflix.addAll(tbListNetflix);
            mlistEstrenos.removeAll(mlistEstrenos);
            mlistEstrenos.addAll(tbListEstrenos);
            //region aleatorizar lista
            Collections.shuffle(mlistSeries);
            Collections.shuffle(mlistPeliculas);
            Collections.shuffle(mlistContenido);
            Collections.shuffle(mlistNetflix);
            Collections.shuffle(mlistEstrenos);
            /*
            Collections.sort(mlistEstrenos, new Comparator<modelFilm>() {
                public int compare(modelFilm o1, modelFilm o2) {
                    return extractInt(o1.getFechaActualizado()) - extractInt(o2.getFechaActualizado());
                }

                int extractInt(String s) {
                    if (s != null) {
                        String num = s.replaceAll("\\D", "").replace("2020","").replace("/","").substring(2,3);
                        System.out.println("SORT LISTA "+num);

                        return num.isEmpty() ? 0 : Integer.parseInt(num);
                    }
                    return 0;
                }
            });
             */
            //endregion



            //region asegurar adapters

            if (mAdapterSerie != null) {
                mAdapterSerie.notifyDataSetChanged();
            }

            if (mAdapterBusqueda != null) {
                mAdapterBusqueda.notifyDataSetChanged();
            }

            if (mAdapterPelicula != null) {
                mAdapterPelicula.notifyDataSetChanged();
            }

            if (mAdapterNetflix != null) {
                mAdapterNetflix.notifyDataSetChanged();
            }

            if (mAdapterEstrenos != null) {
                mAdapterEstrenos.notifyDataSetChanged();
            }

            //endregion

            //endregion

            WaitDialog.dismiss();
            TipDialog.dismiss();


            /*
            contenidoCargando.setVisibility(GONE);
            WaitDialog.dismiss();

             */

            Log.i(metodos.class.getName(), " SE HAN CARGADO TODAS LAS LISTAS DESDE EL CACHE");
            System.out.println("INFA: " + metodos.class.getSimpleName() + " SE HAN CARGADO TODAS LAS LISTAS DESDE EL CACHE");



            /*
            for (int i = 0; tbListSeries.size() > i; i++) {
                System.out.println("INFA: tbListContenido ya almacenado" + tbListSeries.get(i).getNombre());
            }

             */

        }

    }

    public static void AlDetectarNuevoCambio(Context mContext, TinyDB tinyDB) {


        Query database = FirebaseDatabase.getInstance().getReference().child("data");


        database.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //  Toast.makeText(mContext, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                //  System.out.println("INFA: AlDetectarNuevoCambio: dataSnapshot  onChildAdded - - - "+s);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Toast.makeText(mContext, "onChildChanged + "+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                //   System.out.println("INFA: AlDetectarNuevoCambio:  onChildChanged - - - "+dataSnapshot.getRef());


                List<modelFilm> tbListContenido = tinyDB.getListObject(TBlistContenido, modelFilm.class);

                String id = dataSnapshot.getKey();


                if (!tbListContenido.isEmpty()) {

                    for (int i = 0; tbListContenido.size() > i; i++) {

                        if (tbListContenido.get(i).getId() != null) {
                            if (tbListContenido.get(i).getId().contains(id)) {
                                // Toast.makeText(mContext, "EL CAMBIO ES DE UN ITEM EN TEMPORAL id: "+id, Toast.LENGTH_LONG).show();
                            } else if (!tbListContenido.get(i).getId().contains(id) && i > tbListContenido.size()) {
                                //  Toast.makeText(mContext, "EL CAMBIO ES DE UN ITEM que NO esta EN TEMPORAL", Toast.LENGTH_LONG).show();
                                if (tbListContenido != null || !tbListContenido.isEmpty()) {
                                    tbListContenido.removeAll(tbListContenido);
                                    tinyDB.putListObject(TBlistContenido, tbListContenido);
                                }

                                class CargarListas extends AsyncTask<Void, Integer, String> {
                                    String TAG = getClass().getSimpleName();

                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        Log.d(TAG + " PreExceute", "On pre Exceute......");
                                        //   Toast.makeText(act_main.this, "cargando", Toast.LENGTH_SHORT).show();


                                    }

                                    protected String doInBackground(Void... arg0) {
                                        Log.d(TAG + " DoINBackGround", "On doInBackground...");


                                        Thread thread = new Thread() {
                                            @Override
                                            public void run() {


                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //do stuff like remove view etc


                                                        getListaCategorias(mContext);
                                                        getListaDeFirebase(mContext);
                                                        getListaDestacados(mContext);


                                                    }
                                                });


                                            }

                                            ;
                                        };
                                        thread.start();

                                        return "You are at PostExecute";
                                    }

                                    protected void onProgressUpdate(Integer... a) {
                                        super.onProgressUpdate(a);
                                        Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

                                    }

                                    protected void onPostExecute(String result) {
                                        super.onPostExecute(result);

                                        WaitDialog.show((AppCompatActivity) mContext, "Cargando nuevo contenido...").setCancelable(true);

                                        Log.d(TAG + " onPostExecute", "" + result);
                                    }
                                }


                                new CargarListas().execute();

                            }
                        }

                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //  Toast.makeText(mContext, "onChildRemoved", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //   Toast.makeText(mContext, "onChildMoved", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //    Toast.makeText(mContext, "onCancelled "+databaseError, Toast.LENGTH_SHORT).show();

            }


        });

    }

    public static void AleatorizarListas() {

        if (mlistPeliculas != null && mAdapterPelicula != null) {
            Collections.shuffle(mlistPeliculas);
            mAdapterPelicula.notifyDataSetChanged();
        }
        if (mlistContenido != null && mAdapterBusqueda != null) {
            Collections.shuffle(mlistContenido);
            mAdapterBusqueda.notifyDataSetChanged();
        }
        if (mlistSeries != null && mAdapterSerie != null) {
            Collections.shuffle(mlistSeries);
            mAdapterSerie.notifyDataSetChanged();
        }
        if (mlistDestacado != null && mAdapterDestacado != null) {
            Collections.shuffle(mlistDestacado);
            mAdapterDestacado.notifyDataSetChanged();
        }
        if (mlistNetflix != null && mlistNetflix != null) {
            Collections.shuffle(mlistNetflix);
            mAdapterNetflix.notifyDataSetChanged();
        }
        if (mlistEstrenos != null && mlistEstrenos != null) {
            Collections.shuffle(mlistEstrenos);
            mAdapterEstrenos.notifyDataSetChanged();
        }


    }

    public static void MarcarDesmarcarVisto(Context mContext, String idFilm, int numeroEpisodio, boolean marcar) {

        TinyDB tinyDB = new TinyDB(mContext);

        /** se usa como key "historial-" mas el id, para crear un lista unica segun el id del film
         * en la lista se guardara un valor int que es la posicion del capitulo en la lista
         */


        String key = "historial-" + idFilm;

        List<Integer> antiguaLista = tinyDB.getListInt(key);

        if (marcar) {
            List<Integer> nuevaLista = antiguaLista;
            nuevaLista.add(numeroEpisodio);
            tinyDB.putListInt(key, nuevaLista);
            //  TipDialog.show((AppCompatActivity) mContext,"como visto.", R.drawable.learn_ic_like).setCancelable(true);
            if (mAdapterEpisodio != null) {
                mAdapterEpisodio.notifyDataSetChanged();
            }
        } else {
            List<Integer> nuevaLista = antiguaLista;
            if (nuevaLista.size() != 0 && nuevaLista != null) {
                for (int i = 0; nuevaLista.size() > i; i++) {
                    if (nuevaLista.get(i) == numeroEpisodio) {
                        nuevaLista.remove(i);
                        //  TipDialog.show((AppCompatActivity) mContext,"Desmarcado como visto.", TipDialog.TYPE.SUCCESS).setCancelable(true);

                        if (mAdapterEpisodio != null) {
                            mAdapterEpisodio.notifyDataSetChanged();
                        }


                    }

                    tinyDB.putListInt(key, nuevaLista);


                    if (mlistEpisodios != null && mAdapterEpisodio != null && rvEpisodios != null) {
                        mAdapterEpisodio = new adapterEpisodio(mContext, mlistEpisodios);
                        rvEpisodios.setAdapter(mAdapterEpisodio);
                    }


                    break;


                }
            }
        }


    }

    public static void getListaCategorias(Context mContext) {


        if (mlistCategoria == null) {
            mlistCategoria = new ArrayList<>();
        }

        Query database = FirebaseDatabase.getInstance().getReference().child("otros").child("categorias").orderByChild("nombre");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistCategoria.removeAll(mlistCategoria);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {


                    modelCategoria categoria = snapshot.getValue(modelCategoria.class);
                    mlistCategoria.add(categoria);


                }


                mAdapterCategoria.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }

    public static void getListaGenero(final String Genero, final Context mContext) {


        //  contenidoCargando.setVisibility(VISIBLE);


        Query database = FirebaseDatabase.getInstance().getReference().child("data");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistGenero.removeAll(mlistGenero);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {


                    if (snapshot.child("info").child("categoria").exists()) {

                        String genero = snapshot.child("info").child("categoria").getValue().toString();

                        if (genero.toLowerCase().contains(Genero.toLowerCase())) {
                            modelFilm generoFilm = snapshot.child("info").getValue(modelFilm.class);
                            mlistGenero.add(generoFilm);
                        }
                    }

                }


                for (int i = 0; mlistGenero.size() > i; i++) {
                    System.out.println("LOGNETT::GENERO " + mlistGenero.get(i).getNombre());
                }


                mAdapterBusqueda.notifyDataSetChanged();
                mAdapterCategoria.notifyDataSetChanged();

                 /*
                        if (contenidoCargando != null && contenidoCargando.getVisibility() == VISIBLE) {
                            contenidoCargando.setVisibility(GONE);
                        }
                        */
                if (mlistGenero == null || mlistGenero.size() == 0) {
                    if (contenidoSearch.getVisibility() == VISIBLE && contenidoSearch != null) {
                        WaitDialog.show((AppCompatActivity) mContext, "Lista vacia", TipDialog.TYPE.ERROR).setCancelable(true);
                        CargarHome(mContext);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }

    public static void getListaContenido(Context mContext) {
        setDebug("metodos", "getListaContenido", "d", "Cargando getListaContenido...", setDebugActivo);

        WaitDialog.show((AppCompatActivity) mContext, "Cargando Contenido..").setCancelable(true);


        TinyDB tinyDB = new TinyDB(mContext);

        List<modelFilm> tbListContenido = tinyDB.getListObject(TBlistContenido, modelFilm.class);

        //  contenidoCargando.setVisibility(VISIBLE);

        if (tbListContenido.isEmpty()) {


            setDebug("metodos", "getListaContenido", "d", "NO hay listas en temporal, se cargaran desde FB", setDebugActivo);


            Query database = FirebaseDatabase.getInstance().getReference().child("data");


            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mlistContenido.removeAll(mlistContenido);
                    tbListContenido.removeAll(tbListContenido);

                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {


                        if (snapshot.child("info").exists()) {

                            setDebug("metodos", "getListaContenido", "d", "Agregando film a mlistContenido...", setDebugActivo);

                            modelFilm film = snapshot.child("info").getValue(modelFilm.class);
                            //mlistContenido.add(film);
                            mlistContenido.add(film);


                        }


                    }


                    //La primera vez se cargara mmlistContenido a la siguiente cargara la liste temporal ya que exite y no realiza nigun procedimiento de esto
                    tinyDB.putListObject(TBlistContenido, mlistContenido);
                    setDebug("metodos", "getListaContenido", "d", "Se agrego mlistContenido a lista temporal ", setDebugActivo);


                    Collections.shuffle(mlistContenido);


                    if (mAdapterBusqueda != null) {
                        mAdapterBusqueda.notifyDataSetChanged();
                    }

                    if (mAdapterSerie != null) {
                        mAdapterSerie.notifyDataSetChanged();
                    }

                    for (int i = 0; mlistContenido.size() > i; i++) {
                        System.out.println("LISTA mlistContenido " + mlistContenido.get(i).getId());
                    }
                    ;


                    WaitDialog.dismiss();


                    System.out.println("INFA: TAMAÑO LISTA mlistContenido --- " + mlistContenido.size());

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


        } else {

            System.out.println("INFA: YA HAY LISTA EN TEMPORTAL, NO SE RELLENARA DESDE EL SERVIDOR");

            //region ACTUALIZAR INFO LISTA YA
            //remover y volver a cargar para evitar informacion duplicada

            mlistContenido.removeAll(mlistContenido);
            mlistContenido.addAll(tbListContenido);
            //aleatorizar lista
            Collections.shuffle(mlistContenido);
            if (mAdapterBusqueda != null) {
                mAdapterBusqueda.notifyDataSetChanged();
            }
            //endregion
            System.out.println("INFA: CARGADO DESDE CACHE");
            setDebug("metodos", "getListaContenido", "d", "YA HAY items en la lista temporal, NO se cargara desde FB", setDebugActivo);
            setDebug("metodos", "getListaContenido", "d", "Tamaño lista temporas es igual a : " + tbListContenido.size(), setDebugActivo);


            // MostrarCargando(mContext);

            /*
            for (int i = 0; tbListContenido.size() > i; i++) {
                System.out.println("INFA: tbListContenido ya almacenado" + tbListContenido.get(i).getNombre());
            }


             */


            WaitDialog.dismiss();

        }

    }

    public static void CargarSearch() {
        contenidoSearch.setVisibility(VISIBLE);
        contenidoHome.setVisibility(GONE);
    }

    public static void getListaIdiomas(String idFilm, final Context mContext) {


        System.out.println("OBTENER EPISODIO: getListaIdiomas");


        Query database = FirebaseDatabase.getInstance().getReference().child("data").child(idFilm).child("episodios");


        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistIdiomas.removeAll(mlistIdiomas);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {


                    if (snapshot.exists()) {

                        String idioma = snapshot.getKey();
                        mlistIdiomas.add(idioma);
                        System.out.println("OBTENER EPISODIO: idioma desde firebase = " + idioma);


                    }


                }


                ElegirIdioma(mContext);


                /*
                mAdapterSerie.notifyDataSetChanged();

                 */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }

    public static void AgregarQuitarFavorito(Context mContext, boolean agregar, String idFilm) {


        String id = idFilm;

        TinyDB tinyDB = new TinyDB(mContext);
        ArrayList<String> antiguaLista = tinyDB.getListString(TBlistFavoritos);

        if (agregar) {
            ArrayList<String> nuevaLista = new ArrayList<>();
            nuevaLista.addAll(antiguaLista);
            nuevaLista.add(id);
            tinyDB.putListString(TBlistFavoritos, nuevaLista);
            TipDialog.show((AppCompatActivity) mContext, "Se agregó a favoritos.", R.drawable.learn_ic_like).setCancelable(true);
            if (ivFavorito != null) {
                ivFavorito.setImageDrawable(mContext.getResources().getDrawable(R.drawable.learn_ic_like));
            }
            if (mAdapterBusqueda != null) {
                mAdapterBusqueda.notifyDataSetChanged();
            }

        } else {
            ArrayList<String> nuevaLista = new ArrayList<>();
            nuevaLista.addAll(antiguaLista);
            if (!nuevaLista.isEmpty() && nuevaLista != null) {
                for (int i = 0; nuevaLista.size() > i; i++) {
                    if (nuevaLista.get(i).toLowerCase().contains(id)) {
                        nuevaLista.remove(i);
                        TipDialog.show((AppCompatActivity) mContext, "Se ha quitado de favoritos.", R.drawable.learn_ic_dislike).setCancelable(true);
                        if (ivFavorito != null) {
                            ivFavorito.setImageDrawable(mContext.getResources().getDrawable(R.drawable.learn_ic_dislike));
                        }
                        if (mAdapterBusqueda != null) {
                            mAdapterBusqueda.notifyDataSetChanged();
                        }


                    }


                }


                //  Toast.makeText(mContext, "Favoritos no esta vacia", Toast.LENGTH_SHORT).show();
                tinyDB.putListString(TBlistFavoritos, nuevaLista);
            }
        }


    }

    public static void initFirebase(final Context mContext, TinyDB tinyDB) {


        try {


            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("otros");
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {


                        //region ACTUALIZAR APP

                        if (dataSnapshot.child("actualizacion").exists()) {
                            setDebug("metodos", "initFirebase", "d", "Confirmando actualizacion disponible...", setDebugActivo);


                            //para la version pelisplus
                            int versionNueva = Integer.parseInt(dataSnapshot.child("actualizacion").child("version").getValue().toString());
                            Boolean estado = dataSnapshot.child("actualizacion").child("estado").getValue(Boolean.class);
                            String urlDescarga = dataSnapshot.child("actualizacion").child("urlDescarga").getValue(String.class);
                            Boolean cancelable = dataSnapshot.child("actualizacion").child("cancelable").getValue(Boolean.class);
                            String mensaje = dataSnapshot.child("actualizacion").child("mensaje").getValue().toString();


                            //si es version la mesa,, remplazar los valores
                            if (BuildConfig.APPLICATION_ID.toLowerCase().contains("mesa")) {
                                versionNueva = Integer.parseInt(dataSnapshot.child("actualizacion").child("app-lamesa").child("version").getValue().toString());
                                estado = dataSnapshot.child("actualizacion").child("app-lamesa").child("estado").getValue(Boolean.class);
                                urlDescarga = dataSnapshot.child("actualizacion").child("app-lamesa").child("urlDescarga").getValue(String.class);
                                cancelable = dataSnapshot.child("actualizacion").child("app-lamesa").child("cancelable").getValue(Boolean.class);
                                mensaje = dataSnapshot.child("actualizacion").child("app-lamesa").child("mensaje").getValue().toString();
                            }


                            int versionActual = BuildConfig.VERSION_CODE;

                            if (estado == true && versionNueva > versionActual) {

                                try {


                                    if (mContext != null) {
                                        setDebug("metodos", "initFirebase", "d", "Mostrando dialogo de actualización...", setDebugActivo);


                                        DialogoActualizar(mContext, versionNueva, mensaje, urlDescarga, cancelable);


                                    }
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                    /*
                                    File archivoUpdate = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/update.apk");
                                    if (archivoUpdate.exists()){
                                        archivoUpdate.delete();
                                    }

                                     */

                            }


                        }


                        //region POCESO DETECTAR CAMBIOS EN DATA

                        if (dataSnapshot.child("utilidad").child("fechaCambiosData").exists()) {
                            // se comprueba fechas
                            String fechaCambioData = (String) dataSnapshot.child("utilidad").child("fechaCambiosData").getValue();
                            String fechaUltimoCambio = tinyDB.getString(TBfechaCambiosData);

                            setDebug("metodos", "initFirebase", "d", "fechaCambioData == " + fechaCambioData, setDebugActivo);
                            setDebug("metodos", "initFirebase", "d", "fechaUltimoCambio == " + fechaUltimoCambio, setDebugActivo);

                            // si las fechas no coinciden, es porque hay cambios por realizar, se borra lista y se recarga
                            if (!fechaCambioData.toLowerCase().contains(fechaUltimoCambio) || fechaUltimoCambio.isEmpty()) {
                                List<modelFilm> tbListContenido = tinyDB.getListObject(TBlistContenido, modelFilm.class);
                                if (tbListContenido != null) {
                                    setDebug("metodos", "initFirebase", "d", "Actualizando nuevo contenido..", setDebugActivo);
                                    tbListContenido.removeAll(tbListContenido);
                                    tinyDB.putListObject(TBlistContenido, tbListContenido);
                                    tinyDB.putString(TBfechaCambiosData, fechaCambioData);
                                    getListaDeFirebase(mContext);
                                }
                            } else {
                                setDebug("metodos", "initFirebase", "d", "No hay contenido por actualizar..", setDebugActivo);
                            }
                        } else {
                            setDebug("metodos", "initFirebase", "d", "fechaCambiosData no existe en FB", setDebugActivo);
                        }

                        //endregion


                    } catch (NumberFormatException e) {
                        setDebug("metodos", "initFirebase", "e", e.getMessage(), setDebugActivo);
                        e.printStackTrace();

                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                }
            });


            //endregion


        } catch (Exception e) {
            setDebug("metodos", "initFirebase", "e", e.getMessage(), setDebugActivo);
            e.printStackTrace();
        }

    }

    public static void setDebug(String CLASS, String metodo, String tipo, String msg, boolean activo) {

        String TAG = "setDebug";
        String MENSAJE = CLASS + "-" + metodo + " = " + tipo + "-" + "msg: " + msg;


        if (activo) {
            switch (tipo) {
                case "e":
                    Log.e(TAG, MENSAJE);
                    break;

                case "w":
                    Log.w(TAG, MENSAJE);
                    break;

                case "d":
                    Log.d(TAG, MENSAJE);
                    break;

                case "i":
                    Log.i(TAG, MENSAJE);
                    break;

                case "v":
                    Log.v(TAG, MENSAJE);
                    break;


            }
        }

    }

    public static void DialogoActualizar(Context mContext, int version, String mensaje, String urlDescarga, boolean cancelable) {
        setDebug("metodos", "DialogoActualizar", "d", "Mostrando DialogoActualizar...", setDebugActivo);


        String obligatorio = "Actualización obligatoria";
        String titulo = "¡Actualización disponible! \nversión " + String.valueOf(version);
        if (cancelable) {
            obligatorio = "(Actualización opcional)";
        } else {
            obligatorio = "(Actualización obligatoria)";
        }

        titulo = titulo + "\n" + obligatorio;
        mensaje = mensaje + "\n(Es necesario el permiso de almacenamiento y de instalación desde " + mContext.getResources().getString(R.string.app_name) + ")"+"\n Nota: Si la actualización es obligatoria y NO funciona, puede descargar la ultima versión desde la pagina web.";

        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        MessageDialog.show((AppCompatActivity) mContext, titulo, mensaje, "ACTUALIZAR").setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN)).setButtonOrientation(LinearLayout.VERTICAL).setOtherButton("VISITAR PAGINA").setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                //    Toast.makeText(mContext, "sdasdasdd", Toast.LENGTH_SHORT).show();
                AbrirPagina(mContext);
                return false;
            }
        }).setCancelable(cancelable).setOnOkButtonClickListener((baseDialog, v) -> {
            DescargarActualizacion(mContext, urlDescarga);
            return false;
        });


    }

    public static <mContext> void DescargarActualizacion(Context mContext, String urlDescarga) {


        if (urlDescarga.contains("http") || urlDescarga.contains("https")) {

            PermissionListener permissionlistener = new PermissionListener() {


                @Override
                public void onPermissionGranted() {
                    //region DESCARGAR ARCHIVO

                    OkHttpClient client = new OkHttpClient.Builder().build();
                    DownloadManager manager = new DownloadManager.Builder().context(mContext)
                            .downloader(OkHttpDownloader.create(client))
                            .threadPoolSize(3)
                            .logger(new Logger() {
                                @Override
                                public void log(String message) {
                                    Log.d("TAG", message);
                                }
                            })
                            .build();


                    File toInstall = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));

                    if (!toInstall.exists()) {
                        toInstall.mkdirs();
                    }

                    String destinoUpdate = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update.apk";


                    DownloadRequest request =
                            new DownloadRequest.Builder()
                                    .url(urlDescarga)
                                    .retryTime(5)
                                    .retryInterval(2, TimeUnit.SECONDS)
                                    .progressInterval(1, TimeUnit.SECONDS)
                                    .priority(Priority.HIGH)
                                    .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI)
                                    .allowedNetworkTypes(DownloadRequest.NETWORK_MOBILE)
                                    .destinationFilePath(destinoUpdate)
                                    .downloadCallback(new DownloadCallback() {
                                        @Override
                                        public void onStart(int downloadId, long totalBytes) {
                                            WaitDialog.dismiss();
                                            WaitDialog.show((AppCompatActivity) mContext, "Iniciando descarga...");
                                        }

                                        @Override
                                        public void onRetry(int downloadId) {
                                            WaitDialog.dismiss();
                                            WaitDialog.show((AppCompatActivity) mContext, "Intentando de nuevo...");
                                        }

                                        @Override
                                        public void onProgress(int downloadId, long bytesWritten, long totalBytes) {

                                            if (totalBytes != 0) {

                                                long porcentaje = ((bytesWritten * 100) / totalBytes);


                                                WaitDialog.dismiss();
                                                WaitDialog.show((AppCompatActivity) mContext, "Descargando... %" + porcentaje).setCancelable(false);
                                                System.out.println("bytesWritten " + bytesWritten + "  totalBytes " + totalBytes);
                                            } else {
                                                ReportarEpisodio(mContext, "Error en descarga, total bytes es 0", "0000", "0", "", "", "");
                                                Toast.makeText(mContext, "Error en la descarga, enviando reporte..", Toast.LENGTH_SHORT).show();
                                            }


                                        }

                                        @Override
                                        public void onSuccess(int downloadId, String filePath) {
                                            WaitDialog.dismiss();
                                            WaitDialog.show((AppCompatActivity) mContext, "Descarga completa.").setCancelable(true).setTip(WaitDialog.TYPE.SUCCESS);
                                            InstalarApp(mContext, new File(filePath));

                                        }

                                        @Override
                                        public void onFailure(int downloadId, int statusCode, String errMsg) {
                                            WaitDialog.dismiss();
                                            WaitDialog.show((AppCompatActivity) mContext, errMsg).setCancelable(true).setTip(WaitDialog.TYPE.ERROR);
                                            ReportarEpisodio(mContext, "Error en descarga, " + errMsg, "0001", "0", "", "", "");
                                            Toast.makeText(mContext, "Error en la descarga, " + errMsg, Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .build();


                    manager.add(request);


                        /*

                    // stop single
                    manager.cancel(downloadId);
                    // stop all
                    manager.cancelAll();


                     */

                }


                //endregion


                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    TipDialog.show((AppCompatActivity) mContext, "Son necesarios  permisos de almacenamiento para realizar la descarga.", TipDialog.TYPE.WARNING).setTipTime(60000).setCancelable(true);
                }


            };


            TedPermission.with(mContext)
                    .

                            setPermissionListener(permissionlistener)
                    .

                            setDeniedMessage("Si no habilita los permisos de almacenamiento no se podra descargar nuevas actualizaciones, ni usar cache para la carga de videos mas rapida\n\nPuede tambien hacerlo manualmente en [Setting] > [Permission]")
                    .

                            setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .

                            check();


        } else {
            TipDialog.show((AppCompatActivity) mContext, "Error al descargar, por favor intente mas tarde.", TipDialog.TYPE.ERROR).setCancelable(true).setTipTime(10000);

        }


    }

    public static void InstalarApp(Context mContext, File ubicacionApk) {
        File toInstall = ubicacionApk;


        //region MIX mixActualizarApp AMPLITUDE

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        //region MIX mixCompartirApp para estadisticas
        JSONObject props = new JSONObject();
        try {
            props.put("Fecha", fecha);
            Bundle params = new Bundle();
            params.putString("Fecha", fecha);



            mFirebaseAnalytics.logEvent(mixActualizarApp, params);
            mixpanel.track(mixActualizarApp, props);
            Amplitude.getInstance().logEvent(mixActualizarApp, props);
        } catch (JSONException e) {
            e.printStackTrace();
        }








        //endregion


        if (toInstall.exists()) {

            Intent install;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(
                        mContext,
                        BuildConfig.APPLICATION_ID + ".provider",
                        new File(String.valueOf(toInstall))
                );
                install = new Intent(Intent.ACTION_VIEW);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                install.setData(contentUri);
                mContext.startActivity(install);
                // finish()
            } else {
                install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(Uri.fromFile(toInstall),
                        "application/vnd.android.package-archive");
                mContext.startActivity(install);

                // finish()
            }

        }

    }

    public static void getFavorito(final Context mContext, String idFilm) {
        setDebug("metodos", "getFavorito", "d", "Cargnado getFavorito...", setDebugActivo);


        try {


            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("data").child(String.valueOf(idFilm)).child("info");
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    try {


                        if (dataSnapshot.exists()) {

                            setDebug("metodos", "getFavorito", "d", "Agregando favorito..", setDebugActivo);

                            modelFilm film = dataSnapshot.getValue(modelFilm.class);
                            mlistFavoritos.add(film);


                            mAdapterBusqueda.notifyDataSetChanged();

                            mAdapterCategoria.notifyDataSetChanged();


                        /*
                        if (contenidoCargando != null && contenidoCargando.getVisibility() == VISIBLE) {
                            contenidoCargando.setVisibility(GONE);
                        }
                        */

                        }

                    } catch (NumberFormatException e) {
                        setDebug("metodos", "getFavorito", "e", e.getMessage(), setDebugActivo);
                        e.printStackTrace();

                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                }
            });


            //endregion


            //endregion


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static void CompartirApp(Context mContext) {


        try {


            //region MIX mixActualizarApp AMPLITUDE

            // para la fecha
            DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
            String fecha = df.format(Calendar.getInstance().getTime());

            //region MIX mixCompartirApp para estadisticas
            JSONObject props = new JSONObject();
            try {
                props.put("Fecha", fecha);
                //para FB
                Bundle params = new Bundle();
                params.putString("Fecha", fecha);



                mFirebaseAnalytics.logEvent(mixCompartirApp, params);
                mixpanel.track(mixCompartirApp, props);
                Amplitude.getInstance().logEvent(mixCompartirApp, props);
            } catch (JSONException e) {
                e.printStackTrace();
            }




            //endregion


            String saltoDeLinea = "\n";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name) + " - " + "La mejor alternativa de Netflix totalmente gratis");
            String infoApp = "Disfruta de: " + saltoDeLinea + "*Ver peliculas y series" + saltoDeLinea + "*Añadir a tus favoritos" + saltoDeLinea + "*Videos en calidad HD" + saltoDeLinea + "*Ver video en ventana flotante" + saltoDeLinea + "y mucho mas..." + saltoDeLinea;
            String shareMessage = "Para mas información, descarga la app aqui:" + "\n\n";
            shareMessage = shareMessage + "https://repelisplusapp.page.link/verpelisonline";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);


            mContext.startActivity(Intent.createChooser(shareIntent, "Compartir con:"));
        } catch (Exception e) {
            //e.toString();
        }


    }

    public static void AbrirPagina(Context mContext) {


        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://repelisplusapp.page.link/verpelisonline")));
        } catch (Exception e) {
            //e.toString();
        }


    }

    public static void SolicitarFilm(Context mContext) {


        String saltoDeLinea = "\n";
        String titulo = "Solicitar contenido";
        String mensaje = "¿No está tú pelicula o serie favorita?" + saltoDeLinea + saltoDeLinea + "Dinos cual es y según la disponibilidad de el film, estará en " + mContext.getResources().getString(R.string.app_name) + " en un lapso de 24 horas como maximo" + saltoDeLinea + saltoDeLinea + "(Esta app es nueva, con tus peticiones nos ayudaras a incrementar el contenido y que haya solo de calidad)";
        String hintText = "¿Pelicula/Serie-Temporada?";


        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        InputDialog.build((AppCompatActivity) mContext)
                //   .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                //  .setInputText("111111")
                .setOkButton("SOLICITAR", new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                        if (inputStr.equals("")) {
                            TipDialog.show((AppCompatActivity) mContext, "El espacio no puede estar vacío", TipDialog.TYPE.ERROR);
                            SolicitarFilm(mContext);
                            return false;
                        } else {
                            EnviarSolicitud(mContext, inputStr);
                            return true;
                        }
                    }
                })
                //  .setCancelButton("CANCELAR")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setHintText(hintText)
                .setInputInfo(new InputInfo()
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .setTextInfo(new TextInfo()
                                .setFontColor(mContext.getResources().getColor(R.color.learn_colorPrimary))
                        )
                )
                .setCancelable(true)
                .show();
    }

    public static void DialogoSugerencia(Context mContext) {


        String saltoDeLinea = "\n";
        String titulo = "Enviar sugerencia";
        String mensaje = "Aqui puedes enviar cualquier sugerencia que desees" + saltoDeLinea + saltoDeLinea + "Esto nos ayudara a implementar nuevo contenido y/o mejoras";
        String hintText = "Sugerencia";


        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        InputDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                //  .setInputText("111111")
                .setOkButton("ENVIAR", new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                        if (inputStr.equals("")) {
                            TipDialog.show((AppCompatActivity) mContext, "El espacio no puede estar vacío", TipDialog.TYPE.ERROR);
                            SolicitarFilm(mContext);
                            return false;
                        } else {
                            EnviarSugerencia(mContext, inputStr);
                            return true;
                        }
                    }
                })
                .setCancelButton("CERRAR")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setHintText(hintText)
                .setInputInfo(new InputInfo()
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .setTextInfo(new TextInfo()
                                .setFontColor(mContext.getResources().getColor(R.color.learn_colorPrimary))
                        )
                )
                .setCancelable(true)
                .show();
    }

    public static void DialogoEnviarReporte(Context mContext, String idFilm, String idEpisodio, String nombreEpisodio, String nombreFilm) {


        String saltoDeLinea = "\n";
        String titulo = "¿No funciona este episodio?";
        String mensajeDialogo = "Si este episodio no funciona, reportarlo ayudara a solucionarlo en un lapso de 24 horas como maximo." + saltoDeLinea + saltoDeLinea + "(Enviar reportes nos ayuda a mantener el contenido funcionando, gracias por su colaboración)";
        String hintText = "Mensaje (opcional)";


        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        InputDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensajeDialogo)
                //  .setInputText("111111")
                .setOkButton("ENVIAR", new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {

                        ReportarEpisodio(mContext, inputStr, idFilm, idEpisodio, nombreEpisodio, nombreFilm, tbIdioma);
                        baseDialog.doDismiss();
                        return true;

                    }
                })
                .setCancelButton("SALIR")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setHintText(hintText)
                .setInputInfo(new InputInfo()
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .setTextInfo(new TextInfo()
                                .setFontColor(mContext.getResources().getColor(R.color.learn_colorPrimary))
                        )
                )
                .setCancelable(true)
                .show();
    }

    public static void getListaEpisodios(String idFilm, String Idioma, Context mContext) {


        WaitDialog.show((AppCompatActivity) mContext, "Cargando lista...").setCancelable(true);

        Query database = FirebaseDatabase.getInstance().getReference().child("data").child(String.valueOf(idFilm)).child("episodios").child(Idioma).orderByChild("idEpisodio");


        System.out.println("getListaEpisodios" + database.getRef().toString());

        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistEpisodios.removeAll(mlistEpisodios);


                try {

                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {

                        System.out.println("SASASAS--- ");


                        if (snapshot.exists()) {


                            modelEpisodio episodio = snapshot.getValue(modelEpisodio.class);
                            mlistEpisodios.add(episodio);
                            System.out.println("PEISODIO--- " + episodio);

                        } else {
                            System.out.println("NO EXISTE--- ");
                        }


                    }

                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    System.out.println("Error CATCH " + e.getMessage());

                }


                // metodo para ordenar lista


                if (mlistEpisodios != null && !mlistEpisodios.isEmpty()) {

                    Collections.sort(mlistEpisodios, new Comparator<modelEpisodio>() {
                        public int compare(modelEpisodio o1, modelEpisodio o2) {
                            return extractInt(o1.getIdEpisodio()) - extractInt(o2.getIdEpisodio());
                        }

                        int extractInt(String s) {
                            if (s != null) {
                                String num = s.replaceAll("\\D", "");
                                return num.isEmpty() ? 0 : Integer.parseInt(num);
                            }
                            return 0;
                        }
                    });

                }


                //region para la list view de edicion de episodios en act_add_episodio


                listEpisodiosListView = new ArrayList<>();


                for (int i = 0; mlistEpisodios.size() > i; i++) {
                    listEpisodiosListView.add(mlistEpisodios.get(i).getIdEpisodio() + " - " + mlistEpisodios.get(i).getNombre());
                }

                if (listEpisodiosListView != null && !listEpisodiosListView.isEmpty()) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.listview_text, listEpisodiosListView);


                    if (lvEpisodios != null) {
                        lvEpisodios.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }

                //endregion



                //depeniendo la cantidad de los episodios se mostrará el filtrar lista
                if (mlistEpisodios != null && !mlistEpisodios.isEmpty() && mlistEpisodios.size() > 10) {
                    etFiltrarEpisodios.setVisibility(VISIBLE);
                } else {
                    etFiltrarEpisodios.setVisibility(GONE);
                }


                mAdapterEpisodio.notifyDataSetChanged();
                WaitDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    public static void getUrlEpisodio(final Context mContext, String idFilm, String idioma, String idEpisodio) {


        try {


            System.out.println("OBTENER EPISODIO: EMPEZAR METODO OBTENER URL");


            WaitDialog.show((AppCompatActivity) mContext, "Opteniendo links...").setCancelable(false);


            if (mlistUrls == null) {
                mlistUrls = new ArrayList<>();
            }


            TinyDB tinyDB = new TinyDB(mContext);


            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("data").child(String.valueOf(idFilm)).child("episodios").child(idioma).child(String.valueOf(idEpisodio)).child("links");
            //   System.out.println("mref: "+mref);
            mref.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (mlistUrls != null) {
                        mlistUrls.removeAll(mlistUrls);

                        try {

                            for (DataSnapshot snapshot :
                                    dataSnapshot.getChildren()) {


                                if (snapshot.exists()) {

                                    String link = snapshot.getValue().toString();
                                    mlistUrls.add(link);
                                    System.out.println("LINKS: " + link);

                                }

                            }


/** comprobar lista
 * pasar a lista de opciones
 * guardar eleccion en tb como url para cargar
 * llamar webview
 */

                            //region COMPROBAR LISTA PARA LINK DE OPCIONES


                            if (mlistUrls != null && mlistUrls.size() > 1) {

                                DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
                                DialogSettings.theme = DialogSettings.THEME.DARK;

                                List<String> opcionMenu = new ArrayList<>();
                                for (int i = 0; mlistUrls.size() > i; i++) {


                                    int posision = i + 1;

                                    String opcion = "Opción " + posision;
                                    opcionMenu.add(opcion);


                                }


                                WaitDialog.dismiss();

                                BaseAdapter baseAdapter = new ArrayAdapter(mContext, com.kongzue.dialog.R.layout.item_bottom_menu_material, opcionMenu);


                                BottomMenu.show((AppCompatActivity) mContext, baseAdapter, new OnMenuItemClickListener() {


                                    @Override
                                    public void onClick(String text, int index) {


                                        if (mlistUrls.get(index).contains("https://") || mlistUrls.get(index).contains("http://")) {

                                            String linkEpisodio = mlistUrls.get(index);




                                            //region MIX mixEpisodioClic para estadisticas
                                            JSONObject props = new JSONObject();
                                            try {
                                                props.put("IdFilm", idFilm);
                                                props.put("Nombre", tbNombreFilm);
                                                props.put("IdEpisodio", idEpisodio);
                                                props.put("LinkEpisodio", linkEpisodio);
                                                //para FB
                                                Bundle params = new Bundle();
                                                params.putString("IdFilm", idFilm);
                                                params.putString("NombreFilm", tbNombreFilm);
                                                params.putString("IdEpisodio", idEpisodio);
                                                params.putString("LinkEpisodio",  mlistUrls.get(0));

                                                mFirebaseAnalytics.logEvent(mixEpisodioClic, params);
                                                mixpanel.track(mixEpisodioClic, props);
                                                Amplitude.getInstance().logEvent(mixEpisodioClic, props);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            //endregion


                                            tinyDB.putString(TBurlEpisodio,linkEpisodio);
                                            mContext.startActivity(new Intent(mContext, act_webview.class));

                                        } else {
                                            TipDialog.show((AppCompatActivity) mContext, "Hubo un error al cargar el video, por favor intente de nuevo mas tarde, o reportelo", TipDialog.TYPE.ERROR).setTipTime(5000).setCancelable(true);
                                            ReportarEpisodio(mContext, "No es un enlace http o https", idFilm, idEpisodio, tinyDB.getString(TBnombreEpisodio), tbNombreFilm, tbIdioma);
                                            Toast.makeText(mContext, "Hubo un error, se enviará un reporte para solucionarlo.", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }).setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER).setFontColor(Color.GRAY)).setTitle("SELECCIONAR SERVIDOR");


                            } else if (mlistUrls != null && !mlistUrls.isEmpty()) {

                                //region MIX mixEpisodioClic para estadisticas
                                JSONObject props = new JSONObject();

                                try {
                                    props.put("IdFilm", idFilm);
                                    props.put("NombreFilm", tbNombreFilm);
                                    props.put("IdEpisodio", idEpisodio);
                                    props.put("LinkEpisodio",  mlistUrls.get(0));
                                    //para FB
                                    Bundle params = new Bundle();
                                    params.putString("IdFilm", idFilm);
                                    params.putString("NombreFilm", tbNombreFilm);
                                    params.putString("IdEpisodio", idEpisodio);
                                    params.putString("LinkEpisodio",  mlistUrls.get(0));

                                    mFirebaseAnalytics.logEvent(mixEpisodioClic, params);
                                    mixpanel.track(mixEpisodioClic, props);
                                    Amplitude.getInstance().logEvent(mixEpisodioClic, props);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //endregion


                                WaitDialog.dismiss();
                                tinyDB.putString(TBurlEpisodio, mlistUrls.get(0));
                                mContext.startActivity(new Intent(mContext, act_webview.class));


                            } else {
                                WaitDialog.dismiss();
                                TipDialog.show((AppCompatActivity) mContext, "Hubo un error al cargar el video, por favor intente de nuevo mas tarde, o reportelo", TipDialog.TYPE.ERROR).setTipTime(5000).setCancelable(true);
                                //  ReportarEpisodio(mContext, "auto reportado", idFilm, numeroEpisodio2,tinyDB.getString(TBnombreEpisodio), tbNombreFilm, tbIdioma);
                            }

                            //endregion


                        } catch (NumberFormatException e) {
                            e.printStackTrace();

                        }


                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                    WaitDialog.dismiss();
                    TipDialog.show((AppCompatActivity) mContext, "Hubo un error al cargar el video", TipDialog.TYPE.ERROR);
                    ReportarEpisodio(mContext, "databaseError = " + databaseError, idFilm, idEpisodio, tinyDB.getString(TBnombreEpisodio), tbNombreFilm, tbIdioma);
                }

            });


            //endregion


            //endregion


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static void getUrlEpisodioEditar(final Context mContext, String idFilm, String idioma, String idEpisodio) {


        try {


            System.out.println("OBTENER EPISODIO: EMPEZAR METODO OBTENER URL");


            WaitDialog.show((AppCompatActivity) mContext, "Opteniendo links...").setCancelable(true);


            if (mlistUrls == null) {
                mlistUrls = new ArrayList<>();
            }


            TinyDB tinyDB = new TinyDB(mContext);


            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("data").child(String.valueOf(idFilm)).child("episodios").child(idioma).child(String.valueOf(idEpisodio)).child("links");
            //   System.out.println("mref: "+mref);
            mref.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (mlistUrls != null) {
                        mlistUrls.removeAll(mlistUrls);

                        try {

                            for (DataSnapshot snapshot :
                                    dataSnapshot.getChildren()) {


                                if (snapshot.exists()) {

                                    String link = snapshot.getValue().toString();
                                    mlistUrls.add(link);
                                    System.out.println("LINKS: " + link);

                                }

                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.listview_text, mlistUrls);


                            if (lvUrls != null) {
                                lvUrls.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }


                            WaitDialog.dismiss();


                        } catch (NumberFormatException e) {
                            e.printStackTrace();

                        }


                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                    WaitDialog.dismiss();
                    TipDialog.show((AppCompatActivity) mContext, "Hubo un error al cargar el video", TipDialog.TYPE.ERROR);
                    ReportarEpisodio(mContext, "databaseError = " + databaseError, idFilm, idEpisodio, tinyDB.getString(TBnombreEpisodio), tbNombreFilm, tbIdioma);
                }

            });


            //endregion


            //endregion


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static void animacionCargandoTop(boolean encender) {

        if (encender) {
            if (animacionCargandoTop != null && animacionCargandoTop.getVisibility() == GONE) {
                animacionCargandoTop.setVisibility(VISIBLE);
            } else {
                animacionCargandoTop.setVisibility(GONE);
            }
        }
    }

    public static String CheckReproductor(Context mContext, TinyDB tinyDB) {


        String reproductor = tinyDB.getString(TBreproductor);


        return reproductor;

    }

    public static void DialogoEnviarReporteFilm(Context mContext, String idFilm, String nombreFilm) {


        List<String> opcionMenu = new ArrayList<>();
        opcionMenu.add("Información incorrecta");
        opcionMenu.add("NO carga algún elemento");
        opcionMenu.add("Falta información");
        opcionMenu.add("Falta episodios");
        opcionMenu.add("Fallan varios episodios");


//您自己的Adapter

        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        BaseAdapter baseAdapter = new ArrayAdapter(mContext, com.kongzue.dialog.R.layout.item_bottom_menu_material, opcionMenu);


        BottomMenu.show((AppCompatActivity) mContext, baseAdapter, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                //注意此处的 text 返回为自定义 Adapter.getItem(position).toString()，如需获取自定义Object，请尝试 datas.get(index)
                //   Toast.makeText(act_main.this, "MAIN", Toast.LENGTH_SHORT).show();
                ReportarFilm(mContext, opcionMenu.get(index), idFilm, nombreFilm);
            }


        }).setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER).setFontColor(Color.GRAY)).setTitle("SELECCIONE LA CAUSA DEL REPORTE:");


    }

    public static List<String[]> ListaCSV(Context mContext, String nombreFileCSV) {


        List<String[]> list = new ArrayList<String[]>();
        String next[] = {};

        try {
            InputStreamReader csvStreamReader = new InputStreamReader(
                    mContext.getAssets().open(
                            nombreFileCSV));

            CSVReader reader = new CSVReader(csvStreamReader);
            for (; ; ) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                    System.out.println("EN CSV -- " + next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;


    }

    public static void AboutUS(Context mContext, TinyDB tinyDB, boolean mostrarSoloUnaVez) {

        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;


        if (mostrarSoloUnaVez == false) {
            String saltoDeLinea = "\n";

            MessageDialog.show((AppCompatActivity) mContext, "Sobre PelisPlusHD", "PelisPlusHD app , es una aplicación que funciona como interfaz y buscador de series y peliculas que son alojadas por terceros.." + saltoDeLinea
                    + saltoDeLinea +
                    "Esta app aun esta en desarrollo, se actualiza el contenido diareamente pero con el envio de peticiones, sugerencias y reportes ayudara a mantener esta aplicacíon en desarrollo, el uso de esta app es totalmente gratuito. " +
                    "¡Bienvenido a PelisPlusHD, disfrutalo!" +
                    saltoDeLinea + saltoDeLinea +
                    "-Trabajando en:" + saltoDeLinea +
                    "* Modo offline (descargar episodios)" + saltoDeLinea +
                    "* Notificaciones" + saltoDeLinea +
                    "* Personalización" + saltoDeLinea +
                    "* Login" + saltoDeLinea + saltoDeLinea + saltoDeLinea +
                    "¨*Con tu apoyo será posible :,)*¨", "OK")
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

        }


    }

    public static int CalcularNumeroColumnas(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }



    public static void EnviarCambioFB(Context mContext) {

        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;


        String saltoDeLinea = "\n";

        MessageDialog.show((AppCompatActivity) mContext, "ENVIAR CAMBIOS A FB", "Se enviara la fecha a 'fechaCambiosData' y hara que los usuarios actualizen la lista contenido", "ENVIAR").setCancelButton("NO")
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {

                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("otros").child("utilidad").child("fechaCambiosData");
                        // para la fecha
                        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
                        String fecha = df.format(Calendar.getInstance().getTime());
                        mref.setValue(fecha);
                        TipDialog.show((AppCompatActivity) mContext,"Fecha de cambio enviada.", TipDialog.TYPE.SUCCESS);

                        return false;
                    }
                })
                .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        Toast.makeText(mContext, "Vale.", Toast.LENGTH_SHORT).show();
                        baseDialog.doDismiss();
                        return true;                    //位于“取消”位置的按钮点击后无法关闭对话框

                    }
                });





    }

    public static void CargarInterAd(Context mContext, String ad_unit_id, int cantidadAleatoria){

        Random numRandom = new Random();
        int numPosibilidad = numRandom.nextInt(cantidadAleatoria);

        //   Toast.makeText(this, String.valueOf(numPosibilidad), Toast.LENGTH_SHORT).show();


        if (numPosibilidad == 3) {

            AdMesa.createLoadInterstitial(mContext, ad_unit_id, null);
        }


     //   Toast.makeText(mContext, String.valueOf(numPosibilidad), Toast.LENGTH_SHORT).show();




    }


}









