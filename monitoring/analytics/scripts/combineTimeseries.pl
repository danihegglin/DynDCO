#!/usr/bin/perl

# configuration
my $dir = "/Users/daniel/git/dyndco/monitoring/";

# define file
my $file = $dir . "experiments/" . $ARGV[0];

# controlling parameter
my $lastTimestamp = 0;
my $utility = 0;
my $counter = 1;

# result file
open(my $fw, '>>', $dir . 'analytics/results/combined.txt') or die "Could not open file: $!";
$header = "timestamp;utility\n";
print $fw $header;

# experiment file
open my $fh, '<', $file or die "Cannot open $file: $!";
while ( my $line = <$fh> ) {

	# split line
	@split = split(";", $line);

	# check timestamp of line
	$timestamp = $split[0];
	$value = $split[2];

	# create new time point
	if($timestamp > $lastTimestamp){

		# write out old utility
		$entry = $counter . ";" . $utility . "\n";
		print $entry;

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

close($fh);
close($fw);