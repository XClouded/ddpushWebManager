<?php
session_start();

$username = $_POST['user_name'];
$password = $_POST['user_pass'];

if( $username == 'admin' && $password == 'admin')
{
    //登陆成功
    $_SESSION['username'] = array('nickname'=>$username, 'level'=>1, 'push_count'=>0, 'send_push_count'=>0, 'send_push_failed_count'=>0);

    $user = array
    (
//        array
//        (
//            'id'=>10000, 'uuid'=>'cfcd208495d565ef66e7dff9f98764da', 'nickname'=>'小明', 'dev_type'=>1,'status'=>0, 'push_count'=>0
//        ),
//        array
//        (
//            'id'=>10001, 'uuid'=>'q5cd2ad535d565ef66e765adf9876d5c', 'nickname'=>'小丽', 'dev_type'=>0,'status'=>1, 'push_count'=>0
//        ),
//        array
//        (
//            'id'=>10000, 'uuid'=>'23dd266495d5d556dde7dff922ed26ko', 'nickname'=>'小白', 'dev_type'=>2,'status'=>1, 'push_count'=>0
//        )
    );

    $_SESSION['username']['user'] = $user;

    echo 1;
}
else
{
    echo -1;
}


//-------------------------------------------------------------------------------------------------------
//  这里可以自己改成。从数据库里判断用户名密码的。我这里就不写了。需要数据库保存管理员账号的功能自己添加
//-------------------------------------------------------------------------------------------------------


