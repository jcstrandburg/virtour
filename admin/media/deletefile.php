<?php

$filename = $_POST['filename'];
unlink($filename);
header("Location: manage.php");

?>