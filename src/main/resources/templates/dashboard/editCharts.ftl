<#include "/commn/meta.ftl">
<title>组件设计</title>
</head>
<body>
<#include "/commn/head.ftl">
<#include "/commn/menu.ftl">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/editCharts.css" />

<section class="Hui-article-box">
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 图表编辑 <span class="c-gray en">&gt;</span> Charts设计 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
	
	<!--iframe显示框-->
	<div class="boxLeft">
		<iframe id="iframe" src="" width="100%" height="100%" margin="5px;"></iframe>
	</div>
	
	<!--右侧组件list显示框-->
	<div class="boxRight">
	   <div class="rightTitle">
	   	  <p>${mdashboard.name!} 的组件列表</p>
	   	  <button class="btn btn-primary radius" onClick="newChart('0');">新建Chart</button>
	   	  <button class="btn btn-primary radius" onClick="newChart('3');">新建Table</button>
	   	  <button class="btn btn-primary radius" onClick="newChart('11');">新建Search</button>
	   </div>
	   <div class="rightList">
	   	  <ul>
	   	  	  <#list mChartsList as list>
	   	  	     <li ><p onclick="jumpToAnother('${list.id!}','${list.dashboard_id!}');">${list.name!}</p></li>
	   	  	  </#list>
	   	  </ul>
	   </div>
	</div>
	
	<!--下侧form编辑框-->
	<div class="boxBottom">
	 <div>组件名称： ${mCharts.name!}</div>
	<form>
		<input type="hidden" id="path" name="path" value="${request.contextPath}">
		<input type="hidden" id="dimension" name="dimension" value="">
		<input type="hidden" id="measure" name="measure" value="">
		<input type="hidden" id="color" name="color" value="">
		<input type="hidden" id="dataSearch" name="dataSearch" value="">
		<div>
			<input class="btn btn-primary radius" type="button" value="保存" onClick="save();">
		</div>
		<table class="bottomTable">
			<tr>
				<td>
					<p>名称(name)</p>
					<input type="text" id="name" name="name" class="input-text radius input" value="">
				</td>
				<td>
					<p>类型(type)</p>
					<select id="type" name="type" class="select radius input">
					  <option value="0">折线图</option>
				      <option value="1">柱状图</option>
					  <option value="2">饼图</option>
					  <option value="3">交叉表</option>
					  <option value="11">搜索框</option>
				    </select>
				</td>
				<td>
					<p>数据集名称(dataSetName)</p>
					<select id="dataSetName" name="dataSetName" class="select radius input">
					  <#list rsTableConfList as list>
				      <option value="${list.ds_name}">${list.ds_display}</option>
					  </#list>
				    </select>
				</td>
				<td>
					<p>暴露关联字段(dataSearch)</p>
				    <input class="btn btn-primary radius" type="button" value="选择" onClick="selectSearch();">
				</td>
				<td>
					<p>选择维度、度量、图例</p>
					<input class="btn btn-primary radius" type="button" value="选择" onClick="selectColumn();">
				</td>
				<td>
					<p></p>
					<input type="text" id="" name="" class="input-text radius input" value="">
				</td>
			</tr>
			<tr>
			    <td>
					<p>宽度(width)</p>
					<input type="text" id="width" name="width" class="input-text radius input" value="">
				</td>
				<td>
					<p>高度(height)</p>
					<input type="text" id="height" name="height" class="input-text radius input" value="">
				</td>
				<td>
					<p>边距(padding)</p>
					<input type="text" id="padding" name="padding" class="input-text radius input" value="">
				</td>
				<td>
					<p>自适应(forceFit)</p>
					<select id="forceFit" name="forceFit" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
				<td>
					<p>背景(background)</p>
					<select id="background" name="background" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
				<td>
					<p>点击背景(plotBackground)</p>
					<select id="plotBackground" name="plotBackground" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
			</tr>
			<tr>
				<td>
					<p>图例(legend)</p>
					<select id="legend" name="legend" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
				<td>
					<p>提示(tooltip)</p>
					<select id="tooltip" name="tooltip" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
				<td>
					<p>辅助文本(guide)</p>
					<select id="guide" name="guide" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
				<td>
					<p>动画(animate)</p>
					<select id="animate" name="animate" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
				<td>
					<p>坐标轴(axis)</p>
					<select id="axis" name="axis" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
			</tr>
			<tr>
				<td>
					<p>显示头部(head)</p>
					<select id="head" name="head" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
				<td>
					<p>显示标题(title)</p>
					<select id="title" name="title" class="select radius input">
					  <option value="0">不显示</option>
				      <option value="1">显示</option>
				    </select>
				</td>
				<td>
					<p></p>
					<input type="text" id="guide" name="guide" class="input-text radius input" value="">
				</td>
				<td>
					<p></p>
					<input type="text" id="" name="" class="input-text radius input" value="">
				</td>
				<td>
					<p></p>
					<input type="text" id="" name="" class="input-text radius input" value="">
				</td>
				<td>
					<p></p>
					<input type="text" id="" name="" class="input-text radius input" value="">
				</td>
			</tr>
		</table>
	</form>
	</div>
	
	</div>
</section>


