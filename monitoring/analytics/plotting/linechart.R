# Create Line Chart
experiment <- read.csv("~/git/dyndco/monitoring/analytics/results/combined.txt", sep=";")
plot(experiment$timestamp, experiment$utility, xlab= "timepoint", ylab= "Utility", type='l', col='red') 

experiment2 <- read.csv("~/Desktop/scalability_m.txt", sep=";")
plot(experiment2$number, experiment2$time, xlab= "number", ylab= "time", type='l', col='red')

experiment3 <- read.csv("~/Desktop/scalability_a.txt", sep=";")
plot(experiment3$number, experiment3$time, xlab= "number", ylab= "time", type='l', col='red')

