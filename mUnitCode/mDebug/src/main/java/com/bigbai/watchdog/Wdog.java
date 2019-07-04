package com.bigbai.watchdog;

import java.util.Date;

/**
 * 看门狗对象，饥饿度（Hp）为0时触发狗叫
 * @author: Zhang
 * @date: 2019/7/2 - 9:26
 * @note Created by com.bigbai.watchdog.
 */
public class Wdog {
    private int MaxHp = 100;
    /** 饥饿度*/
    private int Hp = 100;

    /** id */
    private int id = 0;

    /** name */
    private String name;

    /** 是否饿昏了*/
    private boolean isLive = true;

    /** 复活计数 */
    private int reviveCount = 0;
    /** 上次复活时间 */
    private long lastReviveTime = -1;
    /** 复活间隔时间 */
    private long reviveTime = 0;


    /**
     * 创建一个看门狗，默认Hp为100，
     * @param id
     */
    public Wdog(int id, int maxHp) {
        this.id = id;
        this.MaxHp = maxHp;
        Hp = maxHp;
    }

    public int getHp() {
        return Hp;
    }

    public void setHp(int hp) {
        Hp = hp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 运动后就饿了
     */
    public int sports(){
        Hp--;
        if(Hp <= 0){
            Hp = 0;
        }
        return Hp;
    }

    /**
     * 喂食
     * @return
     */
    public int feed(){
        if(!isLive){ // 饿昏了，无法喂食
         //  return 0;
        }
        Hp = MaxHp;

        return Hp;
    }

    /**
     * 喂食
     * @param hp  需要喂食的量，最大值为MaxHp
     * @return
     */
    public int feed(int hp){
        if(!isLive){ // 饿昏了，无法喂食
            //  return 0;
        }

        Hp += hp;
        if( Hp >= MaxHp) {
            Hp = MaxHp;
        }

        return Hp;
    }

    /**
     * 满血复活看门狗,
     * 如果看门口是活的，就不操作
     * @return
     */
    public boolean revive(){
        if(isLive){
            return !isLive;
        }
        else {
            long time = new Date(System.currentTimeMillis()).getTime(); // 获取时间
            if(lastReviveTime < 0){ // 初始时间为0
                lastReviveTime = time;
            }
            reviveTime = time - lastReviveTime;// 计算上次复活时间间隔
            lastReviveTime = time;

            reviveCount++;
            this.Hp = this.MaxHp;
            isLive = true;
        }
        return true;
    }


    @Override
    public String toString() {
        return "Wdog{" +
                "MaxHp=" + MaxHp +
                ", Hp=" + Hp +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", isLive=" + isLive +
                '}';
    }

    /**
     * 获取复活次数
     * @return
     */
    public int getReviveCount() {
        return reviveCount;
    }

    /**
     * 上次复活时间间隔
     * @return
     */
    public long getReviveTime() {
        return reviveTime;
    }

    public boolean isLive(){
        return isLive;
    }

    public void setDie(){
        isLive = false;
    }
}
