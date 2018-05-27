layui.use(['form', 'layer', 'laydate', 'jquery', 'laypage','upload'], function () {
    var form = layui.form,
        laydate = layui.laydate,
        layer = layui.layer,
        laypage = layui.laypage,
        $ = layui.jquery,
        upload = layui.upload;
//            //loading层
    laydate.render({
        elem: '#date1'
    });
    laydate.render({
        elem: '#date2'
    });

    var uploadIndex;

    var uploadInst1 = upload.render({ //允许上传的文件后缀
        elem: '#test8'
        ,url: '/salary/upload.json'
        ,accept: 'file' //普通文件
        ,exts: 'xls|xlsx' //只允许上传压缩文件
        ,before: function(obj){
            uploadIndex = layer.load(0, {shade: [0.4, '#000']});
        }
        ,done: function(result){
            if (result.code == 200) {
                layer.msg('上传成功');
                renderDate(1);
            } else {
                layer.close(uploadIndex);
                if (result.msg) {
                    layer.alert(result.msg);
                } else {
                    layer.alert("上传失败");
                }
            }
        }
        ,error: function(){
            layer.close(uploadIndex);
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function(){
                uploadInst1.upload();
            });
        }
    });

    var uploadInst2 = upload.render({ //允许上传的文件后缀
        elem: '#test9'
        ,url: '/task/upload.json'
        ,accept: 'file' //普通文件
        ,exts: 'xls|xlsx' //只允许上传压缩文件
        ,before: function(obj){
            uploadIndex = layer.load(0, {shade: [0.4, '#000']});
        }
        ,done: function(result){
            if (result.code == 200) {
                layer.msg('上传成功');
                // renderDate(1);
            } else {
                layer.close(uploadIndex);
                if (result.msg) {
                    layer.alert(result.msg);
                } else {
                    layer.alert("上传失败");
                }
            }
        }
        ,error: function(){
            layer.close(uploadIndex);
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function(){
                uploadInst2.upload();
            });
        }
    });

    $("body").on('change','.layui-input' ,function () {
        $(this).addClass("changered");
    });

    // var deptId = /*[[${dept.id}]]*/;
    var deptId = $('body span.deptInfo').attr('content');

    renderDate(1);

    var index;
    //modify功能
    $("body").on("click", ".news_edit", function () {  //编辑
        var tr = $(this).parents('tr');
        index = layer.open({
            type: 1,
            icon: 1,
            title: "修改员工工资",
            skin: 'layui-layer-molv',
            area: ["50%",'85%'],
            btnAlign: 'c',
            content: $('#addBox').html(),
            success:function (layero, index) {
                form.val("indexForm", {
                    "id": tr.find('td.pkid').attr('data-id')
                    ,"username": tr.find('td.username').text()
                    ,"workNo": tr.find('td.id').text()
                    ,"coeff": tr.find('td.num').text()
                    ,"base": tr.find('td.money').text()
                    ,"seniority": tr.find('td.workmoney').text()
                    ,"busTravel": tr.find('td.cx').text()
                    ,"subDay": tr.find('td.toworkday').text()
                    ,"allowance": tr.find('td.jt').text()
                    ,"bonus": tr.find('td.jj').text()
                });
                form.render(null,'indexForm');
            },
            cancel: function(){
                form.val("indexForm", {
                    "id": ''
                    ,"username": ''
                    ,"workNo": ''
                    ,"coeff": ''
                    ,"base": ''
                    ,"seniority": ''
                    ,"busTravel": ''
                    ,"subDay": ''
                    ,"allowance": ''
                    ,"bonus": ''
                });
                $('.addBox input').removeClass("changered");
            }
        });
    });

    //监听提交
    form.on('submit(demo1)', function(data){
        var tjindex = layer.msg('更新中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post('/salary/update.json',data.field,function (result) {
            var code = result.code;
            if (code == 200) {
                renderDate($('.layui-laypage-curr em:last').text());
                layer.closeAll();
                layer.msg("操作成功");
            } else {
                layer.close(tjindex);
                if(result.msg){
                    layer.alert(result.msg);
                }else{
                    layer.msg("操作失败，请重试");
                }
            }
        },'json');
        return false;
    });

    $("body").on("click", ".loan_edit", function () {  //编辑
        var tr = $(this).parents('tr');
        index = layer.open({
            type: 1,
            icon: 1,
            title: "修改员工扣款",
            skin: 'layui-layer-molv',
            area: ["50%",'85%'],
            btnAlign: 'c',
            content: $('#addLoan').html(),
            success:function (layero, index) {
                form.val("loanForm", {
                    "id": tr.find('td.pkid').attr('data-id')
                    ,"username": tr.find('td.username').text()
                    ,"workNo": tr.find('td.id').text()
                    ,"late": tr.find('td.cd').text()
                    ,"otherDebit": tr.find('td.qt').text()
                    ,"partyDue": tr.find('td.df').text()
                    ,"loan": tr.find('td.jk').text()
                    ,"other": tr.find('td.qt1').text()
                    ,"otherEl": tr.find('td.qt2').text()
                    ,"dateStr":$('.timer').text()
                    ,"loanDate":tr.find('td.jkdate').text()
                    ,"loanNote":tr.find('td.jknote').text()
                    ,"debitDate":tr.find('td.kkdate').text()
                    ,"debitNote":tr.find('td.kknote').text()
                });
                form.render(null,'loanForm');
            }, cancel: function(){
                form.val("loanForm", {
                    "id": ''
                    ,"username": ''
                    ,"workNo": ''
                    ,"late": ''
                    ,"otherDebit": ''
                    ,"partyDue": ''
                    ,"loan": ''
                    ,"other": ''
                    ,"otherEl": ''
                    ,"dateStr":''
                    ,"loanDate":''
                    ,"loanNote":''
                    ,"debitDate":''
                    ,"debitNote":''
                });
                $('.addLoan input').removeClass("changered");
            }
        });
    });

    //监听提交
    form.on('submit(demo2)', function(data){
        var tjindex = layer.msg('更新中，请稍候',{icon: 16,time:false,shade:0.8});
        $.post('/task/update.json',data.field,function (result) {
            var code = result.code;
            if (code == 200) {
                renderDate($('.layui-laypage-curr em:last').text());
                layer.closeAll();
                layer.msg("操作成功");
            } else {
                layer.close(tjindex);
                if(result.msg){
                    layer.alert(result.msg);
                }else{
                    layer.msg("操作失败，请重试");
                }
            }
        },'json');
        return false;
    });

    //del单点功能
    $("body").on("click", ".news_del", function () {  //删除
        deleteBatch($(this).parents('tr').find('td.pkid').attr('data-id'));
    });

    //全选
    function checkAll(c) {

        var status = c.checked;
        var oItems = document.getElementsByName('item');
        for (var i = 0; i < oItems.length; i++) {
            oItems[i].checked = status;
        }
    }

    //delAll功能
    $("#delAll").click(function () {
        var items = $("#listTable input[type='checkbox'][name='item']");
        var $checked = $('#listTable input[type=\'checkbox\'][name=\'item\']:checked');
        var ids = '';
        $.each($checked, function (i, item) {
            if (i == $checked.length - 1) {
                ids += $(item).parents('tr').find('td.pkid').attr('data-id');
            } else {
                ids += $(item).parents('tr').find('td.pkid').attr('data-id') + ",";
            }
        });
        if (items.is(":checked")) {
            console.log(ids)
            deleteBatch(ids);
        } else {
            layer.msg("请选择需要删除的用户");
            return false;
        }
    });

    $("#jtt").change(function () {
        $(this).val(Number($(this).val()) * Number($(this).attr("content")));
    });


    $('.yinchang').click(function () {
        $(".fj").show();
        $(".fjHide").hide();
    });

    $('.yinchang1').click(function () {
        $(".fjHide").show();
        $(".fj").hide();
    });

    function deleteBatch(ids) {
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '提示信息'}, function () {
            console.log(ids)
            var scindex = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            //删除数据
            $.post("/salary/delete.json", {'ids': ids}, function (result) {
                var code = result.code;
                if (code == 200) {
                    layer.closeAll();
                    layer.msg("删除成功");
                    renderDate($('.layui-laypage-curr em:last').text());
                } else {
                    layer.close(scindex);
                    if(result.msg){
                        layer.alert(result.msg);
                    }else{
                        layer.alert("操作失败，请重试");
                    }
                }
            });
        })
    }

    function renderDate(num) {
        var index = layer.load(2, {shade: [0.4, '#000']});
        $.ajax({
            url: "/salary/dept.json",
            type: "post",
            data: {"deptId": deptId, "curPage": num},
            success: function (result) {
                var box = $('#listTable');
                var code = result.code,
                    listData = result.data,
                    page = result.page;
                if (code == 200) {
                    var cell = $('#eg').find('tr');
                    var timerMsg;
                    box.empty();
                    $.each(listData, function (i, item) {
                        if (!timerMsg) {
                            timerMsg = item.year + '年' + item.month + '月';
                        }
                        var jkdate='',kkdate='',jknote='',kknote='';
                        $.each(item.tasks,function (j, task) {
                            if(task.type == 0){
                                jkdate=task.taskDate;
                                jknote=task.note;
                            }
                            if(task.type == 1){
                                kkdate=task.taskDate;
                                kknote=task.note;
                            }
                        });
                        var newCell = cell.clone(true);
                        newCell.find('.pkid').attr("data-id", item.id);
                        newCell.find('.id').text(item.workNo);
                        newCell.find('.workname').text(item.deptName);
                        newCell.find('.username').text(item.username);
                        newCell.find('.money').text(item.base);
                        newCell.find('.num').text(item.coeff);
                        newCell.find('.dayMoney').text(item.dailyWage);
                        newCell.find('.cq').text(item.attendance);
                        newCell.find('.cx').text(item.busTravel);
                        newCell.find('.gx').text(item.holiday);
                        newCell.find('.hj').text(item.workTotal);
                        newCell.find('.workmoney').text(item.seniority);
                        newCell.find('.toworkday').text(item.subDay);
                        newCell.find('.toworkmoney').text(item.subWork);
                        newCell.find('.jt').text(item.allowance);
                        newCell.find('.getMoney').text(item.grossPay);
                        newCell.find('.four').text(item.insurance);
                        newCell.find('.yj').text(item.accuFund);
                        newCell.find('.gs').text(item.incomeTax);
                        newCell.find('.jj').text(item.bonus);
                        newCell.find('.cd').text(item.late);
                        newCell.find('.qt').text(item.otherDebit);
                        newCell.find('.df').text(item.partyDue);
                        newCell.find('.jk').text(item.loan);
                        newCell.find('.qt1').text(item.other);
                        newCell.find('.qt2').text(item.otherEl);
                        newCell.find('.fj').text(
                            Number(item.late) +
                            Number(item.otherDebit) +
                            Number(item.partyDue) +
                            Number(item.loan) +
                            Number(item.other) +
                            Number(item.otherEl)
                        );
                        newCell.find('.sj').text(item.payroll);
                        newCell.find('.ka').text(item.creditCard);
                        newCell.find('.jkdate').text(jkdate);
                        newCell.find('.jknote').text(jknote);
                        newCell.find('.kknote').text(kknote);
                        newCell.find('.kkdate').text(kkdate);
                        box.append(newCell);
                    });
                    //合计当前页面
                    $('tfoot td:eq(0)').text('合计');
                    $('.moneyhj').text(tj(box.find('.money')));
                    $('.hjhj').text(tj(box.find('.hj')));
                    $('.getMoneyhj').text(tj(box.find('.getMoney')));
                    $('.gshj').text(tj(box.find('.gs')));
                    $('.sjhj').text(tj(box.find('.sj')));
                    $('.fjhj').text(tj(box.find('.fj')));
                    $('.timer').text(timerMsg);
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
                    layer.close(index);
                    box.append("暂无数据");
                }
            }, error: function (data) {
                layer.close(index);
            }
        });
        layer.close(index);
    }

    function tj(arr) {
        var num = 0;
        for (var i = 0; i < arr.length; i++) {
            num += Number($(arr[i]).text());
        }
        num = num.toFixed(2);
        return num
    }

});