<?php
session_start();
include 'phpfunction.php';

if($_SESSION['user'] != 0) {

	$row = mysqli_fetch_array(mysqli_query($link, "select * from user where userid = " . $_SESSION['user']));
	
	$content = '<h1>Administrative Functions</h1>		
				<h2><a href="stops.php">Stop Directory</a></h2>';
				
	if($row['username'] == "admin") {
		$content = $content . '<h2><a href="staff.php">Staff Directory</a></h2>
							   <h2><a href="change.php?user=super">Change Super User Password</a></h2>';
	}
		$content = $content . '
			<h2><a href="media/manage.php">Manage Media Files</a></h2>			
			<h2><a href="maps/manage.php">Manage Maps</a></h2>			
		';
}
else {
	header("Location:index.php");
}

echo $header;
echo $content;
	
echo footer('');
?>



