package com.lamesa.netfilms.video;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.kk.taurus.playerbase.assist.AssistPlay;
import com.kk.taurus.playerbase.assist.OnAssistPlayEventHandler;
import com.kk.taurus.playerbase.assist.RelationAssist;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.window.FloatWindow;
import com.kk.taurus.playerbase.window.FloatWindowParams;
import com.lamesa.netfilms.R;
import com.lamesa.netfilms.otros.TinyDB;
import com.lamesa.netfilms.video.cover.CloseCover;
import com.lamesa.netfilms.video.cover.GestureCover;
import com.lamesa.netfilms.video.play.DataInter;
import com.lamesa.netfilms.video.play.ReceiverGroupManager;
import com.lamesa.netfilms.video.utils.PUtil;
import com.lamesa.netfilms.video.utils.WindowPermissionCheck;

import static com.lamesa.netfilms.App.mixpanel;
import static com.lamesa.netfilms.activity.act_film.tbNombreFilm;
import static com.lamesa.netfilms.otros.statics.constantes.TBnombreEpisodio;
import static com.lamesa.netfilms.otros.statics.constantes.TBurlEpisodio;
import static com.lamesa.netfilms.otros.statics.constantes.mixVideoReproducido;

public class FloatWindowActivity extends AppCompatActivity {

    private int mVideoContainerH;
    private FrameLayout mVideoContainer;
    private Button mBtnSwitchPlay;

    private RelationAssist mAssist;
    private ReceiverGroup mReceiverGroup;

    private FloatWindow mFloatWindow;

    private boolean isLandScape;
    private FrameLayout mWindowVideoContainer;

    private final int VIEW_INTENT_FULL_SCREEN = 1;
    private final int WINDOW_INTENT_FULL_SCREEN = 2;

