<?php
session_start();

if( !isset($_SESSION['username']) )
{
	echo "<script>window.parent.location.href='index.php';</script>";
	exit;
}

require 'smarty_config.php';

if( isset($_POST['submit']) )
{
    $user = $_SESSION['username']['user'];

    $id = $_POST['user_id'];

    $uuid = md5($id);
    $nickname = $_POST['user_nickname'];

    $dev_type = (int)$_POST['dev_type'];

    $user[] = array
    (
        'id'=>$id, 'uuid'=>$uuid, 'nickname'=>$nickname, 'dev_type'=>$dev_type,'status'=>0, 'push_count'=>0
    );
    $_SESSION['username']['user'] = $user;

    echo "<script>alert('添加成功');window.parent.location.replace(window.parent.location.href);</script>";
}


$smarty->display('user-add.html');