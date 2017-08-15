package com.simulator;

import com.components.*;
import com.constants.Constants;
import com.trace.tracereader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by zhanghan on 17/6/18.
 */
public abstract class Simulator {


    public Vector<Server> serverList;
    public Vector<Client> clientList;

    public boolean randomChoose = false;

    public Vector<Request> requestBaseList;

    Vector<Request> activeRequestBase = new Vector<Request>();
    Vector<Request> tmp = new Vector<Request>();
    String kind;

    public double totalCompletionTime = 0;
    public int taskNumber = 0;


    BufferedWriter bufferedWriter;

    public Simulator(Vector<Server> servers, Vector<Client> clients, Vector<Request> requestBaseList, String kind){
        this.serverList = servers;
        this.requestBaseList = requestBaseList;
        this.clientList = clients;
        this.kind = kind;
    }

    public Simulator(tracereader tr,String resultfile ){
        this.serverList = tr.getServerList();
        this.requestBaseList = tr.getRequestBaseList();
        this.serverList = tr.getServerList();
        this.clientList = tr.getClientList();

        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter(resultfile));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public Client findbyId(int clientid){
        for(int i = 0; i < clientList.size();i++){
            if(clientList.get(i).getClientId()==clientid)
                return clientList.get(i);
        }
        return null;
    }


    private static Comparator<Request> TimeComparator = new Comparator<Request>() {
        public int compare(Request r1, Request r2) {
           if(r1.getStartTime() > r2.getStartTime() )
               return 1;
           else if(r1.getStartTime()== r2.getStartTime())
               return 0;
           else
               return -1;
        }
    };

    private static Comparator<Request> SEBFComparator = new Comparator<Request>() {
        public int compare(Request r1, Request r2) {
            if(r1.getAlpha()>r2.getAlpha())
                return 1;
            else if(r1.getAlpha()==r2.getAlpha())
                return 0;
            else
                return -1;
        }
    };




    // select according to bottleneck link

    public void BottleneckSelection(Request r) {

        double src[] = new double[serverList.size()];
        double dst[] = new double[clientList.size()];

        for (int i = 0; i < src.length; i++)
            src[i] = 0;

        for (int i = 0; i < dst.length; i++)
            dst[i] = 0;

        HashMap<Server,Double> serverload = new HashMap<Server, Double>();
        for(int i = 0; i < r.findbyId().getServerList().size();i++){
            serverload.put(r.findbyId().getServerList().get(i),0.0);
        }



        for(int i = 0; i < r.findbyId().getServerList().size();i++){
            for(Request r1:activeRequestBase){
                if(r1.getServerChoose().contains(r.findbyId().getServerList().get(i))){
                    serverload.put(r.findbyId().getServerList().get(i),serverload.get(r.findbyId().getServerList().get(i))+r1.getAlpha());
                }
            }
        }



        List<Server> l = new ArrayList<Server>();

        // Rank all the servers that can be selected
        for(int i = 0; i <r.findbyId().getServerList().size();i++) {
            double minload = 1000000;
            Server s = null;
            for (int j = 0; j < r.findbyId().getServerList().size();j++){
                double tmpload=serverload.get(r.findbyId().getServerList().get(j));
                if(tmpload<minload &&l.contains(r.findbyId().getServerList().get(j))==false){
                    minload = tmpload;
                    s = r.findbyId().getServerList().get(j);
                }
            }

            l.add(i,s);
        }

        // select the top N
        for(int i =0; i < r.findbyId().getN();i++){
            r.getServerChoose().add(l.get(i));
        }



        r.Initial();
        r.updateAlpha();


//        System.out.println("sorting......");
//        for(int i = 0; i < l.size();i++){
//            System.out.println(l.get(i).getServerId()+" "+serverload.get(l.get(i)));
//        }
//        System.out.println("sorting end...");
//
//        for(int i =0; i < r.findbyId().getN();i++){
//            System.out.println(r.getServerChoose().get(i).getServerId());
//        }




    }


    // random source selection
    public void RandomSelection(Request r){

        System.out.println("use random source selection");

        Combine cb = new Combine();

        File f = r.findbyId();

        int serverId []= new int[f.getServerList().size()];

        for(int i = 0; i < serverId.length;i++){
            serverId[i] = f.getServerList().get(i).getServerId();
        }

        List l = cb.zuhe(serverId,f.getN());
        Random random = new Random();
        int maxvalue = l.size();
        int sc = random.nextInt(maxvalue);
        int[] serverchooseId = (int[])l.get(sc);



        for(int i = 0; i < serverchooseId.length;i++)
            r.getServerChoose().add(f.findbyId(serverchooseId[i]));

        r.Initial();
        r.updateAlpha();

    }



