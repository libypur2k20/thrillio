package com.semanticsquare.thrillio;


import com.semanticsquare.thrillio.bgjobs.WebpageDownloaderTask;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import com.semanticsquare.thrillio.managers.UserManager;
import com.semanticsquare.thrillio.util.EnumEntities;

import java.util.List;
import java.util.Map;

public class Launch {

    private static List<User> users;
    private static Map<EnumEntities,List<Bookmark>> bookmarks;

    public static void main(String[] args){

        loadData();
        //startBookmarking();
        startBrowsing();

        // Background Jobs.
        runDownloaderJob();

    }

    private static void start() {

    }

    private static void runDownloaderJob(){
        WebpageDownloaderTask task = new WebpageDownloaderTask(true);
        (new Thread(task)).start();

    }

    private static void loadData(){

        System.out.println("1. Loading Data...");
        DataStore.loadData();

        users = UserManager.getInstance().getUsers();
        bookmarks = BookmarkManager.getInstance().getBookmarks();

        System.out.println(users);
        System.out.println(bookmarks);

        //System.out.println("2. Printing Data...");
        //printUserData();
        //printBookmarkData();
    }


    private static void printUserData(){

        for(User user:users){
            System.out.println(user);
        }
    }

    private static void printBookmarkData(){
        for(EnumEntities key : bookmarks.keySet()){
            for(Bookmark bookmark:bookmarks.get(key)){
                System.out.println(bookmark);
            }
        }
    }


    private static void startBookmarking(){
        System.out.println("\n2. Bookmarking...");
        for(User user:users) {
            View.bookmark(user, bookmarks);
        }
    }

    private static void startBrowsing(){
        //System.out.println("\n2. Bookmarking...");
        for(User user:users) {
            View.browse(user,bookmarks);
        }
    }

}
