package com.bigbai.watchdog;

import java.util.List;

/**
 * 看门狗回调
 * @author: Zhang
 * @date: 2019/7/2 - 9:24
 * @note Created by com.bigbai.watchdog.
 */
public interface WatchdogCallBack
{
    /** 犬吠*/
    void dogBark(Wdog wdog);

    /** 喂食失败 */
    void feedFalse(Wdog wdog);

    void walk(List<Wdog> wdogs);
}
