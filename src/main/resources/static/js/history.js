layui.use(['laydate','table','jquery', 'layer','laypage'],function () {
    var laydate = layui.laydate,
        laypage = layui.laypage,
        layer = layui.layer,
        $ = layui.jquery;
    laydate.render({
        elem: '#dateMonth',
        type:'month',
//            position: 'static',
//            showBottom: false,
        btns: ['clear', 'confirm','now'],
        theme: 'molv',
        done: function(value, date, endDate){ //选择日期完毕的回调
            renderDate(1,date.year,date.month);
        }
    });//绑定元素

    renderDate(1);

    function renderDate(curPage,year,month) {
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
        $.ajax({
            url:"salary/history.json",
            type:"post",
            data:{"year":year,"month":month,"curPage":curPage},
            success:function (result) {
                var code = result.code,
                    listData = result.data,
                    page = result.page;
                if (code == 200) {
                    //渲染数据
                    var dataHtml = '';
                    if (listData.length != 0) {
                        $.each(listData,function (i, item) {
                            var fine=item.late+item.otherDebit+item.partyDue+item.loan+item.other;
                            dataHtml+="<tr><td>"+item.workNo+"</td><td>"+item.deptName+"</td><td>"+item.username+"</td><td>"+item.base+"</td><td>"+item.dailyWage+
                                "<td>"+item.workTotal+"</td><td>"+item.seniority+"</td><td>"+item.subWork+"</td><td>"+item.allowance+"</td><td>"+item.grossPay+
                                "</td><td>"+item.insurance+"</td><td>"+item.incomeTax+"</td></td><td>"+item.bonus+"</td><td>"+fine+"</td><td>"+item.payroll+"</td><td>"+item.creditCard+ "</td></tr>";
                        })
                    }else {
                        dataHtml = '<tr><td colspan="16"><div class="btn-btn">暂无数据</div></td></tr>';
                    }

                    $(".news_content").html(dataHtml);
                    //                        $("#curDate").attr("title",year+"-"+month);
                    //分页
                    laypage.render({
                        elem: 'page',
                        limit:page.pageSize,
                        count: page.totalCount, //数据总数
                        curr: page.currentPage,
                        layout: ['count', 'prev', 'page', 'next', 'skip'],
                        jump: function (obj, first) {
                            if (!first) {
                                renderDate(obj.curr,year,month);
                            }
                        }
                    })
                }else{
                    layer.close(cxindex);
                    var msg = result.msg;
                    if (msg) {
                        lay.alert(msg);
                    } else {
                        lay.alert('查询数据失败');
                    }
                }
            },
            error: function(data){
                layer.close(cxindex);
                alert("获取数据失败");
            }
        });
        layer.close(cxindex);
    }
});