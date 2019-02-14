package com.bigbai.mfileutils.spControl;

/*
* SharedPreferences 属性基类
* */
import android.content.SharedPreferences;

public class FalBase {
    SharedPreferences sp;
    String key;
    Object value;
    Object def;
    String explain;// fal说明

    public FalBase(SharedPreferences sp, String key, String value, String def)
    {

    }


    public FalBase(SharedPreferences sp, String key, Integer value, Integer def)
    {
        this.sp = sp;
        this.key = key;
    }

    public FalBase(SharedPreferences sp, String key,Boolean value, Boolean def){
        this.sp = sp;
        this.key = key;


//        if(key == null)
//            return;
//
//        if(value != null)
//        {
//            setValue(value);
//        }
//        else
//        {
//            if(getValue() == null)
//            {
//                if(def != null)
//                {
//                    setValue(def);
//                }
//                else
//                    setValue(false);
//            }
//        }

    }


//    /* 需要在子类重写*/
//    public Object getValue() {
//        return value;
//    }

    /* 需要在子类重写*/
//    public void setValue(Object value) {
//
//    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
    @Override
    public String toString() {
        return key + "=" + value + " ";
    }
}
