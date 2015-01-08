#!/usr/bin/perl

# Configuration
$runs = 10;
$factor = 10;
$max = 10000;

$timeslots = 100;
$meetings = 1;
$agents = 1;

# Categories
@algorithms = ("maxsum", "mgm", "dpop");
@execution = ("synchronous", "asynchronous");
@mode = ("normal", "dynamicConstraints", "dynamicMeetings");

@params1_1 = (2000,5000,10000); 
@params1_2 = (25,50,75,100);

@params2_1 = (2000,5000,10000);
@params2_2 = (0,0.5,1);
@params2_3 = (0,0.5,1);
@params2_4 = (0,0.5,1);
@params2_5 = (1,2,5,10);

# Build all tests
for my $algorithm (@algorithms){
	for my $execution (@execution){
		#for my $mode (@execution)
		$command = "submission.sh " .
			"ALGORITHM='$algorithm' " .
			"EXECUTION='$execution' " .
			"MODE='normal' " .
			"PARAMS='' " .
			"TIMESLOTS=$timeslots " . 
			"MEETINGS=$meetings " .
			"AGENTS=$agents " .
			"RUNS=$runs " .
			"FACTOR=$factor " . 
			"MAX=$max"; 

		system($command);
	}
}

