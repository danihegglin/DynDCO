plot.new()
experiment <- read.csv("~/git/dyndco/monitoring/experiments/results1418302332661.txt", sep=";")

for(experiment$timestamp){
  println(experiment$timestamp)
}

plot(experiment$timestamp, experiment$utility)
mean <- mean(experiment$utility)
abline(h = mean, col = "black", lwd = 2)
