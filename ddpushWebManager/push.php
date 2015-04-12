<?php
session_start();
/**
 * User: brok1n
 *
 */


$uuid = $_GET['uuid'];
$type = $_GET['type'];
$data = $_GET['data'];
//
////http://127.0.0.1:888/?act=user&type=20&uuid=cfcd208495d565ef66e7dff9f98764da&data=%E4%BD%A0%E5%A5%BD%EF%BC%8C%E6%88%91%E6%98%AFWEB%E6%8E%A8%E9%80%81%E6%B6%88%E6%81%AF&ticket=lsdf651sd3f1313sdf354
/*
 * 这里就是调用HTTP 的推送接口
 * act  推送类型
 *      单个用户 user
 *      多个用户 more (多个用户 暂时没有写)
 * type 消息类型 对应ddpush推送的消息类型
 *      通用消息 10
 *      分类消息 11
 *      自定义消息 20
 * uuid 推送目标用户的UUID
 *
 * data 推送的消息内容
 *      通用消息 可以没有该字段也可以设置data=空字符串
 *      分类消息 根据ddpush规矩，值必须是数字型
 *      自定义消息  值必须为字符串
 * ticket 用来验证当前后台提交推送信息是否合法( 是否有权限推送 )
 *      这个值要放到HttpDdpushServer 里判断 用来检测这个推送者是否有权限推送信息
 *
 ***/
$pusUrl = 'http://192.168.1.107:8888/?act=user&type='.$type.'uuid='.$uuid.'&data='.$data.'&ticket=35as1df35a1sdf5a1sdf35';
//

$opts = array(
    'http'=>array(
        'method'=>'GET',
        'timeout'=>10,
    )
);
$context = @stream_context_create($opts);

$result = @file_get_contents($pusUrl, false, $context);
//$result = file_get_contents($pusUrl);

$responseInfo = $http_response_header;
$statusLine = $responseInfo[0];
$status = strstr($statusLine, '200');

if( $status == '')
{
    echo 'failed';
}
else
{
    echo 'success';
}