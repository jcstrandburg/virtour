<?php

/*Make sure user is logged in*/
session_start();
include '../phpfunction.php';
if( !in_array($_SESSION['user'], array(1,2))) {
	header("Location: ../index.php");
}


$filename = $_POST['filename'];
unlink($filename);
header("Location: manage.php");

?>