layui.use(['form', 'layer','table', 'jquery', 'laypage'], function () {
    var table = layui.table,
        layer = layui.layer,
        laypage = layui.laypage,
        $ = layui.jquery;
    //加载页面数据
    renderDate(1);
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
    //提交工资审批
    $("body").on("click", ".tijiao", function () {
        var _this = $(this);
        layer.confirm('确定提交？', {icon: 3, title: '提示信息'}, function () {
            var tjindex = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.4});
            $.post('declare/start.json', {'declareId': _this.attr("data-id")}, function (result) {
                var code = result.code;
                if (code == 200) {
                    layer.closeAll();
                    layer.msg('提交成功');
                    renderDate($('.layui-laypage-curr em:last').text());
                } else {
                    layer.close(tjindex)
                    var msg = result.msg;
                    if (msg) {
                        layer.alert(msg);
                    } else {
                        layer.alert('查询数据失败');
                    }
                }
            });
        })
//                deleteBatch(_this.attr("data-id"));
    });

    //提交工资审批
    $("body").on("click", ".shenhe", function () {
        var _this = $(this);
        layer.confirm('确定提交？', {icon: 3, title: '提示信息'}, function () {
            var tjindex = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.4});
            $.post('declare/complete.json', {'declareId':_this.attr('data-id'),'processInstanceId': _this.next().attr("data-id")}, function (result) {
                var code = result.code;
                if (code == 200) {
                    layer.closeAll();
                    layer.msg('提交成功');
                    renderDate($('.layui-laypage-curr em:last').text());
                } else {
                    layer.close(tjindex)
                    var msg = result.msg;
                    if (msg) {
                        layer.alert(msg);
                    } else {
                        layer.alert('查询数据失败');
                    }
                }
            });
        })
//                deleteBatch(_this.attr("data-id"));
    });

    function renderDate(curPage) {
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post("declare/list.json", {"curPage": curPage}, function (result) {
            var code = result.code,
                listData = result.data,
                page = result.page;
            if (code == 200) {
                //渲染数据
                var dataHtml = '';
                var total = listData.length;
                if (total != 0) {
                    var lastItem = listData[total-1];
                    var indexCount=1;
                    if(!lastItem.declareDate){
                        indexCount=2;
                    }
                    $.each(listData, function (i, item) {
                        if(i == (total -1) && indexCount == 2){
                            var itemHtml='';
                            itemHtml += '<tr>'
                                + '<td>1</td>'
                                + '<td>' + item.declareName + '</td>'
                                + '<td></td>'
                                + '<td>' + item.user.username + '</td>';
                            if (item.status == 0) {
                                itemHtml += '<td>未提交</td><td>'
                                    + '<button class="layui-btn layui-btn-sm tijiao" data-id="'+item.id+'">提交</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn layui-btn-sm layui-btn-disabled" disabled="disabled" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            } else if(item.status==1) {
                                itemHtml += '<td>审核中</td><td>'
                                    + '<button class="layui-btn layui-btn-sm  layui-btn-disabled" disabled="disabled" data-id="'+item.id+'">提交</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn  layui-btn-normal layui-btn-sm chakan" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            }else if(item.status==2) {
                                itemHtml += '<td>审核通过</td><td>'
                                    + '<button class="layui-btn layui-btn-sm  layui-btn-disabled" disabled="disabled" data-id="'+item.id+'">提交</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn layui-btn-normal layui-btn-sm chakan" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            }else if(item.status==3) {
                                itemHtml += '<td>调整中</td><td>'
                                    + '<button class="layui-btn layui-btn-sm shenhe" data-id="'+item.id+'">提交</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn layui-btn-normal layui-btn-sm chakan" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            }
                            dataHtml = itemHtml+dataHtml;
                        }else {
                            var count=i+indexCount;
                            dataHtml += '<tr>'
                                + '<td>' + count + '</td>'
                                + '<td>' + item.declareName + '</td>'
                                + '<td>' + item.declareDate + '</td>'
                                + '<td>' + item.user.username + '</td>';
                            if (item.status == 0) {
                                dataHtml += '<td>未提交</td><td>'
                                    + '<button class="layui-btn layui-btn-sm tijiao" data-id="'+item.id+'">提交</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn layui-btn-sm layui-btn-disabled" disabled="disabled" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            } else if(item.status==1) {
                                dataHtml += '<td>审核中</td><td>'
                                    + '<button class="layui-btn layui-btn-sm  layui-btn-disabled" disabled="disabled" data-id="'+item.id+'">提交</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn  layui-btn-normal layui-btn-sm chakan" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            }else if(item.status==2) {
                                dataHtml += '<td>审核通过</td><td>'
                                    + '<button class="layui-btn layui-btn-sm  layui-btn-disabled" disabled="disabled" data-id="'+item.id+'">提交</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn layui-btn-normal layui-btn-sm chakan" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            }else if(item.status==3) {
                                dataHtml += '<td>调整中</td><td>'
                                    + '<button class="layui-btn layui-btn-sm shenhe" data-id="'+item.id+'">提交</button>'
                                    + '&nbsp;&nbsp;<button class="layui-btn layui-btn-normal layui-btn-sm chakan" data-id="'+item.processInstanceId+'">查看</button>'
                                    + '</td>'
                                    + '</tr>';
                            }
                        }
                    });
                } else {
                    dataHtml = '<tr><td colspan="6">暂无数据</td></tr>';
                }
                $(".news_content").html(dataHtml);
                //分页
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
                })
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