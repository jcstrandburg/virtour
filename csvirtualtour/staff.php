<?php
session_start();
include 'phpfunction.php';

if($_SESSION['user'] != 0) {

	$user = cleanString($link, $_GET['user']);
	
	if(!empty($user)) {
		mysqli_query($link, "delete from user where userid = '$user'");
	}

	$content = '<h1>Staff Directory</h1>';
	
	$users = mysqli_query($link, "select * from user where username != 'admin'");
	
	if(mysqli_num_rows($users) != 0) {
		$table = '';
		while($row = mysqli_fetch_array($users)) {
			$table = $table . "<tr><td>" . $row['username'] . "</td>
					<td><a href='change.php?user=" . $row['userid'] . "'>Reset</a></td>
					<td><a href='staff.php?user=" . $row['userid'] . "'>Remove</a></td>";
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
		<th>User Name</th><th>Reset Password</th><th>Remove Staff</th>
		<?php echo $table?>
	</table>
<?php echo footer($more)?>