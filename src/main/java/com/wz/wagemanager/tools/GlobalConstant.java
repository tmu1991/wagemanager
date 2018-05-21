package com.wz.wagemanager.tools;

/**
 * @author WindowsTen
 * @date 2018/2/23 10:10
 * @description
 */

public interface GlobalConstant {


    String DEFAULT_ROLE="普通用户";

    String DEFAULT_PASSWORD="888888";

    String SESSION_USER_KEY="session_user_key";

    int ERROR_CODE = 500;

    int SUCCESS_CODE = 200;

    String[] properties=new String[]{"deptId","deptName","grossPay","subWork","allowance","insurance","accuFund","incomeTax","payroll"};

    String DEFAULT_FAIL_MSG="操作失败";

    String DEFAULT_DECLARE_SORT_FIELD="declareDate";

    String DEFAULT_SORT_ORDER="DESC";

    String PROCESS_KEY="salaryDeclare";

    String DEFAULT_PAGE_SIZE="10";

    String DEFUALT_CUR_PAGE="1";
}
