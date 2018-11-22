<#include "/commn/meta.ftl">
<title>组件设计</title>
</head>
<body>
<#include "/commn/head.ftl">
<#include "/commn/menu.ftl">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/editCharts.css" />
<script type="text/javascript" src="${request.contextPath}/static/lib/layer/2.4/layer.js"></script>

<section class="Hui-article-box">
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 图表编辑 <span class="c-gray en">&gt;</span> Table设计 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
	
	<!--iframe显示框-->
	<div class="boxLeft">
		<div style="margin-top:20px;">搜索框没有图形显示</div>
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
		<input type="hidden" id="searchItem" name="searchItem" value="">
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
					<p>添加Item</p>
					<input class="btn btn-primary radius" type="button" value="添加item" onClick="addItem();">
				</td>
				<td>
					<p>选择要编辑的item</p>
					<select id="editItem" name="editItem" class="select radius input" onchange="changeForm();">
				    </select>
				</td>
				<td>
					<p>暴露关联字段(dataSearch)</p>
					<input class="btn btn-primary radius" type="button" value="选择列" onClick="selectSearch();">
				</td>
			</tr>
			
		</table>
	</form>

    <!--search 的 item 编辑 form-->
	<div class="searchForm">
		<div>search标签</div>
		<div>
			<input class="btn btn-primary radius" type="button" value="保存search标签" onClick="saveTag();">
			<input type="hidden" id="tag_type" name="tag_type" class="input-text radius input" value="">
		</div>
		<!--编辑数字类型-->
		<table id="numTable" class="bottomTable" style="display: none">
			<tr>
				<td>
					<p>标签名(num_name)</p>
					<input type="text" id="num_name" name="num_name" class="input-text radius input" value="">
				</td>
				<td>
					<p>计算类型(num_cal)</p>
					<select id="num_cal" name="num_cal" class="select radius input">
					  <option value="0">大于</option>
				      <option value="1">大于等于</option>
					  <option value="2">小于</option>
					  <option value="3">小于等于</option>
					  <option value="4">等于</option>
					  <option value="5">不等于</option>
				    </select>
				</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>

        <!--编辑字符串类型-->
		<table id="strTable" class="bottomTable" style="display: none">
			<tr>
				<td>
					<p>标签名(str_name)</p>
					<input type="text" id="str_name" name="str_name" class="input-text radius input" value="">
				</td>
				<td>
					<p>类型(str_type)</p>
					<select id="str_type" name="str_type" class="select radius input">
					  <option value="0">精确匹配</option>
				      <option value="1">单选</option>
					  <option value="2">复选</option>
				    </select>
				</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>

        <!--编辑日期类型-->
		<table id="dateTable" class="bottomTable" style="display: none">
			<tr>
				<td>
					<p>标签名(name)</p>
					<input type="text" id="date_name" name="date_name" class="input-text radius input" value="">
				</td>
				<td>
					<p>日期类型(date_type)</p>
					<select id="date_type" name="date_type" class="select radius input">
					  <option value="0">日</option>
				      <option value="1">周</option>
					  <option value="2">月</option>
					  <option value="3">年</option>
				    </select>
				</td>
				<td>
					<p>相对时间(time_type)</p>
					<select id="time_type" name="time_type" class="select radius input">
					  <option value="0">相对时间</option>
				      <option value="1">绝对时间</option>
				    </select>
				</td>
				<td>
					<p>日期区间(from_type)</p>
					<select id="from_type" name="from_type" class="select radius input">
					  <option value="0">日期区间</option>
				      <option value="1">日期</option>
				    </select>
				</td>
				<td>
					<p>时间偏移量(-1过去时间，1未来时间)</p>
					<input type="text" id="time_from" name="time_from" class="input-text radius input" value="">
					-
					<input type="text" id="time_to" name="time_to" class="input-text radius input" value="">
				</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
		
	</div>

	</div>

	</div>
</section>


<#include "/commn/foot.ftl">

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${request.contextPath}/static/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
var searchJson = {};  // 全局对象  search 的 searchJson；
$(function(){
	//   显示图表
    var chartId = ${mCharts.id!}; 
    //$("#iframe").attr("src","${edit_url}" + chartId);
    
    // 填充数据
    var config = ${mCharts.config!};
    $("#name").val(config.name);
    $("#type").val(config.type);
    $("#dataSetName").val(config.dataSetName);
    $("#dataSearch").val(config.dataSearch);
    $("#searchItem").val(config.searchItem);
    $("#width").val(config.width);
    $("#padding").val(config.padding);
    searchJson = config.searchJson;
    
    // 选择要编辑的item
    selectItem();
});


