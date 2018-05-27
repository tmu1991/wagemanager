package com.wz.wagemanager;

import com.wz.wagemanager.entity.ActWork;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.service.RoleService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.DataUtil;
import com.wz.wagemanager.tools.ExcelUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WagemanagerApplicationTests {
	private static final String[] WORD_PROPERTIES = new String[]{"deptName", "workNo", "customNo", "username", "arrive", "reality"};
@Resource
private DeptService deptService;
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
//	@Test
//	public void test() throws Exception {
//		BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
//		String filePath="C:\\Users\\WindowsTen\\Desktop\\wz\\1\\2017-12.xls";
//		List<SysUser> sysUsers = new ArrayList<> ();
//		SysRole sysRole = roleService.findRoleById (3L);
//		ExcelUtil.readExcel(filePath, 1, 0, WORD_PROPERTIES, ActWork.class)
//				.forEach (actWork -> {
//					actWork.setUsername(DataUtil.deleteStrSpace(actWork.getUsername()));
////                    actWork.setArrive(actWork.getArrive().replace(".0",""));
//					actWork.setCustomNo(actWork.getCustomNo().replace(".0",""));
////                    actWork.setReality(actWork.getReality().replace(".0",""));
//					actWork.setWorkNo(actWork.getWorkNo().replace(".0",""));
//			SysUser byWorkNo = userService.findByWorkNo (actWork.getWorkNo ());
//			if(byWorkNo == null){
//				SysDept sysDept = deptService.getDeptByDeptName (actWork.getDeptName ());
//				if(sysDept == null){
//					sysDept = deptService.save (SysDept.builder().deptName (actWork.getDeptName ()).build());
//				}
//				sysUsers.add (SysUser.builder()
//						.base (new BigDecimal ("5700"))
//						.username (actWork.getUsername ())
//						.createDate (new Date ()).createUser ("admin")
//						.customNo (actWork.getCustomNo ())
//						.password (encoder.encode("123456"))
//						.status (1).workNo (actWork.getWorkNo ())
//						.sysDept (sysDept).sysRole (sysRole)
//						.build());
//			}
//		});
//		userService.batchInsert (sysUsers);
//	}
//	@Resource
//	private UserRepository userRepository;
//	@Test
//	public void selectAll(){
//		SysUser sysUser = userService.getByUsernameAndSysDept ("admin", 2L);
//		System.out.println (sysUser);
//	}
//
//	@Resource
//	private EntityManager entityManager;
//	@Test
//	public void contextLoads() {
//		String sql = "select h.deptId,h.deptName," +
//				"sum(h.grossPay),sum(h.subWork),sum(h.allowance),sum(h.insurance)," +
//				"sum(h.accuFund),sum(h.incomeTax),sum(h.payroll)" +
//				"from HiSalary h WHERE h.year =2017 AND h.month =12 GROUP BY h.deptId";
//		Query query = entityManager.createQuery (sql);
//		List resultList = query.getResultList ();
//		if (resultList != null) {
//			Iterator iterator = resultList.iterator ();
//			while (iterator.hasNext ()) {
//				System.out.println ((HiSalary)iterator.next ());
//			}
//		}
//	}

	@Test
	public void testUser(){
		Page<SysUser> byPage = userService.findByPage(null, null, null,new PageRequest(0, 10));
		System.out.println(byPage.getTotalElements());
		System.out.println(byPage.getTotalPages());
		byPage.getContent().forEach(System.out::println);
	}

	@Test
	public void insert() throws Exception {
		String filePath="D:\\Desktop\\wagemanager\\wzwork\\xls\\2017-12.xls";
		List<SysUser> users=new ArrayList<>();
		ExcelUtil.readExcel(filePath, 1, 0, WORD_PROPERTIES, ActWork.class)
				.forEach(actWork -> {
					SysUser user=new SysUser();
					user.setUsername(DataUtil.deleteStrSpace(actWork.getUsername()));
					user.setCustomNo(actWork.getCustomNo().replace(".0",""));
					user.setWorkNo(actWork.getWorkNo().replace(".0",""));
					user.setBase(randomBD());
					user.setCreditCard(randomCard());
					user.setSysDept(deptService.getDeptByDeptName(actWork.getDeptName()));
					user.setSysRole(roleService.findRoleById("3"));
					BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
					user.setPassword (passwordEncoder.encode ("123456"));
					user.setStatus(1);
					users.add(user);
				});
		userService.batchInsert(users);
	}

	private BigDecimal randomBD(){
		return BigDecimal.valueOf(RandomUtils.nextLong(1000,10000));
	}

	private String randomCard(){
		int len=16;
		String card="";
		for(int i=0;i<len;i++){
			card+=RandomUtils.nextInt(0,9);
		}
		return card;
	}
	@Resource
	private DeclareService declareService;
	@Test
	public void test(){
		declareService.findNotComplete(deptService.findById("2")).forEach(System.out::println);
	}
	@Test
	public void testUser1(){
		SysUser user=new SysUser();
		user.setUsername("law");
		user.setBase(randomBD());
		user.setCreditCard(randomCard());
		user.setSysRole(roleService.findRoleById("4028809d635c8d2f01635c8daa390004"));
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
		user.setPassword (passwordEncoder.encode ("123456"));
		user.setStatus(1);
		userService.insertUser (user);
	}
	@Resource
	private RepositoryService repositoryService;
	@Resource
	private RuntimeService runtimeService;
	@Test
	public void testRemove(){
		repositoryService.deleteDeployment("50001",true);
		repositoryService.deleteDeployment("47501",true);
		repositoryService.deleteDeployment("45001",true);
		repositoryService.deleteDeployment("42501",true);
		repositoryService.deleteDeployment("40001",true);
		repositoryService.deleteDeployment("37501",true);
		repositoryService.deleteDeployment("32501",true);
		repositoryService.deleteDeployment("22501",true);
		runtimeService.deleteProcessInstance("32505","");
		runtimeService.deleteProcessInstance("32513","");
		runtimeService.deleteProcessInstance("35001","");
	}
}
