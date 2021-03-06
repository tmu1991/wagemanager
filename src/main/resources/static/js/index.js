layui.use(['form', 'layer', 'laydate', 'jquery', 'laypage','upload'], function () {
    var form = layui.form,
        laydate = layui.laydate,
        layer = layui.layer,
        laypage = layui.laypage,
        $ = layui.jquery,
        upload = layui.upload;

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
                layer.closeAll();
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
        // ,error: function(){
        //     layer.close(uploadIndex);
        //     //演示失败状态，并实现重传
        //     var demoText = $('#demoText');
        //     demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
        //     demoText.find('.demo-reload').on('click', function(){
        //         uploadInst1.upload();
        //     });
        // }
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
                layer.closeAll();
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
        // ,error: function(){
        //     layer.close(uploadIndex);
        //     //演示失败状态，并实现重传
        //     var demoText = $('#demoText');
        //     demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
        //     demoText.find('.demo-reload').on('click', function(){
        //         uploadInst2.upload();
        //     });
        // }
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
            area:['60%','90%'],
            scrollbar: true,
            // maxmin: true,
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
                    ,"repairWork": tr.find('td.bq').text()
                    ,"subDay": tr.find('td.toworkday').text()
                    ,"subWork": tr.find('td.toworkmoney').text()
                    ,"allowance": getJT(tr.find('td.jt').text(),tr.find('td.cq').text(),tr.find('td.bq').text())
                    ,"bonus": tr.find('td.jj').text()
                    ,"remark":tr.find('td.bz').text()
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
                    ,"subWork": ''
                    ,"allowance": ''
                    ,"bonus": ''
                    ,"remark":''
                });
                $('.addBox input').removeClass("changered");
            }
        });
    });
    
    function getJT(jt,kq,bq) {
        return Number(Number(jt)/(Number(kq)+Number(bq))).toFixed(2);
    }

    //监听提交
    form.on('submit(demo1)', function(data){
        var tjindex = layer.msg('更新中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post('/salary/update.json',data.field,function (result) {
            var code = result.code;
            if (code == 200) {
                layer.closeAll();
                layer.msg("操作成功");
                renderDate($('.layui-laypage-curr em:last').text());
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

    $("body").on("click", ".tianjiahang", function () {
        var count = $(this).attr('data-id');
        count++;
        $(this).parents(".loanList").append('<div class="layui-input-block" style="margin-left: 110px;margin-bottom: 5px">' +
            '                <div class="layui-input-inline div-loan" style="width: 15%;">' +
            '                <input name="tasks['+count+'].id" hidden="hidden">' +
            '                <input type="text" name="tasks['+count+'].amount" class="layui-input"> ' +
            '                </div> ' +
            '                <div class="layui-input-inline div-loan" style="width: 20%;"> ' +
            '                <select class="layui-select" name="tasks['+count+'].type"> ' +
            '                <option value="0">借款</option>' +
            '                <option value="1">其他扣款</option>' +
            '                </select>' +
            '                </div>' +
            '                <div class="layui-input-inline div-loan" style="width: 25%;">' +
            '                <input placeholder="扣款发生日期" type="text" id="date'+count+'" name="tasks['+count+'].taskDate" class="layui-input"> ' +
            '                </div>' +
            '                <div class="layui-input-inline div-loan" style="width: 25%;"> ' +
            '                <input placeholder="备注" type="text" name="tasks['+count+'].note" class="layui-input"> ' +
            '                </div>                                 <div data-id="'+count+'" class="layui-input-inline tianjiahang" style="width: 3%;"> ' +
            '                <button style="margin-left: 20px !important;margin-top:10px;border-radius: 100px;border: medium none;" class="layui-btn layui-btn-xs"><i class="layui-icon">&#xe654;</i></button> ' +
            '                </div>' +
            '                </div>');
        form.render('select');
        var date =new Date();
        laydate.render({
            elem: '#date'+count //指定元素
            ,max:'date'
        });
        $(this).remove();
    });

    $("body").on("click", ".loan_edit", function () {  //编辑
        var tr = $(this).parents('tr');
        var workNo = tr.find('td.id').text();
        $.post("/task/salary.json",{"workNo":workNo},function (result) {
            if(result.code == 200){
            var htmlStr='<div class="layui-col-md10">' +
                '                <form style="margin-top: 10px;margin-bottom: 20px" class="layui-form" lay-filter="loanForm" id="loanForm">' +
                '                <div class="layui-form-item">' +
                '                <label class="layui-form-label">工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label>' +
                '            <div class="layui-input-block">' +
                '                <input name="id" value="" hidden="hidden"/>' +
                '                <input name="dateStr" value="" hidden="hidden"/>' +
                '                <input name="deptId" value="" hidden="hidden"/>' +
                '                <input name="deptName" value="" hidden="hidden"/>' +
                '                <input disabled type="text" lay-verify="required" name="workNo" class="layui-input">' +
                '                </div>' +
                '                </div>' +
                '                <div class="layui-form-item">' +
                '                <label class="layui-form-label">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label>' +
                '            <div class="layui-input-block">' +
                '                <input disabled type="text" lay-verify="required" name="username" class="layui-input">' +
                '                </div>' +
                '                </div>' +
                '                <div class="layui-form-item">' +
                '                <label class="layui-form-label">迟到/早退：</label>' +
                '            <div class="layui-input-block">' +
                '                <input type="text" lay-verify="required" name="late" class="layui-input">' +
                '                </div>' +
                '                </div>' +
                '                <div class="layui-form-item">' +
                '                <label class="layui-form-label">党&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：</label>' +
                '            <div class="layui-input-block">' +
                '                <input type="text" lay-verify="required" name="due" class="layui-input">' +
                '                </div>' +
                '                </div>' +
                '                <div class="layui-form-item">' +
                '                <label class="layui-form-label">其&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;他：</label>' +
                '            <div class="layui-input-block">' +
                '                <input type="text" lay-verify="required" name="other" class="layui-input">' +
                '                </div>' +
                '                </div>' +
                '                <div class="layui-form-item">' +
                '                <label class="layui-form-label">其&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;他：</label>' +
                '            <div class="layui-input-block">' +
                '                <input type="text" lay-verify="required" name="otherEl" class="layui-input">' +
                '                </div>' +
                '                </div>' +
                '                <div class="layui-form-item loanList"><label class="layui-form-label">扣&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;款：</label>';
            var count=0;
            var data={
                "id": tr.find('td.pkid').attr('data-id')
                ,"username": tr.find('td.username').text()
                ,"workNo": tr.find('td.id').text()
                ,"late": tr.find('td.cd').text()
                ,"due": tr.find('td.df').text()
                ,"other": tr.find('td.qt1').text()
                ,"otherEl": tr.find('td.qt2').text()
                ,"deptId":$('.deptInfo').attr('content')
                ,"deptName":$('.deptInfo').text()
            };
            var dateArrays=[];
            var date=new Date();
            $.each(result.data,function (i, item) {
                data['tasks['+count+'].type']=item.type;
                // data['tasks['+count+'].taskDate']=item.taskDate;
                dateArrays.push({
                    elem: '#date'+count //指定元素
                    ,max:'date'
                    ,value:item.taskDate
                });
                htmlStr+='<div class="layui-input-block" style="margin-bottom: 5px">' +
                    '<div class="layui-input-inline div-loan" style="width: 15%;">' +
                    '<input name="tasks['+count+'].id" value="'+item.id+'" hidden="hidden">' +
                    '<input placeholder="扣款金额" type="text" name="tasks['+count+'].amount" value="'+item.amount+'" class="layui-input">' +
                    '</div>' +
                    '<div class="layui-input-inline div-loan" style="width: 20%;">' +
                    '<select class="layui-select" name="tasks['+count+'].type">' +
                    '<option value="0">借款</option>' +
                    '<option value="1">其他扣款</option>' +
                    '</select>' +
                    '</div>' +
                    '<div class="layui-input-inline div-loan" style="width: 25%;">' +
                    '<input placeholder="扣款发生日期" type="text" id="date'+count+'" name="tasks['+count+'].taskDate" class="layui-input">' +
                    '</div>' +
                    '<div class="layui-input-inline div-loan" style="width: 25%;">' +
                    '<input placeholder="备注" type="text" name="tasks['+count+'].note" value="'+item.note+'" class="layui-input">' +
                    '</div>' +
                    '</div>';
                count++;
            });
            htmlStr+='<div class="layui-input-block" style="margin-left: 110px;margin-bottom: 5px">' +
                '<div class="layui-input-inline div-loan" style="width: 15%;">' +
                '<input name="tasks['+count+'].id" hidden="hidden">' +
                '<input placeholder="扣款金额" type="text" name="tasks['+count+'].amount" class="layui-input">' +
                '</div>' +
                '<div class="layui-input-inline div-loan" style="width: 20%;">' +
                '<select class="layui-select" name="tasks['+count+'].type">' +
                '<option value="0">借款</option>' +
                '<option value="1">其他扣款</option>' +
                '</select>' +
                '</div>' +
                '<div class="layui-input-inline div-loan" style="width: 25%;">' +
                '<input placeholder="扣款发生日期" type="text" id="date'+count+'" name="tasks['+count+'].taskDate" class="layui-input">' +
                '</div>' +
                '<div class="layui-input-inline div-loan" style="width: 25%;">' +
                '<input placeholder="备注" type="text" name="tasks['+count+'].note" class="layui-input">' +
                '</div>' +
                '<div class="layui-input-inline tianjiahang" data-id="'+count+'"  style="width: 3%;">' +
                '<button style="margin-left: 20px !important;margin-top:10px;border-radius: 100px;border: medium none;" class="layui-btn layui-btn-xs"><i class="layui-icon">&#xe654;</i></button>' +
                '</div>' +
                '</div></div>' +
                '                <div class="layui-form-item">' +
                '                <div class="btn-btn layui-input-block">' +
                '                <button class="layui-btn" lay-submit="" lay-filter="demo2">提交</button>' +
                '                <button type="reset" class="layui-btn layui-btn-primary">重置</button>' +
                '                </div>' +
                '                </div>' +
                '                </form>' +
                '                </div>';
            dateArrays.push({
                elem: '#date'+count //指定元素
                ,max:'date'
            });
            index = layer.open({
                type: 1,
                icon: 1,
                title: "修改员工扣款",
                skin: 'layui-layer-molv',
                area: ["55%"],
                // btnAlign: 'c',
                content: htmlStr,
                success:function (layero, index) {
                    $.each(dateArrays,function (i, item) {
                        console.log(item)
                        laydate.render(item);
                    });
                    form.val("loanForm", data);
                    form.render(null,'loanForm');
                }
            });
            }
        });
    });

    //监听提交
    form.on('submit(demo2)', function(data){
        var tjindex = layer.msg('更新中，请稍候',{icon: 16,time:false,shade:0.4});
        $.post('/task/update.json',data.field,function (result) {
            var code = result.code;
            if (code == 200) {
                layer.closeAll();
                layer.msg("操作成功");
                renderDate($('.layui-laypage-curr em:last').text());
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
        var $checked = $('#listTable input[type=checkbox][name=item]:checked');
        var ids = '';
        $.each($checked, function (i, item) {
            if (i == $checked.length - 1) {
                ids += $(item).parents('tr').find('td.pkid').attr('data-id');
            } else {
                ids += $(item).parents('tr').find('td.pkid').attr('data-id') + ",";
            }
        });
        if (items.is(":checked")) {
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
            var scindex = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.4});
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
                    var timerMsg='';
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
                        newCell.find('.idnumber').text(item.iDNumber);
                        newCell.find('.workname').text(item.deptName);
                        newCell.find('.username').text(item.username);
                        newCell.find('.money').text(Number(item.base).toFixed(2));
                        newCell.find('.num').text(Number(item.coeff).toFixed(2));
                        newCell.find('.dayMoney').text(Number(item.dailyWage).toFixed(2));
                        newCell.find('.cq').text(item.attendance);
                        newCell.find('.cx').text(item.busTravel);
                        newCell.find('.gx').text(item.holiday);
                        newCell.find('.hj').text(item.workTotal);
                        newCell.find('.bq').text(item.repairWork);
                        newCell.find('.workmoney').text(Number(item.seniority).toFixed(2));
                        newCell.find('.toworkday').text(Number(item.subDay).toFixed(2));
                        newCell.find('.toworkmoney').text(Number(item.subWork).toFixed(2));
                        newCell.find('.jt').text(Number(item.allowance).toFixed(2));
                        newCell.find('.getMoney').text(Number(item.grossPay).toFixed(2));
                        newCell.find('.four').text(Number(item.insurance).toFixed(2));
                        newCell.find('.yj').text(Number(item.accuFund).toFixed(2));
                        newCell.find('.gs').text(Number(item.incomeTax).toFixed(2));
                        newCell.find('.jj').text(Number(item.bonus).toFixed(2));
                        newCell.find('.cd').text(Number(item.late).toFixed(2));
                        newCell.find('.qt').text(Number(item.otherDebit).toFixed(2));
                        newCell.find('.df').text(Number(item.partyDue).toFixed(2));
                        newCell.find('.jk').text(Number(item.loan).toFixed(2));
                        newCell.find('.qt1').text(Number(item.other).toFixed(2));
                        newCell.find('.qt2').text(Number(item.otherEl).toFixed(2));
                        newCell.find('.fj').text(
                            Number(Number(item.late) +
                                Number(item.otherDebit) +
                                Number(item.partyDue) +
                                Number(item.loan) +
                                Number(item.other) +
                                Number(item.otherEl)).toFixed(2)
                        );
                        var payroll=Number(item.payroll).toFixed(2);
                        if(payroll<0){
                            newCell.find('.sj').html('<span style="color: red;">'+payroll+'</span>');
                        }else{
                            newCell.find('.sj').text(payroll);
                        }
                        newCell.find('.ka').text(item.creditCard);
                        newCell.find('.bz').text(item.remark);
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
                    });
                    layer.closeAll();
                } else {
                    layer.close(index);
                    box.append("暂无数据");
                }
            }, error: function (data) {
                layer.close(index);
            }
        });
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