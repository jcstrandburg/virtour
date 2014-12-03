<?php

/*Make sure user is logged in*/
session_start();
include '../phpfunction.php';
if( $_SESSION['user'] == 0) {
	header("Location: ../index.php");
}


$filename = $_POST['filename'];
unlink($filename);
header("Location: manage.php");

?>
