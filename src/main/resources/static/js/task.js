layui.use(['form', 'layer', 'table', 'jquery', 'laypage'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laypage = layui.laypage,
        $ = layui.jquery;
    //加载页面数据
    renderDate(1);

    $("body").on("click", ".chakan", function () {
        var _this = $(this);
        $('.commentList').html('');
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post('declare/comment.json', {'processInstanceId': _this.attr("data-id")}, function (result) {
            var code = result.code,
                listData = result.data;
            if (code == 200) {
                $.each(listData, function (index, item) {
                    var count = index + 1;
                    var startTime = item.startTime ? item.startTime : '';
                    var endTime = item.endTime ? item.endTime : '';
                    $('.commentList').append(
                        '<tr>' +
                        '<td>' + count + '</td>' +
                        '<td>' + item.activityName + '</td>' +
                        '<td>' + startTime + '</td>' +
                        '<td>' + endTime + '</td>' +
                        '<td>' + item.assignUser + '</td>' +
                        '<td>' + item.assignMsg + '</td></tr>');
                });
                table.render(null, 'commentList');
                /* 再弹出添加界面 */
                layer.open({
                    skin: 'layui-layer-molv',
                    shadeClose: true, //开启遮罩关闭
                    closeBtn: 0, //不显示关闭按钮
                    shade: 0.6,//遮罩透明度
                    type: 1,
                    title: ["审核流程"],
                    style: 'position:fixed;margin-top:30%;',
                    area: ["70%"],
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
        layer.close(cxindex)
    });

    var index;
    $("body").on("click", ".shenhe", function () {
        index = layer.open({
            type: 1,
            title: "审批意见",
            area: ["40%", '50%'],
            content: $("#test1").html(),
        });
        /* 渲染表单 */
        var _parent = $(this).parents('tr');
        $('.taskId').val(_parent.find('td:eq(0)').attr('data-id'));
        $('.processInstanceId').val($(this).attr('data-id'));
        form.render(null, 'userForm');
    });

    //监听提交
    form.on('submit(demo1)', function (data) {
        var obj = data.field;
        obj.msg = 1;
        return verify(obj);
    });

    form.on('submit(demo2)', function (data) {
        var obj = data.field;
        obj.msg = 0;
        return verify(obj);
    });

    function verify(objParam) {
        var tjindex = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post('declare/complete.json',objParam,function (result) {
            var code = result.code;
            if (code == 200) {
                layer.closeAll();
                layer.msg('审核成功');
                renderDate($('.layui-laypage-curr em:last').text());
            } else {
                layer.close(tjindex);
                var msg = result.msg;
                if (msg) {
                    layer.alert(msg);
                } else {
                    layer.alert('操作失败');
                }
            }
        },'json');
        layer.close(tjindex);
        return false;
    }

    function renderDate(curPage) {
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post("declare/task.json", {"curPage": curPage}, function (result) {
            var code = result.code,
                listData = result.data,
                page = result.page;
            if (code == 200) {
                //渲染数据
                var dataHtml = '';
                $(window.parent.document).find(".layui-badge").text(listData.length);
                if (listData.length != 0) {
                    $.each(listData, function (i, item) {
                        var count = i + 1;
                        dataHtml += '<tr>'
                            + '<td data-id="' + item.taskId + '">' + count + '</td>'
                            + '<td><a href="index/'+item.dept.id+'">' + item.declareName + '</a></td>'
                            + '<td>' + item.declareDate + '</td>'
                            + '<td>' + item.user.username + '</td>';
                        if (item.status == 0) {
                            dataHtml += '<td>未提交</td>';
                        } else if (item.status == 1) {
                            dataHtml += '<td>审核中</td>';
                        } else if (item.status == 2) {
                            dataHtml += '<td>审核通过</td>';
                        } else if (item.status == 3) {
                            dataHtml += '<td>调整中</td>';
                        }
                        dataHtml += '<td>'
                            + '<button class="layui-btn layui-btn-sm shenhe" data-id="' + item.processInstanceId + '">审核</button>'
                            + '&nbsp;&nbsp;<button class="layui-btn layui-btn-normal layui-btn-sm chakan" data-id="' + item.processInstanceId + '">查看</button>'
                            + '</td>'
                            + '</tr>';
                    });
                } else {
                    dataHtml = '<tr><td colspan="6">暂无数据</td></tr>';
                }
                $(".news_content").html(dataHtml);
                //分页
                laypage.render({
                    elem: 'page',
                    limit: page.pageSize,
                    count: page.totalCount, //数据总数
                    curr: page.currentPage,
                    layout: ['count', 'prev', 'page', 'next', 'skip'],
                    jump: function (obj, first) {
                        if (!first) {
                            renderDate(obj.curr);
                        }
                    }
                })
            } else {
                layer.close(cxindex);
                var msg = result.msg;
                if (msg) {
                    layer.alert(msg);
                } else {
                    layer.alert('查询数据失败');
                }
            }
        });
        layer.close(cxindex);
    }
});