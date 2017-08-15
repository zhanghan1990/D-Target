package com.simulator;

import com.components.Client;
import com.components.Request;
import com.components.Server;
import com.trace.tracereader;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by zhanghan on 17/6/25.
 */
public class FlowSimulator extends Simulator{


    public FlowSimulator(Vector<Server> servers, Vector<Client> clients, Vector<Request> requestBaseList, String kind) {
        super(servers, clients, requestBaseList, kind);
    }



    private static Comparator<Flow> flowsizeComparator = new Comparator<Flow>() {
        public int compare(Flow f1, Flow f) {
            if (f1.flowsize < f.flowsize)
                return 1;
            else if (f1.flowsize == f.flowsize)
                return 0;
            else
                return -1;
        }

    };





    public FlowSimulator(tracereader tr,String dst) {
        super(tr,dst);
        this.kind = "FLOW";
    }

    class Flow{
        public Server s;
        public Client c;
        public double flowsize;
        public Request r;
    }



    @Override
    protected void onSchedule() {

        Vector<Flow> vf = new Vector<Flow>();

        for(int i = 0; i < requestBaseList.size(); i++) {
            Request r = this.requestBaseList.get(i);
            for(int j = 0; j < r.getServerChoose().size();j++){
                if(r.getSharefinished().get(r.getServerChoose().get(j))==false){
                    Flow f = new Flow();
                    f.s=r.getServerChoose().get(j);
                    f.c = findbyId(r.getClientId());
                    f.flowsize = r.getRemainingshareSize().get(r.getServerChoose().get(j));
                    f.r = r;
                    int index = 0;
                    for(Flow f1:vf){
                        if(flowsizeComparator.compare(f,f1)<0)
                            break;
                        index++;
                    }
                    vf.insertElementAt(f,index);

                }
            }
        }

        HashMap<Server,Double> ingressRemainingBandwidth = new HashMap<Server, Double>();

        for(int i = 0; i < serverList.size(); i++){
            ingressRemainingBandwidth.put(serverList.get(i),serverList.get(i).getCapacity());
        }

//        HashMap<Client,Double> egressRemainingBandwidth = new HashMap<Client, Double>();
//
//        for(int i = 0; i < clientList.size();i++){
//            egressRemainingBandwidth.put(clientList.get(i),clientList.get(i).getCapacity());
//        }



        for(Flow f:vf){
            double b = ingressRemainingBandwidth.get(f.s);//Math.min(ingressRemainingBandwidth.get(f.s),egressRemainingBandwidth.get(f.c));
            //System.out.println(b);
            ingressRemainingBandwidth.put(f.s,ingressRemainingBandwidth.get(f.s)-b);
           // egressRemainingBandwidth.put(f.c,egressRemainingBandwidth.get(f.c)-b);
            for(Request r: activeRequestBase){
                if(r.getServerChoose().contains(f.s) && r.equals(f.r)){
                    r.getBandwidth().put(f.s,b);
                }
            }
        }

    }
}
