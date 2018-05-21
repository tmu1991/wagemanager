package com.wz.wagemanager.dao;

import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WindowsTen
 * @date 2018/5/16 11:25
 * @description
 */

public interface DeclareRepository extends JpaRepository<SysDeclare,String>{

    Page<SysDeclare> findByUser(SysUser user, Pageable page);

    @Transactional
    @Modifying (clearAutomatically = true)
    @Query (value = "update sys_declare set ${key} =#{value} where id = #{id}",nativeQuery = true)
    Integer updateByProperty(@Param("key") String key, @Param("value") Object value, @Param("id") String id);

    SysDeclare findByProcessInstanceId(String processInstanceId);
}
