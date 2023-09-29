package com.semanticsquare.thrillio;

import com.semanticsquare.thrillio.constants.*;
import com.semanticsquare.thrillio.entities.*;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import com.semanticsquare.thrillio.managers.UserManager;
import com.semanticsquare.thrillio.util.EnumEntities;
import com.semanticsquare.thrillio.util.FileResourceUtils;
import com.semanticsquare.thrillio.util.IOUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DataStore {

    private static boolean initialized = false;


    private static List<User> users = new ArrayList<>();
    public static List<User> getUsers() {
        return users;
    }

    private static Map<EnumEntities,List<Bookmark>> bookmarks = new HashMap<>();
    public static Map<EnumEntities,List<Bookmark>> getBookmarks() {
        return bookmarks;
    }

    private static List<UserBookmark> userBookmarks = new ArrayList<>();

    public static void loadData(){
        if (initialized == false) {
            bookmarks.put(EnumEntities.WebLink, new ArrayList<>());
            bookmarks.put(EnumEntities.Book, new ArrayList<>());
            bookmarks.put(EnumEntities.Movie, new ArrayList<>());
            initialized = true;
        }
        loadUsers();
        loadWebLinks();
        loadMovies();
        loadBooks();
    }

    private static void loadUsers(){

        List<String> data = new ArrayList<>();

        File file = null;
        try {
            file = FileResourceUtils.getFileFromResource("User");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        IOUtil.read(data,file);
        for(String row : data){

            String[] values = row.split("\t");

            Gender gender = Gender.MALE;
            if (values[5].equals("f")){
                gender = Gender.FEMALE;
            } else if (values[5].equals("t")){
                gender = Gender.TRANSGENDER;
            }

            UserType userType = UserType.valueOf(values[6].toUpperCase());

            User user = UserManager.getInstance().createUser(Long.parseLong(values[0]), values[1], values[2], values[3], values[4], gender, userType );
            users.add(user);
        }
    }

    private static void loadWebLinks(){

        List<String> data = new ArrayList<>();

        try {
            File file = FileResourceUtils.getFileFromResource("WebLink");
            IOUtil.read(data,file);
            for(String line : data){

                String[] fields = line.split("\t");

                 WebLink webLink = BookmarkManager.getInstance().createWebLink(Long.parseLong(fields[0]), fields[1], fields[2], fields[3]);
                bookmarks.get(EnumEntities.WebLink).add(webLink);
            }


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadMovies(){

        List<String> data = new ArrayList<>();

        try {
            File file = FileResourceUtils.getFileFromResource("Movie");

            IOUtil.read(data,file);
            for(String line : data){

                String[] fields = line.split("\t");

                String[] cast = fields[3].split(",");
                String[] directors = fields[4].split(",");

                Movie movie = BookmarkManager.getInstance().createMovie(Long.parseLong(fields[0]), fields[1], Integer.parseInt(fields[2]), cast, directors, MovieGenre.valueOf(fields[5].toUpperCase().replace(" ","_")), Double.parseDouble(fields[6]));
                bookmarks.get(EnumEntities.Movie).add(movie);
            }


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadBooks(){

        List<String> data = new ArrayList<>();

        try {
            File file = FileResourceUtils.getFileFromResource("Book");

            IOUtil.read(data,file);
            int rowCount = 0;
            for(String line : data){

                String[] fields = line.split("\t");
                String[] authors = fields[4].split(",");

                Book book = BookmarkManager.getInstance().createBook(Long.parseLong(fields[0]), fields[1], Integer.parseInt(fields[2]), fields[3], authors, BookGenre.valueOf(fields[5].toUpperCase()), Double.parseDouble(fields[6]));
                bookmarks.get(EnumEntities.Book).add(book);
            }


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveUserBookmark(User user, Bookmark bookmark) {

        UserBookmark userBookmark = new UserBookmark();
        userBookmark.setUser(user);
        userBookmark.setBookmark(bookmark);
        userBookmarks.add(userBookmark);
    }


    public static void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
        for (EnumEntities key:bookmarks.keySet()) {
            bookmarks.get(key).stream().findFirst().ifPresent(b -> {
                b.setKidFriendlyStatus(kidFriendlyStatus);
                b.setKidFriendlyMarkedBy(user);
            });
        }
    }
}
