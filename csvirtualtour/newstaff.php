<?php
session_start();
include 'phpfunction.php';

if($_SESSION['user'] != 0) {

	$click = $_POST['click'];
	$username = cleanString($link, $_POST['username']);
	$password = cleanString($link, $_POST['password']);
	
	if(!empty($click)) {
		if(empty($username) || empty($password)) {
			$alert = "Enter both values";
		}
		else {
			$password = crypt($password, $salt);
			$stmt = $writedb->prepare("insert into user (username, password) values (?, ?)");
            $stmt->bind_param("ss", $username, $password);
            $success = $stmt->execute();
			header("Location:staff.php");
		}
	}
	
	$content = "<h1>Add New Staff</h1>
			<form method='post'>
				<fieldset style='width:300px;margin-left:auto;margin-right:auto;'>
					<legend>New Staff Credential</legend>
					<p style='color: blue;margin-left: auto;margin-right: auto;'>Username:<input  type='text' name='username'></p>
					<p style='color: blue;margin-left: auto;margin-right: auto;'>Password:<input  type='text' name='password'></p>
					<p style='color: red;text-align:center;font-weight: bold;'>".$alert."</p>
					<p><input type='submit' value='Add New Staff' style='width:120px;position:relative; margin-left:100px;'></p>
					<input type='hidden' name='click' value='click'>
				</fieldset>
			</form>
		";
}
else {
	header("Location:index.php");
}

echo $header;
echo $content;
$more = "<a href='admin.php'>Administrative Functions</a>
			</br>";
echo footer($more);
?>
