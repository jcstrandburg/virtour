<?php
session_start();
include 'phpfunction.php';

$did = $_GET['did'];	//delete id
$aid = $_GET['aid'];	//active id
$active = $_GET['active'];

if($_SESSION['user'] != 0) {
	
	//remove selected stop
	if (!empty($did)) {
        $stmt = $writedb->prepare("delete from Stops where StopID = ?");
        $stmt->bind_param("i", $did);
        $success = $stmt->execute();
        $stmt->close();
	}
	
	if(!empty($aid)) {
        $stmt = $writedb->prepare("update Stops set Active = ? where StopID = ?");
        $stmt->bind_param("si", $active, $aid);
        $success = $stmt->execute();
        $stmt->close();
	}
	
	//select all the remaining stops
	$stops = mysqli_query($link, "select * from Stops");
	
	//get all stops
	if(mysqli_num_rows($stops) != 0) {
		$table = '';
		while($row = mysqli_fetch_array($stops)) {
			if($row['Active'] == "yes") {
				$table = $table . "<tr><td>" . $row['StopName'] . "</td>
				<td>" . $row['StopOrder'] . "</td>
				<td><a href='https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=CSVTour://" . $row['StopID'] . "' target='_blank'>Click for QR Code</a></td>
				<td><a href='stopinfo.php?eid=" . $row['StopID'] . "'>Edit</a>/<a href='stops.php?did=" . $row['StopID'] . "' onClick='return confirm(\"Are you sure?\");'>Delete</a>/
				<a href='stops.php?aid=" . $row['StopID'] . "&active=no'>Deactivate</a></td></tr>";
			}
			else {
				$table = $table . "<tr><td>" . $row['StopName'] . "</td>
				<td>" . $row['StopOrder'] . "</td>
				<td>None</td>
				<td><a href='stops.php?aid=" . $row['StopID'] . "&active=yes'>Activate</a></td></tr>";
			}
		}
	}
	
	if($_SESSION['user'] == 1) {
		$more = "<a href='newstop.php'>Add New Stop</a>
			</br>
			<a href='admin.php'>Administrative Functions</a>
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
		<th>Stop Name</th><th>Stop Order</th><th>QR Code</th><th>Modify Stop</th>
		<?php echo $table?>
	</table>
<?php echo footer($more)?>



