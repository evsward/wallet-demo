<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="userlogin/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="id" id="id" value="${pd.id}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户ID:</td>
								<td><input type="number" name="USER_ID" id="USER_ID" value="${pd.USER_ID}" maxlength="32" placeholder="这里输入用户ID" title="用户ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户账户(手机号码):</td>
								<td><input type="text" name="ACCOUNT" id="ACCOUNT" value="${pd.ACCOUNT}" maxlength="20" placeholder="这里输入用户账户(手机号码)" title="用户账户(手机号码)" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户名:</td>
								<td><input type="text" name="USER_NAME" id="USER_NAME" value="${pd.USER_NAME}" maxlength="20" placeholder="这里输入用户名" title="用户名" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">登录密码:</td>
								<td><input type="text" name="PASSWORD" id="PASSWORD" value="${pd.PASSWORD}" maxlength="50" placeholder="这里输入登录密码" title="登录密码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">最近一次登录时间:</td>
								<td><input type="text" name="LASTLOGIN_TIME" id="LASTLOGIN_TIME" value="${pd.LASTLOGIN_TIME}" maxlength="19" placeholder="这里输入最近一次登录时间" title="最近一次登录时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">IP:</td>
								<td><input type="text" name="LASTLOGIN_IP" id="LASTLOGIN_IP" value="${pd.LASTLOGIN_IP}" maxlength="20" placeholder="这里输入IP" title="IP" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">:</td>
								<td><input type="text" name="LASTLOGIN_PORT" id="LASTLOGIN_PORT" value="${pd.LASTLOGIN_PORT}" maxlength="6" placeholder="这里输入" title="" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">启用1，禁用0:</td>
								<td><input type="text" name="STATUS" id="STATUS" value="${pd.STATUS}" maxlength="6" placeholder="这里输入启用1，禁用0" title="启用1，禁用0" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">登陆错误次数:</td>
								<td><input type="number" name="LOGIN_ERROR_TIMES" id="LOGIN_ERROR_TIMES" value="${pd.LOGIN_ERROR_TIMES}" maxlength="32" placeholder="这里输入登陆错误次数" title="登陆错误次数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">登陆错误时间:</td>
								<td><input class="span10 date-picker" name="LASTLOGIN_ERROR_TIME" id="LASTLOGIN_ERROR_TIME" value="${pd.LASTLOGIN_ERROR_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="登陆错误时间" title="登陆错误时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
					</form>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			if($("#USER_ID").val()==""){
				$("#USER_ID").tips({
					side:3,
		            msg:'请输入用户ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USER_ID").focus();
			return false;
			}
			if($("#ACCOUNT").val()==""){
				$("#ACCOUNT").tips({
					side:3,
		            msg:'请输入用户账户(手机号码)',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ACCOUNT").focus();
			return false;
			}
			if($("#USER_NAME").val()==""){
				$("#USER_NAME").tips({
					side:3,
		            msg:'请输入用户名',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USER_NAME").focus();
			return false;
			}
			if($("#PASSWORD").val()==""){
				$("#PASSWORD").tips({
					side:3,
		            msg:'请输入登录密码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PASSWORD").focus();
			return false;
			}
			if($("#LASTLOGIN_TIME").val()==""){
				$("#LASTLOGIN_TIME").tips({
					side:3,
		            msg:'请输入最近一次登录时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LASTLOGIN_TIME").focus();
			return false;
			}
			if($("#LASTLOGIN_IP").val()==""){
				$("#LASTLOGIN_IP").tips({
					side:3,
		            msg:'请输入IP',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LASTLOGIN_IP").focus();
			return false;
			}
			if($("#LASTLOGIN_PORT").val()==""){
				$("#LASTLOGIN_PORT").tips({
					side:3,
		            msg:'请输入',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LASTLOGIN_PORT").focus();
			return false;
			}
			if($("#STATUS").val()==""){
				$("#STATUS").tips({
					side:3,
		            msg:'请输入启用1，禁用0',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STATUS").focus();
			return false;
			}
			if($("#LOGIN_ERROR_TIMES").val()==""){
				$("#LOGIN_ERROR_TIMES").tips({
					side:3,
		            msg:'请输入登陆错误次数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOGIN_ERROR_TIMES").focus();
			return false;
			}
			if($("#LASTLOGIN_ERROR_TIME").val()==""){
				$("#LASTLOGIN_ERROR_TIME").tips({
					side:3,
		            msg:'请输入登陆错误时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LASTLOGIN_ERROR_TIME").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>