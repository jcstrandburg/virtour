<?php
include '../phpfunction.php';

$query = "SELECT * FROM maps ORDER BY `ordering` ASC";
$result = mysqli_query($link, $query);

$json = array();
if ($result) {
    $out_results = array();
    $serverurl = dirname( dirname( "http://".$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI']));

    while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
        $row['url'] = str_replace("%20", " ", $serverurl."/maps/".$row['url']);
        $out_results[] = $row;
    }

    $json['success'] = true;
    $json['error'] = "";
    $json['result'] = $out_results;
}
else {
    $json['success'] = false;
    $json['error'] = "Query failed: ".mysqli_error($link);
    $json['result'] = array();
}

echo json_encode($json);
?>
