package com.components;

import com.constants.Constants;

import java.util.*;

/**
 * Created by zhanghan on 17/6/18.
 */
public class Request {


    public Request(int clientid, int fileid, Vector<File> fileList, double starttime){
        this.clientId = clientid;
        this.fileId = fileid;
        this.startTime = starttime;
        this.fileList = fileList;

    }




    public double getAlpha() {
        return alpha;
    }


    protected double alpha = 0;

    public double durationTime = 0;



    public void updateAlpha(){

        File f = findbyId();

        double finishtime = 0;

        double sharesizetotal = 0;


        for(int i =0; i < this.serverChoose.size();i++){

            Server s = this.serverChoose.get(i);

            double temptime = this.getRemainingshareSize().get(s)/s.getCapacity();

            sharesizetotal += this.getRemainingshareSize().get(s);

            if(temptime > finishtime){
                finishtime = temptime;
            }
        }

        //double clienttime = sharesizetotal/f.findClientbyId(this.clientId).getCapacity();
        //this.alpha = Math.max(finishtime,clienttime);

        this.alpha = finishtime;

    }

    public void Initial(){

        for(int i = 0;i < getServerChoose().size();i++){
            Server s = getServerChoose().get(i);
            getBandwidth().put(s,0.0);
            getRemainingshareSize().put(s,findbyId().getShareSize());
            getSharefinished().put(s,false);
        }


    }











    public String printShareInfo(){

        String ret = "{ ";

        for(int i = 0; i < serverChoose.size(); i++){
            ret+=(" server: "+serverChoose.get(i).getServerId()+" ");
            ret+=(" request Id: "+this.getRequestID()+" ");
            ret+=(" start time: "+this.getStartTime()+" ");
            ret+=(" remaining sharesize: "+remainingshareSize.get(serverChoose.get(i)));
            ret+=(" bandwidth: "+bandwidth.get(serverChoose.get(i)));
            ret+=(" sharefinished: "+sharefinished.get(serverChoose.get(i)));
        }

        ret+=" }";

        return ret;
    }


    public boolean checkFinish(){
        for(int i = 0; i < serverChoose.size(); i++){
            if(sharefinished.get(serverChoose.get(i))==false)
                return false;
        }
        return true;
    }




    public File findbyId(){
        for(int i = 0; i < fileList.size();i++){
            if(fileList.get(i).getFileId()==this.fileId)
                return fileList.get(i);
        }
        return null;
    }






    protected int clientId = 0;

    protected int fileId = 0;

    protected double startTime = 0;

    protected boolean isActive = false;

    protected Vector<Server> serverlist;

    protected Vector<File> fileList;


    protected Map<Server,Double> bandwidth = new HashMap<Server, Double>();

    protected Vector <Server> serverChoose = new Vector<Server>();

    public int getClientId() {
        return clientId;
    }


    public double getStartTime() {
        return startTime;
    }




    public void setActive(boolean active) {
        isActive = active;
    }



    public Map<Server, Double> getBandwidth() {
        return bandwidth;
    }



    public Vector<Server> getServerChoose() {
        return serverChoose;
    }



    public Map<Server, Double> getRemainingshareSize() {
        return remainingshareSize;
    }


    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getRequestID() {
        return requestID;
    }


    public Map<Server, Boolean> getSharefinished() {
        return sharefinished;
    }


    protected Map<Server,Double> remainingshareSize = new HashMap<Server,Double>();

    protected boolean isFinished = false;

    protected int requestID = Constants.REQUESTINDEX++;

    protected Map<Server,Boolean> sharefinished= new HashMap<Server, Boolean>();


}
