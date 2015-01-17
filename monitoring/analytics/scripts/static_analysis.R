
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
  
  plot(timeslots1, x1,type="o", main = "Utility Over Time Comparison (Density 0.25, Synchronous, Agents 20, Meetings 10)", 
       xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), xlim=c(0,2500), ylim=c(0,1))
  lines(timeslots2, x2, pch = 3, type="o") #c(1, rep(0, 100))
  lines(timeslots3, x3, pch = 2, type="o") #??value is limited to 2 instead of maximal 10 in this case c(2, rep(0, 100))
  legend('bottomright', c("MaxSum", "MGM", "DPOP") , lty=1, bty='n', cex=1.2, pch = c(0, 3, 2))
  
  #??quality
  x1 <- (experiment1$a_median + experiment1$m_median) / 833
  x2 <- (experiment2$a_median + experiment2$m_median) / 2500
  x3 <- (experiment3$a_median + experiment3$m_median) / 2500
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", main = "Quality Over Time Comparison (Density 0.25, Synchronous, Agents 20, Meetings 10)", 
       xlab= "execution time [ms]", ylab= "median quality", pch = c(0, rep(0, 100)), xlim=c(0,2500), ylim=c(0,1))
  lines(timeslots2, x2, pch = 3, type="o")
  lines(timeslots3, x3, pch = 2, type="o") #??value is limited to 2 instead of maximal 10 in this case
  legend('bottomright', c("MaxSum", "MGM", "DPOP") , lty=1, bty='n', cex=1.2, pch = c(0, 3, 2))
  
  #??---------------------------------------------------------
  
  # vergleich - scale
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  
  #??utility
  x3 <- experiment1$u_median / 333
  x2 <- experiment2$u_median / 1000
  x1 <- experiment3$u_median / 1000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), 
       main = "Utility Over Time Comparison (Density 0.25, Synchronous, Agents 40, Meetings 10)", xlim=c(0,5000), ylim=c(0,1))
  lines(timeslots2, x2, pch = 3, type="o")
  lines(timeslots3, x3, pch = 2, type="o") #??value is limited to 2 instead of maximal 10 in this case
  legend('topright', c("MaxSum", "MGM", "DPOP") , lty=1, bty='n', cex=1.2, pch = c(0, 3, 2))
  
  #??quality
  x3 <- (experiment1$a_median + experiment1$m_median) / 333
  x2 <- (experiment2$a_median + experiment2$m_median) / 1000
  x1 <- (experiment3$a_median + experiment3$m_median) / 1000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median quality", 
       main = "Quality Over Time Comparison (Density 0.25, Synchronous, Agents 40, Meetings 10)", pch = c(0, rep(0, 50)), xlim=c(0,5000), ylim=c(0,1))
  lines(timeslots2, x2,  pch = 3, type="o")
  lines(timeslots3, x3,  pch = 2, type="o") #??value is limited to 2 instead of maximal 10 in this case
  legend('topright', c("MaxSum", "MGM", "DPOP") , lty=1, bty='n', cex=1.2, pch = c(0, 3, 2)) 
  
  #??----------------------------------------------------------
  
  # density -> vergleich 3er
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.75/20_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 666
  x2 <- (experiment2$u_median) / 666
  x3 <- (experiment3$u_median) / 666
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", 
       main = "Utility Over Density MaxSum (Synchronous, Agents 20, Meetings 10)", pch = c(0, rep(0, 100)), xlim=c(0,5000), ylim=c(0,1))
  lines(timeslots2, x2, pch = 3, type="o")
  lines(timeslots3, x3, pch = 2, type="o")
  legend('topright', c("0.25", "0.5", "0.75") , lty=1, bty='n', cex=1.2, pch = c(0, 3, 2)) 
  
  ##################################
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.75/20_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  x3 <- (experiment3$u_median) / 2000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", 
       main = "Utility Over Density MGM (Synchronous, Agents 20, Meetings 10)", pch = c(0, rep(0, 100)), xlim=c(0,5000), ylim=c(0,1))
  lines(timeslots2, x2, pch = 3, type="o")
  lines(timeslots3, x3, pch = 2, type="o")
  legend('topright', c("0.25", "0.5", "0.75") , lty=1, bty='n', cex=1.2, pch = c(0, 3, 2)) 
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.75/20_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  x3 <- (experiment3$u_median) / 2000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", 
       main = "Utility Over Density DPOP (Synchronous, Agents 20, Meetings 10)", pch = c(0, rep(0, 100)), xlim=c(0,5000), ylim=c(0,1))
  lines(timeslots2, x2, pch = 3, type="o")
  lines(timeslots3, x3, pch = 2, type="o")
  legend('topright', c("0.25", "0.5", "0.75") , lty=1, bty='n', cex=1.2, pch = c(0, 3, 2)) 
  
  ##################################
  
  #??scale -> andere in appendix
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/30_10/mean.txt", sep=";")
  experiment4 <- read.csv("/Users/daniel/Desktop/finalruns/version2//maxsum_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment5 <- read.csv("/Users/daniel/Desktop/finalruns/version2//maxsum_synchronous_normal__0.25/50_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 333
  x2 <- (experiment2$u_median) / 1320
  x3 <- (experiment3$u_median) / 1320
  x4 <- (experiment4$u_median) / 2640
  x5 <- (experiment5$u_median) / 5280
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  timeslots4 <- seq.int(0,length(x4) * 100 - 100,100)
  timeslots5 <- seq.int(0,length(x5) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), main=
       "Utility Over Time - Agent Scalability - MaxSum (Density 0.25, Synchronous, Meetings 10)", xlim=c(0,1500), ylim=c(0,0.5))
  lines(timeslots2,x2, pch = 1, type="o")
  lines(timeslots3,x3, pch = 2, type="o")  
  lines(timeslots4,x4, pch = 3, type="o")  
  lines(timeslots5,x5, pch = 4, type="o")  
  legend('topright', c("10", "20", "30", "40", "50") , lty=1, bty='n', cex=1.2, pch = c(0, 1,2,3,4)) 
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/30_10/mean.txt", sep=";")
  experiment4 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment5 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/50_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 4000
  x3 <- (experiment3$u_median) / 8000
  x4 <- (experiment4$u_median) / 16000
  x5 <- (experiment5$u_median) / 32000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  timeslots4 <- seq.int(0,length(x4) * 100 - 100,100)
  timeslots5 <- seq.int(0,length(x5) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), main=
         "Utility Over Time - Agent Scalability - MGM (Density 0.25, Synchronous, Meetings 10)", xlim=c(0,1500), ylim=c(0,0.5))
  lines(timeslots2,x2, pch = 1, type="o")
  lines(timeslots3,x3,pch = 2, type="o")
  lines(timeslots4,x4,pch = 3, type="o")
  lines(timeslots5,x5,pch = 4, type="o")
  legend('topright', c("10", "20", "30", "40", "50") , lty=1, bty='n', cex=1.2, pch = c(0,1,2,3,4)) 
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/30_10/mean.txt", sep=";")
  experiment4 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment5 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/50_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 4000
  x3 <- (experiment3$u_median) / 8000
  x4 <- (experiment4$u_median) / 16000
  x5 <- (experiment5$u_median) / 32000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  timeslots4 <- seq.int(0,length(x4) * 100 - 100,100)
  timeslots5 <- seq.int(0,length(x5) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), main=
         "Utility Over Time - Agent Scalability - DPOP (Density 0.25, Synchronous, Meetings 10)", xlim=c(0,1500), ylim=c(0,0.5))
  lines(timeslots2,x2,pch = 1, type="o")
  lines(timeslots3,x3,pch = 2, type="o")  
  lines(timeslots4,x4,pch = 3, type="o")
  lines(timeslots5,x5,pch = 4, type="o")
  
  legend('topright', c("10", "20", "30", "40", "50") , lty=1, bty='n', cex=1.2, pch = c(0, 1,2,3,4)) 
  
  #??mode -> alle drei vergleich
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 666
  x2 <- (experiment2$u_median) / 666
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), main=
         "Synchronous vs. Asynchronous - MaxSum (Density 0.25, Agents 20, Meetings 10)", xlim=c(0,3000), ylim=c(0,1))
  lines(timeslots2, x2,pch = 1, type="o")
  legend('topright', c("Synchronous", "Asynchronous") , lty=1, bty='n', cex=1.2, pch = c(0, 1)) 
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_asynchronous_normal__0.25/40_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 333
  x2 <- (experiment2$u_median) / 333
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), main=
         "Synchronous vs. Asynchronous - MaxSum (Density 0.25, Agents 40, Meetings 10)", xlim=c(0,3000), ylim=c(0,1))
  lines(timeslots2, x2,pch = 1, type="o")
  legend('topright', c("Synchronous", "Asynchronous") , lty=1, bty='n', cex=1.2, pch = c(0, 1))
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), main=
         "Synchronous vs. Asynchronous - MGM (Density 0.25, Agents 20, Meetings 10)", xlim=c(0,3000), ylim=c(0,1))
  lines(timeslots2, x2,pch = 1, type="o")
  legend('topright', c("Synchronous", "Asynchronous") , lty=1, bty='n', cex=1.2, pch = c(0, 1))
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  
  timeslots1 <- seq.int(0,length(x1) * 100 - 100,100)
  timeslots2 <- seq.int(0,length(x2) * 100 - 100,100)
  timeslots3 <- seq.int(0,length(x3) * 100 - 100,100)
  
  plot(timeslots1, x1,type="o", xlab= "execution time [ms]", ylab= "median utility", pch = c(0, rep(0, 100)), main=
         "Synchronous vs. Asynchronous - DPOP (Density 0.25, Agents 20, Meetings 10)", xlim=c(0,3000), ylim=c(0,1))
  lines(timeslots2, x2,pch = 1, type="o")
  legend('topright', c("Synchronous", "Asynchronous") , lty=1, bty='n', cex=1.2, pch = c(0, 1))

