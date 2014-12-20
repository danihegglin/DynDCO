#!/bin/bash
#SBATCH --job-name=108
#SBATCH -N 1
#SBATCH --cpus-per-task=10
#SBATCH -t 0-00:59:59
#SBATCH --mem=50
#SBATCH --mail-type=ALL
#SBATCH --export=ALL
#SBATCH -o out/108.out
#SBATCH -e err/108.err

#...commands to be run before jobs starts...
jarname=dyndco.jar
mainClass=ch.uzh.dyndco.testbed.Testrunner
workingDir=/home/slurm/USER_ID-${SLURM_JOB_ID}
vm_args=" -Xmx10240m -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:+UseNUMA -XX:-UseBiasedLocking -XX:MaxInlineSize=1024"

#...copy data from home to work folder if needed....
# copy jar
#srun --ntasks-per-node=1 cp ~/$jarname $workingDir/

# run test directly from home folder
srun --ntasks-per-node=1 --partition=slow /home/user/USER_ID/jdk1.7.0_45/bin/java $jvmParameters -cp ~/data/$jarname $mainClass 108
#or run test from work folder
#srun --ntasks-per-node=1 --partition=slow /home/user/USER_ID/jdk1.7.0_45/bin/java $jvmParameters -cp $workingDir/$jarname $mainClass 108

#...commands to be run after jobs have finished...