<#include "/commn/meta.ftl">
<title>组件列表</title>
</head>
<body>
<#include "/commn/head.ftl">
<#include "/commn/menu.ftl">


<section class="Hui-article-box">
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 图表编辑 <span class="c-gray en">&gt;</span> 组件列表 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<div class="text-c"> 
				<input type="text" class="input-text radius" style="width:250px" placeholder="dashboard的id输入" id="boardId" value="">
				<button type="submit" class="btn btn-success radius" id="btnSearch"><i class="Hui-iconfont">&#xe665;</i> 检索</button>
				<button type="submit" class="btn btn-success radius" id="btnRest"><i class="Hui-iconfont">&#xe68f;</i> 重置</button>
			</div>
			<div class="cl pd-5 bg-1 bk-gray mt-20">
				<span class="l" style="margin-left:3px;">
				  <a href="" id="btnRefresh" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe66c;</i></a>
				</span>
			</div>
			<div class="mt-10">
			<table id="listTable" class="table table-border table-bordered table-bg">
				<thead>
					<tr class="text-c">
					
						<th width="" style="text-align: center!important;">id</th>
						<th width="" style="text-align: center!important;">dashboard_id</th>
						<th style="text-align: center!important;">名称</th>
						<th width="30%" style="text-align: center!important;">config</th>
						<th style="text-align: center!important;">创建时间</th>
						<th style="text-align: center!important;">修改时间</th>
						<th style="text-align: center!important;">操作</th>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
			</table>
			</div>
			
		</article>
	</div>
</section>


<#include "/commn/foot.ftl">

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${request.contextPath}/static/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">

$(function(){
		var oTable = $('#listTable').dataTable({
			"ajax": {
				url: '${request.contextPath}/api/edit/getChartList',
				type:"post",
				async: true,
				data: {
				   "boardId": $('#boardId').val()
				},
				dataSrc: 'data'
			},
			"columns": [
				{
					"data": "id",
					"className":"text-c",
				},
				{
					"data": "dashboard_id",
					"className":"text-c",
				},
				{
					"data": "name",
					"className":"text-c",
				},
				{
					"data": "config",
					"className":"text-c",
				},
				{
					"data": "create_date",
					"className":"text-c",
				},
				{
					"data": "modify_date",
					"className":"text-c",
				},
				{
					"data": null,
					"className":"text-c",
					"render": function(data, type, row, meta) {
						var opt = "";
						//操作选项
						opt = "<a title=\"编辑\" href=\"${request.contextPath}/edit/chart?id=" + data.id + "&dashboardId=" + data.dashboard_id  + "\" id=\"btnEdit\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6df;</i></a>";
						opt = opt +"<a title=\"删除\" href=\"javascript:void(0);\" onclick=\"chartDelete('"+data.id+"');\"" + " id=\"btnAppy\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6e2;</i></a>";
						return opt;
					}
				}
			],
			"sScrollX": true,
			"sScrollXInner": "100%",
			"searching": false,
			"info": false,
			"paging": true,		//表格分页
			"bAutoWidth": true,		//自动宽度
			"fnFooterCallback": function(nFoot, aData, iStart, iEnd, aiDisplay) {
        	}
		});
		
		//检索
		$('#btnSearch').on('click', function() {
		  var param = {
	        "boardId": $('#boardId').val()
	      };
	       oTable.fnSettings().ajax.data = param;
	       oTable.api().ajax.reload(); 
		});
		
		//重置
		$('#btnRest').on('click', function() {
		   $('#boardId').val("");
		});
	})

//  删除
function chartDelete(){
  alert("暂时不能删除");
}

</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>