#!/usr/bin/perl

use POSIX;

# configuration
my $dir = "/Users/daniel/git/dyndco/monitoring/analytics";

# define files
my $filecounter = 0;
my %start = ();
my %finish = ();

foreach my $file (glob("$dir/experiments/*.txt")) {

	#print $file . "\n";

	$filecounter += 1;

	# controlling parameter
	my $lastTimestamp = 0;
	my $utility = 0;
	my $counter = 0;

	# result file
	open(my $fw, '> ', $dir . '/results/combined_'.$filecounter.'.txt') or die "Could not open file: $!";
	$header = "timestamp;utility\n";
	print $fw $header;

	# experiment file
	open my $fh, '<', $file or die "Cannot open $file: $!";
	$linecount = 0;

	my %bucket = ();

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
				$value = $split[2];
				$value =~ s/\s+$//;

				$all = 0;
				if(exists $bucket{$timestamp}){
					$all += $bucket{$timestamp};
				}

				$all += $value;

				$bucket{$timestamp} = $all;
			}
		}
	}

	close($fh);

	$counter = 1;
	for my $timestamp (sort { $a <=> $b } keys %bucket){

		if($timestamp ne "timestamp" && $counter < %bucket){
		
			my $entry = $counter . ";" . $bucket{$timestamp} . "\n";		
			print $fw $entry;
			$counter++;

		}	

	}
	close($fw);
}

# Write mean end point
open(my $fw, '>', $dir . '/results/finish_time.txt') or die "Could not open file: $!";

			print "@finish";

my $finishSum = 0;
my $failCount = 0;
for ($i = 1; $i <= $filecounter; $i++){

	print "start " . $start{$i} . "\n";
	print "finish " . $finish{$i} . "\n";

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
print $fw $finishSum / $filecounter . "\n";

close($fw);