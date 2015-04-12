<?php
session_start();

if( !isset($_SESSION['username']) )
{
	echo "<script>window.parent.location.href='index.php';</script>";
	exit;
}

require_once 'libs/medoo/defaultDb.php';


$id = $_POST['id'];
$status = $_POST['status'];

$result = $database->update('games', array('status'=>$status), array('id'=>$id) );

echo $result;