    protected abstract void onSchedule();


    public void simulation(){


        double currentTime = 0;


        for(int i = 0; i < this.requestBaseList.size(); i++){

            Request r1 = requestBaseList.get(i);

            int index = 0;

            for(Request r:tmp){

                if(TimeComparator.compare(r1,r)<0){
                    break;
                }

                index ++;

            }

            tmp.insertElementAt(r1,index);
        }



        int currentIndex = 0;

        while(true) {

            boolean flag = true;

            for(int i = 0; i < tmp.size();i++){
                if(tmp.get(i).isFinished()==false)
                    flag = false;
            }

            if(flag==true){
                System.out.println(currentTime+" simulation finishes");
                break;
            }


            boolean sharestart = false;
            while(currentIndex < tmp.size()){

                Request r = tmp.get(currentIndex);

                if(r.getStartTime() < currentTime +Constants.SIMULATION_TIMESTEP){
                    if(this.kind.equals("SEBF")==false)
                        RandomSelection(r);
                    else
                      BottleneckSelection(r);
                    currentIndex++;
                    r.setActive(true);

                    Vector<Request> t = new Vector<Request>();


                    for(Request r2: activeRequestBase)
                        if(t.contains(r)==false) {
                            r2.updateAlpha();
                            t.add(r2);
                        }
                    activeRequestBase.removeAllElements();

                    t.add(r);

                    for(int i =0; i < t.size();i++){
                        Request tempr = t.get(i);
                        int index = 0;
                        for(Request r1: activeRequestBase){
                            if(this.kind.equals("SEBF") && SEBFComparator.compare(tempr,r1)<0){
                                break;
                            }
                            index++;
                        }
                        activeRequestBase.insertElementAt(tempr,index);
                   }


//                   for(int i = 0 ;i < activeRequestBase.size();i++){
//                        Request r3 = activeRequestBase.get(i);
//                        System.out.println(r3.printShareInfo());
//                    }


                    sharestart = true;
                }

                else
                    break;
            }

            if(sharestart == true)
                onSchedule();
            boolean sharefinished = false;
            for(int i = 0; i < activeRequestBase.size(); i++){

                Request r = activeRequestBase.get(i);

                for(int j = 0; j < r.getServerChoose().size();j++){
                    Server s =r.getServerChoose().get(j);
                    if(r.getSharefinished().get(s)==false){
                        double size = Constants.SIMULATION_TIMESTEP*r.getBandwidth().get(s);
                        if(r.getRemainingshareSize().get(s)-size > 0){
                            double remainsize = r.getRemainingshareSize().get(s) - size;
                            //System.out.println(currentTime+"  "+remainsize);
                            r.getRemainingshareSize().put(s,remainsize);

                        }
                        else{
                            //System.out.println(currentTime+"s Share "+r.getRequestID()+" "+s.getServerId()+" finishes");
                            r.getRemainingshareSize().put(s,0.0);
                            r.getSharefinished().put(s,true);
                            sharefinished = true;
                        }
                    }

                }

                //java -cp target/erasure_code-1.0-SNAPSHOT.jar com.simulation.simulation SEBF /Users/zhanghan/Documents/文件资料/erasure_code/erasure_sim/test2
                //java -cp target/erasure_code-1.0-SNAPSHOT.jar com.simulation.simulation FAIR /Users/zhanghan/Documents/文件资料/erasure_code/erasure_sim/test2
            }



            for(int i = 0; i < activeRequestBase.size(); i++){
                Request r  = activeRequestBase.get(i);
                if(r.checkFinish()==true){
                    r.setFinished(true);
                    activeRequestBase.remove(r);
                    sharefinished = true;
                    i = 0;
                    //System.out.println(activeRequestBase.size());
                    r.durationTime = currentTime-r.getStartTime();
                    this.totalCompletionTime +=r.durationTime;
                    this.taskNumber+=1;
                    try {
                        this.bufferedWriter.write(currentTime + "  " + r.getRequestID() + " " +
                                r.findbyId().getM() + " " + r.findbyId().getN() + " " + r.durationTime);
                        this.bufferedWriter.newLine();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    System.out.println(currentTime+"  "+r.getRequestID()+" "+
                            r.findbyId().getM()+" "+r.findbyId().getN()+" "+r.durationTime);
                }

            }

            if(sharefinished==true)
                onSchedule();

            currentTime += Constants.SIMULATION_TIMESTEP;

        }

        System.out.println(this.totalCompletionTime/this.taskNumber);
        try {
            this.bufferedWriter.flush();
            this.bufferedWriter.close();// 关闭输出流
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
