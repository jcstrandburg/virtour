<?php
/*
Copyright (c) 2014 
Justin Strandburg
Dylan Huizenga
Tu Vu
Kaylee Breshon

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE 
*/

session_start();
include 'phpfunction.php';

$_SESSION['user'] = 0;
$click = $_POST['click'];
$username = cleanString($link, $_POST['username']);
$password = cleanString($link, $_POST['password']);

if(!empty($click)) {

	//no input
	if(empty($username) || empty($password)) {
		$alert = "Enter both username and password";
	}
	else {
		$result = mysqli_query($link, "select * from user");
		
		//go through all users
		while($row = mysqli_fetch_array($result)) {
			if(strcmp($username, $row['username']) == 0 && strcmp(crypt($password, $salt), $row['password']) == 0) {
				
				//super user
				if(strcmp($username, 'admin') == 0) {
					$_SESSION['user'] = $row['userid'];
					header("Location: admin.php");
				}
				
				//staff
				else {
					$_SESSION['user'] = $row['userid'];
					header("Location: admin.php");
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
