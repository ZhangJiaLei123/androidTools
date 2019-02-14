package com.bigbai.anunit.mFileUnit.spControl;

import android.content.SharedPreferences;

public class FalInt extends FalBase{
    public FalInt(SharedPreferences sp, String key)
    {
        this(sp, key, null, null);
    }

    public FalInt(SharedPreferences sp, String key, int def)
    {
        this(sp, key, null, def);
    }

    public FalInt(SharedPreferences sp, String key, Integer value, Integer def)
    {
        super(sp,key,value,def);
        this.sp = sp;
        this.key = key;
        this.def = def;

        if(key == null)
            return;

        if(value != null)
        {
            setValue(value);
        }
        else
        {
            if(getValue() == null)
                setValue(def);
        }

    }

    public Integer getValue() {
        value = sp.getInt(key, 0);
        return (int)value;
    }

    public void setValue(Integer value) {
        super.value = value;
        sp.edit().putInt(key, value).commit();
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

}
