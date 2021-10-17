package com.flower.ws.rest.params;

public class PaginationParams {
    private final int pageNumber;
    private final int pageSize;

    public PaginationParams(final Integer pageNumber,
                            final Integer pageSize) {
        this.pageNumber = pageNumber != null ? pageNumber : 1;
        this.pageSize = pageSize != null ? pageSize : 100;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}
