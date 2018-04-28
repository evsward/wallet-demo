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
	<!-- 编辑器bootstrap-wysiwyg -->
<!-- 	<link href="static/ace/css/bootstrap.min.css" rel="stylesheet"> -->
	<link href="static/ace/css/prettify.css" rel="stylesheet">
	<link href="static/ace/css/font-awesome.css" rel="stylesheet">
	<link href="static/ace/css/style.css" rel="stylesheet">
	
	<script src="static/weixin/jquery.min.js" type="text/javascript"></script>
	<script src="static/login/js/bootstrap.min.js"></script>
	<script src="static/ace/js/prettify.js"></script>
	<script src="static/ace/js/bootstrap-wysiwyg.js" type="text/javascript"></script>
	<script src="static/ace/js/jquery.hotkeys.js" type="text/javascript"></script>
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
					<form action="crowdfunding/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="PROJECT_ID" id="PROJECT_ID" value="${pd.PROJECT_ID}"/>
						<input type="hidden" name="RICH_TEXT" id="RICH_TEXT" value="${pd.RICH_TEXT}" maxlength="300" placeholder="这里输入富文本路径" title="富文本路径" style="width:80%;"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							
								
								<input type="hidden" name="USERID" id="USERID" value="${pd.USERID}" maxlength="32" placeholder="这里输入用户id" title="用户id" style="width:80%;"/>
								<input type="hidden" name="PROJECT_DESC_DETAIL" id="PROJECT_DESC_DETAIL"  />
							
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">项目名:</td>
								<td><input type="text" name="PROJECT_NAME" id="PROJECT_NAME" value="${pd.PROJECT_NAME}" maxlength="40" placeholder="这里输入项目名" title="项目名" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">所属阶段:</td>
								<%-- <td><input type="number" name="PROJECT_STAGE" id="PROJECT_STAGE" value="${pd.PROJECT_STAGE}" maxlength="32" placeholder="这里输入项目所属阶段" title="项目所属阶段" style="width:80%;"/></td>
								 --%><td>
										<select name="PROJECT_STAGE" title="项目所属阶段">
											<c:forEach items="${ico_projectStage}" var="projectStage">
												<option value="${projectStage.code_value }" <c:if test="${projectStage.code_value == pd.PROJECT_STAGE }">selected</c:if>>${projectStage.description }</option>
											</c:forEach>
										<%-- <option value="1" <c:if test="${pd.PROJECT_STAGE == '1' }">selected</c:if> >开始</option>
										<option value="2" <c:if test="${pd.PROJECT_STAGE == '2' }">selected</c:if> >结束</option>
										<option value="3" <c:if test="${pd.PROJECT_STAGE == '3' }">selected</c:if> >进行时</option>
										 --%></select>
									</td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">公司名称:</td>
								<td><input type="text" name="COMPANY_NAME" id="COMPANY_NAME" value="${pd.COMPANY_NAME}" maxlength="20" placeholder="这里输入公司名称" title="公司名称" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">所在地区:</td>
								<td>
								<select name="OFFICE_ADDRESS" title="办公地址编码">
									<c:forEach items="${ico_officeAddress}" var="officeAddress">
										<option value="${officeAddress.code_value }" <c:if test="${officeAddress.code_value == pd.OFFICE_ADDRESS }">selected</c:if>>${officeAddress.code_name }</option>
									</c:forEach>
								</select>
								<%-- <input type="text" name="OFFICE_ADDRESS" id="OFFICE_ADDRESS" value="${pd.OFFICE_ADDRESS}" maxlength="255" placeholder="这里输入办公地址编码" title="办公地址编码" style="width:80%;"/>
								 --%>
								</td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">详细地址:</td>
								<td><input type="text" name="OFFICE_DETAIL" id="OFFICE_DETAIL" value="${pd.OFFICE_DETAIL}" maxlength="255" placeholder="这里输入办公地址详情" title="办公地址详情" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">团队人数:</td>
								<td><input type="number" name="TEAM_COUNT" id="TEAM_COUNT" value="${pd.TEAM_COUNT}" maxlength="32" placeholder="这里输入团队人数" title="团队人数" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">融资阶段:</td>
								<%-- <td><input type="number" name="IPO_STAGE" id="IPO_STAGE" value="${pd.IPO_STAGE}" maxlength="32" placeholder="这里输入融资阶段" title="融资阶段" style="width:80%;"/></td> --%>
								<td>
									<select name="IPO_STAGE" title="融资阶段">
										<c:forEach items="${ico_ipoStage}" var="ipoStage">
											<option value="${ipoStage.code_value }" <c:if test="${ipoStage.code_value == pd.IPO_STAGE }">selected</c:if>>${ipoStage.code_name }</option>
										</c:forEach>
									<%-- <option value="1" <c:if test="${pd.IPO_STAGE == '1' }">selected</c:if> >A轮</option>
									<option value="2" <c:if test="${pd.IPO_STAGE == '2' }">selected</c:if> >B轮</option>
									<option value="3" <c:if test="${pd.IPO_STAGE == '3' }">selected</c:if> >C轮</option>
									<option value="4" <c:if test="${pd.IPO_STAGE == '4' }">selected</c:if> >未融资</option> --%>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">项目介紹:</td>
								<td><input type="text" name="PROJECT_DESC" id="PROJECT_DESC" value="${pd.PROJECT_DESC}" maxlength="2000" placeholder="这里输入項目介紹" title="項目介紹" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">项目详情:</td>
								<td>
								  <div class="container">
										<div class="jumbotron">
											<div id="alerts"></div>
											<div class="btn-toolbar" data-role="editor-toolbar" data-target="#editor">
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle" data-toggle="dropdown" title="Font"><i class="fa fa-font"></i><b class="caret"></b>
														</a>
													<ul class="dropdown-menu">
													</ul>
												</div>
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle" data-toggle="dropdown" title="Font Size"><i class="fa fa-text-height"></i>&nbsp;<b
															class="caret"></b>
														</a>
													<ul class="dropdown-menu">
														<li><a data-edit="fontSize 5" class="fs-Five">Huge</a></li>
														<li><a data-edit="fontSize 3" class="fs-Three">Normal</a></li>
														<li><a data-edit="fontSize 1" class="fs-One">Small</a></li>
													</ul>
												</div>
												<div class="btn-group">
													<a class="btn btn-default" data-edit="bold" title="Bold (Ctrl/Cmd+B)"><i class="fa fa-bold"></i></a>
													<a class="btn btn-default" data-edit="italic" title="Italic (Ctrl/Cmd+I)"><i class="fa fa-italic"></i></a>
													<a class="btn btn-default" data-edit="strikethrough" title="Strikethrough"><i class="fa fa-strikethrough"></i></a>
													<a class="btn btn-default" data-edit="underline" title="Underline (Ctrl/Cmd+U)"><i class="fa fa-underline"></i></a>
												</div>
												<div class="btn-group">
													<a class="btn btn-default" data-edit="insertunorderedlist" title="Bullet list"><i class="fa fa-list-ul"></i></a>
													<a class="btn btn-default" data-edit="insertorderedlist" title="Number list"><i class="fa fa-list-ol"></i></a>
													<a class="btn btn-default" data-edit="outdent" title="Reduce indent (Shift+Tab)"><i class="fa fa-outdent"></i></a>
													<a class="btn btn-default" data-edit="indent" title="Indent (Tab)"><i class="fa fa-indent"></i></a>
												</div>
												<div class="btn-group">
													<a class="btn btn-default" data-edit="justifyleft" title="Align Left (Ctrl/Cmd+L)"><i class="fa fa-align-left"></i></a>
													<a class="btn btn-default" data-edit="justifycenter" title="Center (Ctrl/Cmd+E)"><i class="fa fa-align-center"></i></a>
													<a class="btn btn-default" data-edit="justifyright" title="Align Right (Ctrl/Cmd+R)"><i class="fa fa-align-right"></i></a>
													<a class="btn btn-default" data-edit="justifyfull" title="Justify (Ctrl/Cmd+J)"><i class="fa fa-align-justify"></i></a>
												</div>
												<div class="btn-group">
													<a class="btn btn-default dropdown-toggle" data-toggle="dropdown" title="Hyperlink"><i class="fa fa-link"></i></a>
													<div class="dropdown-menu input-append">
														<input placeholder="URL" type="text" data-edit="createLink" />
														<button class="btn" type="button">Add</button>
													</div>
												</div>
												<div class="btn-group">
													<a class="btn btn-default" data-edit="unlink" title="Remove Hyperlink"><i class="fa fa-unlink"></i></a>
													<span class="btn btn-default" title="Insert picture (or just drag & drop)" id="pictureBtn"> <i class="fa fa-picture-o"></i>
															<input class="imgUpload" type="file" data-role="magic-overlay" data-target="#pictureBtn" data-edit="insertImage" />
														</span>
												</div>
												<div class="btn-group">
													<a class="btn btn-default" data-edit="undo" title="Undo (Ctrl/Cmd+Z)"><i class="fa fa-undo"></i></a>
													<a class="btn btn-default" data-edit="redo" title="Redo (Ctrl/Cmd+Y)"><i class="fa fa-repeat"></i></a>
												</div>
												<input class="pull-right" type="text" data-edit="inserttext" id="voiceBtn" />
											</div>
											<div id="editor" class="lead" data-placeholder="">${pd.PROJECT_DESC_DETAIL}</div>
										</div>
  
