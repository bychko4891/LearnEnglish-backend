package top.e_learn.learnEnglish.payload.response;

import lombok.Getter;

@Getter
public class GetPaginationEntityPage <T> {

    private T t;

    private int totalPages;

    private long totalElements;

    private int currentPage;

    public GetPaginationEntityPage() {
    }

    public GetPaginationEntityPage(T t, int totalPages, long totalElements, int currentPage) {
        this.t = t;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
    }
}
