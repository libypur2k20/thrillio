package com.semanticsquare.thrillio;

import com.semanticsquare.thrillio.controllers.BookmarkController;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;

public class View {

    public static void bookmark(User user, Bookmark[][] bookmarks){

        System.out.println("\n" + user.getEmail() + "is bookmarking...");

        for(int i=0; i < DataStore.MAX_BOOKMARKS_PER_USER;i++){
            int typeOffset = (int)(Math.random() * DataStore.MAX_BOOKMARK_TYPES);
            int bookmarkOffset = (int)(Math.random() * DataStore.MAX_BOOKMARKS_PER_TYPE);

            Bookmark bookmark = bookmarks[typeOffset][bookmarkOffset];

            BookmarkController.getInstance().saveUserBookmark(user, bookmark);

            System.out.println(bookmark);
        }
    }
}
