layui.use(['form', 'layer','table', 'jquery', 'laypage'], function () {
    var table = layui.table,
        layer = layui.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    //查询
    $(".search_btn").click(function(){
        var IDNumber = $(".IDNumber").val();
        var userName=$(".userName").val();
        if(!IDNumber && !userName){
            layer.msg('查询内容不能为空');
        }else{
            renderDate(1,IDNumber,userName);
        }
    });

    renderDate(1);

    function renderDate(num,IDNumber,userName){
        layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.4});
        table.render({
            elem:'#demo',
            page:false,
            cols: [[
                {field:'year',width:65,fixed:'left','title':'年份',rowspan:2},
                {field:'month',width:60,fixed:'left','title':'月份',rowspan:2},
                {field:'IDNumber',width:90,fixed:'left','title':'身份证号',rowspan:2},
                {field:'workNo',width:90,fixed:'left','title':'考勤编号',rowspan:2},
                {field:'deptName', width:120,'title':'部门',rowspan:2},
                {field:'username',width: 100,'title':'姓名',rowspan:2},
                {field: 'base',width: 100,'title':'基本工资',rowspan:2},
                {field:'coeff',width:80,'title':'系数',rowspan:2},
                {field:'dailyWage', width:100,'title':'日工资',rowspan:2},
                {align:'center',colspan:'5','title':'计资天数'},
//                        {field:'workDay',width:90,'title':'考勤天数'},
                {field: 'seniority',width: 100,'title':'工龄工资',rowspan:2},
                {field:'subDay', width:120,'title':'顶班天数',rowspan:2},
                {field:'subWork',width: 100,'title':'顶班工资',rowspan:2},
                {field: 'allowance',width: 100,'title':'津贴',rowspan:2},
                {field:'grossPay',width:100,'title':'应发工资',rowspan:2},
                {field:'insurance', width:100,'title':'四险',rowspan:2},
                {field:'incomeTax',width: 100,'title':'个人所得税',rowspan:2},
                {field: 'accuFund',width: 100,'title':'一金',rowspan:2},
                {field: 'bonus',width: 120,'title':'奖金',rowspan:2},
                {field: 'fj',align:'center', width:120,'title':'罚金',colspan:6},
                {field: 'fj1',width: 120,'title':'罚金',rowspan:2},
                {field:'payroll',width: 100,'title':'实际工资',rowspan:2},
                {field: 'creditCard',width: 100,'title':'银行卡号',rowspan:2},
                {field: 'remark',width: 100,'title':'备注',rowspan:2}
            ],[
                {field:'attendance', width:80,'title':'考勤'},
                {field:'repairWork', width:80,'title':'补勤'},
                {field:'busTravel',width: 80,'title':'出差'},
                {field: 'holiday',width: 80,'title':'公休'},
                {field: 'workTotal',width: 80,'title':'合计'},
                {field:'late', width:100,'title':'迟到/早退'},
                {field:'otherDebit',width: 100,'title':'其他扣款'},
                {field: 'partyDue',width: 60,'title':'党费'},
                {field: 'loan',width: 60,'title':'借款'},
                {field: 'other',width: 60,'title':'其他'},
                {field: 'otherEl',width: 60,'title':'其他'}
            ]],
            skin: 'row',
            even: true,
            url: "salary/search.json",
            method:"post",
            where:{
                "IDNumber": IDNumber,
                "userName": userName,
                "curPage":num
            },
            text:{
                none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            },
//                    request: {
//                        "workNo": workNo,
//                        "userName": userName,
//                        pageName: 'curPage' //页码的参数名称，默认：page
//                    },
            response: {
                statusCode: 200 //成功的状态码，默认：0
//                        ,countName: 'page.totalCount'
            },
            done:function(res, curr, count) {
                var page=res.page;
                laypage.render({
                    elem: 'page',
                    limit:page.pageSize,
                    count: page.totalCount, //数据总数
                    curr: page.currentPage,
                    layout: ['count', 'prev', 'page', 'next', 'skip'],
                    jump: function (obj, first) {
                        if (!first) {
                            renderDate(obj.curr,$(".workNo").val(),$(".userName").val());
                        }
                    }
                });

                $("[data-field='fj1']").css('display','none');

                $.each($('table tbody tr'),function (index, item) {
                    $(item).find("[data-field='fj1'] div").text(
                        Number($(item).find("[data-field='late']").text())+
                        Number($(item).find("[data-field='otherDebit']").text())+
                        Number($(item).find("[data-field='partyDue']").text())+
                        Number($(item).find("[data-field='loan']").text())+
                        Number($(item).find("[data-field='other']").text())+
                        Number($(item).find("[data-field='otherEl']").text())
                    );
                });

                $("th[data-field='fj']").css('cursor','pointer').on('click',function () {
                    $("[data-field='late']").css('display','none');
                    $("[data-field='otherDebit']").css('display','none');
                    $("[data-field='partyDue']").css('display','none');
                    $("[data-field='loan']").css('display','none');
                    $("[data-field='other']").css('display','none');
                    $("[data-field='otherEl']").css('display','none');
                    $("[data-field='fj']").css('display','none');
                    $("[data-field='fj1']").css('display','');
                });

                $("th[data-field='fj1']").css('cursor','pointer').on('click',function () {
                    $("[data-field='fj1']").css('display','none');
                    $("[data-field='fj']").css('display','');
                    $("[data-field='late']").css('display','');
                    $("[data-field='otherDebit']").css('display','');
                    $("[data-field='partyDue']").css('display','');
                    $("[data-field='loan']").css('display','');
                    $("[data-field='other']").css('display','');
                    $("[data-field='otherEl']").css('display','');
                });
                layer.closeAll();
            }
            //,size: 'lg' //尺寸

        });
    }
});