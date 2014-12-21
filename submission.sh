#!/bin/bash
#SBATCH --job-name=dyndco1
#SBATCH -N 1
#SBATCH --cpus-per-task=10
#SBATCH -t 0-00:59:59
#SBATCH --mem=50
#SBATCH --mail-type=ALL
#SBATCH --export=ALL
#SBATCH -o /home/user/hegglin/log/out/dyndco1.out
#SBATCH -e /home/user/hegglin//log/err/dyndco1.err

#...commands to be run before jobs starts...
jarname=dyndco-1.1-SNAPSHOT.jar
mainClass=ch.uzh.dyndco.testbed.Driver
#workingDir=/home/slurm/USER_ID-${SLURM_JOB_ID}
vm_args=" -Xmx10240m -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:+UseNUMA -XX:-UseBiasedLocking -XX:MaxInlineSize=1024"

#...copy data from home to work folder if needed....
# copy jar
#srun --ntasks-per-node=1 cp ~/$jarname $workingDir/

# run test directly from home folder
srun --ntasks-per-node=1 --partition=fast /home/user/hegglin/jdk/jdk1.8.0_25/bin/java $jvmParameters -cp ~/data/$jarname $mainClass dyndco1
#or run test from work folder
#srun --ntasks-per-node=1 --partition=slow /home/user/USER_ID/jdk1.7.0_45/bin/#java $jvmParameters -cp $workingDir/$jarname $mainClass 108

#...commands to be run after jobs have finished...