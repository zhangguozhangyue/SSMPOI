<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>
<link rel="Favicon Icon" href="${pageContext.request.contextPath }/images/favicon.ico">
<link rel="stylesheet" type="text/css" href="${ctx}/jquery-easyui-1.5.5.4/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx}/jquery-easyui-1.5.5.4/themes/icon.css">
<script type="text/javascript" src="${ctx}/jquery-easyui-1.5.5.4/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/jquery-easyui-1.5.5.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/jquery-easyui-1.5.5.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
  
 function searchStudent(){
	 $("#dg").datagrid('load',
			 {
		"university":encodeURI($("#s_universityName").val())
	 });
 }
 
 function exportStudent(){
	 var name=encodeURI($("#s_studentName").val());
	 window.location.href="${ctx}/student/excel/export.do?search="+name;
 }

 function openUploadFileDialog(){  
     $("#dlg2").dialog('open').dialog('setTitle','excel批量导入数据');  
 }
 
 function uploadExcel(){
     $("#uploadForm").form("submit",{  
         success:function(result){  
             var result=eval('('+result+')');
             if(result.code!=0){  
                 $.messager.alert("系统提示",result.msg);  
             }else{  
                 $.messager.alert("系统提示","上传成功");  
                 $("#dlg2").dialog("close");  
                 $("#dg").datagrid("reload");  
             }  
         }  
     });  
 }
 
 function deleteStudent(){
	 var row = $('#dg').datagrid('getSelected');
	 //alert(row.id);
	 if(row==null || row.id==null || row.id<=0){
		 alert("请选中一行!!");
		 return;
	 }
	 if(confirm("是否删除??")){
		 var postData={id:row.id};
		 $.ajax({
			type: "POST",
		    url: '${ctx}/student/delete.do',         
		    data: JSON.stringify(postData),
		    contentType : "application/json",
		    dataType: "json",
		    success: function (data) {
		        alert(data.msg);
				location.reload(true);
		    }
		});
	 }
	 return false;
 }
 function updateStudent(){
	 var row = $('#dg').datagrid('getSelected');
	 //alert(row.id);
	 if(row==null || row.id==null || row.id<=0){
		 alert("请选中一行!");
		 return;
	 }
	 $('#dlg').dialog('open').dialog('center').dialog('setTitle','修改');
     $('#fm').form('clear');
     
     $('#id').textbox('setValue',row.id);
     $('#sex').textbox('setValue',row.sex);
     $('#university').textbox('setValue',row.university);
     $('#year').textbox('setValue',row.year);
     $('#jvalue').textbox('setValue',row.jvalue);
	 return false;
 }
 function newStudent(){
     $('#dlg').dialog('open').dialog('center').dialog('setTitle','新增');
     $('#fm').form('clear');
 }
 
 function saveStudent(){
	 if(!$('#fm').form('validate')) return;
	 var id=$('input[name="id"]').val();
     var sex=$('input[name="sex"]').val();
     var university=$('input[name="university"]').val();
     var year=$('input[name="year"]').val();
     var jvalue=$('input[name="jvalue"]').val();

     var postData={id:id,sex:sex,university:university,year:year,jvalue:jvalue};
	 $.ajax({
		type: "POST",
	    url: '${ctx}/student/insert/or/update.do',         
	    data: JSON.stringify(postData),
	    contentType : "application/json",
	    dataType: "json",
	    success: function (data) {
	        alert(data.msg);
	        if(data.code==0){
		        $('#dlg').dialog('close');
	            $('#dg').datagrid('reload');
				location.reload(true);
	        }
	    }
	});
 }

</script>
<title>信息统计分析系统</title>
</head>
<body style="margin: 1px">
   <table id="dg" title="学生信息列表" class="easyui-datagrid" fitColumns="true" pagination="true" rownumbers="true" 
 		fit="true" toolbar="#tb" pageSize="25"" pageList="[25,50,100,200]" data-options="singleSelect:true,collapsible:true,url:'${ctx}/student/list.do',method:'get'" >
   <thead>
   	<tr>
   		<th field="cb" checkbox="true" align="center"></th>
   		<th data-options="field:'id',width:50" id=>编号</th>
   		<th data-options="field:'sex',width:50">性别</th>
   		<th data-options="field:'university',width:50">大学</th>
   		<th data-options="field:'year',width:50">入学年</th>
   		<th data-options="field:'jvalue',width:50">J值</th>
   	</tr>
   </thead>
 </table>
 <div id="tb">
 	<div>
 		 学生信息： <input type="text" id="s_universityName" size="20" onkeydown="if(event.keyCode==13) searchStudent()"/>
 		<a href="javascript:searchStudent()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 		<a href="javascript:exportStudent()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">导出excel</a>
 		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openUploadFileDialog()">导入excel</a> 
 		<a href="javascript:deleteStudent()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
 		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newStudent()">新增</a>
 		<a href="javascript:updateStudent()" class="easyui-linkbutton" iconCls="icon-save" plain="true">修改</a>
 		<a href="echarts.jsp" class="easyui-linkbutton" iconCls="icon-large-chart" plain="true">数据统计</a>
 	</div>
 </div>
 
 
 
<div id="dlg2" class="easyui-dialog" style="width:400px;height:180px;padding:10px 20px" closed="true" buttons="#dlg-buttons2">  
    <form id="uploadForm" action="${ctx}/student/excel/upload.do" method="post" enctype="multipart/form-data" >  
        <table>  
            <tr>  
                <td>上传文件：</td>  
                <td><input type="file" name="productFile"></td>  
            </tr>  
        </table>  
    </form>  
</div>  
  
<div id="dlg-buttons2">  
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="uploadExcel()">上传excel</a>  
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close')">关闭</a>  
</div>
<div id="dlg" class="easyui-dialog" style="width:400px" data-options="closed:true,modal:true,border:'thin',buttons:'#dlg-buttons'">
        <form id="fm" method="post" novalidate style="margin:0;padding:20px 50px">
        <div style="margin-bottom:10px;display:none;">
                <input id="id" name="id" class="easyui-textbox" label="id:" style="width:100%">
            </div>
            <div style="margin-bottom:10px">
                <input id="sex" name="sex" class="easyui-textbox" required="true" label="性别:" style="width:100%">
            </div>
            <div style="margin-bottom:10px">
                <input id="university" name="university" class="easyui-textbox" required="true" label="大学:" style="width:100%">
            </div>
            <div style="margin-bottom:10px">
                <input id="year" name="year" class="easyui-textbox" type="number"  required="true" validType="number" label="入学年:" style="width:100%">
            </div>
            <div style="margin-bottom:10px">
                <input id="jvalue" name="jvalue" class="easyui-textbox" type="number" validType="number" label="J值:" style="width:100%">
            </div>
        </form>
    </div>
        <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveStudent()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">取消</a>
    </div>
</body>
</html>