<?php
session_start();
include 'phpfunction.php';

$did = $_GET['did'];	//delete id

if($_SESSION['user'] == 1 || $_SESSION['user'] == 2) {
	
	//remove selected stop
	if (!empty($did)) {
		echo "Deleting";
		mysqli_query($link, "delete from Stops where StopID = '$did)'");
	}
	
	//select all the remaining stops
	$stops = mysqli_query($link, "select * from Stops");
	
	//get all stops
	if(mysqli_num_rows($stops) != 0) {
		$table = '';
		while($row = mysqli_fetch_array($stops)) {
			$table = $table . "<tr><td>{$row['StopID']}</td><td>" . $row['StopName'] . "</td>
			<td>" . $row['StopOrder'] . "</td>
			<td><a href='https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=CSVTour://" . $row['StopID'] . "' target='_blank'>Click for QR Code</a></td>
			<td><a href='stopinfo.php?eid=" . $row['StopID'] . "'>Edit</a>/<a href='stops.php?did=" . $row['StopID'] . "'>Delete</a></td></tr>";
		}
	}
	
	if($_SESSION['user'] == 1) {
		$more = "<a href='newstop.php'>Add New Stop</a>
			</br>";
	}
}
else {
	header("Location:index.php");
}

?>

<?php echo $header?>
	<h1>List Of Stops</h1>
	<table>
		<th>Stop ID</th><th>Stop Name</th><th>Stop Order</th><th>QR Code</th><th>Modify Stop</th>
		<?php echo $table?>
	</table>
<?php echo footer($more)?>



