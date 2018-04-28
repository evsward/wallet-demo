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
	<!-- 编辑器bootstrap-wysiwyg -->
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
					
					<form action="news/${msg }.do" name="Form" id="Form" method="post"  enctype="multipart/form-data">
						<input type="hidden" name="id" id="id" value="${pd.id}"/>
						<input type="hidden" name="RICH_TEXT" id="RICH_TEXT" value="${pd.RICH_TEXT}" maxlength="300" placeholder="这里输入富文本路径" title="富文本路径" style="width:98%;"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
						<input type="hidden" name="CONTENT" id="CONTENT"  />
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标题:</td>
								<td><input type="text" name="TITLE" id="TITLE" value="${pd.TITLE}" maxlength="255" placeholder="这里输入标题" title="标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">副标题:</td>
								<td><input type="text" name="SUBTITLE" id="SUBTITLE" value="${pd.SUBTITLE}" maxlength="255" placeholder="这里输入副标题" title="副标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标题图片:</td>
								<!-- <td>
								<input type="file" id="PICTURE_URL" name="PICTURE_URL" onchange="fileType(this)"/>
								</td> -->
								
								<td>
										<c:if test="${pd == null || pd.PICTURE_URL == '' || pd.PICTURE_URL == null }">
										<input type="file" id="PICTURE_URL" name="PICTURE_URL" onchange="fileType(this)"/>
										</c:if>
										<c:if test="${pd != null && pd.PICTURE_URL != '' && pd.PICTURE_URL != null }">
											<a href="${pd.PICTURE_URL}" target="_blank"><img src="${pd.PICTURE_URL}" width="210"/></a>
											<input type="button" class="btn btn-mini btn-danger" value="删除" onclick="delP('${pd.PICTURE_URL}','${pd.id }');" />
										</c:if>
									</td>
								
							</tr>
							<tr>
								<td style="width:95px;text-align: right;padding-top: 13px;">新闻内容:</td>
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
											<div id="editor" class="lead" data-placeholder="">${pd.CONTENT}</div>
										</div>
									</td>
							</tr>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">富文本路径:</td>
								<td><input type="text" name="RICH_TEXT" id="RICH_TEXT" value="${pd.RICH_TEXT}" maxlength="255" placeholder="这里输入富文本路径" title="富文本路径" style="width:98%;"/></td>
							</tr> --%>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">浏览数:</td>
								<td><input type="number" name="BROWSE_COUNT" id="BROWSE_COUNT" value="${pd.BROWSE_COUNT}" maxlength="32" placeholder="这里输入浏览数" title="浏览数" style="width:98%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">新闻类型:</td>
								<td>
								<select name="NEWS_TYPE" title="新闻类型">
											<c:forEach items="${newsTypes}" var="newsType">
												<option value="${newsType.code_value }" <c:if test="${newsType.code_value == pd.NEWS_TYPE }">selected</c:if>>${newsType.description }</option>
											</c:forEach>
								</select>
								<%-- <input type="text" name="NEWS_TYPE" id="NEWS_TYPE" value="${pd.NEWS_TYPE}" maxlength="64" placeholder="这里输入新闻类型" title="新闻类型" style="width:98%;"/>
								 --%></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标签:</td>
								<td><input type="text" name="LABEL" id="LABEL" value="${pd.LABEL}" maxlength="64" placeholder="这里输入标签" title="标签" style="width:98%;"/></td>
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
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 上传控件 -->
	<script src="static/ace/js/ace/elements.fileinput.js"></script>
		<script type="text/javascript">
		
		//删除图片
		function delP(PICTURE_URL,id){
			 if(confirm("确定要删除图片？")){
				var url = "news/editPictureUrl.do?PICTURE_URL="+PICTURE_URL+"&id="+id+"&guid="+new Date().getTime();
				$.get(url,function(data){
					if(data=="success"){
						alert("删除成功!");
						document.location.reload();
					}
				});
			} 
		}
		//过滤类型
		
		$(top.hangge());
		$(function() {
			//上传
			$('#PICTURE_URL').ace_file_input({
				no_file:'请选择图片 ...',
				btn_choose:'选择',
				btn_change:'更改',
				droppable:false,
				onchange:null,
				thumbnail:false, //| true | large
				whitelist:'gif|png|jpg|jpeg',
				//blacklist:'gif|png|jpg|jpeg'
				//onchange:''
				//
			});
		});
		
		function fileType(obj){
			var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		    if(fileType != '.gif' && fileType != '.png' && fileType != '.jpg' && fileType != '.jpeg'){
		    	$("#PICTURE_URL").tips({
					side:3,
		            msg:'请上传图片格式的文件',
		            bg:'#AE81FF',
		            time:3
		        });
		    	$("#PICTURE_URL").val('');
		    	document.getElementById("PICTURE_URL").files[0] = '请选择图片';
		    }
		}
		
		//保存
		function save(){
			$("#CONTENT").val($("#editor").html()); 
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
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