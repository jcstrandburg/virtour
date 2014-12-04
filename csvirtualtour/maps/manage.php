<?php
session_start();
include '../phpfunction.php';

if( $_SESSION['user'] == 0) {
	header("Location: ../index.php");
}

echo "<html>";
echo make_header('../');
?>

<style>
.clr {
	clear: both;
	font-size: 0;
	height: 0;
}

#filelist {
	width: 50%;
	float: left;
	height: 500px;
	overflow-y: scroll;
}

#preview-wrapper {
	width: 50%;
	float: left;
	height: 500px;
}

#preview {
	width: 100%;
	height: 100%;
}

.filewrapper {
	padding: 3px;
	padding-bottom: 0px;
	margin: 3px;
	border: solid 1px gray;
	border-radius: 0.4em;
}

.mediafile {
	padding: 3px;
	cursor: pointer;
	float: left;
	width: 90%;
}

.previewfile {
	max-width: 95%;
	max-height: 85%;
	width: auto;
	height: auto;
}

.filestats {
	width: 13%;
	float: right;
}

.inlineform {
	float: right;
	margin: 0;
}

#fileuploader {
	padding: 3em;
	border: solid 1px gray;
}
</style>

<script>
function do_preview(url) {
	var regex = /(?:\.([^.]+))?$/;
	var ext = regex.exec(url)[1];
	var img_exts = ['jpg', 'png'];
	
	//if its an image file
	if ($.inArray(ext, img_exts) > -1) {
		$('#preview').html("<img class='previewfile' alt='Preview' src='"+url+"'><br>");
	}
	//else what do?
	else {
		$('#preview').html("No preview available for this file");
	}
	
}

$(document).ready(function() {
	$('.mediafile').click(function(){
		var url = $(this).data('fileurl');
		do_preview(url);
	});
	
	$('#delete-form').click(function(event){
		if ( !confirm("Are you sure? Make sure this file is no longer used on the tour!")) {
			event.preventDefault();
			event.stopPropagation();
		}
	});
	
	//if the file api is supported, add warnings to files that are too large!
	if (window.File) {
		$('#fileToUpload').change(function(){
			filesize = this.files[0].size;
			if (filesize > $(this).data('uploadlimit')) {
				$('#file-warning').html("<h4>This file exceeds your upload limit, which is "+$(this).data("uploadlimit")/(1024*1024)+" MB</h4>");
			}
			else {
				$('#file-warning').html("");
			}
		});
	}
	else {
		//no upload size checking, bummer.
	}
});
</script>

<div id='filelist'>
<h3>Maps on Server:</h3>

<?php
$query = "SELECT * FROM maps ORDER BY `ordering` ASC";
$result = mysqli_query($link, $query);

if ($result) {
    while ($row = mysqli_fetch_array($result)) {
        ?>
        <div class='filewrapper'>
            <div class='mediafile' data-fileurl='<?php echo $row['url'];?>'>
                Map: <?php echo $row['desc'];?> | 
                Ordering: <?php echo $row['ordering'];?> |
                URL: <?php echo $row['url'];?>
            </div>
            <form class='inlineform' id='delete-form' method='post' action='delete.php'>
                <input type='hidden' name='id' value='<?php echo $row['id'];?>'/>
                <input type='hidden' name='filename' value='<?php echo $row['url'];?>'/>
                <input type='submit' name='submit' value='Delete Map'/>
            </form>
            <form class='inlineform' method='post' action='edit.php'>
                <input type='hidden' name='id' value='<?php echo $row['id'];?>'/>
                <input type='submit' name='submit' value='Edit Details'/>
            </form>
            <br class='clr'>
        </div>
        <?php
    }
}
else {
    die("Couldn't load maps!");
}
?>
</div>

<div id='preview-wrapper'>
	<h3>File Preview</h3>
	<div id='preview'>
		
	</div>
</div>
<br class='clr'>

<?php
$upload_limit = find_file_upload_limit();
?>

<div id='fileuploader'>
	<form action="upload.php" method="post" enctype="multipart/form-data">
		<h2>Upload New Map</h2>
		<input type="file" name="fileToUpload" id="fileToUpload" size="70" data-uploadlimit="<?php echo $upload_limit*1024*1024;?>"><br>
        <label for="description">Description: </label>
        <input type="text" name="description" id="description"/><br>
        <label for="ordering">Sorting Order: </label>
        <input type="text" name="ordering" id="ordering"/><br>
		<div id='file-warning'></div>
		<input type="submit" value="Upload Map" name="submit">
	</form>
</div>

<?php
echo footer('<a href="../admin.php">Administrative Functions</a></br>', '../');
?>
