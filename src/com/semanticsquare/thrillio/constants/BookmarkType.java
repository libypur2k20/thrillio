package com.semanticsquare.thrillio.constants;

public enum BookmarkType {
     BOOK("user_book", "book_id"),
     MOVIE("user_movie", "movie_id"),
     WEBLINK("user_weblink", "weblink_id"),;

    private final String columnName;
    private String tableName;

    private BookmarkType(String tableName, String columnName){
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public String getTableName(){
        return this.tableName;
    }

    public String getColumnName(){
        return this.columnName;
    }

}

