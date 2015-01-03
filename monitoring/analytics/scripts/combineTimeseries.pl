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
	open(my $fw, '>>', $dir . '/results/combined_'.$filecounter.'.txt') or die "Could not open file: $!";
	$header = "timestamp;utility\n";
	print $fw $header;

	# experiment file
	open my $fh, '<', $file or die "Cannot open $file: $!";
	$linecount = 0;

	my %bucket = ();

	while ( my $line = <$fh> ) {

		$linecount++;

		# split line
		@split = split(";", $line);

		# check timestamp of line
		my $timestamp = floor($split[0] / 100);

		if($split[1] =~ /start/){
			$start{$filecounter} = $timestamp;
		}
		elsif($split[1] =~ /finished/){
			$finish{$filecounter} = $timestamp;
		}
		else {

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

	for my $timestamp (sort keys %bucket){

		if($timestamp ne "timestamp"){
		
			my $entry = $timestamp . ";" . $bucket{$timestamp} . "\n";		
			print $fw $entry;
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