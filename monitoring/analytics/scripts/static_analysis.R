
#x3 <- rollmedian(experiment3$u_median,100)

#y3 <- experiment3$timepoint * 1000
#x3 <- experiment3$u_median
#plot(y3, x3, xlab= "execution time [s/10]", ylab= "utility", type = "o", pch = c(0, rep(NA, 100)), xlim=c(0,1000), ylim=c(0,1000))

#x2 <- experiment2$u_median
#y2 <- experiment2$timepoint * 1000
#plot(y2, x2, xlab= "execution time [ms]", ylab= "utility", type = "o", pch = c(0, rep(NA, 100)), xlim=c(0,200), ylim=c(0,1000))

#x1 <- experiment1$u_median
#y1 <- experiment1$timepoint * 1000
#plot(y1, x1, xlab= "execution time [ms]", ylab= "utility", type = "o", pch = c(0, rep(NA, 100)), xlim=c(0,200), ylim=c(0,1000))

# quality

  # vergleich - linien
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  
  #??utility
  x1 <- experiment1$u_median / 666
  x2 <- experiment2$u_median / 2000
  x3 <- experiment3$u_median / 2000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", main = "Utility Over Time Comparison (Density 0.25, Agents 20, Meetings 10)", 
       xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), xlim=c(0,3000), ylim=c(0,1))
  lines(timeslots2, x2, pch = 3, type="o") #c(1, rep(0, 100))
  lines(timeslots3, x3, pch = 2, type="o") #??value is limited to 2 instead of maximal 10 in this case c(2, rep(0, 100))
  legend('bottomright', c("MaxSum", "MGM", "DPOP") , lty=1, bty='n', cex=1.2, pch = c(0, 3, 2))
  
  #??quality
  x1 <- (experiment1$a_median + experiment1$m_median) / 666
  x2 <- (experiment2$a_median + experiment2$m_median) / 2000
  x3 <- (experiment3$a_median + experiment3$m_median) / 2000
  
  plot(x3,type="o", xlab= "execution time [ms]", ylab= "quality value", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x1, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  #??---------------------------------------------------------
  
  # vergleich - scale
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  
  #??utility
  x1 <- experiment1$u_median / 666
  x2 <- experiment2$u_median / 2000
  x3 <- experiment3$u_median / 2000
  
  y3 <- experiment3$timepoints
  
  plot(x3,type="o", xlab= "execution time [ms]", ylab= "utility value", pch = c(0, rep(0, 100)), xlim=c(0,50), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x1, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  #??quality
  x1 <- (experiment1$a_median + experiment1$m_median) / 2000
  x2 <- (experiment2$a_median + experiment2$m_median) / 2000
  x3 <- (experiment3$a_median + experiment3$m_median) / 2000
  
  plot(x3,type="o", xlab= "execution time [ms]", ylab= "quality value", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x1, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  
  #??----------------------------------------------------------
  
  # density -> vergleich 3er
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.75/20_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 666
  x2 <- (experiment2$u_median) / 666
  x3 <- (experiment3$u_median) / 666
  
  plot(x1,type="o", xlab= "execution time [ms]", ylab= "utiltiy value", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x3, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.75/20_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  x3 <- (experiment3$u_median) / 2000
  
  plot(x1,type="o", xlab= "execution time [ms]", ylab= "utiltiy value", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x3, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.75/20_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  x3 <- (experiment3$u_median) / 2000
  
  plot(x1,type="o", xlab= "execution time [ms]", ylab= "utiltiy value", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x3, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  #??scale -> andere in appendix
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/30_10/mean.txt", sep=";")
  experiment4 <- read.csv("/Users/daniel/Desktop/finalruns/version2//maxsum_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment5 <- read.csv("/Users/daniel/Desktop/finalruns/version2//maxsum_synchronous_normal__0.25/50_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 666
  x2 <- (experiment2$u_median) / 666
  x3 <- (experiment3$u_median) / 666
  x4 <- (experiment4$u_median) / 666
  x5 <- (experiment5$u_median) / 666
  
  plot(x1,type="l", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2)
  lines(x3)  
  lines(x4)  
  lines(x5)  
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/30_10/mean.txt", sep=";")
  experiment4 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment5 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/50_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  x3 <- (experiment3$u_median) / 2000
  x4 <- (experiment4$u_median) / 2000
  x5 <- (experiment5$u_median) / 2000
  
  plot(x1,type="l", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2)
  lines(x3)  
  lines(x4)  
  lines(x5)  
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/30_10/mean.txt", sep=";")
  experiment4 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment5 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/50_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  x3 <- (experiment3$u_median) / 2000
  x4 <- (experiment4$u_median) / 2000
  x5 <- (experiment5$u_median) / 2000
  
  plot(x1,type="l", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2)
  lines(x3)  
  lines(x4)  
  lines(x5)  
  
  #??mode -> alle drei vergleich
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 666
  x2 <- (experiment2$u_median) / 666
  plot(x1,type="l",col="red", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,30), ylim=c(0,1))
  lines(x2,col="blue")
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_asynchronous_normal__0.25/40_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 333
  x2 <- (experiment2$u_median) / 333
  plot(x1,type="l",col="red", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,30), ylim=c(0,1))
  lines(x2,col="blue") 

  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  plot(x1,type="l",col="red", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,30), ylim=c(0,1))
  lines(x2,col="blue") 
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  plot(x1,type="l",col="red", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,30), ylim=c(0,1))
  lines(x2,col="blue") 


#############################################################################
  
# time
  
  # scale -> meetings -> normal, synchronos table, agents: normal, synchronous
  # maxsum
  y <- c(10,20,30,40,50)
  x1 <- c(31.1,29,87.2,227.8,429.2)
  x2 <- c(244,91.8,70.7,26.7,31.5)
  plot(y, x1,type="l",col="red", xlab= "density", ylab= "convergence time [ms]", pch = c(0, rep(0, 100)), ylim=c(0, 100))
  lines(y, x2,col="blue") 
  lines(y, x3,col="blue")
  
  #??mgm
  y <- c(10,20,30,40,50)
  x1 <- c(46.45,30.7,19.2,39.2,39.83)
  x2 <- c(10.3,3.5,6.2,XXXX,XXXX) # async
  plot(y, x1,type="l",col="red", xlab= "density", ylab= "convergence time [ms]", pch = c(0, rep(0, 100)), ylim=c(0, 100))
  lines(y, x2,col="blue") 
  lines(y, x3,col="blue") 
  
  #??dpop
  y <- c(10,20,30,40,50)
  x1 <- c(100.2,162.375,50.25,392.88,356.5,334.4)
  x2 <- c(77.8,151, XXX,XXX,XXX) # async
  plot(y, x1,type="l",col="red", xlab= "density", ylab= "convergence time [ms]", pch = c(0, rep(0, 100)), ylim=c(0, 100))
  lines(y, x2,col="blue") 
  lines(y, x3,col="blue") 
 
  # density -> graphs
  
  #??10 agents
  y <- c(0.25,0.5,0.75)
  x1 <- c(31.1,12.9,1.5)
  x2 <- c(46.45,44.2,12.6)
  x3 <- c(100.2,91.5,37.16)
  plot(y, x1,type="l",col="red", xlab= "density", ylab= "convergence time [ms]", pch = c(0, rep(0, 100)), ylim=c(0, 200))
  lines(y, x2,col="blue") 
  lines(y, x3,col="blue") 
  
  #??30 agents
  y <- c(0.25,0.5,0.75)
  x1 <- c(87.2,128.28,89.3)
  x2 <- c(19.2,50.5,4.8)
  x3 <- c(50.25,134.6,174.3)
  plot(y, x1,type="l",col="red", xlab= "density", ylab= "convergence time [ms]", pch = c(0, rep(0, 100)), ylim=c(0, 200))
  lines(y, x2,col="blue") 
  lines(y, x3,col="blue") 

