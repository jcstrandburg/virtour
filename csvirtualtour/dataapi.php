<?php
include 'phpfunction.php';

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
			$query = "Select * FROM Stops WHERE StopID='".$this->stopid."'";
			
			$stmt = mysqli_query($link, $query);

			if(mysqli_num_rows($stmt) == 1) {
				$row = mysqli_fetch_assoc($stmt);
				$this->result = $row;
			}
			
			else if ( $this->stopid == -1 ) {
				$query = "Select StopID, StopName, RoomNumber, StopOrder, StopX, StopY, MapID, StopQRIdentifier FROM Stops WHERE Active='yes'";
				$stmt = mysqli_query($link, $query);
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
