# Create Line Chart
experiment <- read.csv("~/git/dyndco/monitoring/analytics/results/combined.txt", sep=";")
plot(experiment$timestamp, experiment$utility, xlab= "timepoint", ylab= "Utility", type='l', col='red') 
