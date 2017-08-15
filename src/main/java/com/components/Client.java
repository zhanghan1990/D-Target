package com.components;

import com.constants.Constants;

/**
 * Created by zhanghan on 17/6/18.
 */


public class Client {

    public Client(double capacity){
        this.capacity = capacity;
    }
    public Client(){
        this.capacity = Constants.EGRESS_CAPACITY;
    }

    public double getCapacity() {
        return capacity;
    }

    private double capacity = Constants.EGRESS_CAPACITY;
    public int getClientId() {
        return clientId;
    }

    private int clientId = Constants.CLIENTINDEX++;


}
