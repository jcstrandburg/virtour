<?php
session_start();
include 'phpfunction.php';

$stopid = $_GET['eid'];

if ( isset($_POST['sent'])) {
	$sent = $_POST['sent'];
	$stopname = $_POST['stopname'];
	$stoporder = $_POST['stoporder'];
	$stopcontent = $_POST['stopcontent'];
}
	
$more = "<a href='stops.php'>List Of Stops</a>
			</br>";

if(isset($_SESSION['user']) && $_SESSION['user'] == 1) {

	if(empty($sent)) {
		$row = mysqli_fetch_array(mysqli_query($link, "select * from Stops where StopID='$stopid'"));
		$stopname = $row['StopName'];
		$stoporder = $row['StopOrder'];
		$stopcontent = $row['StopContent'];
	}
	else {
		$query = "update Stops set StopName = '" . $stopname . "' StopOrder = '" . 
				$stoporder . "' StopContent = '" . $stopcontent . "' where StopID = '" . $stopid . "'";    
		
		mysqli_query($writedb, $query);
		
		header("Location:stopinfo.php?eid='$stopid'");
	}
}
else {
	header("Location:index.php");
}
?>

<?php echo $header?>

<script type="text/javascript">
function set_position(x, y) {
	$('input[name=stopx]').val(x);
	$('input[name=stopy]').val(y);
	
	css_top = ""+y*100+"%";
	css_left = ""+x*100+"%";
	$('#pin').css({'top': css_top, 'left': css_left});
}

function renderJSON() {
    json = framework.renderJSON();
    $("input[name=stopcontent]").val( json);
	document.theform.submit();
}

$(document).ready( function() {
	framework = new ComponentFramework( "compContainer");
	framework.loadFromJSON('<?php echo addslashes($stopcontent)?>');
	$('#clickymap').click(
		function(event){ 
			var offset = $(this).offset();
			offx = event.pageX - offset.left;
			offy = event.pageY - offset.top;
			set_position(offx/$(this).width(), offy/$(this).height());
		}			
	);
});
</script>


<form method="post" name="theform">
	<h1>Content for Stop Name: <input type="text" name="stopname" value="<?php echo $stopname?>"></h1>
	<h3>Stop Order: <input type="text" name="stoporder" value="<?php echo $stoporder?>"></h3>
	
	<h3>Click On The Map For Stop Location</h3>
	<div class='mapWrapperWrapper'>
		<div class='mapWrapper'>
			<img id='pin' src='pin.gif'/>
			<img id="clickymap" src="cf1.png"><!--<img id="fl4" src="cf4.png" width="50%" height="50%">-->
		</div>
	</div>
	<hr>	
	
	<input type="hidden" name="stopcontent">
	<input type="hidden" name="stopx" value="0.0">
	<input type="hidden" name="stopy" value="0.0">	
	<input type="hidden" name="sent" value="<?php echo $stopid?>">
</form>
<button onClick='framework.addComponent(TextComponent)'>Add Text</button>
<button onClick='framework.addComponent(ImageComponent)'>Add Image</button>
<button onClick='framework.addComponent(VideoComponent)'>Add Video</button>
<div name='compContainer' style='border:1px solid;'></div>

<br>

<button onClick='renderJSON()'>Save Changes</button></br></br>

<?php echo footer($more)?>
