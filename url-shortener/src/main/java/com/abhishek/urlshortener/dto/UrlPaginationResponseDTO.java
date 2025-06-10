package com.abhishek.urlshortener.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class UrlPaginationResponseDTO {

    private List<ExpiringUrlDTO> urls;
    private PaginationInfo pagination;

    public UrlPaginationResponseDTO(List<ExpiringUrlDTO> urls, Page<?> pageData) {
        this.urls = urls;
        this.pagination = new PaginationInfo(pageData.getNumber() + 1, pageData.getTotalPages(),
                pageData.getTotalElements());
    }

    public List<ExpiringUrlDTO> getUrls() {
        return urls;
    }

    public void setUrls(List<ExpiringUrlDTO> urls) {
        this.urls = urls;
    }

    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    public static class PaginationInfo {
        private int currentPage;
        private int totalPages;
        private long totalUrls;

        public PaginationInfo(int currentPage, int totalPages, long totalUrls) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.totalUrls = totalUrls;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public long getTotalUrls() {
            return totalUrls;
        }

        public void setTotalUrls(long totalUrls) {
            this.totalUrls = totalUrls;
        }
    }

}
