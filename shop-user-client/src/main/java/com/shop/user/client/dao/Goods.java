//package com.shop.user.client.dao;
//
//import org.springframework.data.elasticsearch.annotations.Document;
//
//@Document(indexName = "goodscat", type = "docs", shards = 1, replicas = 0)//选中elasticsearch索引
//public class Goods {
//
//    private Long id;
//    private String name;
//    private String brand;
//    private String isParent;
//    private String parentId;
//    private Long level;
//    private String pathid;
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getBrand() {
//		return brand;
//	}
//	public void setBrand(String brand) {
//		this.brand = brand;
//	}
//	public String getIsParent() {
//		return isParent;
//	}
//	public void setIsParent(String isParent) {
//		this.isParent = isParent;
//	}
//	public String getParentId() {
//		return parentId;
//	}
//	public void setParentId(String parentId) {
//		this.parentId = parentId;
//	}
//	public Long getLevel() {
//		return level;
//	}
//	public void setLevel(Long level) {
//		this.level = level;
//	}
//	public String getPathid() {
//		return pathid;
//	}
//	public void setPathid(String pathid) {
//		this.pathid = pathid;
//	}
//    
//}
