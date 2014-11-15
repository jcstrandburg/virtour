<?php
session_start();
include 'phpfunction.php';

$_SESSION['user'] = 0;
$click = $_POST['click'];
$username = $_POST['username'];
$password = $_POST['password'];

if(!empty($click)) {

	//no input
	if(empty($username) || empty($password)) {
		$alert = "Enter both username and password";
	}
	else {
		$result = mysqli_query($link, "select * from user");
		
		//go through all users
		while($row = mysqli_fetch_array($result)) {
			if(strcmp($username, $row['username']) == 0 && strcmp($password, $row['password']) == 0) {
				
				//super user
				if(strcmp($username, 'admin') == 0) {
					$_SESSION['user'] = 1;
					header("Location: admin.php");
				}
				
				//staff
				else {
					$_SESSION['user'] = 2;
				}
			}
		}
		
		//no match found
		$alert = "Invalid username or password";
	}
}
?>

<?echo $header?>

	<form method='post'>
		<fieldset style='width:400px;margin-left:auto;margin-right:auto;'>
			<legend>Administrative Login</legend>
			<p style="color: blue;margin-left: auto;margin-right: auto;">Username:<input  type="text" name="username"></p>
			<p style="color: blue;margin-left: auto;margin-right: auto;">Password:<input  type="password" name="password"></p>
			<p style="color: red;text-align:center;font-weight: bold;"><?echo $alert?></p>
			<p><input type="submit" value="Login" style="width:100px;position:relative; margin-left:150px;"></p>
			<input type='hidden' name='click' value='click'>
		</fieldset>
	</form>

</body>
</html>
