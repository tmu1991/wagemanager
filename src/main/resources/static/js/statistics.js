layui.use(['table','jquery','layer','laydate'],function () {
    var $ = layui.jquery,
        laydate = layui.laydate,
        layer=layui.layer;

    renderDate();

    laydate.render({
        elem: '#dateMonth',
        type:'month',
        btns: ['clear', 'confirm','now'],
        theme: 'molv',
        done: function(value, date, endDate){ //选择日期完毕的回调
            renderDate(date.year,date.month);
        }
    });//绑定元素

    function renderDate(year,month) {
        var cxindex = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
        $.ajax({
            url:"salary/statistics.json",
            data:{"year":year,"month":month},
            type:"post",
            success:function (result) {
                var code = result.code,
                    listData = result.data;
                if(code == 200){
                    //渲染数据
                    var dataHtml = '';
                    var grossPay=0,subWork=0,
                        allowance=0,insurance=0,
                        incomeTax=0,accuFund=0,payroll=0;
                    if (listData.length != 0) {
                        $.each(listData,function (i, item) {
                            dataHtml+="<tr>" +
                                "<td>"+item.deptName+"</td>" +
                                "<td>"+item.grossPay+"</td>" +
                                "<td>"+item.subWork+ "</td>" +
                                "<td>"+item.allowance+"</td>" +
                                "<td>"+item.insurance+"</td>" +
                                "<td>"+item.incomeTax+"</td>" +
                                "<td>"+item.accuFund+"</td>" +
                                "<td>"+item.payroll+"</td>" +
                                "</tr>";
                            grossPay+=item.grossPay;
                            subWork+=item.subWork;
                            allowance+=item.allowance;
                            insurance+=item.insurance;
                            incomeTax+=item.incomeTax;
                            accuFund+=item.accuFund;
                            payroll+=item.payroll;
                        });
                    }
                    $(".sum_time").text(result.msg);
                    $(".news_content").html(dataHtml);
                    $(".statistics_sum").html("<td>合计</td><td>"+grossPay+"</td><td>"+subWork+"</td><td>"+allowance+"</td><td>"+insurance+"</td><td>"+incomeTax+"</td><td>"+accuFund+"</td><td>"+payroll+"</td>");
                }else{
                    layer.close(cxindex)
                    var msg = result.msg;
                    if (msg) {
                        layer.alert(msg);
                    } else {
                        layer.alert('查询数据失败');
                    }
                }
            },
            error: function(data){
                alert("获取数据失败");
            }
        });
        layer.close(cxindex)
    }
});