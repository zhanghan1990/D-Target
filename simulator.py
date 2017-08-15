from abc import ABCMeta, abstractmethod
import random
import copy


class Constants():
	
	ZERO = 0.0
	M = 3
	N = 3
	RATIO = 1.5

	SERVERNUMBER = 3
	SERVERINDEX = 0

	CLIENTNUMBER = 200
	CLIENTINDEX = 0 


	FILEINDEX = 0
	STORAGEINIT = 1024*1024*1

	# 1us
	SIMULATION_TIMESTEP = 1.0/1000.0/100

	SIMULATION_END= 86400*1000


	INGRESS_CAPACITY = 10*1024*1024*1024
	EGRESS_CAPACITY = 10*1024*1024*1024


	REQUESTINDEX = 0





class Request():
	'''
	clientId: Id of the client
	fileId: Id of the file
	startTime: start time of the request
	'''
	clientId = 0
	
	fileId = 0

	startTime = 0

	isActive = False

	serverlist = []

	fileList = []

	bandwidth = {}

	serverChoose = {}

	remainingshareSize = {}

	isFinished = False

	requestID = 0

	sharefinished={}



	def __init__(self,clientid,fileid,filelist,starttime):
		self.clientId = clientid
		self.fileId = fileid
		self.startTime = starttime
		self.fileList = filelist
		self.requestID = Constants.REQUESTINDEX
		Constants.REQUESTINDEX+=1


	def findbyId(self):
		#print "fileid ="+str(self.fileId)
		#self.printInfo()
		for f in self.fileList:
			if f.fileId == self.fileId:
				return f
		return None

	def printInfo(self):
		print "clientId = "+ str(self.clientId)+" fileId = "+str(self.fileId)+" startTime = "+str(self.startTime)



	def randomSelect(self):
		file = self.findbyId()
		self.serverChoose = random.sample(file.serverList,Constants.N)

		for s in self.serverChoose:
			self.bandwidth[s] = 0
			self.remainingshareSize[s] = file.shareSize
			self.sharefinished[s] = False
		return self.serverChoose

	def checkFinish(self):
		for s in self.serverChoose:
			if self.sharefinished[s] == False:
				return False
		return True






class Client():
	ClientId = Constants.CLIENTINDEX

	def __init__(self):
		self.ClientId = Constants.CLIENTINDEX
		Constants.CLIENTINDEX += 1



class Server():

	serverId = Constants.SERVERINDEX
	storageSize = Constants.STORAGEINIT
	storageUsage = Constants.ZERO

	fileList  = []

	def __init__(self,storagesize):
		self.serverId = Constants.SERVERINDEX
		Constants.SERVERINDEX+=1
		self.storageSize = storagesize




class File():

	fileSize = 0
	fileId = 0
	serverList = []
	totalServers = []
	shareSize = 0

	

	def __init__(self,filesize,totalservers):
		self.totalServers = totalservers
		self.fileSize = filesize
		self.fileId = Constants.FILEINDEX
		Constants.FILEINDEX +=1




	def printInfo(self):
		print "filesize = "+str(self.fileSize)+" fildId = "+str(self.fileId)





	def store(self):
		self.serverList = random.sample(self.totalServers,Constants.M)
		self.shareSize = (self.fileSize*Constants.RATIO)/(Constants.M+0.0)



'''
Base class for python simulator
'''
class Simulator():

	requestList = []
	__metaclass__ = ABCMeta

	fileList = []
	requestList=[]

	serverList = []

	activeList = []




	def __init__(self,filelist,requestlist,serverlist):
		self.fileList = filelist
		self.requestList = requestlist
		self.serverList = serverlist




	# def sourceSelection(self,request):
	# 	servers = request.randomSelect()
	# 	return servers




	def onSchedule(self):

		serverflow={}
		for s in self.serverList:
			serverflow[s] = 0


		for r in self.requestList:
			
			if r.isActive == False:
				continue
			
			sharenum = 0

			for s in self.serverList:
				file  = r.findbyId()
				if s in r.serverChoose and r.sharefinished[s] == False:
					serverflow[s] +=1


		for r in self.requestList:

			if r.isActive == False:
				continue

			file = r.findbyId()
			for s in r.serverChoose:
				if r.sharefinished[s] == False:
					r.bandwidth[s] = (Constants.INGRESS_CAPACITY+0.0)/(serverflow[s]+0.0)
				else:
					r.bandwidth[s] = 0

 



	def simulate(self):

		currentTime = 0
		
		currentRequstIndex = 0
		
		requestSize = len(self.requestList)

		requestList = sorted(self.requestList,key=lambda requests: requests.startTime)

		while True:

			flag = True

			for r in requestlist:
				if r.isFinished == False:
					flag = False
			
			if flag == True:
				print "simulation end"
				break

			while currentRequstIndex < requestSize:
				currentRequest  = requestList[currentRequstIndex]
				if currentRequest.startTime < currentTime + Constants.SIMULATION_TIMESTEP:

					for r in self.requestList:
						for s in r.serverChoose:
							print "before",r.remainingshareSize[s]

					temp = copy.deepcopy(requestList)

					currentRequest.randomSelect()

					#self.activeList.append(currentRequest)
					
					currentRequstIndex +=1
					currentRequest.isActive = True

					for r in self.requestList:
						for s in r.serverChoose:
							print r.remainingshareSize[s]

					for r in temp:
						for s in r.serverChoose:
							print "temp",s.serverId


				else:
					break

			self.onSchedule()

			for r in self.requestList:
				
				if r.isActive == False:
					continue
				
				for s in r.serverChoose :

					if r.sharefinished[s] == False:
						
						size = (Constants.SIMULATION_TIMESTEP+0.0) * (r.bandwidth[s]+0.0)
						
						if r.remainingshareSize[s] > size:
							r.remainingshareSize[s] -= size
						else:
							r.remainingshareSize[s] = Constants.ZERO
							r.sharefinished[s] = True
						#print currentTime,r.requestID,s.serverId,r.remainingshareSize[s],size



				if r.checkFinish()==True:
					r.isFinished = True
					r.isActive = False
					print str(currentTime)+": request " + str(r.requestID)+" finishes"

						
			
			currentTime += Constants.SIMULATION_TIMESTEP
			#print "currentTime = "+str(currentTime)

if __name__=="__main__":
	
	serverlist = []
	clientlist = []
	filelist = []
	requestlist = []




	
	for i in range(0,Constants.SERVERNUMBER):
		server = Server(Constants.STORAGEINIT)
		serverlist.append(server)

	for i in range(0,Constants.CLIENTNUMBER):
		client = Client()
		clientlist.append(client)

	#for i in range(0,10):
	file = File(20*1024*1024*1024,serverlist)
	file.store()
	filelist.append(file)

	file = File(20*1024*1024*1024,serverlist)
	file.store()
	filelist.append(file)


	for i in range(0,2):
		requestlist.append([])

	request1 = Request(0,0,filelist,0)
	requestlist[0]=request1

	request2 = Request(1,1,filelist,0.2)
	requestlist[1]=request2

	#request3 = Request(2,1,filelist,1)
	#requestlist.append(request3)



	simulator = Simulator(filelist,requestlist,serverlist)

	simulator.simulate()

	# for f in filelist:
	# 	print f.fileSize



	# for c in clientlist:
	# 	print c.ClientId
	# for s in serverlist:
	# 	print s.ServerId
	#simulator = Simulator()