<!-- 								<script id="PROJECT_DESC_DETAIL_VALUE" name="PROJECT_DESC_DETAIL_VALUE"  type="text/plain" style="width:643px;height:259px;"> -->
<!-- 									2222 -->
<!-- 								</script> -->
								<%-- <input type="text" name="PROJECT_DESC_DETAIL" id="PROJECT_DESC_DETAIL" value="${pd.PROJECT_DESC_DETAIL}" maxlength="2000" placeholder="这里输入項目介紹" title="項目介紹" style="width:80%;height:300px"/>
								 --%></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">视频地址:</td>
								<td><input type="text" name="PUBLICITY_VIDEO" id="PUBLICITY_VIDEO" value="${pd.PUBLICITY_VIDEO}" maxlength="500" placeholder="这里输入视频地址" title="视频地址" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">封面图片:</td>
								<td>
								<img src="${pd.COVER_PICTURE}" height="100%" width="100%"/>   </td>
								<%-- <input type="text" name="COVER_PICTURE" id="COVER_PICTURE" value="${pd.COVER_PICTURE}" maxlength="500" placeholder="这里输入封面图片" title="封面图片" style="width:80%;"/>
							 --%></tr>
							<%-- <tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">富文本路径:</td>
								<td><input type="text" name="RICH_TEXT" id="RICH_TEXT" value="${pd.RICH_TEXT}" maxlength="300" placeholder="这里输入富文本路径" title="富文本路径" style="width:80%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">投资币种类型:</td>
								<%-- <td><input type="number" name="INVEST_COIN_TYPE" id="INVEST_COIN_TYPE" value="${pd.INVEST_COIN_TYPE}" maxlength="32" placeholder="这里输入投资币种类型(1.BTC 2.ETH 3.LTC)" title="投资币种类型(1.BTC 2.ETH 3.LTC)" style="width:80%;"/></td>
								 --%>
								 <td>
									<select name="INVEST_COIN_TYPE" title="投资币种类型">
									
									<c:forEach items="${ico_investCoinType}" var="investCoinType">
										<option value="${investCoinType.code_value }" <c:if test="${investCoinType.code_value == pd.INVEST_COIN_TYPE }">selected</c:if>>${investCoinType.code_name }</option>
									</c:forEach>
									<%-- <option value="1" <c:if test="${pd.INVEST_COIN_TYPE == '1' }">selected</c:if> >BTC</option>
									<option value="2" <c:if test="${pd.INVEST_COIN_TYPE == '2' }">selected</c:if> >ETH</option>
									<option value="3" <c:if test="${pd.INVEST_COIN_TYPE == '3' }">selected</c:if> >LTC</option> --%>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">是否有限额 :</td>
							<%-- 	<td>
								
								<input type="number" name="INVEST_LIMIT" id="INVEST_LIMIT" value="${pd.INVEST_LIMIT}" maxlength="32" placeholder="这里输入是否有限额 " title="是否有限额 " style="width:80%;"/>
								</td> --%>
								<td>
									<select name="INVEST_COIN_TYPE" title="是否有限额" id="INVEST_LIMIT_SELECT">
									<option value="0" <c:if test="${pd.INVEST_LIMIT == '0' }">selected</c:if> >无限制</option>
									<option value="1" <c:if test="${pd.INVEST_LIMIT == '1' }">selected</c:if> >有限制</option>
									</select>
								</td>
							</tr>
							<tr id="INVEST_AMOUNT_TR">
								<td style="width:300px;text-align: right;padding-top: 13px;"><span id ="INVEST_AMOUNT_COLOR" style="color: black;">投资金额:</span></td>
								<td><input type="text" name="INVEST_AMOUNT" id="INVEST_AMOUNT" value="${pd.INVEST_AMOUNT}" maxlength="17" placeholder="这里输入投资金额" title="投资金额" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">回报说明:</td>
								<td><input type="text" name="REPAY_DESC" id="REPAY_DESC" value="${pd.REPAY_DESC}" maxlength="2000" placeholder="这里输入回报说明" title="回报说明" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">名额限制:</td>
								<%-- <td><input type="number" name="QUOTA_LIMIT" id="QUOTA_LIMIT" value="${pd.QUOTA_LIMIT}" maxlength="32" placeholder="这里输入名额限制" title="名额限制" style="width:80%;"/></td>
							 --%>
							 	<td>
									<select name="QUOTA_LIMIT" title="名额限制">
									<option value="1" <c:if test="${pd.QUOTA_LIMIT == '1' }">selected</c:if> >是</option>
									<option value="0" <c:if test="${pd.QUOTA_LIMIT == '0' }">selected</c:if> >否</option>
									</select>
								</td>
							 </tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">支持限制:</td>
								<%-- <td><input type="number" name="SUPPORT_LIMIT" id="SUPPORT_LIMIT" value="${pd.SUPPORT_LIMIT}" maxlength="32" placeholder="这里输入支持限制" title="支持限制" style="width:80%;"/></td>
							 --%>
							 	<td>
									<select name="QUOTA_LIMISUPPORT_LIMITT" title="支持限制">
									<option value="1" <c:if test="${pd.SUPPORT_LIMIT == '1' }">selected</c:if> >是</option>
									<option value="0" <c:if test="${pd.SUPPORT_LIMIT == '0' }">selected</c:if> >否</option>
									</select>
								</td>
							 </tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">是否包邮:</td>
								<%-- <td><input type="number" name="IS_FREE_DELIVER" id="IS_FREE_DELIVER" value="${pd.IS_FREE_DELIVER}" maxlength="32" placeholder="这里输入是否包邮" title="是否包邮" style="width:80%;"/></td>
								 --%><td>
									<select name="IS_FREE_DELIVER" title="是否包邮">
									<option value="1" <c:if test="${pd.IS_FREE_DELIVER == '1' }">selected</c:if> >大陆包邮</option>
									<option value="0" <c:if test="${pd.IS_FREE_DELIVER == '0' }">selected</c:if> >否</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">回报时间:</td>
								<td><input type="number" name="REPAY_PERIOD" id="REPAY_PERIOD" value="${pd.REPAY_PERIOD}" maxlength="32" placeholder="这里输入回报时间" title="回报时间" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">ico天数:</td>
								<td><input type="number" name="ICO_PERIOD" id="ICO_PERIOD" value="${pd.ICO_PERIOD}" maxlength="32" placeholder="这里输入ico天数" title="ico天数" style="width:80%;"/></td>
							</tr>
							<%-- <tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">是否有限额:</td>
								<td><input type="number" name="TARGET_LIMIT" id="TARGET_LIMIT" value="${pd.TARGET_LIMIT}" maxlength="32" placeholder="这里输入是否有限额(0:无限额 ,1：有限额)" title="是否有限额(0:无限额 ,1：有限额)" style="width:80%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">目标类型:</td>
								<%-- <td><input type="number" name="TARGET_TYPE" id="TARGET_TYPE" value="${pd.TARGET_TYPE}" maxlength="32" placeholder="这里输入目标类型" title="目标类型" style="width:80%;"/></td>
								 --%><td>
									<select name="TARGET_LIMIT" title="是否包邮" id="TARGET_LIMIT_SELECT">
									<option value="1" <c:if test="${pd.TARGET_LIMIT == '1' }">selected</c:if> >有限制</option>
									<option value="0" <c:if test="${pd.TARGET_LIMIT == '0' }">selected</c:if> >无限制</option>
									</select>
								</td>
							</tr>
							<tr id="TARGET_AMOUNT_TR">
								<td style="width:300px;text-align: right;padding-top: 13px;"><span id ="TARGET_AMOUNT_COLOR" style="color: black;">目标金额:</span></td>
								<td><input type="text" name="TARGET_AMOUNT" id="TARGET_AMOUNT" value="${pd.TARGET_AMOUNT}" maxlength="10" placeholder="这里输入目标金额" title="目标金额" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">投资比率:</td>
								<td><input type="text" name="INVEST_RATE" id="INVEST_RATE" value="${pd.INVEST_RATE}" maxlength="12" placeholder="这里输入投资比率" title="投资比率" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">当前总投资个数:</td>
								<td><input type="text" name="INVEST_COUNT" id="INVEST_COUNT" value="${pd.INVEST_COUNT}" maxlength="12" placeholder="这里输入当前总投资多少个" title="当前总投资多少个" style="width:80%;"/></td>
							</tr>
							
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">联系人:</td>
								<td><input type="text" name="CONTACT_NAME" id="CONTACT_NAME" value="${pd.CONTACT_NAME}" maxlength="40" placeholder="这里输入联系人" title="联系人" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">负责人职位:</td>
								<td><input type="text" name="JOB_POSITION" id="JOB_POSITION" value="${pd.JOB_POSITION}" maxlength="40" placeholder="这里输入负责职位" title="负责职位" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">联系电话:</td>
								<td><input type="text" name="CONTACT_TEL" id="CONTACT_TEL" value="${pd.CONTACT_TEL}" maxlength="11" placeholder="这里输入联系电话" title="联系电话" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">开始时间:</td>
								<td>
								<input  type="hidden" name="NOWTIME" id="NOWTIME" value="<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text"
								 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" placeholder="开始时间" title="开始时间" style="width:80%;"/>
								
								<input  name="START_TIME" id="START_TIME" value="${pd.START_TIME}" type="text"
								 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'NOWTIME\')}',maxDate:'#F{$dp.$D(\'END_TIME\')}'})" readonly="readonly" placeholder="开始时间" title="开始时间" style="width:80%;"/>
								</td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">结束时间:</td>
								<td>
								<input  name="END_TIME" id="END_TIME" value="${pd.END_TIME}" type="text"
								 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'START_TIME\')}',maxDate:'2130-10-01'})" readonly="readonly" placeholder="结束时间" title="结束时间" style="width:80%;"/></td>
							</tr>
						<%-- 	<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input class="span10 date-picker" name="CREATE_TIME" id="CREATE_TIME" value="${pd.CREATE_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="创建时间" title="创建时间" style="width:80%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">状态:</td>
								<td>
									<select name="PROJECT_STATE" title="状态" disabled="true">
										<c:forEach items="${ico_projectState}" var="projectState">
											<option value="${projectState.code_value }" <c:if test="${projectState.code_value == pd.PROJECT_STATE }">selected</c:if>>${projectState.code_name }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">最小投资金额:</td>
								<td><input type="text" name="MIN_INVESTMENT_AMOUNT" id="MIN_INVESTMENT_AMOUNT" value="${pd.MIN_INVESTMENT_AMOUNT}" maxlength="17" placeholder="这里输入最小投资金额" title="最小投资金额" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">最大投资金额:</td>
								<td><input type="text" name="MAX_INVESTMENT_AMOUNT" id="MAX_INVESTMENT_AMOUNT" value="${pd.MAX_INVESTMENT_AMOUNT}" maxlength="17" placeholder="这里输入最大投资金额" title="最大投资金额" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">手续费(%):</td>
								<td><input type="number" name="SERVICE_CHARGE" id="SERVICE_CHARGE" value="${pd.SERVICE_CHARGE}" maxlength="17" placeholder="这里输入手续费" title="手续费" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">其他资料:</td>
								<td>
									<c:forEach items="${attachments}" var="attachment">
										<c:if test="${attachment.type == 1 }">
										<a href="${attachment.path} " target="_blank">白皮书</a>
										</c:if>
										<c:if test="${attachment.type == 2 }">
										<a href="${attachment.path} " target="_blank">商业计划书</a>
										</c:if>
										<c:if test="${attachment.type == 3 }">
										<a href="${attachment.path} " target="_blank">其他资料</a>
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">SAN与目标币兑换比例:</td>
								<td><input type="number" name="INVEST_SANTOBTC" id="INVEST_SANTOBTC" value="${pd.INVEST_SANTOBTC}" maxlength="17" placeholder="输入数字" title="输入数字" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">ETH与目标币兑换比例:</td>
								<td><input type="number" name="INVEST_ETHTOBTC" id="INVEST_ETHTOBTC" value="${pd.INVEST_ETHTOBTC}" maxlength="17" placeholder="输入数字" title="输入数字" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">LTC与目标币兑换比例:</td>
								<td><input type="number" name="INVEST_LTCTOBTC" id="INVEST_LTCTOBTC" value="${pd.INVEST_LTCTOBTC}" maxlength="17" placeholder="输入数字" title="输入数字" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="width:300px;text-align: right;padding-top: 13px;">BTC与目标币兑换比例:</td>
								<td><input type="number" name="INVEST_BTCTOBTC" id="INVEST_BTCTOBTC" value="${pd.INVEST_BTCTOBTC}" maxlength="17" placeholder="输入数字" title="输入数字" style="width:80%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<c:if test="${button == 1 }">  <!-- 查看按钮权限 -->
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">关闭页面</a>
									</c:if>
									<c:if test="${button != 1 }">   <!-- 审核按钮权限 -->
										<c:if test="${QX.confirm == 1 }">
										<a class="btn btn-mini btn-primary" onclick="save();">审核通过</a>
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消审核</a>
										</c:if>
										<c:if test="${QX.confirm != 1 }">
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">关闭页面</a>
										</c:if>
									</c:if>
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
	<!-- 编辑框-->
	<script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = "<%=path%>/plugins/ueditor/";</script>
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
	
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(function () {
			function initToolbarBootstrapBindings() {
				var fonts = ['Serif', 'Sans', 'Arial', 'Arial Black', 'Courier',
					'Courier New', 'Comic Sans MS', 'Helvetica', 'Impact', 'Lucida Grande', 'Lucida Sans', 'Tahoma', 'Times',
					'Times New Roman', 'Verdana'],
					fontTarget = $('[title=Font]').siblings('.dropdown-menu');

				$.each(fonts, function (idx, fontName) {
					fontTarget.append($('<li><a data-edit="fontName ' + fontName + '" style="font-family:\'' + fontName + '\'">' + fontName + '</a></li>'));
				});

				$('a[title]').tooltip({ container: 'body' });

				$('.dropdown-menu input').click(function () { return false; })
					.change(function () {
						$(this).parent('.dropdown-menu').siblings('.dropdown-toggle').dropdown('toggle');
					}).keydown('esc', function () {
						this.value = ''; $(this).change();
					});

				$('[data-role=magic-overlay]').each(function () {
					var overlay = $(this), target = $(overlay.data('target'));
					overlay.css('opacity', 0).css('position', 'absolute').offset(target.offset()).width(target.outerWidth()).height(target.outerHeight());
				});

				if ("onwebkitspeechchange" in document.createElement("input")) {
					var editorOffset = $('#editor').offset();
					//$('#voiceBtn').css('position','absolute').offset({top: editorOffset.top, left: editorOffset.left+$('#editor').innerWidth()-35});
				}

				else {
					$('#voiceBtn').hide();
				}
			};

			function showErrorAlert(reason, detail) {
				var msg = '';
				if (reason === 'unsupported-file-type') {
					msg = "Unsupported format " + detail;
				}
				else {
					console.log("error uploading file", reason, detail);
				}

				$('<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>' +
					'<strong>File upload error</strong> ' + msg + ' </div>').prependTo('#alerts');
			};

			initToolbarBootstrapBindings();

			$('#editor').wysiwyg({ fileUploadError: showErrorAlert });

			window.prettyPrint && prettyPrint();
		});
		
		$(top.hangge());
		//var ue = UE.getEditor('PROJECT_DESC_DETAIL_VALUE');
		$(document).ready(function(){
			
			var inputs = $("input");
			inputs.each(function(){
			    $(this).attr("disabled","disabled"); 
			 });
			$("#START_TIME").removeAttr("disabled"); 
			$("#END_TIME").removeAttr("disabled"); 
			$("#PROJECT_ID").removeAttr("disabled"); 
			
			$("#INVEST_ETHTOBTC").removeAttr("disabled"); 
			$("#INVEST_LTCTOBTC").removeAttr("disabled"); 
			$("#INVEST_SANTOBTC").removeAttr("disabled"); 
			$("#INVEST_BTCTOBTC").removeAttr("disabled"); 
			$("#MIN_INVESTMENT_AMOUNT").removeAttr("disabled"); 
			
			var sels = $('select');
			sels.each(function(){
			    $(this).attr("disabled","disabled"); 
			 });
			
			var INVEST_AMOUNT = ${pd.INVEST_AMOUNT};
			var TARGET_AMOUNT = ${pd.TARGET_AMOUNT};
			if(${pd.TARGET_LIMIT}==0){
				 //$("#TARGET_AMOUNT_TR").hide();
				 $("#TARGET_AMOUNT_COLOR").css("color","red");
				 $("#TARGET_AMOUNT").val(999999); 
			}
			
			if(${pd.INVEST_LIMIT}==0){
				//$("#INVEST_AMOUNT_TR").hide();
				$("#INVEST_AMOUNT_COLOR").css("color","red");
				 $("#INVEST_AMOUNT").val(999999); 
			}
			
			$('#TARGET_LIMIT_SELECT').change(function(){ 
				var p1=$(this).children('option:selected').val();
				if(p1==0){
					// $("#TARGET_AMOUNT_TR").hide();  
					$("#TARGET_AMOUNT_COLOR").css("color","red");
					 $("#TARGET_AMOUNT").val(999999); 
				}else{
					//$("#TARGET_AMOUNT_TR").show();  
					$("#TARGET_AMOUNT_COLOR").css("color","black");
					$("#TARGET_AMOUNT").val(TARGET_AMOUNT); 
				}
			}) 
			
			$('#INVEST_LIMIT_SELECT').change(function(){ 
				var p1=$(this).children('option:selected').val();
				if(p1==0){
					 //$("#INVEST_AMOUNT_TR").hide();  
					 $("#INVEST_AMOUNT_COLOR").css("color","red");
					 $("#INVEST_AMOUNT").val(999999); 
				}else{
					//$("#INVEST_AMOUNT_TR").show(); 
					$("#INVEST_AMOUNT_COLOR").css("color","black");
					$("#INVEST_AMOUNT").val(INVEST_AMOUNT); 
				}
			}) 
		}); 
		//保存
		function save(){
			
			if($("#START_TIME").val()==""){
				$("#START_TIME").tips({
					side:3,
		            msg:'请输入开始时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#START_TIME").focus();
			return false;
			}
			if($("#END_TIME").val()==""){
				$("#END_TIME").tips({
					side:3,
		            msg:'请输入结束时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#END_TIME").focus();
			return false;
			}
			
			
			if($("#INVEST_ETHTOBTC").val()==""){
				$("#INVEST_ETHTOBTC").tips({
					side:3,
		            msg:'请输入ETH与目标币兑换量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INVEST_ETHTOBTC").focus();
			return false;
			}
			if($("#INVEST_LTCTOBTC").val()==""){
				$("#INVEST_LTCTOBTC").tips({
					side:3,
					msg:'请输入LTC与目标币兑换量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INVEST_LTCTOBTC").focus();
			return false;
			}
			if($("#INVEST_SANTOBTC").val()==""){
				$("#INVEST_SANTOBTC").tips({
					side:3,
					msg:'请输入SAN与目标币兑换量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INVEST_SANTOBTC").focus();
			return false;
			}
			if($("#INVEST_BTCTOBTC").val()==""){
				$("#INVEST_BTCTOBTC").tips({
					side:3,
					msg:'请输入BTC与目标币兑换量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INVEST_BTCTOBTC").focus();
			return false;
			}
			if(parseFloat($("#MIN_INVESTMENT_AMOUNT").val())>=parseFloat($("#MAX_INVESTMENT_AMOUNT").val())){
				$("#MAX_INVESTMENT_AMOUNT").tips({
					side:3,
					msg:'最小投资金额不能"大于等于"最大投资金额!',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MAX_INVESTMENT_AMOUNT").focus();
			return false;
			}
			
			
				$("#PROJECT_DESC_DETAIL").val($("#editor").html()); 
				/* var inputs = $("input");
				inputs.each(function(){
				    $(this).removeAttr("disabled"); 
				 });
				var sels = $('select');
				sels.each(function(){
				    $(this).attr("disabled"); 
				 }); */
				
				$("#Form").submit();
				$("#zhongxin").hide();
				$("#zhongxin2").show();
			//}
			
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		
		function initToolbarBootstrapBindings() {
		      var fonts = ['Serif', 'Sans', 'Arial', 'Arial Black', 'Courier', 
		            'Courier New', 'Comic Sans MS', 'Helvetica', 'Impact', 'Lucida Grande', 'Lucida Sans', 'Tahoma', 'Times',
		            'Times New Roman', 'Verdana'],
		            fontTarget = $('[title=Font]').siblings('.dropdown-menu');
		      $.each(fonts, function (idx, fontName) {
		          fontTarget.append($('<li><a data-edit="fontName ' + fontName +'" style="font-family:\''+ fontName +'\'">'+fontName + '</a></li>'));
		      });
		      $('a[title]').tooltip({container:'body'});
		    	$('.dropdown-menu input').click(function() {return false;})
				    .change(function () {$(this).parent('.dropdown-menu').siblings('.dropdown-toggle').dropdown('toggle');})
		        .keydown('esc', function () {this.value='';$(this).change();});
		 
		      $('[data-role=magic-overlay]').each(function () { 
		        var overlay = $(this), target = $(overlay.data('target')); 
		        overlay.css('opacity', 0).css('position', 'absolute').offset(target.offset()).width(target.outerWidth()).height(target.outerHeight());
		      });
		      if ("onwebkitspeechchange"  in document.createElement("input")) {
		        var editorOffset = $('#editor').offset();
		        $('#voiceBtn').css('position','absolute').offset({top: editorOffset.top, left: editorOffset.left+$('#editor').innerWidth()-35});
		      } else {
		        $('#voiceBtn').hide();
		      }
			};
			function showErrorAlert (reason, detail) {
				var msg='';
				if (reason==='unsupported-file-type') { msg = "Unsupported format " +detail; }
				else {
					console.log("error uploading file", reason, detail);
				}
				$('<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>'+ 
				 '<strong>File upload error</strong> '+msg+' </div>').prependTo('#alerts');
			};
		    initToolbarBootstrapBindings(); 
			$('#editor').wysiwyg({ fileUploadError: showErrorAlert} );
		</script>
</body>
</html>