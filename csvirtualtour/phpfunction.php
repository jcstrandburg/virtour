<?php

function make_header($path = './') {
	ob_start();
	?>
	<!DOCTYPE html>
	<head>
		<title>Computer Science Department Virtual Tour</title>
		<link href='<?php echo $path;?>virtualtour.css' type='text/css' rel='stylesheet'>
		<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'></script>
		<script type='text/javascript' src='<?php echo $path;?>componentframework.js'></script>
	</head>
	<body>
		<a href='http://www.wwu.edu'><img src='<?php echo $path;?>images.jpg'></a>
		<div class='title'>Computer Science Department Virtual Tour</div>
		<hr>
		</br>	
	<?php	
	return ob_get_clean();
}

$header = make_header();

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

function get_map_list($path = 'maps/') {
	$extensions = array('jpg', 'png', 'gif');
	
	$matches = array();
	$files = scandir($path);
	
	foreach ($files as $file) {
		$file = $path.$file;
		if (!is_file($file)) {
			continue;
		}
		$extn = pathinfo($file, PATHINFO_EXTENSION);
		if ( in_array($extn, $extensions)) {
			$matches[] = $file;
		}
		else {
		}
	}
	
	return $matches;
}

$link = mysqli_connect("mysql.cs.wwu.edu", "vut3", "6dSHnxgTZx", "vut3")
    or die("Error " . mysqli_error($link));
	
$writedb = mysqli_connect("mysql.cs.wwu.edu", "vut3_writer", "YD2CzfNi", "vut3")
    or die("Error " . mysqli_error($link));
?>
