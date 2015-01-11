#!/usr/bin/perl

use POSIX;

sub median {
    my @vals = sort {$a <=> $b} @_;
    my $len = @vals;
    if($len%2) #odd?
    {
        return $vals[int($len/2)];
    }
    else #even
    {
        return ($vals[int($len/2)-1] + $vals[int($len/2)])/2;
    }
}

# percentage of different assignments
sub analyseAgent {
	my $agent_index = $_[0];
	$agent_index = substr($agent_index, 1,-1);
	@elements = split(",",$agent_index);
	$size = scalar @elements;

	%distinct = ();
	for $element (@elements){
		@subelements = split(/ -> /,$element);
		$distinct{$subelements[1]} = 1;
	}
	
	if($size > 0){
		return ((keys %distinct)/$size);
	}
	else{
		return 0;
	}
}

# percentage of same assignments
sub analyseMeeting {
	my $meeting_index = $_[0];
	$meeting_index = substr($meeting_index, 1,-1);
	@elements = split(",",$meeting_index);
	$size = scalar @elements;
	
	%distinct = ();
	for $element (@elements){
		@subelements = split(/ -> /,$element);
		$distinct{$subelements[1]} = 1;
	}

	if($size > 0){
		return (1-(((keys %distinct) - 1)/$size));
	}
	else{
		return 0;
	}
}

# configuration
my $dir = "/Users/daniel/git/dyndco/monitoring/analytics";
my $experiments = $dir . "/experiments";
my $results = $dir. "/results";

# go through all directories
opendir my $dh, $experiments or die "$0: opendir: $!";
my @subdirs = grep {-d "$experiments/$_" && ! /^\.{1,2}$/} readdir($dh);

