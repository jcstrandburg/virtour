<?php

/*Make sure user is logged in*/
session_start();
include '../phpfunction.php';
if( !in_array($_SESSION['user'], array(1,2))) {
	header("Location: ../index.php");
}

$id = $_POST['id'];
$filename = $_POST['filename'];

$filedir = dirname($filename);
if ($filedir !== ".") {
    die("That file is not in this directory, this operation is not allowed for security reasons");
}

$query = "DELETE FROM `maps` WHERE `id`=$id";
if (mysqli_query($writedb, $query)) {
    unlink($filename);
}
else {
    die("Operation failed: ".mysqli_error($writedb));
}

header("Location: manage.php");
?>
