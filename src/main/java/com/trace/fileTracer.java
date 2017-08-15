package com.trace;

import com.components.*;
import com.components.File;

import java.io.*;

/**
 * Created by zhanghan on 17/6/23.
 */
public class fileTracer extends tracereader{


    private String pathToInfoBenchmarkTraceFile;

    public fileTracer(int seed, int serverNumber, int clientNumber, int fileNumber, int requestNumber) {
        super(seed, serverNumber, clientNumber, fileNumber, requestNumber);
    }

    public fileTracer(String pathToInfoBenchmarkTraceFile) {
       this.pathToInfoBenchmarkTraceFile = pathToInfoBenchmarkTraceFile;
    }

    @Override
    public void generateTrace() {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(pathToInfoBenchmarkTraceFile);
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open " + pathToInfoBenchmarkTraceFile);
            System.exit(1);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        // Read servernumber, clientnumber, filenumer,requestnumber in the trace
        try {
            String line = br.readLine();
            String[] splits = line.split("\\s+");

            this.serverNumber = Integer.parseInt(splits[0]);
            this.clientNumber= Integer.parseInt(splits[1]);
            this.fileNumber = Integer.parseInt(splits[2]);
            this.requestNumber = Integer.parseInt(splits[3]);


            for(int i = 0; i < this.serverNumber;i++){
                line = br.readLine();
                splits = line.split("\\s+");
                int lIndex = 0;
                double storage = Double.valueOf(splits[lIndex++]);
                double capacity = Double.valueOf(splits[lIndex++]);
                Server s = new Server(storage,capacity);
                this.serverList.add(s);
            }

            for(int i = 0; i < this.clientNumber;i++){
                line = br.readLine();
                splits = line.split("\\s+");

                int lIndex = 0;
                double capacity = Double.valueOf(splits[lIndex++]);

                Client c = new Client(capacity);
                this.clientList.add(c);
            }


            for(int i = 0; i < this.fileNumber; i++){
                line = br.readLine();
                splits = line.split("\\s+");
                int lIndex = 0;
                double filesize = Double.valueOf(splits[lIndex++]);
                double ratio = Double.valueOf(splits[lIndex++]);
                int M = Integer.valueOf(splits[lIndex++]);
                int N = Integer.valueOf(splits[lIndex++]);

                File f = new File(filesize,serverList,clientList,M,N);

                f.setShareSize(f.getFileSize()/ratio);

                for(int j = 0; j < M; j++){
                    int serverid = Integer.valueOf(splits[lIndex++]);
                    Server s = f.findbyId(serverid);
                    f.getServerList().add(s);
                }
                this.fileList.add(f);

            }

            for(int i = 0; i < this.requestNumber;i++){
                line = br.readLine();
                splits = line.split("\\s+");
                int lIndex = 0;
                int clientid = Integer.valueOf(splits[lIndex++]);
                int fileid = Integer.valueOf(splits[lIndex++]);
                double startTime = Double.valueOf(splits[lIndex++]);

                Request r = new Request(clientid,fileid,this.fileList,startTime);

                this.requestBaseList.add(r);
            }
            fis.close();

        } catch (IOException e) {
            System.err.println("Missing trace description in " + pathToInfoBenchmarkTraceFile);
            System.exit(1);
        }

    }
}