    private int mWhoIntentFullScreen;
    private TinyDB tinyDB;
    private String URLepisodio;
    private String NOMBREepisodio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_switch_play);
        tinyDB = new TinyDB(this);
        mBtnSwitchPlay = findViewById(R.id.btn_switch_play);
        mVideoContainer = findViewById(R.id.videoContainer);
        mVideoContainer.post(new Runnable() {
            @Override
            public void run() {
                mVideoContainerH = mVideoContainer.getHeight();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int width = (int) (widthPixels * 0.8f);

        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//8.0+
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            type =  WindowManager.LayoutParams.TYPE_PHONE;
        }




        mWindowVideoContainer = new FrameLayout(this);
        mFloatWindow = new FloatWindow(this, mWindowVideoContainer,
                new FloatWindowParams()
                        .setWindowType(type)
                        .setX(100)
                        .setY(400)
                        .setWidth(width)
                        .setHeight(width*9/16));
        mFloatWindow.setBackgroundColor(Color.BLACK);

        mAssist = new RelationAssist(this);
        mAssist.getSuperContainer().setBackgroundColor(Color.BLACK);
        mAssist.setEventAssistHandler(eventHandler);

        mReceiverGroup = ReceiverGroupManager.get().getLiteReceiverGroup(this);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true);
        mAssist.setReceiverGroup(mReceiverGroup);

        changeMode(false);


        //url a cargar

        URLepisodio = tinyDB.getString(TBurlEpisodio);
    //    URLepisodio = "https://edge9-hr.cvattv.com.ar/live/c3eds/FOXHD/SA_MOBILE_FTA/FOXHD.m3u8";
        NOMBREepisodio = "("+tbNombreFilm+") - "+tinyDB.getString(TBnombreEpisodio);




        DataSource dataSource = new DataSource();
        dataSource.setData(URLepisodio);
        dataSource.setTitle(NOMBREepisodio);

        mAssist.setDataSource(dataSource);
        mAssist.attachContainer(mVideoContainer);
        mAssist.play();


        enterFullScreen();


          mixpanel.timeEvent(mixVideoReproducido);

        mAssist.setOnReceiverEventListener(new OnReceiverEventListener() {
            @Override
            public void onReceiverEvent(int eventCode, Bundle bundle) {
                System.out.println("INFA: "+getClass().getName()+" onReceiverEvent eventCode: "+eventCode+ " | bundle: "+bundle);
            }
        });

        mAssist.setOnPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onPlayerEvent(int eventCode, Bundle bundle) {
              //  System.out.println("INFA: "+getClass().getName()+" onPlayerEvent eventCode: "+eventCode+ " | bundle: "+bundle);
              //  System.out.println("INFA: "+getClass().getName()+" mAssist.getBufferPercentage(); getBufferPercentage: "+mAssist.getBufferPercentage());
             //  mAssist.getBufferPercentage();
                // start the timer for the event "Image Upload"


                switch (eventCode) {
                    case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_BUFFERING_START");
                        break;
                    case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_DATA_SOURCE_SET");
                        break;
                    case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_START:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_PROVIDER_DATA_START");
                        break;
                    case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_TO:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_SEEK_TO");

                        break;

                    case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_VIDEO_RENDER_START");
                        mixpanel.track(mixVideoReproducido);
                        break;

                    case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_BUFFERING_END");
                        break;

                    case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_STOP");
                        break;

                    case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_ERROR:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_PROVIDER_DATA_ERROR");
                        break;

                    case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                        System.out.println("onPlayerEvent: PLAYER_EVENT_ON_SEEK_COMPLETE");
                        break;



                }

                /*
                if(bundle.get){

                }

                 */
            }
        });

        mAssist.setOnErrorEventListener(new OnErrorEventListener() {
            @Override
            public void onErrorEvent(int eventCode, Bundle bundle) {
                System.out.println("INFA: "+getClass().getName()+" setOnErrorEventListener eventCode: "+eventCode+ " | bundle: "+bundle);

            }
        });

    }

    private void changeMode(boolean window){
        if(window){
            mReceiverGroup.removeReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER);
            mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_CLOSE_COVER, new CloseCover(this));
        }else{
            mReceiverGroup.removeReceiver(DataInter.ReceiverKey.KEY_CLOSE_COVER);
            mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER, new GestureCover(this));
        }
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, !window);
    }

    private OnAssistPlayEventHandler eventHandler = new OnAssistPlayEventHandler(){
        @Override
        public void onAssistHandle(AssistPlay assist, int eventCode, Bundle bundle) {
            super.onAssistHandle(assist, eventCode, bundle);
            switch (eventCode){
                case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                    onBackPressed();
                    break;
                case DataInter.Event.EVENT_CODE_ERROR_SHOW:
                    mAssist.stop();
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                    if(isLandScape){
                        quitFullScreen();
                    }else{
                        mWhoIntentFullScreen =
                                mFloatWindow.isWindowShow()?
                                        WINDOW_INTENT_FULL_SCREEN:
                                        VIEW_INTENT_FULL_SCREEN;
                        enterFullScreen();
                    }
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_CLOSE:
                    normalPlay();
                    break;
            }
        }
        @Override
        public void requestRetry(AssistPlay assist, Bundle bundle) {
            if(PUtil.isTopActivity(FloatWindowActivity.this)){
                super.requestRetry(assist, bundle);
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        ViewGroup.LayoutParams params = mVideoContainer.getLayoutParams();
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = mVideoContainerH;
        }
        mVideoContainer.setLayoutParams(params);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        enterFullScreen();
    }

    private void enterFullScreen(){
        if(PUtil.isTopActivity(this)){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if(mWhoIntentFullScreen==WINDOW_INTENT_FULL_SCREEN){
                normalPlay();
            }
        }else{
            startActivity(new Intent(getApplicationContext(), FloatWindowActivity.class));
        }
    }

    private void quitFullScreen(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(mWhoIntentFullScreen==WINDOW_INTENT_FULL_SCREEN){
            // windowPlay(); PENDIENTE DE USAR ACCION EN BOTON Y NO AUTOMATICO NOTA POR MESA
        }
    }

    private void normalPlay(){
        mBtnSwitchPlay.setText(R.string.window_play);
        changeMode(false);
        mAssist.attachContainer(mVideoContainer);
        closeWindow();
    }

    public void switchWindowPlay(View view){
        if(mFloatWindow.isWindowShow()){
            normalPlay();
        }else{
            if(WindowPermissionCheck.checkPermission(this)){
                windowPlay();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WindowPermissionCheck.onActivityResult(this, requestCode, resultCode, data, null);
    }

    private void windowPlay() {
        if(!mFloatWindow.isWindowShow()){
            mBtnSwitchPlay.setText(R.string.page_play);
            changeMode(true);
            mFloatWindow.setElevationShadow(20);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                mFloatWindow.setRoundRectShape(50);
            mFloatWindow.show();
            mAssist.attachContainer(mWindowVideoContainer);
        }
    }

    private void closeWindow(){
        if(mFloatWindow.isWindowShow()){
            mFloatWindow.close();
        }
    }

    @Override
    public void onBackPressed() {
        if(isLandScape){
            quitFullScreen();
            /*
            if(WindowPermissionCheck.checkPermission(this)){
                // windowPlay(); PENDIENTE DE USAR ACCION EN BOTON Y NO AUTOMATICO NOTA POR MESA
            }

             */
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        int state = mAssist.getState();
        if(state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        if(mAssist.isInPlaybackState()){
            mAssist.pause();
            /*
            if(WindowPermissionCheck.checkPermission(this)){
                if (isLandScape){
                    quitFullScreen();
                   // windowPlay(); PENDIENTE DE USAR ACCION EN BOTON Y NO AUTOMATICO NOTA POR MESA
                }
            } else {
                mAssist.pause();
            }

             */
        }else{
            mAssist.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int state = mAssist.getState();
        if(state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        if(mAssist.isInPlaybackState()){
            mAssist.resume();
        }else{
            mAssist.rePlay(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeWindow();
        mAssist.destroy();
    }

}
