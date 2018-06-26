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
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post('declare/comment1.json', {'declareId': _this.attr("data-id")}, function (result) {
            var code = result.code,
                listData = result.data;
            if (code == 200) {
                layer.closeAll();
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
    });

    var index;
    $("body").on("click", ".shenhe", function () {
        index = layer.open({
            type: 1,
            title: "审批意见",
            area: ["40%", '50%'],
            content: $("#test1").html()
        });
        form.val("userForm",{
            'declareId':$(this).attr('data-id'),
            'deptId':$(this).parents('tr').find('td:first').attr('data-id')
        });
        form.render(null, 'userForm');
    });

    //全选
    form.on('checkbox(allChoose)', function (data) {
        var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
        child.each(function (index, item) {
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    var kkindex;

    form.on('submit(demo5)', function (data) {
        var obj = data.field;
        obj.msg = 1;
        return verify(obj);
    });
    //监听提交
    form.on('submit(demo1)', function (data) {
        var obj = data.field;
        var tjindex = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post('salary/loan.json',{"declareId":obj.declareId,"deptId":obj.deptId},function (result) {
            if(result.code==200){
                if(result.data==0){
                    $.post('task/dept.json',{"deptId":obj.deptId},function (result) {
                        var code = result.code,
                            data = result.data;
                        if(code == 200){
                            if(data.length>0){
                                var dataHtml='';
                                $.each(data, function (i, item) {
                                    dataHtml += '<tr>'
                                        + '<td>';
                                    if(item.status==0){
                                        dataHtml+='<input checked value="' + item.id + '" type="checkbox" name="ids" lay-skin="primary" lay-filter="choose">';
                                    }else{
                                        dataHtml+='<input value="' + item.id + '" type="checkbox" name="ids" lay-skin="primary" lay-filter="choose">';
                                    }
                                    dataHtml+='</td>'
                                        + '<td>' + item.username + '</td>'
                                        + '<td>' + item.workNo + '</td>'
                                        + '<td>' + item.amount + '</td>';
                                    if(item.type == 0){
                                        dataHtml+='<td>借款</td>';
                                    }else{
                                        dataHtml+='<td>其他扣款</td>';
                                    }
                                    dataHtml+= '<td>' + item.taskDate + '</td><td>' + item.note + '</td></tr>';
                                });
                                $(".load_content").html(dataHtml);
                                $("#deptId").val(obj.deptId);
                                kkindex = layer.open({
                                    type: 1,
                                    title: "待扣款明细",
                                    area: ["60%"],
                                    content: $("#test2").html(),
                                    success:function () {
                                        form.render('checkbox');
                                    }
                                });
                                layer.close(tjindex);
                            }else{
                                layer.close(tjindex);
                                obj.msg = 1;
                                return verify(obj);
                            }
                        }else{
                            layer.close(tjindex);
                            if(result.msg){
                                layer.alert(result.msg);
                            }else{
                                layer.alert('查询扣款失败');
                            }
                        }
                    })
                }else{
                    layer.close(tjindex);
                    obj.msg = 1;
                    return verify(obj);
                }
            }else{
                layer.close(tjindex);
                if(result.msg){
                    layer.alert(result.msg);
                }else{
                    layer.alert('查询扣款失败');
                }
            }
        });
        return false;
    });

    form.on('submit(demo2)', function (data) {
        var obj = data.field;
        obj.msg = 0;
        return verify(obj);
    });

    form.on('submit(demo3)', function (data) {
        var checkboxList = $('div.layui-layer .load_content input[type="checkbox"][name="ids"]');
        var checkIds=[],unCheckIds=[];
        $.each(checkboxList,function (i, item) {
            if($(this).is(":checked")){
                checkIds.push($(this).val())
            }else{
                unCheckIds.push($(this).val())
            }
        });
        var declareId = $('.info-input > input[name="declareId"]').val();
        var deptId = $('.info-input > input[name="deptId"]').val();
        $.post('task/charged.json',{'unCheckIds':unCheckIds,'checkIds':checkIds,'deptId':deptId,'declareId':declareId},function (result) {
            if(result.code == 200){
                layer.close(kkindex);
            }else{
                layer.close(kkindex);
                if(result.msg){
                    layer.alert(result.msg);
                }else{
                    layer.alert('操作失败')
                }
            }
        });
        return false;
    });

    function verify(objParam) {
        var tjindex = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post('declare/complete1.json',objParam,function (result) {
            var code = result.code;
            if (code == 200) {
                layer.closeAll();
                layer.msg('审核成功');
                $(".layui-form input[name='sign']").val(1);
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
        return false;
    }

    function renderDate() {
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post("declare/pool.json", function (result) {
            var code = result.code,
                listData = result.data;
            if (code == 200) {
                //渲染数据
                var dataHtml = '';
                if (listData.length != 0) {
                    var grossPay=0,subWork=0,allowance=0,insurance=0,accuFund=0,incomeTax=0,
                        lateTotal=0,
                        otherDebitTotal=0,
                        partyDueTotal=0,
                        loanTotal=0,
                        otherTotal=0,
                        otherElTotal=0,
                        payroll=0;
                    $.each(listData, function (i, item) {
                        // var debit=Number()+Number()+Number(item.partyDue)+Number(item.loan)+Number(item.other)+Number(item.otherEl);
                        dataHtml += '<tr>'
                            + '<td data-id="' + item.deptId + '"><a href="index/'+item.deptId+'"> ' + item.deptName + '</a></td>'
                            + '<td>' + item.grossPay + '</td>'
                            + '<td>' + item.subWork + '</td>'
                            + '<td>' + item.allowance + '</td>'
                            + '<td>' + item.insurance + '</td>'
                            + '<td>' + item.accuFund + '</td>'
                            + '<td>' + item.incomeTax + '</td>'
                            + '<td>' + item.late + '</td>'
                            + '<td>' + item.otherDebit + '</td>'
                            + '<td>' + item.partyDue + '</td>'
                            + '<td>' + item.loan + '</td>'
                            + '<td>' + item.other + '</td>'
                            + '<td>' + item.otherEl + '</td>'
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
                        lateTotal+=item.late;
                        otherDebitTotal+=item.otherDebit;
                        partyDueTotal+=item.partyDue;
                        loanTotal+=item.loan;
                        otherTotal+=item.other;
                        otherElTotal+=item.otherEl;
                        // debitTotal+=Number(debit);
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
                        '<td>'+lateTotal+'</td>' +
                        '<td>'+otherDebitTotal+'</td>' +
                        '<td>'+partyDueTotal+'</td>' +
                        '<td>'+loanTotal+'</td>' +
                        '<td>'+otherTotal+'</td>' +
                        '<td>'+otherElTotal+'</td>' +
                        '<td>'+payroll+'</td>' +
                        '<td></td>' +
                        '</tr>';
                } else {
                    dataHtml = '<tr><td colspan="10">暂无数据</td></tr>';
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