<#include "/commn/foot.ftl">

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${request.contextPath}/static/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
$(function(){
	//   显示图表
    var chartId = ${mCharts.id!}; 
    $("#iframe").attr("src","http://localhost:8000/smart_report/editCharts/" + chartId);
    
    // 填充数据
    var config = ${mCharts.config!};
    $("#name").val(config.name);
    $("#type").val(config.type);
    $("#dataSetName").val(config.dataSetName);
    $("#dataSearch").val(config.dataSearch);
    $("#height").val(config.height);
    $("#width").val(config.width);
    $("#padding").val(config.padding);
    $("#forceFit").val(config.forceFit);
    $("#background").val(config.background);
    $("#animate").val(config.animate);
    $("#legend").val(config.legend);
    $("#tooltip").val(config.tooltip);
    $("#guide").val(config.guide);
    $("#head").val(config.head);
    $("#title").val(config.title);
    // 配置 隐藏字段
    $("#dimension").val(config.dimension);
    $("#measure").val(config.measure);
    $("#color").val(config.color);
});

//  dataSearch暴露字段回填
function dataSearch(){
	// table id
	var config = ${mCharts.config!};
	var ds_name = $("#dataSetName").val();
	var searchlist = {};
	$.ajax({
		url: "${request.contextPath}/api/edit/getColumns",
	    type:"post",
	    async: false,
	    data:{"ds_name":ds_name},
	    success:function(data){
	    	searchlist = data.list;
	    }
	});
	$("#dataSearch").append("<option  value="+""+">--</option>");
	for(var i = 0; i < searchlist.length; i++){
		var rsc_display = searchlist[i].rsc_display;
		var id = searchlist[i].id;
		$("#dataSearch").append("<option  value=" + id + ">" + rsc_display + "</option>");
	}
	$("#dataSearch").val(config.dataSearch);
}

// 跳转 其他组件
function jumpToAnother(id , dashboard_id){
	window.location.href = "${request.contextPath}/edit/chart?id=" + id + "&dashboardId=" + dashboard_id + "";
}
// 保存
function save(){
	var config = {};
	var name = $("#name").val();
	config.name = name;
	var type = $("#type").val();
	config.type = type;
	var dataSetName = $("#dataSetName").val();
	config.dataSetName = dataSetName;
	var dataSearch = $("#dataSearch").val();
	config.dataSearch = dataSearch;
	var width = $("#width").val();
	config.width = width;
	var height = $("#height").val();
	config.height = height;
	var padding = $("#padding").val();
	config.padding = padding;
	var forceFit = $("#forceFit").val();
	config.forceFit = forceFit;
	var background = $("#background").val();
	config.background = background;
	var plotBackground = $("#plotBackground").val();
	config.plotBackground = plotBackground;
	var animate = $("#animate").val();
	config.animate = animate;
	var axis = $("#axis").val();
	config.axis = axis;
	var legend = $("#legend").val();
	config.legend = legend;
	var tooltip = $("#tooltip").val();
	config.tooltip = tooltip;
	var guide = $("#guide").val();
	config.guide = guide;
	var dimension = $("#dimension").val();
	config.dimension = dimension;
	var measure = $("#measure").val();
	config.measure = measure;
	var color = $("#color").val();
	config.color = color;

	var head = $("#head").val();
	config.head = head;
	var title = $("#title").val();
	config.title = title;

	var param = JSON.stringify(config);
	$.ajax({
		url:"${request.contextPath}/api/edit/update",
		type:"post",
		async:false,
		data:{"id":"${mCharts.id!}","config":param},
		success:function(data){
		  alert("保存成功");
		  alert(param);
		},
		error:function(data){
		  alert("保存失败");
		}
	});
	
}

//  选择图表的 维度 度量 图例
function selectColumn(){
  var ds_name = $("#dataSetName").val();
  layer.open({
      type: 2,
      title: '选择图表的维度、度量、图例',
      shadeClose: true,
      shade: false,
      maxmin: true, //开启最大化最小化按钮
      area: ['893px', '600px'],
      content: '${request.contextPath}/edit/chartColumn?ds_name=' + ds_name + ''
    });
}

// 选择图表的暴露字段
function selectSearch(){
	var ds_name = $("#dataSetName").val();
	layer.open({
      type: 2,
      title: '选择暴露字段',
      shadeClose: true,
      shade: false,
      maxmin: true, //开启最大化最小化按钮
      area: ['893px', '600px'],
      content: '${request.contextPath}/edit/dataSearch?ds_name=' + ds_name + ''
    });
}

function newChart(type){
	var config = {};
	var name = "";
	config.name = name;
    // type 为参数
	config.type = type;
	var dataSetName = "";
	config.dataSetName = dataSetName;
	var dataSearch = "";
	config.dataSearch = dataSearch;
	var width = "";
	config.width = width;
	var height = "";
	config.height = height;
	var padding = "";
	config.padding = padding;
	var forceFit = "";
	config.forceFit = forceFit;
	var background = "";
	config.background = background;
	var plotBackground = "";
	config.plotBackground = plotBackground;
	var animate = "";
	config.animate = animate;
	var axis = "";
	config.axis = axis;
	var legend = "";
	config.legend = legend;
	var tooltip = "";
	config.tooltip = tooltip;
	var guide = "";
	config.guide = guide;
	var dimension = "";
	config.dimension = dimension;
	var measure = "";
	config.measure = measure;
	var color = "";
	config.color = color;

	var head = "";
	config.head = head;
	var title = "";
	config.title = title;
	
	var searchJson = {};
	config.searchJson = searchJson;

	var param = JSON.stringify(config);
	$.ajax({
		url:"${request.contextPath}/api/edit/newChart",
		type:"post",
		async:false,
		data:{"id":"${dashboardId!}","config":param},
		success:function(data){
			if(data.success == "success"){
				alert("新建成功");
			}else{
				alert("新建失败");
			} 
		},
		error:function(data){
		  alert("新建失败");
		}
	});
}

</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>