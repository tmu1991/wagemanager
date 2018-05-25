layui.use(['form', 'layer', 'table', 'jquery', 'laypage'], function () {
    var form = layui.form,
        table = layui.table,
        layer = layui.layer,
        laypage = layui.laypage,
        $ = layui.jquery;
//            //loading层

    $(".form-control").on('change', function () {
        $(this).addClass("changered");
    });

    // var deptId = /*[[${dept.id}]]*/;
    console.log($('body span.deptInfo'))
    console.log($('body span.deptInfo').val())
    var deptId = $('body span.deptInfo').val();

    renderDate(1);

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

    function xlsFile(obj) {
        if (!obj.files) {
            return;
        }
        var fileObj = obj.files[0];
        if (typeof (fileObj) === "undefined" || fileObj.size <= 0) {
            layer.alert("请选择文件");
            return;
        }
        var formFile = new FormData();
        formFile.append("action", "UploadVMKImagePath");
        formFile.append("file", fileObj); //加入文件对象
        var index = top.layer.load(0, {shade: [0.4, '#000']});
        $.ajax({
            url: "/salary/upload.json",
            data: formFile,
            type: "post",
            dataType: "json",
            cache: false,//上传文件无需缓存
            processData: false,//用于对data参数进行序列化处理 这里必须false
            contentType: false, //必须
            success: function (result) {
                top.layer.close(index);
                if (result.code == 200) {
                    renderDate(1);
                } else {
                    top.layer.close(index);
                    if (result.msg) {
                        layer.alert(result.msg);
                    } else {
                        layer.alert("上传失败");
                    }
                }

            },
            error: function (data) {
                top.layer.close(index);
                layer.alert("获取数据失败");
            }
        })
    }

    $("#close").click(function () {
        $('.addBox input').removeClass("changered");
        $('.addBox').hide();
    });

    function confirmAct() {
        if (confirm('确定要执行此操作吗?')) {
            return true;
        }
        return false;
    }

    //del单点功能
    $("body").on("click", ".news_del", function () {});
    function del(obj) {
        if (confirmAct() === true) {
            var wage_id = $(obj).parent().parent().find("td:first").attr("data-id");
            var data = {"ids": wage_id};
            mondifyOpr("/salary/delete.json", data);
        }
    }

    //全选
    $("body").on("click", ".news_del", function () {});
    function checkAll(c) {
        var status = c.checked;
        var oItems = document.getElementsByName('item');
        for (var i = 0; i < oItems.length; i++) {
            oItems[i].checked = status;
        }
    }

    //delAll功能
    $("body").on("click", ".news_del", function () {});
    function delAll() {
        var items = document.getElementsByName("item");
        if (items.length == 0) {
            alert('请选择要删除的记录');
        } else {
            if (confirmAct() === true) {
                var cur = $(".current:first").text();
                var select_date = $('.timer').text();

                var ids = '';
                $.each(items, function (i, item) {
                    if (item.checked) {
                        ids += $(item).parent().parent().find("td:first").attr("data-id") + ",";
                    }
                });
                var data = {
                    "ids": ids,
                    "curPage": cur,
                    "date": select_date,
                    "deptId": deptId
                };
                mondifyOpr("/salary/delete.json", data);
            }
        }

    }

    //update功能
    $("body").on("click", ".news_del", function () {});
    function update() {
        if (confirm('确定要执行此操作吗?')) {
            var form_data = $("#update_form").serialize();
            var parmArr = form_data.split("&");
            var cur = $(".current:first").text();
            var select_date = $('.timer').text();
            var data = {"form": form_data};
            mondifyOpr("/salary/update.json", data);
        }
    }

    function mondifyOpr(url, data) {
        $.ajax({
            url: url,
            type: "post",
            data: data,
            success: function (data) {
                if (data.code === 200 && data.data !== null && data.data !== '') {
                    var box = $('#listTable');
                    var cell = $('#eg').find('tr');
                    $('.timer').text(data.msg);
                    box.empty();
                    $.each(data.data, function (i, item) {
                        var newCell = cell.clone(true);
                        newCell.find('.pkid').attr("content", item.id);
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
                        newCell.find('.sj').text(item.payroll);
                        newCell.find('.ka').text(item.creditCard);
                        box.append(newCell);
                    });
                    //合计当前页面
                    $('tfoot td:eq(0)').text('合计');
                    $('.moneyhj').text(tj(box.find('.money')));
                    $('.hjhj').text(tj(box.find('.hj')));
                    $('.getMoneyhj').text(tj(box.find('.getMoney')));
                    $('.gshj').text(tj(box.find('.gs')));
                    $('.sjhj').text(tj(box.find('.sj')));
                    $('.addBox').hide();
                    renderDate(1);
                } else {
                    layer.alert("操作失败，请刷新后重试");
                }
            },
            error: function (data) {
                layer.alert("获取数据失败");
            }
        });
    }

    $("#jtt").change(function () {
        $(this).val(Number($(this).val()) * Number($(this).attr("content")));
    });

    //modify功能
    $("#eg").on("click", ".btn-info", function () {
        $('.addBox').show();
        var wageId = document.getElementById("wageId");
        var oNum = document.getElementById('num');
        var oUser = document.getElementById('username');
        var oPwd = document.getElementById('pwd');
        var oBirth = document.getElementById('birth');
        var oAddre = document.getElementById('addre');
        var gz = document.getElementById('bir');
        var jt = document.getElementById('jtt');
        var jj = document.getElementById('jjj');
        var fj = document.getElementById('fjj');
        var cc = document.getElementById('ccc');
        var oTr = obj.parentNode.parentNode;
        var aTd = oTr.getElementsByTagName('td');
        rowIndex = obj.parentNode.parentNode.rowIndex;
        wageId.value = $(aTd[0]).attr("content");
        oNum.value = aTd[1].innerHTML;
        oUser.value = aTd[3].innerHTML;
        oPwd.value = aTd[5].innerHTML;
        oBirth.value = aTd[11].innerHTML;
        oAddre.value = aTd[12].innerHTML;
        jt.value = aTd[14].innerHTML;
        jt.setAttribute("content", aTd[7].innerText);
        jj.value = aTd[19].innerHTML;
        fj.value = aTd[26].innerHTML;
//						Number(aTd[20].innerHTML) + Number(aTd[21].innerHTML) + Number(aTd[22].innerHTML) + Number(aTd[23].innerHTML) + Number(aTd[24].innerHTML) + Number(aTd[25].innerHTML);
        gz.value = aTd[4].innerHTML;
        cc.value = aTd[8].innerHTML;
    });
    // function modify(obj) {

        //alert(i);
    // }

    $('.yinchang').click(function () {
        $(".fj").show();
        $(".fjHide").hide();
    });

    $('.yinchang1').click(function () {
        $(".fjHide").show();
        $(".fj").hide();
    });

});