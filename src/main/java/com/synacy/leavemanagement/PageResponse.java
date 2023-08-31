package com.synacy.leavemanagement;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final int totalCount;

    private final int pageNumber;

    private final List<T> content;

    public PageResponse(int totalCount, int pageNumber, List<T> content) {
        this.totalCount = totalCount;
        this.pageNumber = pageNumber;
        this.content = content;
    }
}
