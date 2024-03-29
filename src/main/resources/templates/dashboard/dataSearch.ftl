<#include "/commn/meta.ftl">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/editCharts.css" />
<script type="text/javascript" src="${request.contextPath}/static/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${request.contextPath}/static/lib/layer/2.4/layer.js"></script>
</head>
<body>
<div class="Hui-article">

<div>
	<input class="btn btn-primary radius" type="button" value="保存" onClick="saveColumn();">
	填入暴露字段的id
	<input type="text" id="dataSearch" name="dataSearch" class="input-text radius input" value="">
</div>
<table class="columnTable">
	<thead>
		<tr>
		    <td>类型</td>
			<td>类型</td>
			<td>名称</td>
			<td>字段名</td>
			<td>类型</td>
		</tr>
	</thead>
	<tbody>
		<#list dimension as list>
		  <tr>
		    <td>${list.id?c}</td>
		  	<td>${list.rsc_category}</td>
		  	<td>${list.rsc_display}</td>
		  	<td>${list.rsc_name}</td>
		  	<td>${list.rsc_type}</td>
		  </tr>
		</#list>
	</tbody>
</table>
</div>
<script type="text/javascript">
$(function(){
   $('#dataSearch').val(parent.$('#dataSearch').val());
});

function saveColumn(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.$("#dataSearch").val($("#dataSearch").val());
	parent.layer.close(index);

}

</script>
</body>
</html>