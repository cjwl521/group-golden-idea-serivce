package com.chinasoft.goldidea.common;

public class PageModel {
	
    private int curPage;             //当前页  
	private int pageCount;           //总页数
	private int pageSize=500;         //每页显示多少条
	private int count;               //总记录
	
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPageCount() {
		pageCount = (count + this.pageSize -1) / this.pageSize;
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
 
	
	
}
