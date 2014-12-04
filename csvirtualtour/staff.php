<?php
session_start();
include 'phpfunction.php';

if($_SESSION['user'] != 0) {

	$content = '<h1>Staff Directory</h1>';
	
	$users = mysqli_query($link, "select * from user where username != 'admin'");
	
	if(mysqli_num_rows($users) != 0) {
		$table = '';
		while($row = mysqli_fetch_array($users)) {
			$table = $table . "<tr><td>" . $row['username'] . "</td>
					<td><a href='change.php?user=" . $row['userid'] . "'>Reset</a></td>";
		}
	}
	
	$more = "<a href='newstaff.php'>Add New Staff</a>
			</br>
			<a href='admin.php'>Administrative Functions</a>
			</br>";
}
else {
	header("Location:index.php");
}
?>

<?php echo $header?>
	<h1>Staff Directory</h1>
	<table>
		<th>User Name</th><th>Reset Password</th>
		<?php echo $table?>
	</table>
<?php echo footer($more)?>