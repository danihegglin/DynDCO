
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
  x1 <- experiment1$u_median / 2000
  x2 <- experiment2$u_median / 2000
  x3 <- experiment3$u_median / 2000
  
  y3 <- experiment3$timepoints
  
  plot(x3,type="o", xlab= "execution time [ms]", ylab= "utility value", pch = c(0, rep(0, 100)), xlim=c(0,30), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x1, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  #??quality
  x1 <- (experiment1$a_median + experiment1$m_median) / 750
  x2 <- (experiment2$a_median + experiment2$m_median) / 2000
  x3 <- (experiment3$a_median + experiment3$m_median) / 2000
  
  plot(x3,type="o", xlab= "execution time [ms]", ylab= "quality value", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x1, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  #??---------------------------------------------------------
  
  # vergleich - linien
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_asynchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_asynchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_asynchronous_normal__0.25/40_10/mean.txt", sep=";")
  
  #??utility
  x1 <- experiment1$u_median / 2000
  x2 <- experiment2$u_median / 2000
  x3 <- experiment3$u_median / 2000
  
  y3 <- experiment3$timepoints
  
  plot(x3,type="o", xlab= "execution time [ms]", ylab= "utility value", pch = c(0, rep(0, 100)), xlim=c(0,30), ylim=c(0,1))
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
  
  x1 <- (experiment1$u_median) / 500
  x2 <- (experiment2$u_median) / 500
  x3 <- (experiment3$u_median) / 500
  
  plot(x1,type="o", xlab= "execution time [ms]", ylab= "utiltiy value", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x3, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrun/version2/mgm_synchronous_normal__0.75/20_10/mean.txt", sep=";")
  
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  x3 <- (experiment3$u_median) / 2000
  
  plot(x1,type="o", xlab= "execution time [ms]", ylab= "utiltiy value", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2, pch = c(1, rep(0, 100)))
  lines(x3, pch = c(2, rep(0, 100))) #??value is limited to 2 instead of maximal 10 in this case
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrun/sversion2/dpop_synchronous_normal__0.75/20_10/mean.txt", sep=";")
  
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
  
  x1 <- (experiment1$u_median) / 1000
  x2 <- (experiment2$u_median) / 2000
  x3 <- (experiment3$u_median) / 2000
  x4 <- (experiment4$u_median) / 2000
  x5 <- (experiment5$u_median) / 2000
  
  plot(x1,type="l", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1000))
  lines(x2)
  lines(x3)  
  lines(x4)  
  lines(x5)  
  
  #??mode -> alle drei vergleich
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  plot(x1,type="l",col="red", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2,col="blue")
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_synchronous_normal__0.25/40_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/maxsum_asynchronous_normal__0.25/40_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  plot(x1,type="l",col="red", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2,col="blue") 

  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/mgm_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  plot(x1,type="l",col="red", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2,col="blue") 
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_synchronous_normal__0.25/20_10/mean.txt", sep=";")
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/version2/dpop_asynchronous_normal__0.25/20_10/mean.txt", sep=";")
  x1 <- (experiment1$u_median) / 2000
  x2 <- (experiment2$u_median) / 2000
  plot(x1,type="l",col="red", xlab= "timepoint", ylab= "Utility", pch = c(0, rep(0, 100)), xlim=c(0,25), ylim=c(0,1))
  lines(x2,col="blue") 


#############################################################################
  
# time
  
  # scale -> meetings -> normal, synchronos table, agents: normal, synchronous


 
  # density -> graph
  
  


