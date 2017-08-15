package com.simulation;

import com.simulator.*;
import com.trace.fileTracer;

/**
 * Created by zhanghan on 17/6/18.
 */
public class simulation {

    public static void main(String[] argv){

        System.out.println(argv.length);
        if(argv.length<2){
            System.out.println("usage java -cp /target/erasure_code-1.0-SNAPSHOT.jar com.simulation.simulation Mode trace-file-Name");
            System.exit(1);
        }


        String tracefile = argv[1];
        fileTracer ft = new fileTracer(tracefile);
        ft.generateTrace();


        Simulator simulator = new FairSimulator(ft,argv[2]);

        if(argv[0].equals("FAIR")){
            System.out.println("choose FAIR");
            simulator = new FairSimulator(ft,argv[2]);
        }
        else if(argv[0].equals("SEBF")){
            System.out.println("choose SEBF");
            simulator = new SEBFSimulator(ft,argv[2]);
        }
        else if(argv[0].equals("FIFO")){
            System.out.println("choose FIFO");
            simulator = new FIFOSimulator(ft,argv[2]);
        }
        else if(argv[0].equals("FLOW")){
            System.out.println("choose pFabric");
            simulator = new FlowSimulator(ft,argv[2]);
        }


        simulator.simulation();
    }
}
