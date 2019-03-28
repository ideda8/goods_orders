package com.da.goods;

import javax.persistence.*;

@Entity
@Table(name = "tb_goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long goodsId;
    @Column(name = "title")
    private String title;
    @Column(name = "price")
    private Double price;
    @Column(name = "count")
    private Integer count;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
