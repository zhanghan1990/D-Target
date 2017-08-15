package com.trace;

import com.components.*;

import java.util.Random;
import java.util.Vector;

/**
 * Created by zhanghan on 17/6/22.
 */
public abstract class tracereader {
    protected Vector<Server> serverList = new Vector<Server>();
    protected Vector<File> fileList = new Vector<File>();
    protected Vector<Request> requestBaseList = new Vector<Request>();
    protected Vector<Client> clientList = new Vector<Client>();

    protected  int serverNumber;
    protected  int fileNumber;
    protected  int requestNumber;
    protected  int clientNumber;

    protected int seed;

    protected  Random ranGen;

    public Vector<Server> getServerList() {
        return serverList;
    }

    public Vector<File> getFileList() {
        return fileList;
    }

    public Vector<Request> getRequestBaseList() {
        return requestBaseList;
    }

    public Vector<Client> getClientList() {
        return clientList;
    }

    public tracereader(int seed, int serverNumber, int clientNumber,
                       int fileNumber, int requestNumber){

        this.seed = seed;
        this.serverNumber = serverNumber;
        this.clientNumber = clientNumber;
        this.fileNumber = fileNumber;
        this.requestNumber = requestNumber;
        ranGen = new Random(this.seed);
    }


    public tracereader(){

    }


    protected abstract void generateTrace();

}
