# Create Line Chart
# <<<<<<< HEAD
# experiment <- read.csv("~/git/dyndco/monitoring/analytics/results/calculated_1418860163.txt", sep=";")
#  plot(experiment$timepoint, experiment$median, xlab= "timepoint", ylab= "Median", type='l', col='red') 
#  mod <- lm(experiment$median ~ experiment$timepoint)
#  abline(mod, col = "red")
# plot(experiment$timepoint, experiment$utility, xlab= "timepoint", ylab= "Mean", type='l', col='red') 
# plot(experiment$timepoint, experiment$mean, xlab= "timepoint", ylab= "Mean", type='l', col='red') 
# =======
experiment <- read.csv("~/git/dyndco/monitoring/analytics/results/t20_a5_m16/calculated_1418908704.txt", sep=";")
plot(experiment$timepoint, experiment$utility, xlab= "timepoint", ylab= "Utility", type='l', col='red')

plot(experiment$timepoint, experiment$median, xlab= "timepoint", ylab= "Utility", type='l', col='black')
mod <- lm(experiment$median ~ experiment$timepoint)
abline(mod, col = "blue")

plot(experiment$timepoint, experiment$mean, xlab= "timepoint", ylab= "Utility", type='l', col='red')

experiment2 <- read.csv("~/Desktop/scalability_m.txt", sep=";")
plot(experiment2$number, experiment2$time, xlab= "number", ylab= "time", type='l', col='red')

experiment3 <- read.csv("~/Desktop/scalability_a.txt", sep=";")
plot(experiment3$number, experiment3$time, xlab= "number", ylab= "time", type='l', col='red')
# >>>>>>> c5870fe60a6309c6245b225ad8d5077818c271d5
