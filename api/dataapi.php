<?php
require_once( "database.php");

abstract class DataAPI {
	private $stopid;
	protected $result;

	public function __construct( $stopid) {
		$this->stopid = $stopid;
	}
	
	public function Execute() {
		global $dbname, $dbuser, $dbpass;
	
		try {
			$db = new PDO($dbname, $dbuser, $dbpass);

			$query = "Select * FROM Stops WHERE StopID='".$this->stopid."'";
			$stmt = $db->query( $query);
			
			if ( $stmt->rowCount() == 1 ) {
			
				$row = $stmt->fetch( PDO::FETCH_ASSOC);
				$this->result = $row;
			}
			else {
				$this->EchoErrorAndDie( "DB query returned no results for StopID=".$this->stopid);
			}
			
			if ( $this->stopid == 0 ) {
				$query = "Select StopID, StopName, StopPosition, StopX, StopY, StopQRIdentifier FROM Stops WHERE StopID<>0";
				$stmt = $db->query( $query);
				$rows = $stmt->fetchAll( PDO::FETCH_ASSOC);
				
				$this->result["StopList"] = $rows;
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