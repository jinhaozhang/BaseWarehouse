package com.example.basewarehouse.bean;

import java.io.Serializable;

public class PageBean implements Serializable {
//     "currentPage": 0,
//             "pageSize": 0,
//             "totalPages": 0,
//             "totoalResults": 0
    public int currentPage;
    public int pageSize;
    public int totalPages;
    public int totoalResults;
//    {"pageNum":2,"pageSize":10,"totalRows":99,"totalPages":10}}
    public String pageNum;
    public String totalRows;
}