#############################################################################
  
# time
  
  # scale -> meetings -> normal, synchronos table, agents: normal, synchronous
  # maxsum
  y <- c(10,20,30,40,50)
  x1 <- c(31.1,29,87.2,227.8,429.2)
  x2 <- c(244,91.8,70.7,26.7,31.5)
  plot(y, x1,type="o", xlab= "Agents", ylab= "convergence time [ms]", main=
       "Convergence Time - Agent Scalability - Synchronous vs. Asynchronous - MaxSum (Density 0.25, Meetings 10)", pch = c(0, rep(0, 100)), ylim=c(0, 250))
  lines(y, x2,pch = 1, type="o")
  legend('topright', c("Synchronous", "Asynchronous") , lty=1, bty='n', cex=1.2, pch = c(0, 1))
  
  #??mgm
  y <- c(10,20,30,40,50)
  x1 <- c(46.45,30.7,19.2,39.2,39.83)
  x2 <- c(10.3,3.5,6.2,5.2,10.6) # async
  plot(y, x1,type="o", xlab= "Agents", ylab= "convergence time [ms]", main=
         "Convergence Time - Agent Scalability - Synchronous vs. Asynchronous - MGM (Density 0.25, Meetings 10)", pch = c(0, rep(0, 100)), ylim=c(0, 250))
  lines(y, x2,pch = 1, type="o")
  legend('topright', c("Synchronous", "Asynchronous") , lty=1, bty='n', cex=1.2, pch = c(0, 1))
  
  #??dpop
  y <- c(10,20,30,40,50)
  x1 <- c(100.2,162.375,50.25,392.88,356.5)
  x2 <- c(77.8,151, 192,188.5,224.3) # async
  plot(y, x1,type="o", xlab= "Agents", ylab= "convergence time [ms]", main=
         "Convergence Time - Agent Scalability - Synchronous vs. Asynchronous - DPOP (Density 0.25, Meetings 10)", pch = c(0, rep(0, 100)), ylim=c(0, 250))
  lines(y, x2,pch = 1, type="o")
  legend('topright', c("Synchronous", "Asynchronous") , lty=1, bty='n', cex=1.2, pch = c(0, 1))
 
  # density -> graphs
  
  #??10 agents
  y <- c(0.25,0.5,0.75)
  x1 <- c(31.1,12.9,1.5)
  x2 <- c(46.45,44.2,12.6)
  x3 <- c(100.2,91.5,37.16)
  plot(y, x1,type="o",xlab= "density", ylab= "convergence time [ms]", main=
       "Utility Over Time - Density (Agents 10, Meetings 10)", pch = c(0, rep(0, 100)), ylim=c(0, 200))
  lines(y, x2,pch = 1, type="o")
  lines(y, x3,pch = 2, type="o")
  legend('topright', c("MaxSum", "MGM", "DPOP") , lty=1, bty='n', cex=1.2, pch = c(0, 1,2))
  
  #??30 agents
  y <- c(0.25,0.5,0.75)
  x1 <- c(87.2,128.28,89.3)
  x2 <- c(19.2,50.5,4.8)
  x3 <- c(50.25,134.6,174.3)
  plot(y, x1,type="o",xlab= "density", ylab= "convergence time [ms]", main=
         "Utility Over Time - Density (Agents 30, Meetings 10)", pch = c(0, rep(0, 100)), ylim=c(0, 200))
  lines(y, x2,pch = 1, type="o")
  lines(y, x3,pch = 2, type="o")
  legend('topright', c("MaxSum", "MGM", "DPOP") , lty=1, bty='n', cex=1.2, pch = c(0, 1,2))
