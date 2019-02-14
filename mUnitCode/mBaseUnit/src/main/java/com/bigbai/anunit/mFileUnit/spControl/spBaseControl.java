package com.bigbai.anunit.mFileUnit.spControl;

/*
* SharedPreferences 管理基类
* SharedPreferences SP =
*   getSharedPreferences("包名_preferences", MODE_PRIVATE);
* */
import android.content.SharedPreferences;

public class spBaseControl {
    public FalBoolean FirstRun;          //首次运行
    SharedPreferences SP;

    public spBaseControl(SharedPreferences SP)
    {
        this.SP = SP;
        FirstRun = new FalBoolean(SP,"_FirstRun_",null, true);
    }

    public boolean getFirstRun()
    {
        return FirstRun.getValue();
    }

    public String toString()
    {
        return "";
    }

}
