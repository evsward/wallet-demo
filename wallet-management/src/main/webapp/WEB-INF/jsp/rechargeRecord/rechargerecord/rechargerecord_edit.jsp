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
					
					<form action="rechargerecord/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="id" id="id" value="${pd.id}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户id:</td>
								<td><input type="text" name="USER_ID" id="USER_ID" value="${pd.USER_ID}" maxlength="32" placeholder="这里输入用户id" title="用户id" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">编号:</td>
								<td><input type="text" name="CODE" id="CODE" value="${pd.CODE}" maxlength="50" placeholder="这里输入编号" title="编号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">充值金额:</td>
								<td><input type="text" name="MONEY" id="MONEY" value="${pd.MONEY}" maxlength="17" placeholder="这里输入充值金额" title="充值金额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">账户余额:</td>
								<td><input type="text" name="REMAINDER_MONEY" id="REMAINDER_MONEY" value="${pd.REMAINDER_MONEY}" maxlength="17" placeholder="这里输入账户余额" title="账户余额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态:</td>
								
								<td>
								<select name="STATUS" title="是否有限额" id="STATUS">
									<option value="0" <c:if test="${pd.STATUS == '0' }">selected</c:if> >待审核</option>
									<option value="1" <c:if test="${pd.STATUS == '1' }">selected</c:if> >审核通过</option>
									<option value="2" <c:if test="${pd.STATUS == '2' }">selected</c:if> >待确认</option>
									<option value="3" <c:if test="${pd.STATUS == '3' }">selected</c:if> >已确认</option>
									</select>
								<%-- <input type="text" name="STATUS" id="STATUS" value="${pd.STATUS}" maxlength="20" placeholder="这里输入状态" title="状态" style="width:98%;"/> --%>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">货币种类:</td>
								 <td>
									<select name="CURRENCY" title="投资币种类型">
									<c:forEach items="${ico_investCoinType}" var="investCoinType">
										<option value="${investCoinType.code_value }" <c:if test="${investCoinType.code_value == pd.CURRENCY }">selected</c:if>>${investCoinType.code_name }</option>
									</c:forEach>
									</select>
								</td>
								<%-- <td><input type="text" name="CURRENCY" id="CURRENCY" value="${pd.CURRENCY}" maxlength="255" placeholder="这里输入货币种类" title="货币种类" style="width:98%;"/></td>
						 --%>	</tr>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单号:</td>
								<td><input type="text" name="ORDID" id="ORDID" value="${pd.ORDID}" maxlength="47" placeholder="这里输入订单号" title="订单号" style="width:98%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单时间:</td>
								<td><input type="text" name="ORDDATE" id="ORDDATE" value="${pd.ORDDATE}" maxlength="32" placeholder="这里输入订单时间" title="订单时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单号:</td>
								<td><input type="text" name="ORDER_NO" id="ORDER_NO" value="${pd.ORDER_NO}" maxlength="40" placeholder="这里输入订单号" title="订单号" style="width:98%;"/></td>
							</tr>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input type="text" name="CREATE_TIME" id="CREATE_TIME" value="${pd.CREATE_TIME}" maxlength="19" placeholder="这里输入创建时间" title="创建时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">修改时间:</td>
								<td><input type="text" name="UPDATE_TIME" id="UPDATE_TIME" value="${pd.UPDATE_TIME}" maxlength="19" placeholder="这里输入修改时间" title="修改时间" style="width:98%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">操作用户id:</td>
								<td><input type="text" name="BY_USER_ID" id="BY_USER_ID" value="${pd.BY_USER_ID}" maxlength="32" placeholder="这里输入操作用户id" title="操作用户id" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">矿工费:</td>
								<td><input type="text" name="FREE" id="FREE" value="${pd.FREE}" maxlength="17" placeholder="这里输入矿工费" title="矿工费" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">交易hash:</td>
								<td><input type="text" name="TX_HASH" id="TX_HASH" value="${pd.TX_HASH}" maxlength="255" placeholder="这里输入交易hash" title="交易hash" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">地址:</td>
								<td><input type="text" name="ADDRESS" id="ADDRESS" value="${pd.ADDRESS}" maxlength="255" placeholder="这里输入" title="" style="width:98%;"/></td>
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
			/* if($("#USER_ID").val()==""){
				$("#USER_ID").tips({
					side:3,
		            msg:'请输入用户id',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USER_ID").focus();
			return false;
			}
			if($("#CODE").val()==""){
				$("#CODE").tips({
					side:3,
		            msg:'请输入编号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CODE").focus();
			return false;
			}
			if($("#MONEY").val()==""){
				$("#MONEY").tips({
					side:3,
		            msg:'请输入充值金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MONEY").focus();
			return false;
			}
			if($("#REMAINDER_MONEY").val()==""){
				$("#REMAINDER_MONEY").tips({
					side:3,
		            msg:'请输入账户余额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REMAINDER_MONEY").focus();
			return false;
			}
			if($("#STATUS").val()==""){
				$("#STATUS").tips({
					side:3,
		            msg:'请输入状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STATUS").focus();
			return false;
			}
			if($("#CURRENCY").val()==""){
				$("#CURRENCY").tips({
					side:3,
		            msg:'请输入货币种类',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CURRENCY").focus();
			return false;
			}
			if($("#ORDID").val()==""){
				$("#ORDID").tips({
					side:3,
		            msg:'请输入订单号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORDID").focus();
			return false;
			}
			if($("#ORDDATE").val()==""){
				$("#ORDDATE").tips({
					side:3,
		            msg:'请输入订单时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORDDATE").focus();
			return false;
			} */
			if($("#ORDER_NO").val()==""){
				$("#ORDER_NO").tips({
					side:3,
		            msg:'请输入订单号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORDER_NO").focus();
			return false;
			}
			/* if($("#CREATE_TIME").val()==""){
				$("#CREATE_TIME").tips({
					side:3,
		            msg:'请输入创建时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CREATE_TIME").focus();
			return false;
			}
			if($("#UPDATE_TIME").val()==""){
				$("#UPDATE_TIME").tips({
					side:3,
		            msg:'请输入修改时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#UPDATE_TIME").focus();
			return false;
			}
			if($("#BY_USER_ID").val()==""){
				$("#BY_USER_ID").tips({
					side:3,
		            msg:'请输入操作用户id',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BY_USER_ID").focus();
			return false;
			}
			if($("#FREE").val()==""){
				$("#FREE").tips({
					side:3,
		            msg:'请输入矿工费',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#FREE").focus();
			return false;
			}
			if($("#TX_HASH").val()==""){
				$("#TX_HASH").tips({
					side:3,
		            msg:'请输入交易hash',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TX_HASH").focus();
			return false;
			}
			if($("#ADDRESS").val()==""){
				$("#ADDRESS").tips({
					side:3,
		            msg:'请输入',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ADDRESS").focus();
			return false;
			} */
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