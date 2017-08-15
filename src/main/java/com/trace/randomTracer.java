package com.trace;

import com.components.*;
import com.constants.Constants;

/**
 * Created by zhanghan on 17/6/22.
 */
public class randomTracer extends  tracereader {
    public randomTracer(int seed, int serverNumber, int clientNumber, int fileNumber, int requestNumber) {
        super(seed, serverNumber, clientNumber, fileNumber, requestNumber);
    }

    public randomTracer() {
        super();
    }

    @Override
    public void generateTrace() {

        for(int i = 0; i < this.serverNumber;i++){
            Server s  = new Server(Constants.STORAGEINIT,Constants.EGRESS_CAPACITY);
            serverList.add(s);
        }


        for(int i = 0; i < this.clientNumber;i++){
            Client c = new Client();
            clientList.add(c);
        }

        for(int i = 0; i < this.fileNumber;i++){
            File f = new File(Constants.INGRESS_CAPACITY,serverList,clientList,Constants.M,Constants.N);
            f.store();
            fileList.add(f);
        }

        for(int i = 0; i < this.requestNumber; i++){

            int clientId = ranGen.nextInt(this.clientNumber-1);
            int fileId = ranGen.nextInt(this.fileNumber-1);
            double startTime = Math.abs(ranGen.nextDouble());

            Request r = new Request(clientId,fileId,fileList,startTime);

            requestBaseList.add(r);
        }
    }

}
