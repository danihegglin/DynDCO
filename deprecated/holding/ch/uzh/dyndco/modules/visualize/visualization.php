<?php
include 'class.MySQL.php';

// Parameters
//$from = $_GET['from'];
//$to = $_GET['to'];

// DB Query
$mysql = new MySQL('load','root','','127.0.0.1');
$query = "Select * from load4 ORDER BY id";
$results = $mysql->ExecuteSQL($query);

// Process Results
$index = array();
foreach($results as $result){
	
	// Get Value
	$value = $result['load'];
	
	// Bag this
	$bag['load'] = $value; // Real value
	
	// Update Containers
	array_push($index,$bag);
	$bag = array();
	
}

// Format Results for Google Charts
$entries = array();
$index_count = 1;
foreach($index as $entry){
	
	// Prefix
	$insert = "['".$index_count."'";
	
	// Content
	foreach($entry as $item){	
		if($item != null) {
			$insert .= ", " . $item;
		}
	}
	
	// Postfix
	$insert .= "],";
	
	// Update Containers & Counters
	array_push($entries,$insert);
	$index_count++;
}
?>

<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          <?php echo "[
          
          'Measure', 
          'Real'
          
          ]" ?>,
          <?php 
          	foreach($entries as $entry){
	        echo $entry;
          }
          ?>
        ]);

        var options = {
          title: 'Load Monitoring'
        };

        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
    </script>
    
    <script type="text/JavaScript">
		function timedRefresh(timeoutPeriod) {
			setTimeout("location.reload(true);",timeoutPeriod);
		}
	</script>
	 
  </head>
  <body onload="JavaScript:timedRefresh(10000);">
    <div id="chart_div" style="width: 1400px; height: 800px;"></div>
  </body>
</html>