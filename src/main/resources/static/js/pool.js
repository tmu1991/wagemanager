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
        $.post('declare/comment1.json', {'declareId': _this.attr("data-id")}, function (result) {
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
        $('.declareId').val($(this).attr('data-id'));
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
        $.post('declare/complete1.json',objParam,function (result) {
            var code = result.code;
            if (code == 200) {
                layer.close(index);
                layer.msg('审核成功');
                renderDate();
            } else {
                layer.close(tjindex)
                var msg = result.msg;
                if (msg) {
                    layer.alert(msg);
                } else {
                    layer.alert('操作失败');
                }
            }
        },'json');
        layer.close(tjindex)
        return false;
    }

    function renderDate() {
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post("declare/pool.json", function (result) {
            var code = result.code,
                listData = result.data;
            if (code == 200) {
                //渲染数据
                var dataHtml = '';
                if (listData.length != 0) {
                    var grossPay=0,subWork=0,allowance=0,insurance=0,accuFund=0,incomeTax=0,debitTotal=0,payroll=0;
                    $.each(listData, function (i, item) {
                        var debit=Number(item.late)+Number(item.otherDebit)+Number(item.partyDue)+Number(item.loan)+Number(item.other)+Number(item.otherEl);
                        dataHtml += '<tr>'
                            + '<td data-id="' + item.deptId + '">' + item.deptName + '</td>'
                            + '<td>' + item.grossPay + '</td>'
                            + '<td>' + item.subWork + '</td>'
                            + '<td>' + item.allowance + '</td>'
                            + '<td>' + item.insurance + '</td>'
                            + '<td>' + item.accuFund + '</td>'
                            + '<td>' + item.incomeTax + '</td>'
                            + '<td>' + debit + '</td>'
                            + '<td>' + item.payroll + '</td>'
                            + '<td class=""><button class="layui-btn layui-btn-sm shenhe" data-id="' + item.declareId + '">审核</button>'
                            + '&nbsp;&nbsp;<button class="layui-btn layui-btn-normal layui-btn-sm chakan" data-id="' + item.declareId + '">查看</button>'
                            + '</td>'
                            + '</tr>';
                        grossPay+=item.grossPay;
                        subWork+=item.subWork;
                        allowance+=item.allowance;
                        insurance+=item.insurance;
                        accuFund+=item.accuFund;
                        incomeTax+=item.incomeTax;
                        debitTotal+=Number(debit);
                        payroll+=item.payroll;
                    });
                    dataHtml+='<tr>' +
                        '<td>合计</td>' +
                        '<td>'+grossPay+'</td>' +
                        '<td>'+subWork+'</td>' +
                        '<td>'+allowance+'</td>' +
                        '<td>'+insurance+'</td>' +
                        '<td>'+accuFund+'</td>' +
                        '<td>'+incomeTax+'</td>' +
                        '<td>'+debitTotal+'</td>' +
                        '<td>'+payroll+'</td>' +
                        '<td></td>' +
                        '</tr>';
                } else {
                    dataHtml = '<tr><td colspan="10">暂无数据</td></tr>';
                }
                $(".news_content").html(dataHtml);
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
    }

});