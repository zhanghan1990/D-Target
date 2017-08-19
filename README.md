# D-Target
D-Target is a scheduler that tries to minimize File Access Time (FAT) in data center network
ex1 and ex2 contain some traces 

## How to compile
mvn package

## How to run

java -cp /target/erasure_code-1.0-SNAPSHOT.jar com.simulation.simulation Mode trace-file-Name trace-destination

where, Mode can be chosen as SEBF, FLOW, FAIR, Barrat

## How to generate random trace
python traceGenerator.py


