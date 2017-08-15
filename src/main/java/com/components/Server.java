package com.components;

import com.constants.Constants;

/**
 * Created by zhanghan on 17/6/18.
 */


public class Server {


    public Server(double storagesize,double capacity){
        this.storageSize = storagesize;
        this.capacity = capacity;
    }

    public int getServerId() {
        return serverId;
    }

    public boolean canStore(double filesize){
        double remainingStorage = this.storageSize - this.storageUsage;
        if(remainingStorage > filesize){
            return true;
        }
        System.out.println("machine remainging size is "+remainingStorage+" is not enough");
        return false;
    }


    private int  serverId = Constants.SERVERINDEX ++;

    private double storageUsage = Constants.ZERO;

    private double storageSize = Constants.STORAGEINIT;

    private double capacity = Constants.INGRESS_CAPACITY;

    public double getCapacity() {
        return capacity;
    }


    @Override
    public String toString() {
        return "Server {" +
                "serverId=" + serverId +
                ", storageUsage=" + storageUsage +
                ", storageSize=" + storageSize +
                '}';
    }
}
