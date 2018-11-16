<%@page import="cn.zg.model.YearCount"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<link rel="Favicon Icon" href="${pageContext.request.contextPath }/images/favicon.ico">
<title>信息统计分析系统</title>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<link rel="stylesheet" type="text/css"
	href="${ctx}/jquery-easyui-1.5.5.4/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/jquery-easyui-1.5.5.4/themes/icon.css">
<script type="text/javascript"
	src="${ctx}/jquery-easyui-1.5.5.4/jquery.min.js"></script>
<script type="text/javascript"
	src="${ctx}/jquery-easyui-1.5.5.4/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/jquery-easyui-1.5.5.4/locale/easyui-lang-zh_CN.js"></script>

<!-- 引入 echarts.js -->
<script src="js/echarts.min.js"></script>
<a href="student.jsp" class="easyui-linkbutton" iconCls="icon-undo"
	plain="true">返回</a>
</head>


<body style="margin: 1px">
	<table border="1">
		<tr>
			<td>
				<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
				<div id="main1" style="width: 940px; height: 500px;"></div> <script
					type="text/javascript">
					// 基于准备好的dom，初始化echarts实例

					var myChart = echarts
							.init(document.getElementById('main1'));

					//从数据库读取数据赋值给echarts
					var dataArray = [];
					var legendDate = [];
					$.ajax({
						async : false,
						cache : false,
						url : '${ctx}/student/year.do',
						contentType : 'application/json',
						type : 'GET',
						dataType : 'json',
						success : function(data) {
							for (i = 0; i < data.length; i++) {
								var dataObj = {
									value : 0,
									name : ''
								}
								dataObj.name = data[i].year;
								dataObj.value = data[i].count;
								dataArray[i] = dataObj;
								legendDate[i] = data[i].year;
							}
						}
					});

					option = {
						title : {
							text : '各入学年份占比统计',
							subtext : ' ',
							x : 'center'
						},
						tooltip : {
							trigger : 'item',
							formatter : "{a} <br/>{b} : {c} ({d}%)"
						},
						legend : {
							orient : 'vertical',
							left : 'left',
							data : legendDate
						},
						series : [ {
							name : '各年入学人数占比',
							type : 'pie',
							radius : '55%',
							center : [ '50%', '50%' ],
							data : dataArray,
							itemStyle : {
								emphasis : {
									shadowBlur : 10,
									shadowOffsetX : 0,
									shadowColor : 'rgba(0, 0, 0, 0.5)'
								}
							}
						} ]
					};
					myChart.setOption(option);
				</script>

			</td>

			<td>
				<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
				<div id="main2" style="width: 940px; height: 500px;"></div> <script
					type="text/javascript">
					var myChart = echarts
							.init(document.getElementById('main2'));
                   //从数据库读取数据
					var legendData = [];
					var seriesData = [];
					var selected = {};
					$.ajax({
						async : false,
						cache : false,
						url : '${ctx}/student/university.do',
						contentType : 'application/json',
						type : 'GET',
						dataType : 'json',
						success : function(data) {
							for (i = 0; i < data.length; i++) {
								var dataObj = {
									value : 0,
									name : ''
								}
								dataObj.name = data[i].university;
								dataObj.value = data[i].count;
								seriesData[i] = dataObj;
								legendData[i] = data[i].university;
								selected[legendData[i]] = i < 6;
							}
						}
					});
					
					option = {
						title : {
							text : '各大高校人数占比统计',
							subtext : ' ',
							x : 'center'
						},
						tooltip : {
							trigger : 'item',
							formatter : "{a} <br/>{b} : {c} ({d}%)"
						},
						legend : {
							type : 'scroll',
							orient : 'vertical',
							right : 10,
							top : 20,
							bottom : 20,
							data : legendData,

							selected : selected
						},
						series : [ {
							name : '高校入学人数',
							type : 'pie',
							radius : '55%',
							center : [ '40%', '50%' ],
							data : seriesData,
							itemStyle : {
								emphasis : {
									shadowBlur : 10,
									shadowOffsetX : 0,
									shadowColor : 'rgba(0, 0, 0, 0.5)'
								}
							}
						} ]
					};
					
					myChart.setOption(option);
				</script>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
				<div id="main3" ></div> <script
					type="text/javascript">
				/**
					var myChart = echarts
							.init(document.getElementById('main3'));
					option = {
						title : {
							text : '入学人数总和前十各年份入学详细情况',
							subtext : ' ',
							x : ''
						},

						tooltip : {
							trigger : 'axis',
							axisPointer : { // 坐标轴指示器，坐标轴触发有效
								type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend : {
							data : [ '直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎' ]
						},
						grid : {
							left : '3%',
							right : '4%',
							bottom : '3%',
							containLabel : true
						},
						xAxis : {
							type : 'value'
						},
						yAxis : {
							type : 'category',
							data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
						},
						series : [ {
							name : '直接访问',
							type : 'bar',
							stack : '总量',
							label : {
								normal : {
									show : true,
									position : 'insideRight'
								}
							},
							data : [ 320, 302, 301, 334, 390, 330, 320 ]
						}, {
							name : '邮件营销',
							type : 'bar',
							stack : '总量',
							label : {
								normal : {
									show : true,
									position : 'insideRight'
								}
							},
							data : [ 120, 132, 101, 134, 90, 230, 210 ]
						}, {
							name : '联盟广告',
							type : 'bar',
							stack : '总量',
							label : {
								normal : {
									show : true,
									position : 'insideRight'
								}
							},
							data : [ 220, 182, 191, 234, 290, 330, 310 ]
						}, {
							name : '视频广告',
							type : 'bar',
							stack : '总量',
							label : {
								normal : {
									show : true,
									position : 'insideRight'
								}
							},
							data : [ 150, 212, 201, 154, 190, 330, 410 ]
						}, {
							name : '搜索引擎',
							type : 'bar',
							stack : '总量',
							label : {
								normal : {
									show : true,
									position : 'insideRight'
								}
							},
							data : [ 820, 832, 901, 934, 1290, 1330, 1320 ]
						} ]
					}
					myChart.setOption(option);
					**/
				</script>
			</td>
           
		</tr>
	</table>
	 <table id="dg" title="大学统计表" class="easyui-datagrid" fitColumns="true" pagination="true" rownumbers="true" 
 		fit="true" toolbar="#tb" pageSize=25 pageList="[25,50,100,200]" data-options="singleSelect:true,collapsible:true,url:'${ctx}/student/university.do',method:'get'" >
   <thead>
   	<tr>
   		<th data-options="field:'university',width:50">大学</th>
   		<th data-options="field:'count',width:50">数量</th>
   	</tr>
   </thead>
   </table>

</body>
</html>
