package com.lamesa.netfilms.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplitude.api.Amplitude;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.interfaces.OnNotificationClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.Notification;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.netfilms.BuildConfig;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.adapter.adapterCategoria;
import com.lamesa.netfilms.adapter.adapterDestacado;
import com.lamesa.netfilms.adapter.adapterFilm;
import com.lamesa.netfilms.model.modelCategoria;
import com.lamesa.netfilms.model.modelDestacado;
import com.lamesa.netfilms.model.modelFilm;
import com.lamesa.netfilms.modelAd.modelAddFilm;
import com.lamesa.netfilms.otros.TinyDB;
import com.lamesa.netfilms.otros.statics.Animacion;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lamesa.netfilms.App.mFirebaseAnalytics;
import static com.lamesa.netfilms.App.mixpanel;
import static com.lamesa.netfilms.activity.act_film.tbNombreFilm;
import static com.lamesa.netfilms.otros.metodos.CargarInterAd;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.netfilms.otros.statics.constantes.TBlistPeliculas;
import static com.lamesa.netfilms.otros.statics.constantes.mixAdClic;
import static com.lamesa.netfilms.otros.statics.constantes.mixEpisodioClic;
import static com.lamesa.netfilms.otros.statics.constantes.setDebugActivo;
import static com.lamesa.netfilms.otros.metodos.AboutUS;
import static com.lamesa.netfilms.otros.metodos.AbrirPagina;
import static com.lamesa.netfilms.otros.metodos.AleatorizarListas;
import static com.lamesa.netfilms.otros.metodos.CalcularNumeroColumnas;
import static com.lamesa.netfilms.otros.metodos.CargarHome;
import static com.lamesa.netfilms.otros.metodos.CargarSearch;
import static com.lamesa.netfilms.otros.metodos.CompartirApp;
import static com.lamesa.netfilms.otros.metodos.DialogoSugerencia;
import static com.lamesa.netfilms.otros.metodos.SolicitarFilm;
import static com.lamesa.netfilms.otros.metodos.getFavorito;
import static com.lamesa.netfilms.otros.metodos.getListaCategorias;
import static com.lamesa.netfilms.otros.metodos.getListaDeFirebase;
import static com.lamesa.netfilms.otros.metodos.getListaDestacados;
import static com.lamesa.netfilms.otros.metodos.initFirebase;
import static com.lamesa.netfilms.otros.metodos.setDebug;

public class act_main extends AppCompatActivity {

