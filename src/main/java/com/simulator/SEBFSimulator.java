package com.simulator;

import com.components.Client;
import com.components.Request;
import com.components.Server;
import com.trace.tracereader;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by zhanghan on 17/6/22.
 */
public class SEBFSimulator extends  Simulator{

    public SEBFSimulator(Vector<Server> servers, Vector<Client> clients, Vector<Request> requestBaseList, String kind) {
        super(servers, clients, requestBaseList,kind);
    }

    public SEBFSimulator(tracereader tr,String dst) {
        super(tr,dst);
        this.kind = "SEBF";

    }

    @Override
    protected void onSchedule() {
        HashMap<Server,Integer> serverFlow = new HashMap<Server, Integer>();

        for(int i = 0; i < serverList.size();i++){
            serverFlow.put(serverList.get(i),0);
        }

//        HashMap<Client,Integer> clientFlow = new HashMap<Client, Integer>();
//        for(int i = 0; i < clientList.size();i++){
//            clientFlow.put(clientList.get(i),0);
//        }



        HashMap<Server,Double> ingressRemainingBandwidth = new HashMap<Server, Double>();

        for(int i = 0; i < serverList.size(); i++){
            ingressRemainingBandwidth.put(serverList.get(i),serverList.get(i).getCapacity());
        }

//        HashMap<Client,Double> egressRemaingingBandwidth = new HashMap<Client, Double>();
//        for(int i = 0; i < clientList.size();i++){
//            egressRemaingingBandwidth.put(clientList.get(i),clientList.get(i).getCapacity());
//        }



        for(int i = 0; i < activeRequestBase.size(); i++){
            Request r = activeRequestBase.get(i);
            for(int j = 0; j < r.getServerChoose().size(); j++){
                Server s = r.getServerChoose().get(j);
                if(r.getSharefinished().get(s)==false) {
                    serverFlow.put(s,serverFlow.get(s)+1);
                    //clientFlow.put(findbyId(r.getClientId()),clientFlow.get(findbyId(r.getClientId()))+1);
                }

            }


        }






        for(int i = 0; i < activeRequestBase.size(); i++){
            Request r = activeRequestBase.get(i);

            double maxtime = r.getAlpha();
           // System.out.println(r.getRequestID()+" "+maxtime);
            // compute bandwidth of each task here

            for(int j = 0; j < r.getServerChoose().size();j++){
                if(r.getSharefinished().get(r.getServerChoose().get(j))==false){

                    double bandwidth =r.getRemainingshareSize().get(r.getServerChoose().get(j))/maxtime;

                    System.out.println(r.getRequestID()+" "+r.getRemainingshareSize().get(r.getServerChoose().get(j))+ " "+maxtime+" "+bandwidth);

                    if(bandwidth > ingressRemainingBandwidth.get(r.getServerChoose().get(j))){
                        bandwidth = ingressRemainingBandwidth.get(r.getServerChoose().get(j));
                    }

//                    if(bandwidth > egressRemaingingBandwidth.get(findbyId(r.getClientId()))){
//                        bandwidth = egressRemaingingBandwidth.get(findbyId(r.getClientId()));
//                    }

                    //System.out.println(bandwidth);

                    double remaining=ingressRemainingBandwidth.get(r.getServerChoose().get(j));
                    remaining-=bandwidth;
                    ingressRemainingBandwidth.put(r.getServerChoose().get(j),remaining);

//                    remaining = egressRemaingingBandwidth.get(findbyId(r.getClientId()));
//                    remaining-=bandwidth;
//                    egressRemaingingBandwidth.put(findbyId(r.getClientId()),remaining);

                    r.getBandwidth().put(r.getServerChoose().get(j),bandwidth);
                }
            }

        }



        // allocate remaining bandwidth and allocate them to each flows
        for(int i = 0; i < activeRequestBase.size(); i++) {

            Request r = activeRequestBase.get(i);

            for(int j = 0; j < r.getServerChoose().size();j++){

                if(r.getSharefinished().get(r.getServerChoose().get(j))==false){

                    double bandwidth = r.getBandwidth().get(r.getServerChoose().get(j));

                    double each  = ingressRemainingBandwidth.get(r.getServerChoose().get(j))/serverFlow.get((r.getServerChoose().get(j)));
//                            ,egressRemaingingBandwidth.get(findbyId(r.getClientId()))/clientFlow.get(findbyId(r.getClientId())));

                    bandwidth += each;

                    //System.out.println(bandwidth);

                    r.getBandwidth().put(r.getServerChoose().get(j),bandwidth);

                }


            }
        }
    }
}
