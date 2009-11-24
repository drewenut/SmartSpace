<?php
/*
 * Created on Jun 26, 2009
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
require_once("config.php");
require_once("lib.php");

$position = $_POST['position'];
$destination = $destDir.$destFile;
$jsonString = stripslashes($position);
writeFile($destination, $jsonString);
?>
