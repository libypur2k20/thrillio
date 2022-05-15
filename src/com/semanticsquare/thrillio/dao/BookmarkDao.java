package com.semanticsquare.thrillio.dao;

import com.semanticsquare.thrillio.DataStore;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;

public class BookmarkDao {

    public Bookmark[][] getBookmarks() {

        return DataStore.getBookmarks();

    }

    public void saveUserBookmark(User user, Bookmark bookmark) {

        DataStore.saveUserBookmark(user, bookmark);
    }
}
