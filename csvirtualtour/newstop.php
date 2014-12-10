<?php
session_start();
include 'phpfunction.php';

if (isset($_POST['sent'])) {
	$sent = $_POST['sent'];
	$stopname = $_POST['stopname'];
	$stoporder = $_POST['stoporder'];
	$stopcontent = stripslashes( $_POST['stopcontent']);
	$stopx = $_POST['stopx'];
	$stopy = $_POST['stopy'];
    $mapid = $_POST['mapid'];
}

$more = "<a href='stops.php'>List Of Stops</a>
			</br>";

if($_SESSION['user'] == 1 || $_SESSION['user'] == 2) {
	if(!empty($sent)) {
		$query = "insert into Stops (StopName, StopContent, StopOrder, StopX, StopY, MapID) values 
			(?, ?, ?, ?, ?, ?)";

        $stmt = $writedb->prepare($query);
        $stmt->bind_param("ssiddi", $stopname, $stopcontent, $stoporder, $stopx, $stopy, $mapid);
        $success = $stmt->execute();
        $stmt->close();

        $stmt = $writedb->prepare("update Stops set StopQRIdentifier = StopID where StopName=?");	
        $stmt->bind_param("s", $stopname);
        $success = $stmt->execute();
        $stmt->close();
		header("Location:stops.php");
	}
}
else {
	header("Location:index.php");
}

$maps = get_map_list();
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
		
        $('#map-selector').change(
	        function(event) {
		        var url = $(this).val();
                
                var id = $(this).val();
                var url = "maps/"+$(this).find(":selected").data("url");
		        $('#clickymap').attr('src', url);
		        $('input[name=mapid]').val(id);
	        }
        );	
	});
</script>


<h1>Add a New Stop</h1>

<form method="POST" name="theform">
	<h2>Stop Name: <input type="text" name="stopname"></h2>
	<h3>Stop Order: <input type="text" name="stoporder"></h3>
	<br>
	
	<div class='mapSelector'>
		<h3>Select Map</h3>
		<select id='map-selector'>
			<?php
			$maps = get_map_list();
            $counter = 0;
			foreach ($maps as $map) {
                if ($counter == 0) {
                    $selected = "selected";
                    $firstmap = $map;
                }
                else {
                    $selected = "";
                }
                ++$counter;
				echo "<option $selected value='{$map['id']}' data-url='{$map['url']}'>{$map['desc']}</option>";

			}
			?>
		</select>
	</div>	

	<h3>Click On The Map For Stop Location</h3>
	<div class='mapWrapperWrapper'>
		<div class='mapWrapper'>
			<img id='pin' src='pin.gif'/>
			<img id="clickymap" src="<?php echo "maps/".$firstmap['url'];?>">
		</div>
	</div>
	<hr>	

	<h3>Stop Content</h3>
	<input type="hidden" name="stopx" value="0.0">
	<input type="hidden" name="stopy" value="0.0">
	<input type="hidden" name="mapid" value="<?php echo $firstmap['id'];?>">	
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

