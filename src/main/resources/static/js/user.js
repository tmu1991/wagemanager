layui.use(['form', 'layer', 'jquery', 'laypage'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;
    //加载页面数据
    renderDate(1);
    listRole();
    listDept();

    //查询
    $(".search_btn").click(function(){
        var username=$('#keyw').val();
        var deptId=$('#listDept').val();
        var roleId=$('#listRole').val();
//            if(!username && !deptId && !roleId){
//                layer.msg("请输入需要查询的内容");
//            } else{
        var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
        renderDate(1,username,deptId,roleId);
        layer.close(index);
//            }
    });
    //批量删除
    $("#delAll").click(function () {
        var $checkbox = $('.news_list tbody input[type="checkbox"][name="ids"]');
        var $checked = $('.news_list tbody input[type="checkbox"][name="ids"]:checked');
        var ids = '';
        $.each($checked, function (i, item) {
            if (i == $checked.length - 1) {
                ids += $(item).val();
            } else {
                ids += $(item).val() + ",";
            }
        });
        if ($checkbox.is(":checked")) {
            deleteBatch(ids);
        } else {
            layer.msg("请选择需要删除的用户");
        }
    });

    //全选
    form.on('checkbox(allChoose)', function (data) {
        var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
        child.each(function (index, item) {
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    var index;
    //操作
    $("body").on("click", ".news_edit", function () {  //编辑
        /* 再弹出添加界面 */
        index = layer.open({
            type: 1,
            title: "更新员工信息",
            skin: "myclass",
            area: ["50%"],
            content: $("#test").html(),
        });
        /* 渲染表单 */
        var _parent = $(this).parents('tr');
        $('.primaryKey').val(_parent.find('td:eq(0) input').val());
        $('.username').val(_parent.find('td:eq(1)').text());
        $('.workNo').val(_parent.find('td:eq(2)').text());
        $('.modDept').val(_parent.find('td:eq(3)').attr('data-id'));
        $('.modRole').val(_parent.find('td:eq(4)').attr('data-id'));
        var value = _parent.find('td:eq(6)').attr('data-id');
        if(value == 1){
            $('.status').prop('checked',true).val(value);
        }else{
            $('.status').prop('checked',false).val(value);
        }
        form.render(null,'userForm');
    });

    /* 点击添加按钮提出添加员工界面 */
    $("#addEmplpyeeBtn").click(function () {
        /* 再弹出添加界面 */
        index = layer.open({
            type: 1,
            title: "添加员工",
            skin: "myclass",
            area: ["50%"],
            content: $("#test").html()
        });
        /* 渲染表单 */
        $('.primaryKey').val('');
        $('.username').val('');
        $('.workNo').val('');
        $('.deptName').val('');
        $('.roleName').val('');
        $('.status').val('1');
        form.render(null,'userForm');
    });

    //监听提交
    form.on('submit(demo1)', function(data){
        $.post('user/insert.json',data.field,function (result) {
            var code = result.code;
            if (code == 200) {
                layer.msg("操作成功");
                var username=$('#keyw').val();
                var deptId=$('#listDept').val();
                var roleId=$('#listRole').val();
                renderDate($('.layui-laypage-curr em:last').text(),username,deptId,roleId);
                layer.close(index);
            } else {
                layer.msg("操作失败，请重试");
            }
        },'json');
        return false;
    });

    //监听指定开关
    form.on('switch(switchTest)', function(obj){
        var switchVal =this.checked ? '1' : '0';
        $('#addEmployeeForm input[name="status"]').val(switchVal);
    });

    $("body").on("click", ".news_del", function () {  //删除
        var _this = $(this);
        deleteBatch(_this.attr("data-id"));
    });

//            });
    function renderDate(curPage,username,deptId,roleId) {
        $.post("user/list.json", {"curPage": curPage,"username":username,"deptId":deptId,"roleId":roleId}, function (result) {
            var code = result.code,
                listData = result.data,
                page = result.page;
            if (code == 200) {
                //渲染数据
                var dataHtml = '';
                if (listData.length != 0) {
                    $.each(listData, function (i, item) {
                        dataHtml += '<tr>'
                            + '<td><input value="' + item.id + '" type="checkbox" name="ids" lay-skin="primary" lay-filter="choose"></td>'
                            + '<td>' + item.username + '</td><td>' + item.workNo + '</td>';
                        var dept = item.sysDept, role = item.sysRole;
                        if (dept && dept != null) {
                            dataHtml += '<td data-id="' + dept.id + '">' + dept.deptName + '</td>';
                        } else {
                            dataHtml += '<td></td>';
                        }
                        if (role && role != null) {
                            dataHtml += '<td data-id="' + role.id + '">' + role.roleName + '</td>';
                        } else {
                            dataHtml += '<td></td>';
                        }
                        if (item.loginTime && item.loginTime != null) {
                            dataHtml += '<td>' + item.loginTime + '</td>';
                        } else {
                            dataHtml += '<td></td>';
                        }

                        if (item.status == 0) {
                            dataHtml += '<td data-id="'+item.status+'" style="color:#f00">禁用</td>';
                        } else {
                            dataHtml += '<td data-id="'+item.status+'">正常</td>';
                        }
                        dataHtml += '<td>'
                            + '<a class="layui-btn layui-btn-sm news_edit"><i class="iconfont icon-edit"></i> 编辑</a>'
                            + '<a class="layui-btn layui-btn-danger layui-btn-sm news_del" data-id="'+item.id+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                            + '</td>'
                            + '</tr>';
                    });
                } else {
                    dataHtml = '<tr><td colspan="8">暂无数据</td></tr>';
                }
                $(".news_content").html(dataHtml);
                $('.news_list thead input[type="checkbox"]').prop("checked", false);
                form.render('checkbox');
                //分页
                //总页数大于页码总数
                laypage.render({
                    elem: 'page',
                    limit:page.pageSize,
                    count: page.totalCount, //数据总数
                    curr: page.currentPage,
                    layout: ['count', 'prev', 'page', 'next', 'skip'],
                    jump: function (obj, first) {
                        if (!first) {
                            renderDate(obj.curr,username,deptId,roleId);
                        }
                    }
                })
            } else {
                var msg = result.msg;
                if (msg) {
                    lay.alert(msg);
                } else {
                    lay.alert('查询数据失败');
                }
            }
        });
    }

    function deleteBatch(ids) {
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '提示信息'}, function () {
            var index = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            //删除数据
            $.post("user/delete.json", {'ids': ids}, function (result) {
                var code = result.code;
                $('.news_list thead input[type="checkbox"]').prop("checked", false);
                form.render();
                layer.close(index);
                if (code == 200) {
                    layer.msg("删除成功");
                } else {
                    layer.msg("删除失败，请重试");
                }
                var username=$('#keyw').val();
                var deptId=$('#listDept').val();
                var roleId=$('#listRole').val();
                renderDate($('.layui-laypage-curr em:last').text(),username,deptId,roleId);
            });
        })
    }

    function listDept() {
        $.get('dept/all.json', function (result) {
            var departments = result.data;
            var listDept = $('#listDept');
            var modDept=$('#modDept');
            var optHtml="<option value=''></option>";
            $.each(departments, function (index, data) {
                optHtml+= "<option value='" + data.id + "'>" + data.deptName + "</option>";
            });
            listDept.html(optHtml);
            modDept.html(optHtml);
        });
    }

    function listRole() {
        $.get('role/all.json', function (result) {
            var roles = result.data;
            var listRole = $('#listRole');
            var modRole = $('#modRole');
            var optHtml="<option value=''></option>";
            $.each(roles, function (index, data) {
                var roleNmae = data.roleName;
                if (data.dept) {
                    roleNmae = data.dept.deptName + roleNmae;
                }
                optHtml+= "<option value='" + data.id + "'>" + roleNmae + "</option>";
            });
            listRole.html(optHtml);
            modRole.html(optHtml);
        });
    }
});