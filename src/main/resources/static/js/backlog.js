layui.use(['form', 'layer','table', 'jquery', 'laytpl','laypage'], function () {
    var table = layui.table,
        layer = layui.layer,
        laypage = layui.laypage;

    renderDate(1);

    function renderDate(num){
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
        table.render({
            elem:'#demo',
            page:false,
            cols: [[
                {field:'deptName','title':'部门'},
                {field:'username','title':'姓名'},
                {field:'workNo','title':'考勤编号'},
                {field:'amount','title':'金额'},
                {field:'type','title':'类型',templet:function (t) {
                    if(t.type == 0){
                        return "借款";
                    }
                    if(t.type == 1){
                        return "其他扣款";
                    }
                }},
                {field:'taskDate','title':'日期'},
                {field:'note','title':'备注'}
            ]],
            skin: 'row',
            even: true,
            url: "task/list.json",
            method:"post",
            where:{
                "curPage":num
            },
            text:{
                none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            },
            response: {
                statusCode: 200 //成功的状态码，默认：0
            },
            done:function(res, curr, count) {
                var page=res.page;
                laypage.render({
                    elem: 'page',
                    limit:page.pageSize,
                    count: page.totalCount, //数据总数
                    curr: page.currentPage,
                    layout: ['count', 'prev', 'page', 'next', 'skip'],
                    jump: function (obj, first) {
                        if (!first) {
                            renderDate(obj.curr);
                        }
                    }
                });
            }
            //,size: 'lg' //尺寸
        });
        layer.close(cxindex)
    }
});