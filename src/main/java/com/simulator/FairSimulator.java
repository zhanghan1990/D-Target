package com.simulator;

import com.components.Client;
import com.components.Request;
import com.components.Server;
import com.trace.tracereader;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by zhanghan on 17/6/21.
 */
public class FairSimulator extends Simulator{
    public FairSimulator(Vector<Server> servers, Vector<Client> clients, Vector<Request> requestBaseList, String kind) {
        super(servers, clients, requestBaseList,kind);
    }

    public FairSimulator(tracereader tr,String dst) {
        super(tr,dst);
        this.kind = "FAIR";

    }

    protected void onSchedule() {

        HashMap<Server,Integer> serverFlow = new HashMap<Server, Integer>();

        for(int i = 0; i < serverList.size();i++){
            serverFlow.put(serverList.get(i),0);
        }

//        HashMap<Client,Integer> clientFlow = new HashMap<Client, Integer>();
//
//        for(int i = 0; i < clientList.size();i++){
//            clientFlow.put(clientList.get(i),0);
//        }


        for(int i = 0; i < activeRequestBase.size(); i++){
            Request r = activeRequestBase.get(i);
            for(int j = 0; j < r.getServerChoose().size(); j++){
                Server s = r.getServerChoose().get(j);
                if(r.getSharefinished().get(s)==false) {
                    serverFlow.put(s,serverFlow.get(s)+1);
//                    clientFlow.put(findbyId(r.getClientId()),clientFlow.get(findbyId(r.getClientId()))+1);
                }

            }

        }




        for(int i = 0; i < activeRequestBase.size(); i++){

            Request r = activeRequestBase.get(i);

            for(int j = 0; j < r.getServerChoose().size();j++){
                if(r.getSharefinished().get(r.getServerChoose().get(j))==false){
                    double bandwidth =(r.getServerChoose().get(j).getCapacity()+0.0)/(serverFlow.get(r.getServerChoose().get(j)));
                            //findbyId(r.getClientId()).getCapacity()/(clientFlow.get(findbyId(r.getClientId()))));
                    r.getBandwidth().put(r.getServerChoose().get(j),bandwidth);
                }
            }

        }
    }
}
