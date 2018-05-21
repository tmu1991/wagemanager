package com.wz.wagemanager.tools;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CriteriaUtils<T> {
    public static <T> Specification<T> getSpe(Map<String,Object> properties){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            properties.forEach((k,v) -> {
                if(v != null && !"".equals(v)){
                    list.add (criteriaBuilder.equal(root.get(k).as(v.getClass()),v));
                }
            });
            Predicate[] p = new Predicate[list.size()];
            criteriaQuery.where(criteriaBuilder.and(list.toArray(p)));
            return criteriaQuery.getRestriction();
        };
    }
}
