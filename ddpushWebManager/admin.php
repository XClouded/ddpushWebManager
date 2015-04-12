<?php
session_start();

if( !isset($_SESSION['username']) )
{
	echo "<script>window.location.href='index.php';</script>";
	exit;
}

include "smarty_config.php";

$adminUser = $_SESSION['username'];
if( $adminUser['level'] == '1' )
{
    $smarty->assign('admin_level', '超级管理员');
}

$smarty->assign('admin_nickname', $adminUser['nickname']);
$smarty->display("admin.html");
