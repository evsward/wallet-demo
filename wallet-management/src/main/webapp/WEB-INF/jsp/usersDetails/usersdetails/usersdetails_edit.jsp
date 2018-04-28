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
					
					<form action="usersdetails/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="id" id="id" value="${pd.id}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">账号:</td>
								<td><input disabled="true" type="text" name="ACCOUNT" id="ACCOUNT" value="${pd.ACCOUNT}" maxlength="100" placeholder="这里输入用户账号" title="用户账号" onblur="hasA()" style="width:98%;"/></td>
							</tr>
							 	<input type="hidden" name="PASSWORD" id="PASSWORD" value="${pd.PASSWORD}" maxlength="100" placeholder="这里输入用户密码" title="用户密码" style="width:98%;"/>
							<%--<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户头像:</td>
								<td><input type="text" name="USER_IMG" id="USER_IMG" value="${pd.USER_IMG}" maxlength="100" placeholder="这里输入用户头像" title="用户头像" style="width:98%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">身份证:</td>
								<td><input type="text" name="ID_CARD" id="ID_CARD" value="${pd.ID_CARD}" maxlength="20" placeholder="这里输入身份证" title="身份证" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员Email :</td>
								<td><input type="text" name="EMAIL" id="EMAIL" value="${pd.EMAIL}" maxlength="60" placeholder="这里输入会员Email " title="会员Email " style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">公钥:</td>
								<td><input disabled="true" type="text" name="PUBLIC_KEY" id="PUBLIC_KEY" value="${pd.PUBLIC_KEY}" maxlength="200" placeholder="这里输入公钥" title="公钥" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">省信息:</td>
								<td><input type="text" name="PROVINCE" id="PROVINCE" value="${pd.PROVINCE}" maxlength="200" placeholder="这里输入省信息" title="省信息" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">地址:</td>
								<td><input disabled="true" type="text" name="ADDRESS" id="ADDRESS" value="${pd.ADDRESS}" maxlength="200" placeholder="这里输入地址" title="地址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户地址信息:</td>
								<td><input type="text" name="ADDRESS_MSG" id="ADDRESS_MSG" value="${pd.ADDRESS_MSG}" maxlength="200" placeholder="这里输入用户地址信息" title="用户地址信息" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">性别:</td>
								<td>
									<select name="SEX" title="性别">
									<option value="1" <c:if test="${pd.SEX == '1' }">selected</c:if> >男</option>
									<option value="2" <c:if test="${pd.SEX == '2' }">selected</c:if> >女</option>
									</select>
								 </td>
							</tr>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">种子:</td>
								<td><input type="text" name="SEEDS" id="SEEDS" value="${pd.SEEDS}" maxlength="200" placeholder="这里输入种子" title="种子" style="width:98%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户名 :</td>
								<td><input type="text" name="USER_NAME" id="USER_NAME" value="${pd.USER_NAME}" maxlength="60" placeholder="这里输入用户名 " title="用户名 " style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户类型:</td>
								<td>
								<select name="USER_TYPE" title="用户类型" disabled="true">
										<!-- 1-普通会员 2-钻石会员创业会员 3-店铺 4-供应商 5-代理商 6-国内物流 7-国外物流: -->
									<option value="1" <c:if test="${pd.USER_TYPE == '1' }">selected</c:if> >普通会员</option>
									<option value="2" <c:if test="${pd.USER_TYPE == '2' }">selected</c:if> >钻石会员创业会员</option>
									<option value="3" <c:if test="${pd.USER_TYPE == '3' }">selected</c:if> >店铺</option>
									<option value="4" <c:if test="${pd.USER_TYPE == '4' }">selected</c:if> >供应商 </option>
									<option value="5" <c:if test="${pd.USER_TYPE == '5' }">selected</c:if> >代理商</option>
									<option value="6" <c:if test="${pd.USER_TYPE == '6' }">selected</c:if> >国内物流</option>
									<option value="7" <c:if test="${pd.USER_TYPE == '7' }">selected</c:if> >国外物流</option>
									 </select>
								
								<%-- <input type="text" name="USER_TYPE" id="USER_TYPE" value="${pd.USER_TYPE}" maxlength="2" placeholder="这里输入用户类型:1-普通会员 2-钻石会员创业会员 3-店铺 4-供应商 5-代理商 6-国内物流 7-国外物流" title="用户类型:1-普通会员 2-钻石会员创业会员 3-店铺 4-供应商 5-代理商 6-国内物流 7-国外物流" style="width:98%;"/>
								 --%></td>
							</tr>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">注册时间:</td>
								<td><input type="text" name="REG_TIME" id="REG_TIME" value="${pd.REG_TIME}" maxlength="60" placeholder="这里输入注册时间" title="注册时间" style="width:98%;"/></td>
							</tr> --%>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">推荐人的usercode:</td>
								<td><input type="text" name="PARENT_USERCODE" id="PARENT_USERCODE" value="${pd.PARENT_USERCODE}" maxlength="60" placeholder="这里输入推荐人的usercode" title="推荐人的usercode" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员推荐码:</td>
								<td><input type="text" name="USERCODE" id="USERCODE" value="${pd.USERCODE}" maxlength="60" placeholder="这里输入会员推荐码" title="会员推荐码" style="width:98%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">昵称:</td>
								<td><input type="text" name="ALIAS" id="ALIAS" value="${pd.ALIAS}" maxlength="60" placeholder="这里输入昵称" title="昵称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">移动电话:</td>
								<td><input type="text" name="MOBILE_PHONE" id="MOBILE_PHONE" value="${pd.MOBILE_PHONE}" maxlength="20" placeholder="这里输入移动电话" title="移动电话" style="width:98%;"/></td>
							</tr>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">二维码:</td>
								<td><input type="text" name="RCODE" id="RCODE" value="${pd.RCODE}" maxlength="255" placeholder="这里输入二维码" title="二维码" style="width:98%;"/></td>
							</tr> 
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">身份证号:</td>
								<td><input type="text" name="IDNO" id="IDNO" value="${pd.IDNO}" maxlength="25" placeholder="这里输入身份证号" title="身份证号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">交易密码:</td>
								<td><input type="text" name="TRANS_PASSWORD" id="TRANS_PASSWORD" value="${pd.TRANS_PASSWORD}" maxlength="100" placeholder="这里输入交易密码" title="交易密码" style="width:98%;"/></td>
							</tr> 
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收款二维码:</td>
								<td><input type="text" name="PAY_RCODE" id="PAY_RCODE" value="${pd.PAY_RCODE}" maxlength="50" placeholder="这里输入收款二维码" title="收款二维码" style="width:98%;"/></td>
							</tr>--%>
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
	<script src="plugins/My97DatePicker/WdatePicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			if($("#ACCOUNT").val()==""){
				$("#ACCOUNT").tips({
					side:3,
		            msg:'请输入账号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ACCOUNT").focus();
			return false;
			}
			/* if($("#PASSWORD").val()==""){
				$("#PASSWORD").tips({
					side:3,
		            msg:'请输入密码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PASSWORD").focus();
			return false;
			} */
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
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		
		
		//判断账号是否存在
		function hasA(){
			var ACCOUNT = $("#ACCOUNT").val();
			var id = $("#id").val();
			
			$.ajax({
				type: "POST",
				url: '<%=basePath%>usersdetails/hasA.do',
		    	data: {ACCOUNT:ACCOUNT,USER_ID:id,tm:new Date().getTime()},
				dataType:'json',
				cache: false,
				success: function(data){
					 if("success" != data.result){
						 $("#ACCOUNT").tips({
								side:3,
					            msg:'账号'+ACCOUNT+'已存在',
					            bg:'#AE81FF',
					            time:3
					        });
						 $('#ACCOUNT').val('');
					 }
				}
			});
		}
		
		
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>