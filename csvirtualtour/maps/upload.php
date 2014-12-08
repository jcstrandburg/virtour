<?php

/*Make sure user is logged in*/
session_start();
include '../phpfunction.php';
if( $_SESSION['user'] == 0) {
	header("Location: ../index.php");
}


$target_dir = "./";

$limit_mb = find_file_upload_limit();

if ( count($_FILES) == 0) {
	die("No files received! You may have exceeded the file upload limit, which appears to be {$limit_mb} MB");
}	

if(isset($_POST["submit"])) {

    $basename = str_replace(' ','',basename($_FILES["fileToUpload"]["name"]));
	$target_file = $target_dir . $basename;
    	
	if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
        $mapdesc = $_POST["description"];
        $mapordering = $_POST['ordering'];
        $query = "INSERT INTO `maps`(`url`, `desc`, `ordering`) VALUES (?, ?, ?)";
        $stmt = $writedb->prepare($query);
        $stmt->bind_param("ssi", $basename, $mapdesc, $mapordering);
        if (!$stmt->execute()) {
            unlink( $target_file);
            die( "Operation failed: ".$stmt->error);
        }

        header("Location: manage.php");
    } else {
        die("Sorry, there was an error uploading your file. You either need to adjust your permissions or you may have exceeded the file upload limit, which appears to be {$limit_mb} MB");
    }
}
else {
	echo "How did this happen";
}
?>
