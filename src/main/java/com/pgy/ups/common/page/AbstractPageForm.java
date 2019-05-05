package com.pgy.ups.common.page;


import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.pgy.ups.common.model.Model;

public class AbstractPageForm<T extends AbstractPageForm<T>> extends Model {
    /**
     *
     */
    private static final long serialVersionUID = -3602263301503589062L;



    private  int pageNumber = 1;

    private int pageSize = 10;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @SuppressWarnings("unchecked")
    public final T enablePaging() {
        PageHelper.startPage(pageNumber,pageSize);
        return (T) this;
    }

}