<p align="center">
  <a href="https://rebrand.ly/topplusweb">
    <img alt="PelisPlusHD" title="PelisPlusHD" src="https://rebrand.ly/ghpplus-header" width="1000">
  </a>
</p>
<h4 align="center">La mejor aplicación de streaming de películas y series <br>
 para que disfrutes de tus contenidos favoritos totalmente gratis <br>
¡solo disfrútala!.</h4>
<p align="center">

<p align="center">
 <a href="https://rebrand.ly/topplusweb">
    <img alt="website" title="App web" src="https://firebasestorage.googleapis.com/v0/b/itcloudme.appspot.com/o/recursos%2Fdise%C3%B1o%2Fapp%2Ficon%2Fbanner%2Fwebsite.png?alt=media&token=13207e5f-1e31-486b-a494-4dc4797a086d" width="140">
  </a>
 <a href="https://rebrand.ly/pplusmixpanel">
    <img alt="mixpanel" title="Mixpanel" src="https://firebasestorage.googleapis.com/v0/b/itcloudme.appspot.com/o/recursos%2Fdise%C3%B1o%2Fapp%2Ficon%2Fbanner%2Fmixpanel.png?alt=media&token=562b91dc-5e81-4d91-ac78-f719b03ec55c" width="140">
  </a>
</p>
 <p align="center">
    <a href="#pelisplushd">PelisPlusHD</a>
    ·
<a href="#features">Features</a>
·
<a href="#built-with-">Built With</a>
·
<a href="#how-it-works-%EF%B8%8F">How It Works</a>
<br />

  </p>



## PelisPlusHD

![Current Version](https://img.shields.io/badge/version-1.3-green.svg)

PelisPlusHD es una aplicación para dispositivos Android gracias a la cual podrás disfrutar de películas y series sin límites. <br>
Disfruta de todos tus contenidos favoritos de forma gratuita eligiendo la categoría que más te guste.

Disponible para Android.

NOTA: _Esta aplicación se desarrolló en el año 2020 con la intención de poner en practica diferentes tecnologias y uso de Android Studio la cual le faltan mejoras en la estructura del codigo y buenas practicas, adicional esta app no esta siendo monetizada y no se encuentra en GooglePlay debido a politicas de uso ya que es una app con intenciones totalmente educativas.
Para su descarga gratuita visitar el sitio web https://repelisplusapp.web.app._


<p>
<img src="https://firebasestorage.googleapis.com/v0/b/socialdown-app.appspot.com/o/pplus%2Fpplus-phone.gif?alt=media&token=4693e25b-232f-45a1-88d3-08490ec2c3f5" align="right" alt="pelisplus" width="320" height="686">
	 </p>

## Features

Algunas de las cosas que puedes hacer en PelisPlusHD:

* Disfrutar de las mejores películas y series mientras te relajas.
* Seleccionar la categoría que más te guste.
* Lista de destacados y favoritos.
* Agregar películas y series a favoritos.



## Built With 🛠

- [Firebase](https://firebase.google.com/) - SDK de productos de Google.
  - [Firebase Analitycs](https://firebase.google.com/docs/crashlytics) - Rastreo de eventos y analisis.
  - [Firebase Crashlitycs](https://firebase.google.com/docs/crashlytics) - Informe de fallas en tiempo real.
  - [Firebase Hosting](https://firebase.google.com/docs/hosting/) - Hosting del sitio web https://repelisplusapp.web.app
  - [Firebase Realtime Database](https://firebase.google.com/docs/database/) - Uso de base de datos en la nube, en tiempo real.  
  - [Firebase Storage](https://firebase.google.com/docs/storage/) - Almacenamiento en la nube.
  - [Firebase Cloud Messaging](  https://firebase.google.com/docs/cloud-messaging) - Manejo de notificaiones push.
- [Amplitude](https://github.com/amplitude/Amplitude-Android) - Rastreo de eventos personalizados.
- [DialogX](https://fontawesome.com) - Muestra diálogos personalizados de una manera simple y fácil.
- [Exoplayer](https://github.com/google/ExoPlayer) - Player de Android para reproducir video.
- [Glide](https://github.com/bumptech/glide) - Cargue de imagenes y miniaturas.
- [Mixpanel](https://mixpanel.com/es/) - Rastreo de eventos personalizados.
- [TinyDB](https://github.com/kcochibili/TinyDB--Android-Shared-Preferences-Turbo) - Base de datos con SharedPreference. 
- [JSOUP](https://github.com/jhy/jsoup) - web scraping para la extracción de datos de una pagina web.




## How It Works ✔️

En rasgos generales se usa Firebase para el manejo de la lectura de datos desde Firebase Realtime Database, especificamente de una base de datos de Peliculas y Series que contiene información como id, imagen, nombre, tipo, calidad y fecha de actualización. La clase PeliculaSerie es un objeto que almacena esta información y cuenta con los métodos Getter y Setter necesarios para acceder a esta información. Los datos son mostrados en un RecyclerView y al dar click se carga el enlace del video en un WebView con bloqueador de anuncios, esto debido a que los videos son almacenados en servidores externos los cuales cuantan con publicidad ej. (https://www.fembed.com/v/385z6imjk8zw12y).

```java
// Esta clase representa un objeto Pelicula o Serie que se almacena en Firebase Realtime Database
public class PeliculaSerie {
    private String id;
    private String imagen;
    private String nombre;
    private String tipo;
    private String calidad;
    private String fechaActualizado;

    public PeliculaSerie() {
        // Constructor vacío necesario para usar Firebase Realtime Database
    }

    public PeliculaSerie(String id, String imagen, String nombre, String tipo, String calidad, String fechaActualizado) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.tipo = tipo;
        this.calidad = calidad;
        this.fechaActualizado = fechaActualizado;
    }
    
    // getters and setters  
    ...
   
}

// Clase para manejar la lectura de datos desde Firebase Realtime Database
public class FirebaseHelper {
    private static final String PELICULAS_SERIES_NODE = "peliculas_series";
    private static final String ID_NODE = "id";
    private static final String IMAGEN_NODE = "imagen";
    private static final String NOMBRE_NODE = "nombre";
    private static final String TIPO_NODE = "tipo";
    private static final String CALIDAD_NODE = "calidad";
    private static final String FECHA_ACTUALIZADO_NODE = "fecha_actualizado";

    private DatabaseReference mDatabase;

    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getPeliculasSeries(final FirebaseCallback callback) {
        mDatabase.child(PELICULAS_SERIES_NODE).addValueEventListener(new ValueEventListener() {
```

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments 💬

* [Flaticon](https://www.flaticon.es/)
* [Font Awesome](https://fontawesome.com)
* [SpinKit](https://github.com/ybq/Android-SpinKit)
