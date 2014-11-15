<?php


$header = "
	<!DOCTYPE html>
	<head>
		<title>Computer Science Department Virtual Tour</title>
		<link href='virtualtour.css' type='text/css' rel='stylesheet'>
		<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'></script>
		<script type='text/javascript' src='componentframework.js'></script>
	</head>
	<body>
		<a href='http://www.wwu.edu'><img src='images.jpg'></a>
		<div class='title'>Computer Science Department Virtual Tour</div>
		<hr>
		</br>
";

function footer($more) {
	$html = "
		</br></br>
		<hr>
		<h3>" .
			$more .
			"<a href='index.php'>Logout</a>
		</h3>
		</body>
		</html>
	";

	return $html;
}

$link = mysqli_connect("mysql.cs.wwu.edu", "vut3", "6dSHnxgTZx", "vut3")
    or die("Error " . mysqli_error($link));
	
$writedb = mysqli_connect("mysql.cs.wwu.edu", "vut3_writer", "YD2CzfNi", "vut3")
    or die("Error " . mysqli_error($link));
?>
