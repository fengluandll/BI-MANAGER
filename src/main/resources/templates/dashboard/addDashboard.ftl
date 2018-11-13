<#include "/commn/meta.ftl">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/editCharts.css" />
<script type="text/javascript" src="${request.contextPath}/static/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${request.contextPath}/static/lib/layer/2.4/layer.js"></script>
</head>
<body>
<div class="Hui-article">
<article class="cl pd-20">
	<form  method="post" class="form form-horizontal" id="thisForm">
	<div class="row cl">
		<label class="form-label col-xs-4 col-sm-3"><span class="c-red">名称*</span></label>
		<div class="formControls col-xs-8 col-sm-9">
			<input type="text" class="input-text" value="" id="name" name="name">
		</div>
	</div>
	</form>
	
	<div class="row cl">
		<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
			<a href="javascript:void(0);"  onclick="save();" id="add" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i>保存</a>
		</div>
	</div>

	
</article>
</div>
<script type="text/javascript">
$(function(){
   
});

//  保存
function save(){
  var name = $("#name").val();
  $.ajax({
		url:"${request.contextPath}/api/mDashboard/saveDashboard",
		type:"post",
		async:false,
		data:{"name":name},
		success:function(data){
		  
		},
		error:function(data){
		  alert("新建失败");
		}
	});
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}

</script>
</body>
</html>