<?php

/*Make sure user is logged in*/
session_start();
include '../phpfunction.php';
if( $_SESSION['user'] == 0) {
	header("Location: ../index.php");
}

$id = $_POST['id'];
$filename = $_POST['filename'];

$filedir = dirname($filename);
if ($filedir !== ".") {
    die("That file is not in this directory, this operation is not allowed for security reasons");
}

$stmt = $writedb->prepare("DELETE FROM `maps` WHERE `id`=?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    unlink($filename);
}
else {
    die("Operation failed: ".$stmt->error);
}

header("Location: manage.php");
?>