for my $subdir (@subdirs){

	# create subdir in results
	system("mkdir $results/$subdir");

	# get all files of certain sub run: meeting, agent
	%tests = ();
	@allFiles = glob("$experiments/$subdir/*.txt");
	for my $file (@allFiles){

		# Utility files
		if($file !~ /_stats/ && $file !~ /_conflicts/){
			@elements = split("-", $file);
		 	
		 	$filename = $elements[1];
		 	@numbers = split(/[a-z]/,$filename);
		 	$agents = $numbers[1];
		 	$meetings = $numbers[2];
		 	$id = $agents . "_" . $meetings;

		 	$runFiles = "";
		 	if(exists $tests{$id}){
		 		$runFiles .= $tests{$id};
		 	}
		 	$runFiles .= $file . ";";
		 	$tests{$id} = $runFiles;
	 	}
	}

	for my $runFiles (keys %tests){

		@combinedFiles = ();
		$subsubdir = $results."/".$subdir."/".$runFiles;

		@localRun = split("_",$runFiles);
		$localAgents = $localRun[0];
		$localMeetings = $localRun[1];
		$localMaxUtil = $localAgents * $localMeetings;

		system("mkdir $subsubdir");

		@files = split(";",$tests{$runFiles});

		# define files
		my $filecounter = 0;
		my %start = ();
		my %finish = ();
		
		#################################################
		# build combined files
		#################################################
		foreach my $file (@files) {

		 	@elements = split("/", $file);
		 	$filename = $elements[-1];

			$filecounter += 1;

			# controlling parameter
			my $lastTimestamp = 0;
			my $utility = 0;
			my $counter = 0;

			# result file
			$combined_file = $subsubdir . '/combined_' . $filename;
			push(@combinedFiles, $combined_file);
			system("touch $combined_file");
			open(my $fw, '>', $combined_file) or die "Could not open file $combined_file: $!";
			$header = "timestamp;utility;agent_index;meeting_index \n";
			print $fw $header;

			# experiment file
			open my $fh, '<', $file or die "Cannot open $file: $!";
			$linecount = 0;

			my %bucket = ();
			my %agent = ();
			my %meeting = ();

			my $normalizedTimestamp = 0;
			my $lastTimestamp = 0;

			while ( my $line = <$fh> ) {

				$linecount++;

				# split line
				@split = split(";", $line);

				if($split[1] =~ /start/){
					$start{$filecounter} = 0; # is always 0 when normalized
				}
				elsif($split[1] =~ /finished/){
					$finish{$filecounter} = $lastTimestamp; # real stop time is the last utility update from the agents
				}
				else {

					# check timestamp of line
					my $timestamp = floor($split[0]/100);

					if($normalizedTimestamp == 0){
						print $timestamp
						$normalizedTimestamp = $timestamp
					}

					$timestamp = ($timestamp - $normalizedTimestamp) + 2; # makes timestamps start at 0
					$lastTimestamp = $timestamp;

					if($timestamp ne "0"){

						# get values
						$utility = $split[2];
						$utility =~ s/\s+$//;
						$agent_index = $split[3];
						$agent_index =~ s/\s+$//;
						$meeting_index = $split[4];
						$meeting_index =~ s/\s+$//;
						$agent_q = analyseAgent($agent_index);
						$meeting_q = analyseMeeting($meeting_index);

						# normalize values
						#$utility = ($utility - 0) / ($localMaxUtil;

						$utilities = 0;
						if(exists $bucket{$timestamp}){
							$utilities += $bucket{$timestamp};
						}
						$agent = 0;
						if(exists $agent{$timestamp}){
							$agent += $agent{$timestamp};
						}
						$meeting = 0;
						if(exists $meeting{$timestamp}){
							$meeting += $meeting{$timestamp};
						}

						$utilities += $utility;
						$agent += $agent_q;
						$meeting += $meeting_q;

						$bucket{$timestamp} = $utilities;
						$agent{$timestamp} = $agent;
						$meeting{$timestamp} = $meeting;
					}
				}
			}

			close($fh);

			$counter = 1;
			for my $timestamp (sort { $a <=> $b } keys %bucket){

				if($timestamp ne "timestamp" && $counter < %bucket){
				
					my $entry = 
						$counter . ";" . 
						$bucket{$timestamp} . ";" . 
						$agent{$timestamp} . ";" . 
						$meeting{$timestamp} . ";" . 
						"\n";		
					print $fw $entry;
					$counter++;

				}	

			}
			close($fw);
		}

		#################################################
		# Write time file
		#################################################
		open(my $fw, '>', $subsubdir . '/finish_time.txt') or die "Could not open file: $!";

		my $finishSum = 0;
		my $failCount = 0;
		for ($i = 1; $i <= $filecounter; $i++){

			if(!exists $finish{$i} || !exists $start{$i}){
				$failCount++;
				$filecounter--;
			}
			else {
				$duration = $finish{$i} - $start{$i};
				$finishSum += $duration;
				print $fw $duration . "\n";
			}
		}
		print $fw "---\n";
		if($filecounter > 0){
			print $fw $finishSum / $filecounter . "\n";
		}

		close($fw);


		#################################################
		# Build final mean file
		#################################################

		# timeseries matrix
		my %matrix = ();

		# read all files from directory
		my $max = 100;
		foreach my $fp (@combinedFiles) {

		  	open my $fh, "<", $fp or die "can't read open '$fp': $OS_ERROR";
		  	$count = 0;

		  	my $timepoint;
		  	my $utility;
		  	my $agent_index;
		  	my $meeting_index;
		 
		  	while ( my $line = <$fh>) {

		  		$count++;

			    # split line
				@split = split(";", $line);

				# check timestamp of line
				$timepoint = $split[0];
				$utility = $split[1];
				$agent_index = $split[2];
				$meeting_index = $split[3];

				# clear whitespace
				$timepoint =~ s/\s+$//;
				$utility =~ s/\s+$//;
				$agent_index =~ s/\s+$//;
				$meeting_index =~ s/\s+$//;

				# add to matrix
				my $timepoints = "";
				if(exists $matrix{$timepoint}){
					$timepoints = $matrix{$timepoint};
				}
				$timepoints .= $utility . "," . $agent_index . "," . $meeting_index . ";"; 
				$matrix{$timepoint} = $timepoints;

		  	}
		  	close $fh;

		 #  if($timepoint !~ /timestamp/){
			#   print $timepoint . "|" . $utility . "| \n";
			#   while ( $count < $max){
			#    	$timepoint++;
			#    	$count++;

			#  	if(exists $matrix{$timepoint}){
			# 		$timepoints = $matrix{$timepoint};
	 	# 		}
			# 	$timepoints .= $utility . "," . $agent_index . "," . $meeting_index . ";"; 
			# 	$matrix{$timepoint} = $timepoints;
			#   }
			# }
		}

		# create mean file
		open(my $fw, '>', $subsubdir . '/mean.txt') or die "Could not open file: $!";
		$header = "timepoint;u_mean;u_median;u_sum;a_mean;a_median;a_sum;m_mean;m_median;m_sum\n";
		print $fw $header;

		# foreach file add up timepoints: mean, median
		my $max_util = 0;
		for ($i=1; $i<(keys %matrix); $i++){

			my $values = $matrix{$i};

			my @values = split(";", $values);
			my $numOfValues = scalar @values;

			# MEAN
			my $u_sum = 0;
			my $a_sum = 0;
			my $m_sum = 0;
			for my $value (@values){

				@sums = split(",",$value);

				$u_sum += $sums[0];
				$a_sum += $sums[1];
				$m_sum += $sums[2];
			}

			my $u_mean = 0;
			my $a_mean = 0;
			my $m_mean = 0;
			if($numOfValues > 0){
				$u_mean = $u_sum / $numOfValues;
				$a_mean = $a_sum / $numOfValues;
				$m_mean = $m_sum / $numOfValues;
			}

			# MEDIAN
			my @util_list = ();
			my @agent_list = ();
			my @meeting_list = ();
			for my $value (@values){
				@elements = split(",",$value);
				push(@util_list, $elements[0]);
				push(@agent_list, $elements[1]);
				push(@meeting_list, $elements[2]);
			}
			my $u_median = median(@util_list);
			my $a_median = median(@agent_list);
			my $m_median = median(@meeting_list);

			# WRITE TO FILE
			$entry = $i . ";" . 
				$u_mean . ";" . $u_median . ";" . $u_sum . ";" . 
				$a_mean . ";" . $a_median . ";" . $a_sum. ";" . 
				$m_mean . ";" . $m_median . ";" . $m_sum . "\n";
			print $fw $entry;

			# max util evaluation
			if($u_median > $max_util){
				$max_util = $u_median;
			}

		}

		close($fw);

		#################################################
		# Write max util file
		#################################################
		open(my $fw, '>', $subsubdir . '/max_util.txt') or die "Could not open file: $!";
		print $fw $max_util;
		close($fw);
	}

	sub trim {
		$string = $_[0];
		$string =~ s/\s+$//;
		return $string;
	}

	sub readTime {
		
		$subsubdir = $_[0];
		$line_time = "";

		$filename = $subsubdir . 'finish_time.txt';
		if(-e $filename){

			open(my $fr, '<', $filename) or die "Could not open file: $!";
			$endReached = "false";
			while ( my $line = <$fr>) {
				if($endReached eq "true"){
					$line_time .= trim($line) . ";";
				}
				if($line =~ /\-/){
					$endReached = "true";
				}
			}
			close($fr);
		}
		else {
			$line_time .= "-1;";
		}
		return $line_time;
	}

	#################################################
	# Build matrix files: convergence time, quality, max util, messages (10,20,30,)
	################################################# 
	open(my $fw, '>', $results . "/" . $subdir . '/matrix_time.txt') or die "Could not open file: $!";
	
	$max_agents = 100;
	$max_meetings = 100;
	$steps = 5;

	for($y = 0; $y <= $max_meetings; $y += $steps){
		my $line_time = "";
		for($x = 0; $x <= $max_agents; $x += $steps){

			if($y == 0){
				if($x == 0) {
					$line_time .= ";";
				}
				else {
					$line_time .= $x . ";";
				}
			}
			else {
				if($x == 0){
					$line_time .= $y . ";";
				}
				else {
					# the sub sub directory
					$subsubdir = $results."/".$subdir."/".$x . "_" . $y . "/";
					
					# time
					$line_time .= readTime($subsubdir);

					# quality
					# max util
					# messages
				}
			}
		}
		print $fw $line_time . "\n";
	}

	close($fw);

	#################################################
	# Build scalability files: meetings, agents -> utilities, time, messages (10,20,30,...)
	#################################################
	$static_agents = 10;
	$static_meetings = 10;
	$max_agents = 100;
	$max_meetings = 100;
	$steps = 10;

	# agent scalability$
	open(my $fw, '>', $results . "/" . $subdir . '/scalability_agent_time.txt') or die "Could not open file: $!";
	print $fw "agents;time\n";
	for($y = 10; $y <= $max_agents; $y += $steps){

		$line_time = $y.";";

		# the sub sub directory
		$subsubdir = $results."/".$subdir."/" . $y . "_" . $static_meetings . "/";

		# time
		$line_time .= readTime($subsubdir);
		# quality
		# max util
		# max messages
					
		print $fw $line_time . "\n";
		
	}
	close($fw);

	# meeting scalability
	open(my $fw, '>', $results . "/" . $subdir . '/scalability_meeting_time.txt') or die "Could not open file: $!";
	print $fw "meetings;time\n";
	for($y = 10; $y <= $max_meetings; $y += $steps){

		$line_time = "";

		# the sub sub directory
		$subsubdir = $results."/".$subdir."/" . $static_agents . "_" . $y . "/";

		# time
		$line_time .= readTime($subsubdir);
		# quality
		# max util
		# max messages
					
		print $fw $line_time . "\n";
		
	}
	close($fw);


	#################################################
	# Build distribution files: time, quality, max util, messages (10,100,1000)
	#################################################


	#################################################
	# Copy example runs: 10, 100, 1000
	#################################################



}