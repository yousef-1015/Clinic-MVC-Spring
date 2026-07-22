package com.example.clinicmvcspring.dtos;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class PaginatedListDTO<T> {

    @Schema(description = "Current page index (0-based)", example = "0")
    private int currentPage;
    @Schema(description = "Number of items per page", example = "5")
    private int pageSize;
    @Schema(description = "List of items on current page")
    private List<T> data;
    @Schema(description = "Total number of available pages", example = "3")
    private int totalPages;
    @Schema(description = "Total count of items across all pages", example = "15")
    private long totalItems;

    public PaginatedListDTO(List<T> data, int currentPage, int pageSize, long totalItems) {
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

    public long getTotalItems() {
        return this.totalItems;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public List<T> getData() {
        return this.data;

    }

}
