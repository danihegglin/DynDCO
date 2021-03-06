#!/usr/bin/perl

# Run Configuration
$runs = 10; # How many runs per setting
$factoragents = 10; # 0 for scalability test of meetings
$factormeetings = 0; # 0 for scalability test of agents
$maxagents = 30; # max number of agents in the setting
$maxmeetings = 10; # max number of meetings in the setting

# Problem Configuration
$timeslots = 100;
$meetings = 10;	
$agents = 30;

# while($meetings < $maxmeetings){
# 	while($agents < $maxagents){

		# Test Categories
		@density = (0.25); # 0.5, 0.75, 1
		@algorithms = ("maxsum","dpop","mgm"); # ,"mgm","dpop"
		@execution = ("synchronous", "asynchronous"); #, "asynchronous"
		@mode = ("dynamicVariables"); # "dynamicConstraints","dynamicConstraints",dynamicVariables","dynamicDomain"

		# Params for dynamicConstraints, dynamicDomain, dynamicVariables
		@changeMode = ("multiple"); #"multiple"
		@interval = (500,1000,1500,2000); # 1000,5000 
		@percentage = (0.25,0.5,0.75,1); # Percentage 0.50,0.75,1
		@newMeeting = (0); # Next Meeting Probability (otherwise existing meeting)
		@newAgent = (1); # Next Agent Probability (otherwise existing agent)
		@action = (1,0); # Add/Remove Probability
		@number = (1); # Number of changed variables

		# Build all tests
		@commands = ();
		for my $density (@density){
			for my $algorithm (@algorithms){
				for my $execution (@execution){
					for my $mode (@mode) {

						$command_pre = "sh submission.sh " .
							"'$density' " . # Density
							"'$algorithm' " . # Algorithm
							"'$execution' " . # Execution
							"'$mode' "; # Mode

						$command_post = "$timeslots " . # Timeslots
							"$meetings " . # Meetings
							"$agents " . # Agents
							"$runs " . # Runs per setting
							"$factoragents " . # Increase of agents
							"$factormeetings " . # Increase of meetings
							"$maxagents " . # Max Agents
							"$maxmeetings &"; # Max Meetings

						if($mode eq "normal"){
							# Params null
							my $command = $command_pre . "'' " . $command_post;
							push(@commands, $command);
						}
						elsif($mode eq "dynamicConstraints" || $mode eq "dynamicDomain"){
							for my $interval (@interval){
								for my $percentage (@percentage){
									for my $changeMode (@changeMode){
										# Params: interval, percentage
										my $command = $command_pre . "'$interval,$percentage,$changeMode' " . $command_post;
										push(@commands, $command);
									}
								}
							}
						}
						elsif($mode eq "dynamicVariables"){
							for my $interval (@interval){
								for my $newMeeting (@newMeeting){
									for my $newAgent (@newAgent){
										for my $action (@action){
											for my $number (@number){
												# Params: interval, newMeeting, newAgent, Action, Number
												my $command = $command_pre . "'$interval,$newMeeting,$newAgent,$action,$number' " . $command_post;
												push(@commands, $command);
											}
										}
									}
								}
							}
						}

					}
				}
			}
		}

		# Run tests
		for my $command (@commands){
			print($command . ";");
			#system($command);
		}

	# 	$agents += 50;
		
	# 	if($agents < $maxagents){
	# 		sleep 3800; #3600
	# 	}
	# }
	
	# $meetings += 5;
	# $agents = 5;

	# if($meetings < $maxmeetings){
	# 	sleep 3800; #3600
	# }
# }

exit(0)

