<?php
session_start();
include '../phpfunction.php';

if( $_SESSION['user'] == 0) {
	header("Location: ../index.php");
}

$mapid = $_POST['id'];
$stmt = $link->prepare("SELECT `id`, `url`, `desc`, `ordering` FROM maps WHERE `id`=?");
if (!$stmt) {
    die( mysqli_error($link));
}
$stmt->bind_param("i", $mapid);
$map = array();
$stmt->execute();
$stmt->bind_result($map['id'], $map['url'], $map['desc'], $map['ordering']);
$stmt->fetch();

if ( isset($_POST['operation']) && $_POST['operation'] === 'do-edit') {

    if (isset($_FILES['fileToUpload']) && $_FILES['fileToUpload']['size'] > 0) {

        $basename = str_replace(' ','',basename($_FILES["fileToUpload"]["name"]));
	    $target_file = $target_dir . $basename;

        //attempt to create the new file
	    if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {

            //delete the old file if the filename is different from the original
            if ( $map['url'] !== $basename) {
                if (!unlink( $map['url'])) {
                    die("Operation Aborted: Unable to delete old map file!");
                }
                $map['url'] = $basename;
            }

        } else {
            die("Operation Aborted: There was an error uploading your file. You may have exceeded the file upload limit, which appears to be {$limit_mb} MB");
        }    
    }

    $map['ordering'] = $_POST['ordering'];
    $map['desc'] = $_POST['description'];

    $query = "UPDATE maps SET `ordering`=?, `desc`=?, `url`=? WHERE `id`=?";
    $stmt = $writedb->prepare($query);
    $stmt->bind_param('issi', $map['ordering'], $map['desc'], $map['url'], $map['id']);
    $result = $stmt->execute();

    header('location: manage.php');
}
else {

    echo "<html>";
    echo make_header('../');
    ?>

    <h1>Edit Map Details</h1>
    <form method='POST' action='edit.php' enctype="multipart/form-data">
        <input type='hidden' name='operation' value='do-edit'>
        <input type='hidden' name='id' value='<?php echo $map['id'];?>'>
        <label for='ordering'>Sort Order</label>
        <input type='text' name='ordering' id='ordering' value='<?php echo $map['ordering'];?>'><br>
        <label for='description'>Map Title</label>
        <input type='text' name='description' id='description' value='<?php echo $map['desc'];?>'><br>
        <label for='fileToUpload'>New Image File (don't upload a file if you wan't to keep the old one)</label>
		<input type="file" name="fileToUpload" id="fileToUpload" size="70" data-uploadlimit="<?php echo $upload_limit;?>"><br>
        <input type='submit' name='Submit' value='Submit Changes'>
    </form>

<?php
    echo footer('');
}
?>



