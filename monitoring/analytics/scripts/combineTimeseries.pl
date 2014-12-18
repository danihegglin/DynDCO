#!/usr/bin/perl

# configuration
my $dir = "/Users/daniel/git/dyndco/monitoring/analytics";

# define files
my $filecounter = 0;
my $finishSum = 0;
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
	while ( my $line = <$fh> ) {

		$linecount++;

		# split line
		@split = split(";", $line);

		if($split[1] =~ /finished/){
			#print "finished on: " . $linecount . "\n";
			$finishSum += $linecount;
		}
		else {

			# check timestamp of line
			$timestamp = $split[0];
			$value = $split[2];

			# create new time point
			if($timestamp > $lastTimestamp){

				# write out old utility
				$entry = $counter . ";" . $utility . "\n";
				#print $entry;

				if($lastTimestamp > 0){
					print $fw $entry;
				}

				# renew utility
				$utility = 0;

				# update lastTimestamp
				$lastTimestamp = $timestamp;

				# update counter
				$counter++;
			}

			# push value to utility
			$utility += $value

		}
	}

	close($fh);
	close($fw);
}

# Write mean end point
print $finishSum / $filecounter . "\n";