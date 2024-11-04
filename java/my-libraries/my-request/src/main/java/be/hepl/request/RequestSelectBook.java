package src.main.java.be.hepl.request;

import be.hepl.model.viewmodel.BookSearchVM;

public class RequestSelectBook implements Request {
    private BookSearchVM searchFilters;

    public BookSearchVM getSearchFilters() {
        return searchFilters;
    }

    public void setSearchFilters(BookSearchVM searchFilters) {
        this.searchFilters = searchFilters;
    }

    @Override
    public String toString() {
        return "RequestSelectBook [searchFilters=" + searchFilters + "]";
    }
}