//  选择要编辑的item
function selectItem(){
	var searchItem = $("#searchItem").val();
	var searchlist = {};
	$.ajax({
		url: "${request.contextPath}/api/edit/getColumnsByIdList",
	    type:"post",
	    async: false,
	    data:{"idList":searchItem},
	    success:function(data){
	    	searchlist = data.list;
	    }
	});
	$("#editItem").append("<option  value="+""+">--</option>");
	for(var i = 0; i < searchlist.length; i++){
		var value = searchlist[i].id;
		var content = searchlist[i].rsc_display;
		$("#editItem").append("<option  value=" + value + ">" + content + "</option>");
	}
}

//  根据 item 的select  选择要展示的form内容
function changeForm(){
	var id = $("#editItem").val();
	var rsColumnConf = {};
	$.ajax({
		url: "${request.contextPath}/api/edit/getColumnsById",
	    type:"post",
	    async: false,
	    data:{"id":id},
	    success:function(data){
	    	rsColumnConf = data.rsColumnConf;
	    }
	});
	var config = ${mCharts.config!};
	var searchJson = config.searchJson;
	if(rsColumnConf.rsc_type == "1"){
		// 数字
		$("#tag_type").val(rsColumnConf.rsc_type);
		$("#strTable").css('display', 'none');
		$("#dateTable").css('display', 'none');
		$("#numTable").css('display', 'block');
		$("#num_name").val(searchJson[id].name);
		$("#num_cal").val(searchJson[id].cal);
	}else if(rsColumnConf.rsc_type == "2"){
		// 字符
		$("#tag_type").val(rsColumnConf.rsc_type);
		$("#numTable").css('display', 'none');
		$("#dateTable").css('display', 'none');
        $("#strTable").css('display', 'block');
        $("#str_name").val(searchJson[id].name);
        $("#str_type").val(searchJson[id].str_type);
	}else if(rsColumnConf.rsc_type == "3"){
		// 日期
		$("#tag_type").val(rsColumnConf.rsc_type);
		$("#strTable").css('display', 'none');
		$("#numTable").css('display', 'none');
		$("#dateTable").css('display', 'block');
		$("#date_name").val(searchJson[id].name);
        $("#date_type").val(searchJson[id].date_type);
        $("#time_type").val(searchJson[id].time_type);
        $("#from_type").val(searchJson[id].from_type);
        $("#time_from").val(searchJson[id].time_from);
        $("#time_to").val(searchJson[id].ntime_toame);
	}
}

// 保存 编辑的 search 标签
function saveTag(){
	var id = $("#editItem").val();
	var type = $("#tag_type").val();
	if(type == "1"){
		var name = $("#num_name").val();
		var cal = $("#num_cal").val();
		var obj = {};
		obj.id = id;
		obj.type = type;
		obj.name = name;
		obj.cal = cal;
		//var json = JSON.parse(searchJson);
		//json[id] = obj;
		//searchJson = JSON.stringify(json);
		searchJson[id] = obj;
	}else if(type == "2"){
		var name = $("#str_name").val();
		var str_type = $("#str_type").val();
		var obj = {};
		obj.id = id;
		obj.type = type;
		obj.name = name;
		obj.str_type = str_type;
		searchJson[id] = obj;
	}else if(type == "3"){
		var name = $("#date_name").val();
        var date_type = $("#date_type").val();
        var time_type = $("#time_type").val();
        var from_type = $("#from_type").val();
        var time_from = $("#time_from").val();
        var time_to = $("#time_to").val();
		var obj = {};
		obj.id = id;
		obj.type = type;
		obj.name = name;
		obj.date_type = date_type;
		obj.time_type = time_type;
		obj.from_type = from_type;
		obj.time_from = time_from;
		obj.time_to = time_to;
		searchJson[id] = obj;
	}
	// 调用最外层的保存
	this.save();
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
	var searchItem = $("#searchItem").val();
	config.searchItem = searchItem;
	config.searchJson = searchJson;

	var param = JSON.stringify(config);
	$.ajax({
		url:"${request.contextPath}/api/edit/update",
		type:"post",
		async:false,
		data:{"id":"${mCharts.id!}","config":param},
		success:function(data){
		  
		},
		error:function(data){
		  alert("保存失败");
		}
	});
	// 刷新页面
	window.location.reload();
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

// 选择seacrch的item
function addItem(){
	var ds_name = $("#dataSetName").val();
	layer.open({
      type: 2,
      title: '添加item',
      shadeClose: true,
      shade: false,
      maxmin: true, //开启最大化最小化按钮
      area: ['893px', '600px'],
      content: '${request.contextPath}/edit/addItem?ds_name=' + ds_name + ''
    });
}


//  新建chart
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
	var searchItem = "";
	config.searchItem = searchItem;
	var width = "";
	var padding = "";
	config.padding = padding;
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
	// 刷新页面
	window.location.reload();
}

</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>