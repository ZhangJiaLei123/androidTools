package com.bigbai.anunit.mFileUnit.spControl;

import android.content.SharedPreferences;

public class FalBoolean extends FalBase{

    public FalBoolean(SharedPreferences sp, String key)
    {
        this(sp, key, null, null);
    }

    public FalBoolean(SharedPreferences sp, String key, Boolean def)
    {
        this(sp, key, null, def);
    }

    public FalBoolean(SharedPreferences sp, String key, Boolean value, Boolean def)
    {
        super(sp,key,value,def);
        this.sp = sp;
        this.key = key;

        if(key == null)
            return;

        if(value != null)
        {
            setValue(value);
        }
        else
        {
            if(getValue() == null)
            {
                if(def != null)
                {
                    setValue(def);
                }
                else
                    setValue(false);
            }
        }

    }

    public Boolean getValue() {
        value = sp.getBoolean(key, false);
        return (Boolean) value;
    }

    public void setValue(Boolean value) {
        super.value = value;
        sp.edit().putBoolean(key, value).commit();
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
