package com.simulator;

import com.components.Client;
import com.components.Request;
import com.components.Server;
import com.trace.tracereader;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by zhanghan on 17/6/25.
 */
public class FIFOSimulator extends Simulator{

    public FIFOSimulator(Vector<Server> servers, Vector<Client> clients, Vector<Request> requestBaseList, String kind) {
        super(servers,clients, requestBaseList, kind);
    }

    public FIFOSimulator(tracereader tr,String dst) {
        super(tr,dst);
        this.kind = "FIFO";

    }

    protected void onSchedule() {

        HashMap<Server,Double> ingressRemainingBandwidth = new HashMap<Server, Double>();

        for(int i = 0; i < serverList.size(); i++){
            ingressRemainingBandwidth.put(serverList.get(i),serverList.get(i).getCapacity());
        }

//        HashMap<Client,Double> egressRemainingBandwidth = new HashMap<Client, Double>();
//
//        for(int i = 0; i < clientList.size();i++){
//            egressRemainingBandwidth.put(clientList.get(i),clientList.get(i).getCapacity());
//        }




        HashMap<Request,Client> rc = new HashMap<Request, Client>();

        int size = this.activeRequestBase.size();

        for(int i = 0; i < size; i++){

            Request r = this.activeRequestBase.get(i);

            for(int j = 0; j < r.getServerChoose().size();j++){

                if(r.getSharefinished().get(r.getServerChoose().get(j))==false){
                    double serverremaining = ingressRemainingBandwidth.get(r.getServerChoose().get(j));
                   // double clientremaing = egressRemainingBandwidth.get(findbyId(r.getClientId()))/r.getServerChoose().size();
                    double remaining =serverremaining;// Math.min(serverremaining,clientremaing);
                    //System.out.println(r.getRequestID()+" "+remaining+" "+r.getServerChoose().get(j).getServerId());
                    r.getBandwidth().put(r.getServerChoose().get(j),remaining);
                    ingressRemainingBandwidth.put(r.getServerChoose().get(j),0.0);
                }
            }
        }
    }
}
