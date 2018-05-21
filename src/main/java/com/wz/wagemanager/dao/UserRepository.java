package com.wz.wagemanager.dao;


import com.wz.wagemanager.entity.SysUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Transient;
import java.util.List;

public interface UserRepository extends JpaRepository<SysUser,String>,JpaSpecificationExecutor<SysUser> {

    SysUser findByUsername(String username);

    @Query("select s from SysUser s where s.username=?1 and s.sysDept.id=?2")
    SysUser getByUsernameAndSysDept(String username, String deptId);

    SysUser getUserById(String id);

//    Collection<GrantedAuthority> loadUserAuthorities(String username);

    void updateUser(SysUser sysUser);

    SysUser findByUsernameAndWorkNo(String username, String workNo);

    SysUser findByWorkNo(String workNo);

    int countSysUserBySysDept(String deptId);

    List<SysUser> findAllBySysDept(String deptId, Pageable pageRequest);

    @Modifying
    @Transient
    void deleteByIdIn(String[] ids);


    //    void batchInsert(List<SysUser> list);

//    void insertSysUser(SysUser sysUser);
}
