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
	
        if ( $this->stopid == -1) {
		    $query = "Select StopID, StopName, RoomNumber, StopOrder, StopX, StopY, MapID, StopQRIdentifier FROM Stops WHERE Active='yes'";
		    $stmt = mysqli_query($link, $query);
		    $rows = fake_fetch_all($stmt);
		    $this->result["StopList"] = $rows;
        }
        else {
			$stmt = mysqli_prepare($link, "Select StopID, StopName, RoomNumber, StopContent, StopX, StopY, StopQRIdentifier, StopOrder, MapID, Active FROM Stops WHERE StopID=?");
            $stmt->bind_param("i", $this->stopid);
            $stmt->execute();
            $row = array();
            $stmt->bind_result($row['StopID'], $row['StopName'], $row['RoomNumber'], $row['StopContent'], $row['StopX'], $row['StopY'], $row['StopQRIdentifier'], $row['StopOrder'], $row['MapID'], $row['Active']);
            $stmt->fetch();

		    $this->result = $row;
        }

		$this->EchoResults();			
	}
	
	abstract protected function EchoResults();
	
	abstract protected function EchoErrorAndDie( $msg);
}
?>
