<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" import="java.util.*" %>
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
					
					<form action="crowdfundingdetail/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="crowdfunding_detail_id" id="crowdfunding_detail_id" value="${pd.crowdfunding_detail_id}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<input type="hidden" name="PROJECT_ID" id="PROJECT_ID" value="${pd.PROJECT_ID}" maxlength="32" placeholder="这里输入参与锁定众筹项目ID" title="参与锁定众筹项目ID" style="width:98%;"/>
							<tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">项目开始时间:</td>
								<td>
								<input  readonly="readonly" type="text" name="STARTDATE" id="STARTDATE" value="${STARTDATE}" type="text"
								  readonly="readonly"  style="width:80%;"/>
								</td>
							</tr>
							
							<tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">锁定众筹项目开始时间:</td>
								<td>
								<input  type="hidden" name="NOWTIME" id="NOWTIME" value="<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text"
								 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" placeholder="开始时间" title="开始时间" style="width:80%;"/>
								
								<input  name="LOCK_PROJECT_START_TIME" id="LOCK_PROJECT_START_TIME" value="<fmt:formatDate value="${pd.LOCK_PROJECT_START_TIME}" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text"
								 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'NOWTIME\')}',maxDate:'#F{$dp.$D(\'STARTDATE\')}'})" readonly="readonly" placeholder="开始时间" title="开始时间" style="width:98%;"/>
								</td>
							</tr>
							<tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">锁定众筹项目结束时间:</td>
								<td>
								
								
								<input  name="LOCK_PROJECT_END_TIME" id="LOCK_PROJECT_END_TIME" value="<fmt:formatDate value="${pd.LOCK_PROJECT_END_TIME}" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text"
								 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'LOCK_PROJECT_START_TIME\')}',maxDate:'#F{$dp.$D(\'STARTDATE\')}'})" readonly="readonly" placeholder="结束时间" title="结束时间" style="width:98%;"/></td>
							</tr>
							<%-- <tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">目标锁定金额:</td>
								<td><input type="number"     name="TARGET_LOCK_AMOUNT" id="TARGET_LOCK_AMOUNT" value="${pd.TARGET_LOCK_AMOUNT}"  placeholder="这里输入目标锁定金额" title="目标锁定金额" style="width:98%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">锁定最大可投金额:</td>
								<td><input type="number"  readonly="readonly" name="LOCK_MAX_AMOUNT" id="LOCK_MAX_AMOUNT" value="${pd.LOCK_MAX_AMOUNT}"  placeholder="这里输入锁定最大可投金额，默认是1" title="锁定最大可投金额，默认是1" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">锁定最小可投金额:</td>
								<td><input type="number"  max="999999" min="0.0001" name="LOCK_MIN_AMOUNT" id="LOCK_MIN_AMOUNT" value="${pd.LOCK_MIN_AMOUNT}"  placeholder="这里输入锁定最小可投金额，默认是0.01" title="锁定最小可投金额，默认是0.01" style="width:98%;"/></td>
							</tr>
							<%-- <tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">实际锁定金额:</td>
								<td><input type="number"  max="999999" min="0.0001" name="ACTUAL_LOCK_AMOUNT" id="ACTUAL_LOCK_AMOUNT" value="${pd.ACTUAL_LOCK_AMOUNT}"  placeholder="这里输入实际锁定金额" title="实际锁定金额" style="width:98%;"/></td>
							</tr> --%>
							<%-- <tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">实际锁定金额所占比例:</td>
								<td><input type="text" name="ACTUAL_LOCK_RATE" id="ACTUAL_LOCK_RATE" value="${pd.ACTUAL_LOCK_RATE}" maxlength="12" placeholder="这里输入实际锁定金额所占比例" title="实际锁定金额所占比例" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:155px;text-align: right;padding-top: 13px;">项目投资回报率(%):</td>
								<td><input type="text" name="LOCK_BACK_RATE" id="LOCK_BACK_RATE" value="${pd.LOCK_BACK_RATE}" maxlength="12" placeholder="这里输入项目投资回报率" title="项目投资回报率" style="width:98%;"/></td>
							</tr> --%>
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
			/* if($("#PROJECT_ID").val()==""){
				$("#PROJECT_ID").tips({
					side:3,
		            msg:'请输入参与锁定众筹项目ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PROJECT_ID").focus();
			return false;
			}*/
			if($("#LOCK_PROJECT_START_TIME").val()==""){
				$("#LOCK_PROJECT_START_TIME").tips({
					side:3,
		            msg:'请输入锁定众筹项目开始时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOCK_PROJECT_START_TIME").focus();
			return false;
			}
			if($("#LOCK_PROJECT_END_TIME").val()==""){
				$("#LOCK_PROJECT_END_TIME").tips({
					side:3,
		            msg:'请输入锁定众筹项目结束时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOCK_PROJECT_END_TIME").focus();
			return false;
			}
			if($("#TARGET_LOCK_AMOUNT").val()==""||$("#TARGET_LOCK_AMOUNT").val()<0.0001||$("#TARGET_LOCK_AMOUNT").val()>999999.9999){
				$("#TARGET_LOCK_AMOUNT").tips({
					side:3,
		            msg:'请输入目标锁定金额，最小值为0.0001,最大为999999.9999',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TARGET_LOCK_AMOUNT").focus();
			return false;
			}
			if($("#LOCK_MAX_AMOUNT").val()==""||$("#LOCK_MAX_AMOUNT").val()<0.0001||$("#LOCK_MAX_AMOUNT").val()>999999.9999){
				$("#LOCK_MAX_AMOUNT").tips({
					side:3,
		            msg:'请输入锁定最大可投金额，最小值为0.0001,最大为999999.9999',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOCK_MAX_AMOUNT").focus();
			return false;
			}
			if($("#LOCK_MIN_AMOUNT").val()==""||$("#LOCK_MIN_AMOUNT").val()<0.0001||$("#LOCK_MIN_AMOUNT").val()>999999.9999){
				$("#LOCK_MIN_AMOUNT").tips({
					side:3,
		            msg:'请输入锁定最小可投金额，最小值为0.0001,最大为999999.9999',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOCK_MIN_AMOUNT").focus();
			return false;
			}
			if(parseFloat($("#LOCK_MIN_AMOUNT").val())>=parseFloat($("#LOCK_MAX_AMOUNT").val())){
				$("#LOCK_MIN_AMOUNT").tips({
					side:3,
		            msg:'请输入锁定最小可投金额不能"大于等于"锁定最大金额!',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOCK_MIN_AMOUNT").focus();
			return false;
			}
			/* if($("#ACTUAL_LOCK_AMOUNT").val()==""){
				$("#ACTUAL_LOCK_AMOUNT").tips({
					side:3,
		            msg:'请输入实际锁定金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ACTUAL_LOCK_AMOUNT").focus();
			return false;
			}
			if($("#ACTUAL_LOCK_RATE").val()==""){
				$("#ACTUAL_LOCK_RATE").tips({
					side:3,
		            msg:'请输入实际锁定金额所占比例',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ACTUAL_LOCK_RATE").focus();
			return false;
			}
			if($("#CREATE_TIME").val()==""){
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
		            msg:'请输入更新时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#UPDATE_TIME").focus();
			return false;
			}
			if($("#EXT1").val()==""){
				$("#EXT1").tips({
					side:3,
		            msg:'请输入备用字段',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#EXT1").focus();
			return false;
			}
			if($("#LOCK_BACK_RATE").val()==""){
				$("#LOCK_BACK_RATE").tips({
					side:3,
		            msg:'请输入项目投资回报率',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOCK_BACK_RATE").focus();
			return false;
			}  */
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