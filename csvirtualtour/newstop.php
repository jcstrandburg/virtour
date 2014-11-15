<?php
session_start();
include 'phpfunction.php';

if (isset($_POST['sent'])) {
	$sent = $_POST['sent'];
	$stopname = $_POST['stopname'];
	$stoporder = $_POST['stoporder'];
	$stopcontent = $_POST['stopcontent'];
	$stopx = $_POST['stopx'];
	$stopy = $_POST['stopy'];
}

$more = "<a href='stops.php'>List Of Stops</a>
			</br>";

if($_SESSION['user'] == 1 || $_SESSION['user'] == 2) {
	if(!empty($sent)) {
		$query = "insert into Stops (StopName, StopContent, StopOrder, StopX, StopY) values 
			('" . $stopname . "', '" . $stopcontent. "', '" . $stoporder . "', '" . $stopx . "', '" . $stopy . "')";
		mysqli_query($writedb, $query);
		
		mysqli_query($writedb, "update Stops set StopQRIdentifier = StopID where StopName = '" . $stopname . "'");
		header("Location:stops.php");
	}
}
else {
	header("Location:index.php");
}
?>

<?php  echo $header ?>

<script>
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
		framework = new ComponentFramework( 'compContainer');
		set_position( 0.5, 0.5);

		$("#clickymap").click(
			function(event){ 
				var offset = $(this).offset();
				offx = event.pageX - offset.left;
				offy = event.pageY - offset.top;
				set_position(offx/$(this).width(), offy/$(this).height());
			}	
		);		
	});
</script>


<h1>Add a New Stop</h1>

<form method="POST" name="theform">
	<h2>Stop Name: <input type="text" name="stopname"></h2>
	<h3>Stop Order: <input type="text" name="stoporder"></h3>
	<br>

	<h3>Click On The Map For Stop Location</h3>
	<div class='mapWrapperWrapper'>
		<div class='mapWrapper'>
			<img id='pin' src='pin.gif'/>
			<img id="clickymap" src="cf1.png"><!--<img id="fl4" src="cf4.png" width="50%" height="50%">-->
		</div>
	</div>
	<hr>	

	<h3>Stop Content</h3>
	<input type="hidden" name="stopx" value="0.0">
	<input type="hidden" name="stopy" value="0.0">
	<input type="hidden" name="mapindex" value="0">	
	<input type="hidden" name="stopcontent">
	<input type="hidden" name="sent" value="sent">
</form>

<button onClick='framework.addComponent(TextComponent)'>Add Text</button>
<button onClick='framework.addComponent(ImageComponent)'>Add Image</button>
<button onClick='framework.addComponent(VideoComponent)'>Add Video</button>

<div name='compContainer' style='border:1px solid;'></div>

<br>

<button onClick='renderJSON()'>Add New Stop</button></br></br>

<?php echo footer($more)?>

