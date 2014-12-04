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

function get_map_list() {
    global $link;
    $query = "SELECT * FROM maps ORDER BY `ordering` ASC";
    $result = mysqli_query($link, $query);

    $maps = array();
    if ($result) {
        while ($row = mysqli_fetch_array($result)) {

            $maps[$row["id"]] = $row;
        }
    }

    return $maps;
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

function get_size_limit($fieldname) {
    $val = trim(ini_get($fieldname));
    $postfix = strtolower($val[strlen($val)-1]);
    switch ($postfix) {
	    case 'k':
		    $val = intval($val)*1024;
		    break;
	    case 'm':
		    $val = intval($val)*1024*1024;
		    break;
	    case 'g':
		    $val = intval($val)*1024*1024*1024;
		    break;
    }
    return $val;
}

function find_file_upload_limit() {
    //find the maximum file upload size
    $max_upload = get_size_limit('upload_max_filesize');
    $max_post = get_size_limit('post_max_size');
    $memory_limit = get_size_limit('memory_limit');
    $upload_limit = min($max_upload, $max_post, $memory_limit);
    $limit_mb = $upload_limit / (1024*1024);

    return $limit_mb;
}


require_once("dbconnections.php");

?>
