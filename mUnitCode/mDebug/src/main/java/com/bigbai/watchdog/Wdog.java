package com.bigbai.watchdog;

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
            isLive = false;
        }
        return Hp;
    }

    /**
     * 喂食
     * @return
     */
    public int feed(){
        if(!isLive){ // 饿昏了，无法喂食
            return 0;
        }
        Hp++;
        if(Hp >= MaxHp){
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
            this.Hp = this.MaxHp;
            isLive = true;
        }
        return true;
    }
}
