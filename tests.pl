#!/usr/bin/perl

# Configuration
$runs = 10;
$factoragents = 10;
$factormeetings = 0;
$maxagents = 1000;
$maxmeetings = 10;

$timeslots = 1000;
$meetings = 10;
$agents = 5;

# Categories
@algorithms = ("maxsum", "mgm","dpop");
@execution = ("synchronous", "asynchronous");
@mode = ("normal", "dynamicConstraints", "dynamicMeetings");

@params1_1 = (2000,5000,10000); 
@params1_2 = (0.25,0.50,0.75,1);

@params2_1 = (2000,5000,10000);
@params2_2 = (0,0.5,1);
@params2_3 = (0,0.5,1);
@params2_4 = (0,0.5,1);
@params2_5 = (1,2,5,10);

# Build all tests
for my $algorithm (@algorithms){
	# for my $execution (@execution){
		#for my $param (@params1_1){
		$command = "sh submission.sh " .
				"'$algorithm' " .
				"'synchronous' " .
				"'normal' " .
				"'' " .
				"$timeslots " . 
				"$meetings " .
				"$agents " .
				"$runs " .
				"$factoragents " . 
				"$factormeetings " .
				"$maxagents " .  
				"$maxmeetings &"; 

		print($command . "\n");
		system($command);
		#}
	# }
}

