<?php
session_start();
include 'phpfunction.php';

$change = $_GET['change'];

if($_SESSION['user'] == 1) {
	$content = '<h1>Administrative Functions</h1>';
	
	//change super user password
	if($change == 'yes') {
		$content = $content . '
			<form method="post">
		<table align="center" class="tform">
		<tr><td>New Password:</td><td><input  type="password" name="newpass"></td></tr>
		<tr><td>Confirm New Password:</td><td><input  type="password" name="confpass"></td></tr>
		</table>
		<p><input type="submit" value="Login" style="width:100px;position:relative; margin-left:150px;"></p>
		</form>
		';
	}
	
	//administrative functions
	else {
		$content = $content . '			
			<h2><a href="stops.php">Stop Directory</a></h2>
			<h2><a href="stops.php">Staff Directory</a></h2>
			<h2><a href="admin.php?change=yes">Change Password</a></h2>
		';
	}
}
else {
	header("Location:index.php");
}

echo $header;
echo $content;
	
echo footer('');
?>



