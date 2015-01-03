#!/usr/bin/perl

sub median
{
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

# configuration
my $dir = "/Users/daniel/git/dyndco/monitoring/analytics/results";

# timeseries matrix
my %matrix = ();

# read all files from directory
foreach my $fp (glob("$dir/*.txt")) {
	
  	open my $fh, "<", $fp or die "can't read open '$fp': $OS_ERROR";
  	$count = 0;
  	
  	while ( my $line = <$fh>) {

	    # split line
		@split = split(";", $line);

		# check timestamp of line
		$timepoint = $split[0];
		$value = $split[1];

		# clear whitespace
		$timepoint =~ s/\s+$//;
		$value =~ s/\s+$//;

		# add to matrix
		my $timepoints = "";
		if(exists $matrix{$timepoint}){
			$timepoints = $matrix{$timepoint};
			$timepoints .= ";".$value
		}
		else {
			$timepoints = $value;
		}
		#print "length timepoints: " . scalar @timepoints;
		$matrix{$timepoint} = $timepoints;

  	}
  	close $fh;
}

# open results file
open(my $fw, '>>', $dir . '/calculated_'.time().'.txt') or die "Could not open file: $!";
$header = "timepoint;mean;median;utility\n";
print $fw $header;

# foreach file add up timepoints: mean, median
for ($i=1; $i<=@matrix; $i++){

	my $values = $matrix[$i];

	my @values = split(";", $values);
	my $numOfValues = scalar @values;

	# MEAN
	my $sum = 0;
	for my $value (@values){
		$sum += $value;
	}
	print "sum: " . $sum . "\n";

	my $mean = 0;
	if($numOfValues > 0){
		$mean = $sum / $numOfValues;
	}
	print "mean: " . $mean . "\n";

	# MEDIAN
	$median = median(@values);
	print "median: " . $median . "\n";

	# WRITE TO FILE
	$entry = $i . ";" . $mean . ";" . $median . ";" . $sum . "\n";
	print $fw $entry;

}

close($fw);