<?php

/*Make sure user is logged in*/
session_start();
include '../phpfunction.php';
if( $_SESSION['user'] == 0) {
	header("Location: ../index.php");
}


$target_dir = "./";

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
//find the maximum file upload size
$max_upload = get_size_limit('upload_max_filesize');
$max_post = get_size_limit('post_max_size');
$memory_limit = get_size_limit('memory_limit');
$upload_limit = min($max_upload, $max_post, $memory_limit);
$limit_mb = $upload_limit / (1024*1024);

if ( count($_FILES) == 0) {
	die("No files received! You may have exceeded the file upload limit, which appears to be {$limit_mb} MB");
}	

if(isset($_POST["submit"])) {

	$target_file = str_replace(' ','',$target_dir . basename($_FILES["fileToUpload"]["name"]));
    
	if (file_exists($target_file)) {
		die("File already exists on server!");
	}
	
	if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
        header("Location: manage.php");
    } else {
        die("Sorry, there was an error uploading your file. You may have exceeded the file upload limit, which appears to be {$limit_mb} MB");
    }
}
else {
	echo "How did this happen";
}
?>
