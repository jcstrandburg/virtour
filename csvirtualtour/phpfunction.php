<?php

$salt = "whyareweheredonotaskthatquestionagain";
$qrCodeSite = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=CSVTour://";

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

function cleanString($link, $input) {
   //remove html tags
   $input = strip_tags($input);
   
   //Escape special characters
   $input = mysqli_real_escape_string($link, $input);
   
   return $input;
}

function checkURL($url) {
	$response = @get_headers($url);
	if($response == FALSE) {
		header("Location:newqr.php");
	}
}

$link = mysqli_connect("mysql.cs.wwu.edu", "vut3", "", "vut3")
    or die("Error " . mysqli_error($link));
	
$writedb = mysqli_connect("mysql.cs.wwu.edu", "vut3_writer", "", "vut3")
    or die("Error " . mysqli_error($link));
?>
