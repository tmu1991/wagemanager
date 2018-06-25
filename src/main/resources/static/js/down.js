layui.use(['layer','table', 'jquery', 'laydate'], function () {
    var table = layui.table,
        layer = layui.layer,
        laydate = layui.laydate,
        $ = layui.jquery;
    laydate.render({
        elem: '#dateMonth',
        type:'month',
//            position: 'static',
//            showBottom: false,
        btns: ['clear', 'confirm','now'],
        theme: 'molv',
        done: function(value, date, endDate){ //选择日期完毕的回调
            renderDate(date.year,date.month);
        }
    });//绑定元素
    //加载页面数据
    renderDate();
    //查看审批进度
    $("body").on("click", ".chakan", function () {
        var _this = $(this);
        $('.commentList').html('');
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post('declare/comment.json',{'processInstanceId':_this.attr("data-id")},function (result) {
            var code = result.code,
                listData = result.data;
            if (code == 200) {
                layer.closeAll();
                $.each(listData,function (index, item) {
                    var count=index+1;
                    var startTime=item.startTime?item.startTime:'';
                    var endTime=item.endTime?item.endTime:'';
                    $('.commentList').append(
                        '<tr>' +
                        '<td>'+count+'</td>' +
                        '<td>'+item.activityName+'</td>' +
                        '<td>'+startTime+'</td>' +
                        '<td>'+endTime+'</td>' +
                        '<td>'+item.assignUser+'</td>' +
                        '<td>'+item.assignMsg+'</td></tr>');
                });
                table.render(null,'commentList');
                /* 再弹出添加界面 */
                layer.open({
                    type: 1,
                    title: "审核流程",
                    shadeClose: true, //开启遮罩关闭
                    closeBtn: 0, //不显示关闭按钮
                    shade: 0.6,//遮罩透明度
                    skin: 'layui-layer-molv',
                    area: ["80%"],
                    content: $("#test").html()
                });
            } else {
                layer.close(cxindex)
                var msg = result.msg;
                if (msg) {
                    layer.alert(msg);
                } else {
                    layer.alert('查询数据失败');
                }
            }
        });
    });

    function renderDate(year,month) {
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post("declare/down.json", {"year": year,"month":month}, function (result) {
            var code = result.code,
                listData = result.data;
            if (code == 200) {
                if(!$("#dateMonth").val()){
                    $("#dateMonth").val(result.msg);
                }
                //渲染数据
                var dataHtml = '';
                var total = listData.length;
                if (total != 0) {
                    $.each(listData, function (i, item) {
                        var index=i+1;
                        dataHtml += '<tr> '
                                + '<td>'+index+'</td>'
                                + '<td>' + item.declareName + '</td>'
                                + '<td>'+item.declareDate+'</td>'
                                + '<td>' + item.user.username + '</td>';
                            if(item.status==1) {
                                dataHtml += '<td>审核中</td><td>'
                                    + '<button class="layui-btn layui-btn-sm  layui-btn-disabled" disabled="disabled">下载</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn  layui-btn-normal layui-btn-sm chakan" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td> '
                                    + '</tr>';
                            }else if(item.status==2) {
                                dataHtml += '<td>审核通过</td><td>'
                                    + '<a class="layui-btn layui-btn-sm" href="salary/download.json?year='+item.year+'&month='+item.month+'&deptId='+item.dept.id+'">下载</a>'
                                    + '&nbsp;&nbsp;<button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-disabled" disabled="disabled">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            }else if(item.status==3) {
                                dataHtml += '<td>调整中</td><td>'
                                    + '<button class="layui-btn layui-btn-sm layui-btn-disabled" disabled="disabled">下载</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn layui-btn-normal layui-btn-sm chakan" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            }
                    });
                } else {
                    dataHtml = '<tr><td colspan="6">暂无数据</td></tr>';
                }
                $(".news_content").html(dataHtml);
                layer.closeAll();
            } else {
                layer.close(cxindex)
                var msg = result.msg;
                if (msg) {
                    layer.alert(msg);
                } else {
                    layer.alert('查询数据失败');
                }
            }
        });
    }
});