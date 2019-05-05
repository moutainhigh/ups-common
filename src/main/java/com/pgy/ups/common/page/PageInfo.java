package com.pgy.ups.common.page;

import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.pgy.ups.common.model.Model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageInfo<T> extends Model {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private Long pageSize;

    private Long total=0L;

    private List<T> list;

    private String html;

    public  PageInfo(List<T> recordList) {
        if(recordList == null){
            return;
        }
        list = new ArrayList<T>();
        list.addAll(recordList);
        Pagination pagination = PageHelper.getPagination();
        total = pagination.getTotal();
        this.pageSize = new Long(pagination.getSize());
        PageHelper.remove();
    }
    
    public PageInfo(Page<T> page) {
    	 list=page.getContent();
    	 total=page.getTotalElements();
    	 pageSize=Long.valueOf(page.getSize());
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Long getPageSize() {
        return pageSize;
    }


    public Long getTotal() {
        return total;
    }


    public List<T> getList() {
        return list;
    }


}

