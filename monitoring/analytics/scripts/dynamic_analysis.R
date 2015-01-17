
# rate: percentage over time

x1 <- (0.25/500) *1000
x2 <- (0.5/500)*1000
x3 <- (0.75/500)*1000
x4 <- (1/500)*1000

x5 <- (0.25/1000)*1000
x6 <- (0.5/1000)*1000
x7 <- (0.75/1000)*1000
x8 <- (1/1000)*1000

x9 <- (0.25/1500)*1000
x10 <- (0.5/1500)*1000
x11 <- (0.75/1500)*1000
x12 <- (1/1500)*1000

x13 <- (0.25/2000)*1000
x14 <- (0.5/2000)*1000
x15 <- (0.75/2000)*1000
x16 <- (1/2000)*1000

#??file open
y1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_dynamicVariables_500-0-1-0-1_0.25/_/mean.txt", sep=";")
y2 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicVariables_500-0-1-1-1_0.25/_/mean.txt", sep=";")
y3 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_synchronous_dynamicVariables_1000-0-1-0-1_0.25/_/mean.txt", sep=";")
y4 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_aynchronous_dynamicVariables_1000-0-1-0-1_0.25/_/mean.txt", sep=";")

#maxsum_synchronous_dynamicVariables_500-0-1-0-1_0.25
#maxsum_synchronous_dynamicVariables_500-0-1-1-1_0.25


# fixes
y4 <- 200
y8 <- 200
y12 <- 200
y16 <- 200

#??get values
y1t <- c(y1$u_median)
y2t <- c(y2$u_median)
y3t <- c(y3$u_median)
y4t <- c(y4$u_median)
y5t <- c(y5$u_median)
y6t <- c(y6$u_median)
y7t <- c(y7$u_median)
y8t <- c(y8$u_median)
y9t <- c(y9$u_median)
y10t <- c(y10$u_median)
y11t <- c(y11$u_median)
y12t <- c(y12$u_median)
y13t <- c(y13$u_median)
y14t <- c(y14$u_median)
y15t <- c(y15$u_median)
y16t <- c(y16$u_median)

#??average utility
y1f <- mean(y1t)
y2f <- mean(y2t)
y3f <- mean(y3t)
#y4f <- y4
y5f <- mean(y5t)
y6f <- mean(y6t)
y7f <- mean(y7t)
#y8f <- y8
y9f <- mean(y9t)
y10f <- mean(y10t)
y11f <- mean(y11t)
#y12f <- y12
y13f <- mean(y13t)
y14f <- mean(y14t)
y15f <- mean(y15t)
#y16f <- y16

#??create sequences
xs <- c(x1,x2,x3)
        #x5,x6,x7,x8,x9,x10)
        #x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16)
xs2 <- c(x1,x2,x3,x5,x6,x7,x9,x10,x11,x13,x14,x15)

test1 <- c(y1f,y2f,y3f)
test2 <- c(y5f,y6f,y7f)
test3 <- c(y9f,y10f,y11f)
test4 <- c(y13f,y14f,y15f)
          #y5f,y6f,y7f,y8f,y9f,y10f)
          #,y11f,y12f,y13f,y14f,y15f,y16f)

#test5 <- c(y1f,y2f,y3f,y5f,y6f,y7f,y9f,y10f,y11f,y13f,y14f,y15f)
#plot(xs2,test5,xlab= "Rate [Change/Time]", ylab= "avg. utility", pch = c(0, rep(0, 100)), main=
       "Avg. Utility over Rate [Change/Time] - maxsum (Density 0.25, Agents 30, Meetings 15)",ylim=c(100,300))
#abline(lm(test5~xs2), col="black")

#??plot: x -> rate, y = average utility
rate <- c(0.25,0.5)
final1 <-c(y1f,y2f)
final2 <-c(y3f,y4f)
plot(rate,final1,type="o", xlab= "Rate", ylab= "avg. utility", pch = c(0, rep(0, 100)), main=
       "Avg. Utility over Rate - maxsum (Density 0.25, Synchronous, Agents 30, Meetings 15)",ylim=c(0,500))
lines(rate,final2,pch = 1, type="o")

legend('topright', c("synchronous","asynchronous") , lty=1, bty='n', cex=1.2, pch = c(0, 1))

