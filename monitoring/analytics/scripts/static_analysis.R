
# quality

  # vergleich - linien
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.25/20_20/mean.txt", sep=";")
  plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black', pch=0)
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/results/mgm_synchronous_normal__0.25/20_20/mean.txt", sep=";")
  plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black', pch=1)
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrunsresults/dpop_synchronous_normal__0.25/20_20/mean.txt", sep=";")
  plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black', pch=2)
  plot(experiment3$u_median,type="l",col="red", xlab= "timepoint", ylab= "Utility")
  lines(experiment2$u_median,col="blue")
  lines(experiment1$u_median,col="green")
  legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))

  # scale - punkte
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.5/20_10/mean.txt", sep=";")
  plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrunsresults/maxsum_synchronous_normal__0.75/30_10/mean.txt", sep=";")
  plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  plot(experiment3$u_median,type="l",col="red", xlab= "timepoint", ylab= "Utility")
  lines(experiment2$u_median,col="blue")
  lines(experiment1$u_median,col="green")
  legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.5/10_20/mean.txt", sep=";")
  plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrunsresults/maxsum_synchronous_normal__0.75/10_30/mean.txt", sep=";")
  plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  plot(experiment3$u_median,type="l",col="red", xlab= "timepoint", ylab= "Utility")
  lines(experiment2$u_median,col="blue")
  lines(experiment1$u_median,col="green")
  legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))

  # density
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.5/10_10/mean.txt", sep=";")
  plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrunsresults/maxsum_synchronous_normal__0.75/10_10/mean.txt", sep=";")
  plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  plot(experiment3$u_median,type="l",col="red", xlab= "timepoint", ylab= "Utility")
  lines(experiment2$u_median,col="blue")
  lines(experiment1$u_median,col="green")
  legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))


  # mode
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.25/10_10/mean.txt", sep=";")
  plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_normal__0.5/10_10/mean.txt", sep=";")
  plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  plot(experiment1$u_median,type="l",col="red", xlab= "timepoint", ylab= "Utility")
  lines(experiment2$u_median,col="blue")
  legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))

#############################################################################
  
# time
  
  # scale
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.25/10_10/time.txt", sep=";")
  plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/results/mgm_synchronous_normal__0.25/20_20/time.txt", sep=";")
  plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrunsresults/dpop_synchronous_normal__0.25/30_30/time.txt", sep=";")
  plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  
  plot(experiment3$u_median,type="l",col="red")
  lines(experiment2$u_median,col="blue")
  lines(experiment1$u_median,col="green")
  legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.25/10_10/time.txt", sep=";")
  plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/results/mgm_synchronous_normal__0.25/20_20/time.txt", sep=";")
  plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrunsresults/dpop_synchronous_normal__0.25/30_30/time.txt", sep=";")
  plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  
  plot(experiment3$u_median,type="l",col="red")
  lines(experiment2$u_median,col="blue")
  lines(experiment1$u_median,col="green")
  legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))
  
  experiment1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_normal__0.25/10_10/time.txt", sep=";")
  plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment2 <- read.csv("/Users/daniel/Desktop/finalruns/results/mgm_synchronous_normal__0.25/20_20/time.txt", sep=";")
  plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
  experiment3 <- read.csv("/Users/daniel/Desktop/finalrunsresults/dpop_synchronous_normal__0.25/30_30/time.txt", sep=";")
  plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')

  plot(experiment3$u_median,type="l",col="red")
  lines(experiment2$u_median,col="blue")
  lines(experiment1$u_median,col="green")
  legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))

 
  # density
  
  
  # mode


