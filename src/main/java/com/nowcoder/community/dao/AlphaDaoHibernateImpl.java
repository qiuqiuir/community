package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @auther xurou
 * @date 2022/5/23
 */

@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
