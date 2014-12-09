<?php
session_start();
include 'phpfunction.php';

if($_SESSION['user'] != 0) {

	$user = $_GET['user'];
	$click = $_POST['click'];
	$new1 = cleanString($link, $_POST['new1']);
	$new2 = cleanString($link, $_POST['new2']);
	
	$content = '<h1>Change Password For ';
		
	if($user == "super") {
		$content = $content . 'Super User</h1>';
	}
	else {
        $stmt = $link->prepare("select userid, username, password from user where userid =?");
        $stmt->bind_param( "i", $user);
        $stmt->execute();
        $row = array();
        $stmt->bind_result($row['userid'], $row['username'], $row['password']);
        $stmt->fetch();
        $stmt->close();

		$content = $content . $row['username'] . '</h1>';
	}
	
	if(!empty($click)) {
	
		if(empty($new1) || empty($new2)) {
			$alert = "Enter both values before click Change Password";
		}
		else if(strcmp($new1, $new2) == 0) {

            $stmt = $writedb->prepare( "update user set password=? where userid=?");

			if($user == "super") {
                $stmt->bind_param("si", crypt($new1, $salt), $_SESSION['user']);
			}
			else {
                $stmt->bind_param("si", crypt($new1, $salt), $user);
			}

            $result = $stmt->execute();
            $stmt->close();
			header("Location:admin.php");
		}
		else {
				$alert = "Passwords Entered Not Match";
		}
	}

	$content = $content . "
			<form method='post'>
				<fieldset style='width:300px;margin-left:auto;margin-right:auto;'>
					<legend>Enter New Password</legend>
					<p style='color: blue;margin-left: auto;margin-right: auto;'>New Password:<input  type='password' name='new1'></p>
					<p style='color: blue;margin-left: auto;margin-right: auto;'>Confirm New Password:<input  type='password' name='new2'></p>
					<p style='color: red;text-align:center;font-weight: bold;'>".$alert."</p>
					<p><input type='submit' value='Change Password' style='width:120px;position:relative; margin-left:100px;'></p>
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
