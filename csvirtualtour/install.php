<?php
/*Startup stuff*/
$salt = "whyareweheredonotaskthatquestionagain";
error_reporting(0);

if (isset($_POST['stage'])) {
    $install_stage = $_POST['stage'];
}
else {
    $install_stage = 0;
}


?>

<html>
<head>
<title>
CS Virtual Tour Admin Interface Installation
</title>

<style>
.success {
    padding: 1em;
    background-color: rgb(200,255,200);
    font-weight: bold;
}

.warning {
    padding: 1em;
    background-color: rgb(255,255,100);
    font-weight: bold;
}

.error {
    padding: 1em;
    background-color: rgb(255,200,200);
    font-weight: bold;
}

pre {
    border: solid 1px gray;
    background-color: white;
    font-weight: normal;
}
</style>

</head>
<body>
<h2>CS Virtual Tour Admin Interface Installation</h2>


<?php
/*Stage0 stuff*/
if ( $install_stage == 0) {
    ?>
    <h3>Step 1</h3>
    <form method='post' action='install.php'>
        <input type='hidden' name='stage' value='1'/>
        <h4>Database Settings</h4>
        <input type='text' name='dbhost' id='dbhost' value='localhost' />
        <label for='dbhost'>Database Host</label>
        <br>
        <input type='text' name='dbname' id='dbname' value='' />
        <label for='dbname'>Database Name</label>
        <br>
        <input type='text' name='readuser' id='readuser' value='' />
        <label for='readuser'>Database Read User</label>
        <br>
        <input type='text' name='readpass' id='readpass' value='' />
        <label for='readpass'>Database Read Password</label>
        <br>
        <input type='text' name='writeuser' id='writeuser' value='' />
        <label for='writeuser'>Database Write User</label>
        <br>
        <input type='text' name='writepass' id='writepass' value='' />
        <label for='writepass'>Database Write Password</label>
        <br>

        <h4>Admin Account Settings</h4>
        <input type='text' name='adminpass' id='adminpass' value='' />
        <label for='adminpass'>What password would you like to use for the admin account</label>
        <br>

        <input type='submit' value='Go'/>
    </form>
    <?php
}
else if ( $install_stage == 1) {

    echo "<h3>Step 2</h3>";
    $host = $_POST['dbhost'];
    $dbname = $_POST['dbname'];
    $read_user = $_POST['readuser'];
    $read_pass = $_POST['readpass'];
    $write_user = $_POST['writeuser'];
    $write_pass = $_POST['writepass'];
    $admin_pass = $_POST['adminpass'];
    $admin_pass = crypt($admin_pass, $salt);

    /*test database credentials*/
    $link = mysqli_connect($host, $read_user, $read_pass, $dbname);
    if ($link) {
        echo "<div class=success>Read database credentials validated</div>";
    }
    else {
        die("<div class=error>Read database credentials couldn't be validated. Installation aborted! ".mysqli_connect_error($link)."<div>");
    }
    $writedb = mysqli_connect($host, $write_user, $write_pass, $dbname);
    if ($writedb) {
        echo "<div class=success>Write database credentials validated</div>";
    }
    else {
        die("<div class=error>Write database credentials couldn't be validated. Installation aborted! ".mysqli_connect_error($writedb)."<div>");
    }
    


ob_start();
?>
CREATE TABLE IF NOT EXISTS `maps` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(2083) COLLATE utf8_unicode_ci NOT NULL,
  `desc` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `ordering` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=17 ;

CREATE TABLE IF NOT EXISTS `Stops` (
  `StopID` int(11) NOT NULL AUTO_INCREMENT,
  `StopName` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `RoomNumber` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `StopContent` varchar(10240) COLLATE utf8_unicode_ci NOT NULL,
  `StopX` double NOT NULL,
  `StopY` double NOT NULL,
  `StopQRIdentifier` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `StopOrder` int(11) NOT NULL,
  `MapID` int(11) NOT NULL,
  `Active` varchar(3) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'yes',
  PRIMARY KEY (`StopID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=28 ;


INSERT INTO `Stops` (`StopID`, `StopName`, `RoomNumber`, `StopContent`, `StopX`, `StopY`, `StopQRIdentifier`, `StopOrder`, `MapID`, `Active`) VALUES
(25, 'Dummy Stop', 'CF XXX', '[{"type":"text","title":"Title","content":"Dummy Content"}]', 0.25, 0.25, '25', 4, 0, 'yes');

CREATE TABLE IF NOT EXISTS `user` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `UserID` (`userid`,`username`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;


INSERT INTO `user` (`userid`, `username`, `password`) VALUES
(1, 'admin', '<?php echo $admin_pass;?>');

<?php
$install_query = ob_get_clean();

/*Create the dbconnections file*/
ob_start();
echo "<?php";
?>

$link = mysqli_connect("<?php echo $host;?>", "<?php echo $read_user;?>", "<?php echo $read_pass;?>", "<?php echo $dbname;?>")
    or die("Error " . mysqli_connect_error($link));
   
$writedb = mysqli_connect("<?php echo $host;?>", "<?php echo $write_user;?>", "<?php echo $write_pass;?>", "<?php echo $dbname;?>")
    or die("Error " . mysqli_connect_error($writedb));

<?php
echo "?>";
    $dbfile_contents = ob_get_clean();

    if (file_put_contents("dbconnections.php", $dbfile_contents)) {
        echo "<div class=success>dbconnections.php successfully created</div>";
    }
    else {
        echo "<div class=warning>Couldn't write file! Please create a new file in the current directory called dbconnections.php with the following contents:<pre>".nl2br(htmlentities($dbfile_contents))."</pre></div>";
    }

    if (mysqli_multi_query($writedb, $install_query)) {
        echo "<div class=success>Database successfully set up</div>";
    }
    else {
        die("<div class=error>Database setup failed! Installation aborted. ".mysqli_error($writedb)."</div>");
    }

    echo "<h2>Installation appears to be successful. <strong>Please delete this file (install.php) now for security purposes.</strong></h2>";
}
?>


</body>
</html>
