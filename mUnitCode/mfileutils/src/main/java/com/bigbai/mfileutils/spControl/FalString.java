package com.bigbai.mfileutils.spControl;

import android.content.SharedPreferences;

public class FalString extends FalBase{

    public FalString(SharedPreferences sp, String key)
    {
        this(sp, key, null, null);
    }

    public FalString(SharedPreferences sp, String key, String def)
    {
        this(sp, key, null, def);
    }

    public FalString(SharedPreferences sp, String key, String value, String def)
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
            {
                if(def != null)
                {
                    setValue(def);
                }
                else
                    setValue("null");
            }
        }

    }

    public String getValue() {
        value = sp.getString(key, "null");

        return (String) value;
    }

    public void setValue(String value) {
        super.value = value;
        sp.edit().putString(key, value).commit();
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
