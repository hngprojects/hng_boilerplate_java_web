package com.example.hng4.DTOs;

import java.util.List;

import com.example.hng4.Models.Blog;

public class BlogSearchResponse {

    private int currentPage;
    private int totalPages;
    private int totalResults;
    private List<Blog> blogs;
    private Meta meta;

    public static class Meta {
        private boolean hasNext;
        private int total;
        private Integer nextPage;
        private Integer prevPage;

        // Getters and setters

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public Integer getNextPage() {
            return nextPage;
        }

        public void setNextPage(Integer nextPage) {
            this.nextPage = nextPage;
        }

        public Integer getPrevPage() {
            return prevPage;
        }

        public void setPrevPage(Integer prevPage) {
            this.prevPage = prevPage;
        }
    }

    // Getters and setters

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

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
