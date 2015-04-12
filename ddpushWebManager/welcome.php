<?php
session_start();

if( !isset($_SESSION['username']) )
{
	echo "<script>window.parent.location.href='index.php';</script>";
	exit;
}

require 'smarty_config.php';

$pushCount = $_SESSION['username']['push_count'];
$sendPushCount = $_SESSION['username']['send_push_count'];
$sendPushFailedCount = $_SESSION['username']['send_push_failed_count'];



$lastIp = $_SERVER['REMOTE_ADDR'];
$lastTime = date("Y-m-d H:i:s");

$smarty->assign('last_ip', $lastIp);
$smarty->assign('last_time', $lastTime);

$smarty->assign('admin_level', '超级管理员');
$smarty->assign('push_count', $pushCount);
$smarty->assign('send_push_count', $sendPushCount);
$smarty->assign('send_push_failed_count', $sendPushFailedCount);

$smarty->display("welcome.html");