package com.components;

import com.constants.Constants;

import java.util.Random;
import java.util.Vector;

/**
 * Created by zhanghan on 17/6/18.
 */
public class File {

    public File(double fileSize,Vector<Server> totalservers,Vector<Client> totalClients,int M ,int N){
        this.fileSize = fileSize;
        this.totalServers = totalservers;
        this.M = M;
        this.N = N;
        this.clientList = totalClients;

    }


    public Vector<Client> clientList;

    private int M = Constants.M;

    private double Ratio = Constants.RATIO;

    public int getM() {
        return M;
    }

    public int getN() {
        return N;
    }

    private int N = Constants.N;

    private double fileSize = 0;
    private int fileId = Constants.FILEINDEX++;

    public double getFileSize() {
        return fileSize;
    }



    public int getFileId() {
        return fileId;
    }



    public Vector<Server> getServerList() {
        return serverList;
    }



    public Vector<Server> getTotalServers() {
        return totalServers;
    }



    public double getShareSize() {
        return shareSize;
    }

    public void setShareSize(double shareSize) {
        this.shareSize = shareSize;
    }

    private Vector <Server> serverList = new Vector<Server>();
    private Vector <Server> totalServers;
    private double shareSize = 0;


    public Server findbyId(int id){
        for(int i = 0; i < this.getTotalServers().size();i++){
            if(this.getTotalServers().get(i).getServerId()==id)
                return this.getTotalServers().get(i);
        }
        return null;
    }


    public Client findClientbyId(int id){
        for(int i = 0; i < this.clientList.size();i++){
            if(this.clientList.get(i).getClientId()==id)
                return this.clientList.get(i);
        }
        return null;
    }



    public boolean store(){

        int totalSize = totalServers.size();

        boolean[] used = new boolean[totalSize];

        for(int i = 0; i < totalSize; i++){
            used[i] = false;
        }

        int p = 0;
        boolean flag = false;

        for(int i =0; i < totalSize; i++){

            if(totalServers.get(i).canStore(this.fileSize/this.Ratio))
                p++;

            //System.out.println(p);

            if(p >= this.M){
                flag = true;
                break;
            }
        }

        if(flag == false) {
            System.out.println("can not store the file");
            return false;
        }


        int i = 0;

        while(i< this.M){

            int maxvalue = totalSize;
            Random random = new Random();

            int s = random.nextInt(maxvalue);
            Server server = totalServers.get(s);
            if(used[s] == false && server.canStore(this.fileSize/this.Ratio)) {

                serverList.add(server);
                used[s] = true;
                i++;
            }


        }

        this.shareSize = this.fileSize/this.Ratio;

        //System.out.println(this.shareSize);
        return true;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileSize=" + fileSize +
                ", fileId=" + fileId +
                ", shareSize=" + shareSize +
                '}';
    }

    public String Getstoragelist(){
        int size = serverList.size();
        String storage="ServerList{";
        for(int i = 0; i < size; i++){
            storage += serverList.get(i).getServerId()+" ";
        }
        return storage;
    }
}
