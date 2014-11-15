<?php
include 'phpfunction.php';
//if (!defined('PDO::ATTR_DRIVER_NAME')) {
//echo 'PDO unavailable';
//}

function fake_fetch_all($result) {
for ($res = array(); $tmp = mysqli_fetch_array($result);) {
$res[] = $tmp;
}
return $res;
}

abstract class DataAPI {
	private $stopid;
	protected $result;
	public function __construct( $stopid) {
		$this->stopid = $stopid;
	}
	
	public function Execute() {
		global $dbname, $dbuser, $dbpass, $link;
	
		try {
			//$db = new PDO($dbname, $dbuser, $dbpass);
			$query = "Select * FROM Stops WHERE StopID='".$this->stopid."'";
			
			$stmt = mysqli_query($link, $query);
			//$stmt = $db->query( $query);
			
			//if ( $stmt->rowCount() == 1 ) {
			if(mysqli_num_rows($stmt) == 1) {
				//$row = $stmt->fetch( PDO::FETCH_ASSOC);
				$row = mysqli_fetch_assoc($stmt);
				$this->result = $row;
			}
			
			else if ( $this->stopid == 0 ) {
				$query = "Select StopID, StopName, StopOrder, StopX, StopY, StopQRIdentifier FROM Stops";
				//$stmt = $db->query( $query);
				$stmt = mysqli_query($link, $query);
				//$rows = $stmt->fetchAll( PDO::FETCH_ASSOC);
				$rows = fake_fetch_all($stmt);
				$this->result["StopList"] = $rows;
			}	
			
			else {
				$this->EchoErrorAndDie( "DB query returned no results for StopID=".$this->stopid);
			}
			$this->EchoResults();			
		}
		catch (PDOException $ex) {
			$this->EchoErrorAndDie( "Exception: ".$ex->getMessage());			
		}
	}
	
	abstract protected function EchoResults();
	
	abstract protected function EchoErrorAndDie( $msg);
}
?>