    public static List<modelDestacado> mlistDestacado;
    public static List<modelFilm> mlistSeries;
    public static List<modelFilm> mlistPeliculas;
    public static List<modelFilm> mlistFavoritos = new ArrayList<>();
    public static List<modelCategoria> mlistCategoria;
    public static RecyclerView mrvBusqueda;
    public static adapterFilm mAdapterBusqueda;
    public static adapterFilm mAdapterSerie;
    public static adapterCategoria mAdapterCategoria;
    public static adapterFilm mAdapterPelicula;
    public static adapterDestacado mAdapterDestacado;
    public static RecyclerView mrvDestacado;
    public static RecyclerView mrvCategoria;
    public static RecyclerView mrvFilmPeliculas;
    public static RecyclerView mrvFilmSeries;
    public static List<modelFilm> mlistContenido;
    public static List<modelFilm> mlistGenero;
    public static LinearLayout contenidoCargando;
    public static NestedScrollView contenidoHome;
    public static LinearLayout contenidoSearch;
    public static TinyDB tinyDB;
    public static ArrayList<String> mlistUrls;
    public static SpinKitView animacionCargandoTop;
    public static TextView tvTituloBusqueda;
    public static EditText etBuscar;
    private TextView mtvVerPeliculas;
    private TextView mtvVerSeries;
    private ImageView ivLimpiarBusqueda;
    private BottomNavigationView bottomNavigation;
    private ImageView ivAtrasSearch;
    private ImageView ivMenu;
    private ImageView ivLogo;
    public static int anchoItemFilm = 125;
    private FrameLayout flPeliculas;
    private FrameLayout flSeries;
    public static List<modelFilm> mlistNetflix;
    public static  adapterFilm mAdapterNetflix;
    public static RecyclerView mrvFilmNetflix;
    private TextView mtvVerNetflix;
    public static RecyclerView mrvFilmEstrenos;
    public static List<modelFilm> mlistEstrenos;
    public static adapterFilm mAdapterEstrenos;
    private FrameLayout flEstrenos;
    private FrameLayout flNetflix;
    private TextView mtvVerEstrenos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main2);
        SolicitarPermisos(this);


        tinyDB = new TinyDB(this);


        initFirebase(act_main.this, tinyDB);


        VistasHome();
        VistasSearch();
        etBuscar = findViewById(R.id.etBuscar);
        CargarRvSearch();
        CargarRecyclerHome();


        // Crashlytics.getInstance().crash(); // Force a crash

        new CargarListas().execute();
        CargarAdMain();  // solo en releaseeeeeeeeeeeeeeeeeeeeeeeeeee




    }

    @Override
    public void onBackPressed() {

        if (contenidoHome.getVisibility() == GONE) {
            CargarHome(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }


    //cargar contenido shear y ocultarhome
    public static void AbrirSearch(List<modelFilm> ListaParaMostrar, String TituloBusqueda, Context mContext) {


        for (int i = 0; ListaParaMostrar.size() > i; i++) {
            System.out.println("ListaParaMostrar " + ListaParaMostrar.get(i));
        }

        WaitDialog.show((AppCompatActivity) mContext, "Cargando lista...").setCancelable(false);
        CargarSearch();
        tvTituloBusqueda.setText(TituloBusqueda);
        if(etBuscar!=null){
            etBuscar.setHint("Buscar en "+TituloBusqueda.toLowerCase());
        }
        CargarRvBusqueda(ListaParaMostrar, mContext);
        WaitDialog.dismiss();
    }

    //metodo para crear una nueva lista con los elementos filtrados y enviarla al adaptador
    public static void FiltrarLista(String text, @NotNull List<modelFilm> ListaParaFiltrar, Context mContext) {
        setDebug("act_main", "FiltrarLista", "Filtrando lista", "d", setDebugActivo);
        List<modelFilm> listaFiltrada = new ArrayList<>();


        //  System.out.println("HOLAAAAAAAAAAAAAAAAAAA "+ListaParaFiltrar.size());


        setDebug("act_main", "FiltrarLista", "Filtrando lista ListaParaFiltrar tamaño == " + ListaParaFiltrar.size(), "d", setDebugActivo);

        if (ListaParaFiltrar != null && !ListaParaFiltrar.isEmpty()) {

//            setDebug("act_main","FiltrarLista","Filtrando lista ListaParaFiltrar nombre == "+ListaParaFiltrar.get(4).getNombre(),"d",setDebugActivo);
//



            for (int i = 0; ListaParaFiltrar.size() > i; i++) {


                if (ListaParaFiltrar.get(i).getNombre() != "" && ListaParaFiltrar.get(i).getNombre() != null && ListaParaFiltrar.get(i).getId() != "" && ListaParaFiltrar.get(i).getId() != null) {
                    if (ListaParaFiltrar.get(i).getNombre().toLowerCase().contains(text.toLowerCase()) || ListaParaFiltrar.get(i).getId().toLowerCase().contains(text.toLowerCase())) {
                        listaFiltrada.add(ListaParaFiltrar.get(i));
                    }
                }

            }


        }


        mAdapterBusqueda = new adapterFilm(mContext, listaFiltrada);
        mrvBusqueda.setAdapter(mAdapterBusqueda);


    }

    //cargar una lista en el recyvlerview de el contenido search y enviar a MESA
    public static void CargarRvBusqueda(final List<modelFilm> ListaParaMostrar, Context mContext) {


        int mNoOfColumns = CalcularNumeroColumnas(mContext,anchoItemFilm);
        mrvBusqueda.setLayoutManager(new GridLayoutManager(mContext, mNoOfColumns));
        mrvBusqueda.setItemAnimator(new DefaultItemAnimator());
        mAdapterBusqueda = new adapterFilm(mContext, ListaParaMostrar);
        mrvBusqueda.setAdapter(mAdapterBusqueda);


        // metodo para filtrar la lista cargada en el rv de busqueda y volverla a cargar filtrada

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //metodo para crear una nueva lista con los elementos filtrados y enviarla al adaptador

                FiltrarLista(s.toString(), ListaParaMostrar, mContext);


            }
        });


        WaitDialog.dismiss();


    }


    private void SolicitarPermisos(Context mContext) {

        PermissionListener permissionlistener = new PermissionListener() {


            @Override
            public void onPermissionGranted() {

                //   TipDialog.show((AppCompatActivity) mContext, "Permisos concedidos.", TipDialog.TYPE.SUCCESS).setTipTime(60000).setCancelable(true);
            }


            //endregion


            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                TipDialog.show((AppCompatActivity) mContext, "Si no habilita los permisos de almacenamiento no se podra descargar nuevas actualizaciones, ni usar cache para la carga de videos mas rapida", TipDialog.TYPE.WARNING).setTipTime(60000).setCancelable(true);
            }


        };
        TedPermission.with(mContext)
                .setPermissionListener(permissionlistener).setDeniedMessage("Si no habilita los permisos de almacenamiento no se podra descargar nuevas actualizaciones, ni usar cache para la carga de videos mas rapida\n\nPuede tambien hacerlo manualmente en [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                check();
    }

    private void CargarAdMain() {

        //   new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("830648A2D5D5AF09D0FAED08D38E2353"));
        AdView mAdView = findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("830648A2D5D5AF09D0FAED08D38E2353").build();
        mAdView.loadAd(adRequest);
        // mAdView.setAdSize(AdSize.FLUID);
        // listener
        mAdView.setAdListener(new AdListener() {

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
                //region MIX mixEpisodioClic para estadisticas
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

        });





        String idAd = "ca-app-pub-3040756318290255/2989304154";

        if (BuildConfig.APPLICATION_ID.toLowerCase().contains("mesa")) {
            idAd = "ca-app-pub-3040756318290255/3714825369";
        }


        CargarInterAd(act_main.this, idAd, 10);



        Random numRandom2 = new Random();
        int numPosibilidad2 = numRandom2.nextInt(30);


        if(numPosibilidad2==20) {
            DialogSettings.theme = DialogSettings.THEME.DARK;
            DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
            MessageDialog.show((AppCompatActivity) act_main.this, "INFORMACIÓN", "Recuerda, si una pelicula o serie no funciona por algún motivo reportalo con la respectiva razón. \n\n*Los videos no se almacenan con nuestros datos ni tenemos control de ellos, por lo tanto solucionar esta clase de inconvenientes es unicamente posible con los reportes* \n\nGracias por tú colaboración, sigue disfrutando del contenido. :)", "VALE").setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN)).setButtonOrientation(LinearLayout.VERTICAL).setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
                @Override
                public boolean onClick(BaseDialog baseDialog, View v) {
                    Toast.makeText(act_main.this, "¡Gracias!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

    }

    private void CargarRecyclerHome() {

        //region LISTA CONTENIDO
        if (mlistContenido == null) {
            mlistContenido = new ArrayList<>();
        }
        //   getListaDeFirebase(this);

        //endregion

        //region LISTA NETFLIX
        if (mlistNetflix == null) {
            mlistNetflix = new ArrayList<>();
        }

        mrvFilmNetflix = findViewById(R.id.rvNetflix);
        mrvFilmNetflix.setHasFixedSize(true);

        mrvFilmNetflix.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvFilmNetflix.setItemAnimator(new DefaultItemAnimator());
        mAdapterNetflix= new adapterFilm(this, mlistNetflix);
        mrvFilmNetflix.setAdapter(mAdapterNetflix);

        //   getListaDeFirebase(this);

        //endregion

        //region LISTA ESTRENOS
        if (mlistEstrenos == null) {
            mlistEstrenos = new ArrayList<>();
        }

        mrvFilmEstrenos = findViewById(R.id.rvEstrenos);
        mrvFilmEstrenos.setHasFixedSize(true);

        mrvFilmEstrenos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvFilmEstrenos.setItemAnimator(new DefaultItemAnimator());
        mAdapterEstrenos = new adapterFilm(this, mlistEstrenos);
        mrvFilmEstrenos.setAdapter(mAdapterEstrenos);
        //   getListaDeFirebase(this);

        //endregion

        //region LISTA GENERO
        mlistGenero = new ArrayList<>();

        //endregion

        //region LISTA DESTACADOS
        mrvDestacado = findViewById(R.id.rvDestacado);
        mrvDestacado.setHasFixedSize(true);
        mrvDestacado.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvDestacado.setItemAnimator(new DefaultItemAnimator());
        if (mlistDestacado == null) {
            mlistDestacado = new ArrayList<>();
        }
        mAdapterDestacado = new adapterDestacado(this, mlistDestacado);
        mrvDestacado.setAdapter(mAdapterDestacado);
        //  getListaDestacados(this);
        //endregion


        //region LISTA CATEGORIAS
        mrvCategoria = findViewById(R.id.rvCategorias);
        mrvCategoria.setHasFixedSize(true);
        mrvCategoria.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvCategoria.setItemAnimator(new DefaultItemAnimator());
        if (mlistCategoria == null) {
            mlistCategoria = new ArrayList<>();
        }
        mAdapterCategoria = new adapterCategoria(this, mlistCategoria);
        mrvCategoria.setAdapter(mAdapterCategoria);
        //   getListaCategorias(this);
        //endregion


        //region LISTA SERIES
        mrvFilmSeries = findViewById(R.id.rvSeries);
        mrvFilmSeries.setHasFixedSize(true);

        mrvFilmSeries.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvFilmSeries.setItemAnimator(new DefaultItemAnimator());
        if (mlistSeries == null) {
            mlistSeries = new ArrayList<>();
        }
        mAdapterSerie = new adapterFilm(this, mlistSeries);
        mrvFilmSeries.setAdapter(mAdapterSerie);
        //   getListaDeFirebase(this);
        //endregion


        //region LISTA PELICULAS
        mrvFilmPeliculas = findViewById(R.id.rvPeliculas);
        mrvFilmPeliculas.setHasFixedSize(true);

        mrvFilmPeliculas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvFilmPeliculas.setItemAnimator(new DefaultItemAnimator());
        if (mlistPeliculas == null) {
            mlistPeliculas = new ArrayList<>();
        }
        mAdapterPelicula = new adapterFilm(this, mlistPeliculas);
        mrvFilmPeliculas.setAdapter(mAdapterPelicula);
        //  getListaDeFirebase(this);
        //endregion


        //region LISTA FAVORITOS
        mlistFavoritos = new ArrayList<>();

        //endregion


        //region LISTA URLS

        mlistUrls = new ArrayList<>();


    }

    private void VistasHome() {

        contenidoHome = findViewById(R.id.contenidoHome);
        contenidoSearch = findViewById(R.id.contenidoSearch);
        contenidoCargando = findViewById(R.id.contenidoCargando);
        animacionCargandoTop = findViewById(R.id.animacionCargandoTop);


        ivMenu = findViewById(R.id.ivMenu);

        mtvVerPeliculas = findViewById(R.id.tvVerPeliculas);
        mtvVerSeries = findViewById(R.id.tvVerSeries);
        mtvVerNetflix = findViewById(R.id.tvVerNetflix);
        mtvVerEstrenos = findViewById(R.id.tvVerEstrenos);
        ivLogo = findViewById(R.id.ivLogo);
        flPeliculas = findViewById(R.id.flPeliculas);
        flSeries = findViewById(R.id.flSeries);
        flEstrenos = findViewById(R.id.flEstrenos);
        flNetflix = findViewById(R.id.flNetflix);



        /* animar contenedores
        flNetflix.startAnimation(Animacion.alpha_in(this));
        flEstrenos.startAnimation(Animacion.alpha_in(this));
        flPeliculas.startAnimation(Animacion.alpha_in(this));
        flSeries.startAnimation(Animacion.alpha_in(this));
        flPeliculas.setVisibility(View.VISIBLE);
        flSeries.setVisibility(View.VISIBLE);
        flEstrenos.setVisibility(VISIBLE);
        flNetflix.setVisibility(VISIBLE);

         */

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {


                    case R.id.animacionCargandoTop:

                        TipDialog.show(act_main.this, "Hay contenido cargandose...", TipDialog.TYPE.OTHER).setCancelable(true).setTipTime(5000);

                        break;

                    case R.id.ivLogo:

                        AbrirPagina(act_main.this);
                        //  SubirFilmPelicula(act_main.this);


                        break;

                    case R.id.tvVerPeliculas:


                        List<modelFilm> tbListPeliculas = tinyDB.getListObject(TBlistPeliculas, modelFilm.class);

                        if (tbListPeliculas != null && !tbListPeliculas.isEmpty()) {
                            AbrirSearch(mlistPeliculas, "Peliculas", act_main.this);
                        } else {
                            TipDialog.show(act_main.this, "Lista vacia.", TipDialog.TYPE.ERROR).setCancelable(true);
                        }

                        break;


                    case R.id.tvVerSeries:

                        AbrirSearch(mlistSeries, "Series", act_main.this);

                        break;


                    case R.id.tvVerNetflix:

                        AbrirSearch(mlistNetflix, "Netflix", act_main.this);

                        break;


                    case R.id.tvVerEstrenos:

                        AbrirSearch(mlistEstrenos, "Estrenos", act_main.this);

                        break;


                    case R.id.ivMenu:



                        /*
                                MessageDialog.build(act_main.this)
                                        .setStyle(DialogSettings.STYLE.STYLE_IOS)
                                        .setTheme(DialogSettings.THEME.DARK)
                                        .setTitle("定制化对话框")
                                        .setMessage("我是内容")
                                        .setOkButton("OK", new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {
                                                Toast.makeText(act_main.this, "", Toast.LENGTH_SHORT).show();
                                                return false;
                                            }
                                        })
                                        .show();

                         */


                        //        WaitDialog.show(act_main.this, "请稍候...").setCancelable(true).setTip(R.drawable.learn_ic_check_mark);


                        List<String> opcionMenu = new ArrayList<>();
                        opcionMenu.add("Solicitar pelicula/serie");
                        opcionMenu.add("Enviar sugerencia");
                        opcionMenu.add("Compartir aplicación");
                        opcionMenu.add("Sobre PelisPlusHD");
                        opcionMenu.add("AVISO LEGAL - DMCA");

//您自己的Adapter

                        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
                        DialogSettings.theme = DialogSettings.THEME.DARK;


                        BaseAdapter baseAdapter = new ArrayAdapter(act_main.this, com.kongzue.dialog.R.layout.item_bottom_menu_material, opcionMenu);


                        BottomMenu.show(act_main.this, baseAdapter, new OnMenuItemClickListener() {
                            @Override
                            public void onClick(String text, int index) {
                                //注意此处的 text 返回为自定义 Adapter.getItem(position).toString()，如需获取自定义Object，请尝试 datas.get(index)
                                //   Toast.makeText(act_main.this, "MAIN", Toast.LENGTH_SHORT).show();

                                switch (index) {

                                    case 0:

                                        SolicitarFilm(act_main.this);

                                        break;

                                    case 1:

                                        DialogoSugerencia(act_main.this);

                                        break;

                                    case 2:
                                        CompartirApp(act_main.this);
                                        break;

                                    case 3:


                                        AboutUS(act_main.this, tinyDB, false);


                                        break;

                                    case 4:

                                        DialogSettings.theme = DialogSettings.THEME.DARK;
                                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;


                                        MessageDialog.show((AppCompatActivity) act_main.this, "DMCA", "" +
                                                "" +
                                                "No multimedia file is being hosted by us on this app.\n" +
                                                "\n" +
                                                "We are not associated with the list of contents found on remote servers. We have no connection or association with such content.\n" +
                                                "The mp4, avi, mkv files that are available for download are not hosted on PelisPLsuHD app and are hosted on other servers (therefore, not our host service).\n" +
                                                "This app (PelisPlusHD) functions as a movie search engine and does not store or host any files or other copyrighted material. We follow copyright laws, but if you find any search results that you feel are illegal, you are asked to complete the form and send an email to appbuho@gmail.com\n" +
                                                "In fact, we adhere to the rights of producers and artists. We assure you that your work will be safe and legal, which will result in a positive experience for each of you, whether you are a creator or a musical artist. Please note that if any person knowingly or intentionally misrepresents any material or activity listed in Section 512(f), it would be considered a violation of copyright law. Then, if you are doing so, you are liable for your own harm. But keep one thing in mind: Don’t make any false claims about the infringed content!\n" +
                                                "\n" +
                                                "The complete information contained in the legal notice may also be sent to the interested party providing the content that is being infringed.", "SI", "CANCELAR")
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


                                        break;
                                }

                            }
                        }).setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER).setFontColor(Color.GRAY)).setTitle("MENU");


                        break;

                }


            }
        };


        mtvVerPeliculas.setOnClickListener(listener);
        mtvVerSeries.setOnClickListener(listener);
        ivMenu.setOnClickListener(listener);
        ivLogo.setOnClickListener(listener);
        mtvVerNetflix.setOnClickListener(listener);
        mtvVerEstrenos.setOnClickListener(listener);
        animacionCargandoTop.setOnClickListener(listener);

        ivLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                DialogSettings.theme = DialogSettings.THEME.DARK;

                InputDialog.show((AppCompatActivity) act_main.this, "¿Que te ha parecido PelisPlsHD?", "Cuentanos que te ha gustado de esta aplicaciòn, sientase libre de opinar", "ENVIAR", "CERRAR")
                        .setCancelable(false)
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                // Toast.makeText(act_main.this, "ok", Toast.LENGTH_SHORT).show();
                                //  if(inputStr.contains(""))
                                return false;
                            }
                        }).setOnOtherButtonClickListener(new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {

                        return false;
                    }
                });

                return false;
            }

        });

        BottomNavigation();

    }

    private void SubirFilmPelicula(Context mContext) {


        List<modelAddFilm> mlistAddFilm = new ArrayList<>();


        for (int i = 0; mlistAddFilm.size() > i; i++) {
            SubirFilm(mContext, mlistAddFilm.get(i).getId(), mlistAddFilm.get(i).getAno(), mlistAddFilm.get(i).getCalidad(), mlistAddFilm.get(i).getCategoria(), mlistAddFilm.get(i).getSipnosis(), mlistAddFilm.get(i).getImagen(), mlistAddFilm.get(i).getNombre(), mlistAddFilm.get(i).getTipo(), mlistAddFilm.get(i).getPuntaje());
        }

    }

    private void SubirFilm(Context mContext, String idFilm, String ano, String calidad, String categoria, String descrip, String imagen, String nombre, String tipo, String puntaje) {

        WaitDialog.show((AppCompatActivity) mContext, "Enviando film...").setCancelable(true);


        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(String.valueOf(idFilm));
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("info").child("ano").setValue(ano);
                dataSnapshot.getRef().child("info").child("calidad").setValue(calidad);
                dataSnapshot.getRef().child("info").child("categoria").setValue(categoria);
                dataSnapshot.getRef().child("info").child("descrip").setValue(descrip);
                dataSnapshot.getRef().child("info").child("id").setValue(idFilm);
                dataSnapshot.getRef().child("info").child("imagen").setValue(imagen);
                dataSnapshot.getRef().child("info").child("nombre").setValue(nombre);
                dataSnapshot.getRef().child("info").child("tipo").setValue(tipo);
                dataSnapshot.getRef().child("info").child("puntaje").setValue(puntaje);


                WaitDialog.dismiss();
                //  TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);

                System.out.println("FILM SUBIDO == " + idFilm + " - " + nombre);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar film", TipDialog.TYPE.ERROR);

            }
        });
    }

    private void BottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation);


        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    CargarHome(this);


                    AleatorizarListas();

                    //  CargarListas();
                    //  getListaDeFirebase(act_main.this);
                    //   getListaSeries(act_main.this);
                    menuItem.setChecked(true);
                    break;

                case R.id.navigation_search:

                    AleatorizarListas();


                    AbrirSearch(mlistContenido, "Contenido", act_main.this);


                    // getListaDeFirebase(act_main.this);
                    menuItem.setChecked(true);


                    break;


                case R.id.navigation_favoritos:


                    List<String> ListaDeFavoritos = tinyDB.getListString(TBlistFavoritos);
                    mlistFavoritos.removeAll(mlistFavoritos);
                    //dar reversa a lista para mostrar recientes
                    Collections.reverse(ListaDeFavoritos);
                    if (ListaDeFavoritos != null && !ListaDeFavoritos.isEmpty()) {
                        AbrirSearch(mlistFavoritos, "Favoritos", act_main.this);
                        for (String favorito : ListaDeFavoritos) {
                            getFavorito(act_main.this, favorito);
                        }
                    } else {
                        CargarHome(this);
                        WaitDialog.show(act_main.this, "Aun no tiene favoritos", TipDialog.TYPE.ERROR).setCancelable(true);
                        Toast.makeText(this, "Aun no tiene favoritos", Toast.LENGTH_SHORT).show();
                    }


                    menuItem.setChecked(true);

                    break;
            }

            return false;
        });
    }

    private void VistasSearch() {
        tvTituloBusqueda = findViewById(R.id.tvTituloBusqueda);
        etBuscar = findViewById(R.id.etBuscar);
        ivLimpiarBusqueda = findViewById(R.id.ivLimpiarBusqueda);
        ivAtrasSearch = findViewById(R.id.ivAtrasSearch);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {


                    case R.id.ivLimpiarBusqueda:

                        etBuscar.setText("");

                        break;

                    case R.id.ivAtrasSearch:

                        contenidoSearch.setVisibility(GONE);
                        contenidoHome.setVisibility(VISIBLE);

                        break;


                }


            }
        };


        ivLimpiarBusqueda.setOnClickListener(listener);
        ivAtrasSearch.setOnClickListener(listener);
    }

    private void CargarRvSearch() {
        mrvBusqueda = findViewById(R.id.rvFilmsBusqueda);
        int mNoOfColumns = CalcularNumeroColumnas(getApplicationContext(),anchoItemFilm);
        mrvBusqueda.setLayoutManager(new GridLayoutManager(act_main.this, mNoOfColumns));
        mrvBusqueda.setItemAnimator(new DefaultItemAnimator());
        mAdapterBusqueda = new adapterFilm(act_main.this, mlistContenido);
        mrvBusqueda.setAdapter(mAdapterBusqueda);
    }

    class CargarListas extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute", "On pre Exceute......");
            Toast.makeText(act_main.this, "Cargando...", Toast.LENGTH_SHORT).show();


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


                            getListaCategorias(act_main.this);
                            getListaDeFirebase(act_main.this);
                            getListaDestacados(act_main.this);


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

            // WaitDialog.show(act_main.this, "Cargando contenido...").setCancelable(true);

            Log.d(TAG + " onPostExecute", "" + result);
        }
    }


}
