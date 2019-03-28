package com.da.goods.dao;

import com.da.goods.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    @Modifying //JPA规范
    @Query("update Goods g set g.count = g.count - ?2 where g.goodsId = ?1")    // ?2 第二个参数
    void updateCount(Long goodsId, Integer num);
}