#??file open
y1 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_500-0-25-multiple_0.25/_/mean.txt", sep=";")
y2 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_500-0-5-multiple_0.25/_/mean.txt", sep=";")
y3 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_500-0-75-multiple_0.25/_/mean.txt", sep=";")
y4 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_500-0-1-multiple_0.25/_/mean.txt", sep=";")
y5 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_1000-0-25-multiple_0.25/_/mean.txt", sep=";")
y6 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_1000-0-5-multiple_0.25/_/mean.txt", sep=";")
y7 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_1000-0-75-multiple_0.25/_/mean.txt", sep=";")
y8 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_1000-0-1-multiple_0.25/_/mean.txt", sep=";")
y9 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_1500-0-25-multiple_0.25/_/mean.txt", sep=";")
y10 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_1500-0-5-multiple_0.25/_/mean.txt", sep=";")
y11 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_1500-0-75-multiple_0.25/_/mean.txt", sep=";")
y12 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_1500-0-1-multiple_0.25/_/mean.txt", sep=";")
y13 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_2000-0-25-multiple_0.25/_/mean.txt", sep=";")
y14 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_2000-0-5-multiple_0.25/_/mean.txt", sep=";")
y15 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_2000-0-75-multiple_0.25/_/mean.txt", sep=";")
y16 <- read.csv("/Users/daniel/Desktop/finalruns/results/maxsum_asynchronous_dynamicConstraints_2000-0-1-multiple_0.25/_/mean.txt", sep=";")


# fixes
y4 <- 200
y8 <- 200
y12 <- 200
y16 <- 200

#??get values
y1t <- c(y1$u_median)
y2t <- c(y2$u_median)
y3t <- c(y3$u_median)
y4t <- c(y4$u_median)
y5t <- c(y5$u_median)
y6t <- c(y6$u_median)
y7t <- c(y7$u_median)
y8t <- c(y8$u_median)
y9t <- c(y9$u_median)
y10t <- c(y10$u_median)
y11t <- c(y11$u_median)
y12t <- c(y12$u_median)
y13t <- c(y13$u_median)
y14t <- c(y14$u_median)
y15t <- c(y15$u_median)
y16t <- c(y16$u_median)

#??average utility
y1f <- mean(y1t)
y2f <- mean(y2t)
y3f <- mean(y3t)
#y4f <- y4
y5f <- mean(y5t)
y6f <- mean(y6t)
y7f <- mean(y7t)
#y8f <- y8
y9f <- mean(y9t)
y10f <- mean(y10t)
y11f <- mean(y11t)
#y12f <- y12
y13f <- mean(y13t)
y14f <- mean(y14t)
y15f <- mean(y15t)
#y16f <- y16

#??create sequences
xs <- c(x1,x2,x3)
#x5,x6,x7,x8,x9,x10)
#x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16)
xs2 <- c(x1,x2,x3,x5,x6,x7,x9,x10,x11,x13,x14,x15)

test1 <- c(y1f,y2f,y3f)
test2 <- c(y5f,y6f,y7f)
test3 <- c(y9f,y10f,y11f)
test4 <- c(y13f,y14f,y15f)
#y5f,y6f,y7f,y8f,y9f,y10f)
#,y11f,y12f,y13f,y14f,y15f,y16f)

#test5 <- c(y1f,y2f,y3f,y5f,y6f,y7f,y9f,y10f,y11f,y13f,y14f,y15f)
#plot(xs2,test5,xlab= "Rate [Change/Time]", ylab= "avg. utility", pch = c(0, rep(0, 100)), main=
#Avg. Utility over Rate [Change/Time] - maxsum (Density 0.25, Agents 30, Meetings 15)",ylim=c(100,300))
#abline(lm(test5~xs2), col="black")

#??plot: x -> rate, y = average utility
rate <- c(0.25,0.5,0.75)
plot(rate,test1,type="o", xlab= "Rate", ylab= "avg. utility", pch = c(0, rep(0, 100)), main=
       "Avg. Utility over Rate - maxsum (Density 0.25, Asynchronous, Agents 30, Meetings 15)",ylim=c(0,500))
lines(rate,test2,pch = 1, type="o")
lines(rate,test3,pch = 2, type="o")
legend('topright', c("500ms", "1000ms","1500ms") , lty=1, bty='n', cex=1.2, pch = c(0, 1))

