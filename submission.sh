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

echo "density" $1;
echo "algorithm" $2;
echo "execution" $3;
echo "mode" $4;
echo "param" $5 ;
echo "timeslots" $6;
echo "meetings" $7;
echo "agents" $8;
echo "runs" $9;
echo "factoragents" ${10};
echo "factormeetings" ${11};
echo "maxagents" ${12};
echo "maxmeetings" ${13};

#...commands to be run before jobs starts...
jarname=dyndco-1.1-SNAPSHOT.jar
mainClass=ch.uzh.dyndco.testbed.MultipleDriver
#workingDir=/home/slurm/USER_ID-${SLURM_JOB_ID}
vm_args=" -Xmx8G -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:+UseNUMA -XX:-UseBiasedLocking -XX:MaxInlineSize=1024"

#...copy data from home to work folder if needed....
# copy jar
#srun --ntasks-per-node=1 cp ~/$jarname $workingDir/

# run test directly from home folder
srun --ntasks-per-node=1 --partition=slow --w 1-4 /home/user/hegglin/jdk/jdk1.8.0_25/bin/java $jvmParameters -cp ~/data/$jarname $mainClass --density $1 --algorithm $2 --execution $3 --mode $4 --param '' --timeslots $6 --meetings $7 --agents $8 --runs $9 --factoragents ${10} --factormeetings ${11} --maxagents ${12} --maxmeetings ${13}

#...commands to be run after jobs have finished...