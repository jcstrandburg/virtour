<?php
if (isset($_POST['filters'])) {
	$filters = explode(',', $_POST['filters']);
}
else {
	$filters = NULL;
}


$files = scandir('./');
foreach ($files as $file) {
	if ( !is_file($file)) {
		continue;
	}

	$extn = pathinfo($file, PATHINFO_EXTENSION);
	if ( $filters === NULL || in_array($extn, $filters)) {
		$matches[] = $file;
	}
}

$url = $_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'];
$basepath = dirname($url);
foreach ($matches as $match) {
	$formatted_matches[$match] = "http://{$basepath}/{$match}";
}

echo json_encode($formatted_matches);
?>
