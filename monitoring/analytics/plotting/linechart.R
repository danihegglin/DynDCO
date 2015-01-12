# Synchronous Verlauf
experiment1 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/maxsum_synchronous_normal__0.25/25_10/mean.txt", sep=";")
plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
experiment2 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/mgm_synchronous_normal__0.25/25_10/mean.txt", sep=";")
plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
experiment3 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/dpop_synchronous_normal__0.25/5_10/mean.txt", sep=";")
plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')

plot(experiment3$u_median,type="l",col="red")
lines(experiment2$u_median,col="blue")
lines(experiment1$u_median,col="green")
legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))

# Asynchronous Verlauf
experiment1 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/maxsum_asynchronous_normal__0.25/5_10/mean.txt", sep=";")
plot(experiment1$timepoint, experiment1$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
experiment2 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/mgm_asynchronous_normal__0.25/25_20/mean.txt", sep=";")
plot(experiment2$timepoint, experiment2$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
experiment3 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/dpop_asynchronous_normal__0.25/25_20/mean.txt", sep=";")
plot(experiment3$timepoint, experiment3$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')

plot(experiment3$u_median,type="l",col="red")
lines(experiment2$u_median,col="blue")
lines(experiment1$u_median,col="green")
legend(2000,9.5, c("Maxsum","MGM","DPOP"),lty=c(1,1), lwd=c(2.5,2.5),col=c("red","blue","green"))

# scalability
experiment2 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/maxsum_asynchronous_normal/scalability_meeting_time.txt", sep=";")
plot(experiment2$agents, experiment2$time, xlab= "number", ylab= "time", type='l', col='red')
experiment3 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/maxsum_asynchronous_normal/scalability_agent_time.txt", sep=";")
plot(experiment3$meetings, experiment3$time, xlab= "number", ylab= "time", type='l', col='red')
experiment2 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/mgm_asynchronous_normal/scalability_meeting_time.txt", sep=";")
plot(experiment2$agents, experiment2$time, xlab= "number", ylab= "time", type='l', col='red')
experiment3 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/mgm_asynchronous_normal/scalability_agent_time.txt", sep=";")
plot(experiment3$meetings, experiment3$time, xlab= "number", ylab= "time", type='l', col='red')
experiment2 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/dpop_asynchronous_normal/scalability_meeting_time.txt", sep=";")
plot(experiment2$agents, experiment2$time, xlab= "number", ylab= "time", type='l', col='red')
experiment3 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/dpop_asynchronous_normal/scalability_agent_time.txt", sep=";")
plot(experiment3$meetings, experiment3$time, xlab= "number", ylab= "time", type='l', col='red')

experiment4 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/maxsum_synchronous_dynamicConstraints_2000,0.5/5_10/mean.txt", sep=";")
plot(experiment4$timepoint, experiment4$u_median, xlab= "timepoint", ylab= "Utility", type='l', col='black')

# heatmap
experiment3 <- read.csv("/Users/daniel/git/dyndco/monitoring/analytics/results/maxsum_asynchronous_normal/matrix_time.txt", sep=";")
heatmap <- experiment3[,-1]
rownames(heatmap) <- experiment3[,1]
colnames(heatmap) <- experiment3[,1]
experiment_matrix <- data.matrix(heatmap)
exp_heatmap <- heatmap(experiment_matrix, Rowv=NA, Colv=NA, col = heat.colors(256), scale="column", margins=c(5,10))

# distributions
set.seed(1234)
df <- data.frame(cond = factor( rep(c("A","B"), each=200) ), 
                 rating = c(rnorm(200),rnorm(200, mean=.8)))
library(ggplot2)

# Density curve
ggplot(df, aes(x=rating)) + geom_density()

# Density plots
ggplot(df, aes(x=rating, colour=cond)) + geom_density()

# Find the mean of each group
library(plyr)
cdf <- ddply(df, "cond", summarise, rating.mean=mean(rating))

# Density plots with means
ggplot(df, aes(x=rating, colour=cond)) + geom_density() +
  geom_vline(data=cdf, aes(xintercept=rating.mean,  colour=cond),
             linetype="dashed", size=1)

# Simple Scatterplot
attach(mtcars)
plot(wt, mpg, main="Scatterplot Example", 
     xlab="Car Weight ", ylab="Miles Per Gallon ", pch=19)

# 3D plotting
x <- 1:5/10
y <- 1:5
z <- x %o% y
z <- z + .2*z*runif(25) - .1*z

library(rgl)
persp3d(x, y, z, col="skyblue")
rgl.snapshot("myplot.png")


