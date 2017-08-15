# encoding: utf-8
import numpy as np
import matplotlib.pyplot as plt
import sys
from scipy import stats

from matplotlib import rcParams
rcParams.update({'font.size': 18,'font.weight':'bold'})

patterns = ('/','//','-', '+', 'x', '\\', '\\\\', '*', 'o', 'O', '.')


if __name__=='__main__':

    up=[0.1,0.11,0.12,0.3,0.2,0.1,0.11,0.12]

    down=[0.1,0.11,0.12,0.3,0.2,0.1,0.11,0.12]


    bar1=[1.9,2.0,2.1,2.2,2.3,2.4,2.5,2.6]
    bar2=[1.5,1.4,1.6,1.4,1.45,1.6,1.4,1.45]
    bar3=[0.6,0.65,0.66,0.6,0.65,0.66,0.7,0.8]

    N=8
    ind = np.arange(N)  # the x locations for the groups
    width = 0.2       # the width of the bars
    fig, ax = plt.subplots()
    rects1 = ax.bar(ind, bar1, width,  yerr=[down,up],hatch="//",color='k',ecolor='k')
    rects2 = ax.bar(ind+width, bar2, width,  yerr=[down,up],hatch='-',color='red',ecolor='k')
    rects3 = ax.bar(ind+2*width, bar3, width,  yerr=[down,up],hatch='*',color='blue',ecolor='k')
    ax.set_xticks(ind+width)
    ax.set_xticklabels(('4','6','8','10','12','14','16','18'))
    ax.legend((rects1[0],rects2[0],rects3[0]), ('D-Target','FIFO','SFF'),loc=2)
    ax.set_ylabel('Improvement over TCP',fontsize=18,fontweight='bold')
    ax.set_xlabel('M',fontsize=18,fontweight='bold')
    ax.set_ylim([0,4])
    fig.savefig("M.eps")


    bar1=[2.2,2.3,2.4,2.5,2.6]
    bar2=[1.4,1.45,1.6,1.4,1.45]
    bar3=[0.6,0.65,0.66,0.7,0.8]

    up=[0.3,0.2,0.1,0.11,0.12]

    down=[0.3,0.2,0.1,0.11,0.12]



    N=5
    ind = np.arange(N)  # the x locations for the groups
    width = 0.2       # the width of the bars
    fig, ax = plt.subplots()
    rects1 = ax.bar(ind, bar1, width,  yerr=[down,up],hatch="//",color='k',ecolor='k')
    rects2 = ax.bar(ind+width, bar2, width,  yerr=[down,up],hatch='-',color='red',ecolor='k')
    rects3 = ax.bar(ind+2*width, bar3, width,  yerr=[down,up],hatch='*',color='blue',ecolor='k')
    ax.set_xticks(ind+width)
    ax.set_xticklabels(('3','4','5','6','7'))
    ax.legend((rects1[0],rects2[0],rects3[0]), ('D-Target','FIFO','SFF'),loc=2)
    ax.set_ylabel('Improvement over TCP',fontsize=18,fontweight='bold')
    ax.set_xlabel('N',fontsize=18,fontweight='bold')
    ax.set_ylim([0,4])
    fig.savefig("N.eps")


    bar1=[2.6,2.5,2.4,2.3,2.1]
    bar2=[1.4,1.45,1.6,1.4,1.45]
    bar3=[0.6,0.65,0.66,0.7,0.8]

    up=[0.3,0.2,0.1,0.11,0.12]

    down=[0.3,0.2,0.1,0.11,0.12]



    N=5
    ind = np.arange(N)  # the x locations for the groups
    width = 0.2       # the width of the bars
    fig, ax = plt.subplots()
    rects1 = ax.bar(ind, bar1, width,  yerr=[down,up],hatch="//",color='k',ecolor='k')
    rects2 = ax.bar(ind+width, bar2, width,  yerr=[down,up],hatch='-',color='red',ecolor='k')
    rects3 = ax.bar(ind+2*width, bar3, width,  yerr=[down,up],hatch='*',color='blue',ecolor='k')
    ax.set_xticks(ind+width)
    ax.set_xticklabels(('1.5','2','2.5','3','3.5'))
    ax.legend((rects1[0],rects2[0],rects3[0]), ('D-Target','FIFO','SFF'),loc=2)
    ax.set_ylabel('Improvement over TCP',fontsize=18,fontweight='bold')
    ax.set_xlabel(r'compress ratio $\alpha$',fontsize=18,fontweight='bold')
    ax.set_ylim([0,4])
    fig.savefig("alpha.eps")

