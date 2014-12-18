# Create Line Chart
experiment <- read.csv("~/git/dyndco/monitoring/analytics/results/calculated_1418860163.txt", sep=";")
 plot(experiment$timepoint, experiment$median, xlab= "timepoint", ylab= "Median", type='l', col='red') 
 mod <- lm(experiment$median ~ experiment$timepoint)
 abline(mod, col = "red")
plot(experiment$timepoint, experiment$utility, xlab= "timepoint", ylab= "Mean", type='l', col='red') 
plot(experiment$timepoint, experiment$mean, xlab= "timepoint", ylab= "Mean", type='l', col='red') 