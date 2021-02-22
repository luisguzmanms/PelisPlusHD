package com.lamesa.netfilms.video.play;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.android.gms.common.util.DataUtils;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.provider.BaseDataProvider;

public class DemoDataProvider extends BaseDataProvider {

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mDelayRunnable;

    @Override
    public void handleSourceData(final DataSource sourceData) {
        if(TextUtils.isEmpty(sourceData.getData())){
            cancel();
            //模拟请求数据的过程
            mHandler.postDelayed(mDelayRunnable = new Runnable() {
                @Override
                public void run() {
                    DataSource data = new DataSource("asdasd");
                    data.setTitle("音乐和艺术如何改变世界");
                    //success result data
                    onProviderMediaDataSuccess(data);
                    //if occur error, you can call this method
                    //onProviderMediaDataError(bundle);
                }
            }, 1000);
        }else{
            onProviderMediaDataSuccess(sourceData);
        }
    }

    @Override
    public void cancel() {
        if(mDelayRunnable!=null)
            mHandler.removeCallbacks(mDelayRunnable);
    }

    @Override
    public void destroy() {
        cancel();
    }

}
