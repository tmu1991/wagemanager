layui.use(['form', 'layer','table', 'jquery', 'laypage'], function () {
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
                {field:'username','title':'操作员'},
                {field:'startTime','title':'登录时间'},
                {field:'operName','title':'操作账户'},
                {field:'operation','title':'操作事项'},
                {field:'args','title':'操作明细'},
                {field: 'createTime','title':'操作时间'}
            ]],
            skin: 'row',
            even: true,
            url: "log/list.json",
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