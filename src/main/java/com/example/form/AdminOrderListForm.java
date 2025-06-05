package com.example.form;

/**
 * 管理者注文一覧画面のフォーム
 * 
 * @author aya_ito
 */
public class AdminOrderListForm {

    private String searchField;
    private String searchValue1;
    private String searchValue2;
    private Integer page;
    private Integer id;

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchValue1() {
        return searchValue1;
    }

    public void setSearchValue1(String searchValue1) {
        this.searchValue1 = searchValue1;
    }

    public String getSearchValue2() {
        return searchValue2;
    }

    public void setSearchValue2(String searchValue2) {
        this.searchValue2 = searchValue2;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdminOrderListForm [ searchField=" + searchField + ", searchValue1=" + searchValue1 + ", searchValue1="
                + searchValue1 + ", page=" + page + ", id=" + id + "]";
    }
}
