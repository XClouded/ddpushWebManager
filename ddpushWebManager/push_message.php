<?php
session_start();

if( !isset($_SESSION['username']) )
{
	echo "<script>window.parent.location.href='index.php';</script>";
	exit;
}

require 'smarty_config.php';


$id = $_GET['id'];
$uuid = $_GET['uuid'];

$smarty->assign('user_id', $id);
$smarty->assign('user_uuid', $uuid);


$smarty->display('push_message.html');