package com.bigbai.msystem;

import android.content.Context;
import android.media.AudioManager;

/**
 * @brief：音量管理
 * @author: Zhang
 * @date: 2019/6/15 - 18:47
 * @note Created by com.bigbai.msystem.
 */
public class MVolume {

    public static int setVolume(Context context, int value){
        int a = (int) Math.ceil((value)* getMax(context)*0.01);
        a = a<=0 ? 0 : a;
        a = a>=100 ? 100 : a;

        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, a,0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, a,0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, a,0);
        return getVolume(context);
    }

    /**
     * 获取最大音量
     * @param context
     * @return
     */
    public static int getMax(Context context){
        //获取系统的Audio管理者
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }



    /**
     * 获取 当前音量
     * @param context
     * @return
     */
    public static int getVolume(Context context){
        //获取系统的Audio管理者
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //当前音量
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
