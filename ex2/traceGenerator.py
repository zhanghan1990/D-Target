import random
import sys

class Constants:
    FILEMAX = 1024*1024*1024*10
    RATIO = 1.5
    INGRESS = 1024*1024*1024
    EGRESS = 1024*1024*1024
    STORATE = 1024*1024*1024

    TIMERANGE =50

    M =20

    N = 5


def Generator(serverNumber,clientNumber,fileNumber,requestNumber,filename):

    f = open(filename,"w")

    f.write(str(serverNumber)+" "+str(clientNumber)+" "+str(fileNumber)+" "+str(requestNumber)+"\n")

    serverId = []

    for i in range(0,serverNumber):
        serverId.append(i)
        f.write(str(Constants.STORATE)+" "+str(Constants.INGRESS)+"\n")

    for i in range(0,clientNumber):
        f.write(str(Constants.EGRESS)+"\n")

    # generate file information
    for i in range(0,fileNumber):
        filesize = random.randint(1, Constants.FILEMAX-1)
        ratio = Constants.RATIO
        M = Constants.M
        N = Constants.N
        f.write(str(filesize)+" ")
        f.write(str(ratio)+" ")
        f.write(str(M)+" ")
        f.write(str(N)+" ")

        #select servers to store
        serverChoose = random.sample(serverId,M)

        for j in serverChoose:
            f.write(str(j)+" ")
        f.write("\n")

    for i in range(0,requestNumber):
        clientid = random.randint(0,clientNumber-1)
        f.write(str(clientid)+" ")
        fileid = random.randint(0,fileNumber-1)
        f.write(str(fileid)+" ")
        startTime = random.randint(0,Constants.TIMERANGE)
        f.write(str(startTime)+"\n")


    f.close()


if __name__=="__main__":
    if(len(sys.argv)< 2):
        print "usage python tracegenertor.py filename"
        sys.exit(1)
    Generator(20,20,1000,100,sys.argv[1])