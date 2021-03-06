# encoding: utf-8
import numpy as np
import matplotlib.pyplot as plt
import sys
from scipy import stats

from matplotlib import rcParams
rcParams.update({'font.size': 18,'font.weight':'bold'})

patterns = ('/','//','-', '+', 'x', '\\', '\\\\', '*', 'o', 'O', '.')

##first read from file
fairpath="FAIR"
sebfpath="SEBF"
fifopath="FIFO"
flowpath="FLOW"

SHORT=5*1024*1024
NARROW=50



def getAverage(arralylist):
    return np.mean(arralylist)


def getRange(arraylist,element):
    return stats.percentileofscore(arraylist, element)


def getElements(arraylist,percentage):
    result=[]
    for element in arraylist:
        pos=getRange(arraylist,element)
        if pos <= percentage:
            result.append(element)
    return result



def getPercentile(arraylist,percentage):
    a=np.array(arraylist)
    p=np.percentile(a,percentage)
    return p




def getResult(path):

    f=open(path,"r")
    totaline=f.readlines()
    bin1=0
    bin2=0
    bin3=0
    bin4=0
    bin5=0

    c1=0
    c2=0
    c3=0
    c4=0
    c=0
    for line in totaline:
        arrayline=line.split()
        currentTime = float(arrayline[0])
        requestId = arrayline[1]
        M = int(arrayline[2])
        N = int(arrayline[3])
        
        duration=float(arrayline[4])

        if N >= 2 and N <4:
            c1 +=duration
            bin1 +=1
        elif N >=4 and N <6:
            c2+=duration
            bin2+=1
        elif N >=6 and N <8:
            c3+=duration
            bin3+=1
        else:
            c4+=duration
            bin4+=1
        bin5+=1
        c+=duration
    #print bin1,bin2,bin3,bin4
    return c/bin5,c1/bin1,c2/bin2,c3/bin3,c4/bin4
        #print M,N,duration
    #     if line[0] == 'J':
    #         arrayline=line.split()
    #         #analyze job 
    #         jobname=arrayline[0]
    #         starttime=float(arrayline[1])
    #         finishtime=float(arrayline[2])
    #         mappers=int(arrayline[3])
    #         reducers=int(arrayline[4])
    #         totalshuffle=float(arrayline[5])
    #         maxshuffle=float(arrayline[6])
    #         duration=float(arrayline[7])
    #         deadlineduration=float(arrayline[8])
    #         shufflesum=float(arrayline[9])
    #         weight=float(arrayline[10])
    #         width=mappers
    #         if mappers < reducers:
    #             width=reducers
    #         else:
    #             width=mappers
    #         if maxshuffle < SHORT and width < NARROW:
    #             wc1+=weight*duration
    #             bin1+=1
    #         elif maxshuffle >= SHORT and width < NARROW:
    #             wc2+=weight*duration
    #             bin2+=1
    #         elif maxshuffle < SHORT and width > NARROW:
    #             wc3+=weight*duration
    #             bin3+=1
    #         else:
    #             wc4+=weight*duration
    #             bin4+=1
    # wc=wc1+wc2+wc3+wc4
    # return wc1,wc2,wc3,wc4,wc

    


if __name__=='__main__':

    for i in range(1,11):
        fifo=fifopath+"r"+str(i)
        sebf=sebfpath+"r"+str(i)
        flow=flowpath+"r"+str(i)
        fair=fairpath+"r"+str(i)
        

        print getResult(fifo)
        print getResult(sebf)
        print getResult(flow)
        print getResult(fair)
        print "DDDD"
        # sebfwc1,sebfwc2,sebfwc3,sebfwc4,sebfwc=getResult(sebf)
        # wc1,wc2,wc3,wc4,wc=getResult(weight)
        # sebfactor1.append(fwc1/sebfwc1)
        # sebfactor2.append(fwc2/sebfwc2)
        # sebfactor3.append(fwc3/sebfwc3)
        # sebfactor4.append(fwc4/sebfwc4)
        # sebfactor.append(fwc/sebfwc)
        # wf1.append(fwc1/wc1)
        # wf2.append(fwc2/wc2)
        # wf3.append(fwc3/wc3)
        # wf4.append(fwc4/wc4)
        # wf.append(fwc/wc)


    # N=8
    # ind = np.arange(N)  # the x locations for the groups
    # width = 0.2       # the width of the bars
    # fig, ax = plt.subplots(dpi=1000)
    # rects1 = ax.bar(ind, sebfactor3, width, hatch="//",color='red',ecolor='k')
    # rects2 = ax.bar(ind+width, wf3, width, hatch='-',color='k',ecolor='k')
    # ax.set_xticks(ind+width)
    # ax.set_xticklabels(('0','1','2','3','4','5','6','7'))
    # ax.legend((rects1[0],rects2[0]), ('Vary','Yosemite'),loc=2)
    # ax.set_ylabel('improvement over fifo allocation')
    # ax.set_ylim([0,1000])
    # fig.savefig("3.eps",dpi=1000)