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
mainClass=ch.uzh.dyndco.testbed.MultipleDriver
#workingDir=/home/slurm/USER_ID-${SLURM_JOB_ID}
vm_args=" -Xmx10240m -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:+UseNUMA -XX:-UseBiasedLocking -XX:MaxInlineSize=1024"

#...copy data from home to work folder if needed....
# copy jar
#srun --ntasks-per-node=1 cp ~/$jarname $workingDir/

# run test directly from home folder
srun --ntasks-per-node=1 --partition=02 /home/user/hegglin/jdk/jdk1.8.0_25/bin/java $jvmParameters -cp ~/data/$jarname $mainClass \ 
--algorithm $ALGORITHM --execution $EXECUTION --mode $MODE --param $PARAMS \ 
--timeslots $TIMESLOTS --meetings $MEETINGS --agents $AGENTS --runs $RUNS --factor $FACTOR --max $MAX

#...commands to be run after jobs have finished...