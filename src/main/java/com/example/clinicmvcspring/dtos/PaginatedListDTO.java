package com.example.clinicmvcspring.dtos;

import java.util.List;

public class PaginatedListDTO<T> {

    private int currentPage;
    private int pageSize;
    private List<T> data;
    private int totalPages;
    private int totalItems;

    public PaginatedListDTO(List<T> data, int currentPage, int pageSize, int totalItems) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (pageSize > 0) ? (int) Math.ceil((double) totalItems / pageSize) : 0;
    }

    

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setData(List<T> data) {
        this.data = data;

    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public int getTotalItems() {
        return this.totalItems;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public List<T> getData() {
        return this.data;

    }

}
