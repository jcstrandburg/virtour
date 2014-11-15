<?php
require_once( "dataapi.php");
function json_echo( $success, $result, $error) {
	$json = array();
	$json["success"] = $success;
	$json["result"] = $result;
	$json["error"] = $error;	
	return json_encode( $json);
}
class JSONDataAPI extends DataAPI{
	public function __construct( $stopid) {
		parent::__construct( $stopid);
	}
	
	protected function EchoResults() {
		echo json_echo( true, $this->result, "");
	}
	
	protected function EchoErrorAndDie( $msg) {
		$json = array();
		$json["success"] = false;
		$json["result"] = "";
		$json["error"] = $msg;
		die( json_echo( false, "", $msg));
	}
}
if ( isset( $_GET["stopid"])) {
	$id = (int)$_GET["stopid"];
	$abc = new JSONDataAPI( $id);
	$abc->Execute();
}
else  {
	die( json_echo( false, "", "No stop id provided"));
}
?>