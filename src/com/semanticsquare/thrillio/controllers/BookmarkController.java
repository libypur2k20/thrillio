package com.semanticsquare.thrillio.controllers;

import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.managers.BookmarkManager;

public class BookmarkController {

    private BookmarkController(){}

    private static class BookMarkControllerInstance{
        public static BookmarkController INSTANCE = new BookmarkController();
    }

    public static BookmarkController getInstance(){
        return BookMarkControllerInstance.INSTANCE;
    }

    public void saveUserBookmark(User user, Bookmark bookmark){
        BookmarkManager.getInstance().saveUserBookmark(user,bookmark);
    }
}