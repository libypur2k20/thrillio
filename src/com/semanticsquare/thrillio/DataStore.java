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
import java.sql.*;
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

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            // new com.mysql.jdbc.Driver()
            // OR
            // System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
            // OR java.sql.DriverManager
            // DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // try-with-resources ==> conn & stmt will be closed automatically.
        // Connection string: <protocol>:<sub-protocol>:<data-source details>
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/jid_thrillio", "root", "root");
            Statement stmt = conn.createStatement();){
            loadUsers(stmt);
            loadWebLinks(stmt);
            loadMovies(stmt);
            loadBooks(stmt);
        } catch (SQLException e){
            e.printStackTrace();
        }


    }

//    private static void loadUsers(){
//
//        List<String> data = new ArrayList<>();
//
//        File file = null;
//        try {
//            file = FileResourceUtils.getFileFromResource("User");
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//        IOUtil.read(data,file);
//        for(String row : data){
//
//            String[] values = row.split("\t");
//
//            Gender gender = Gender.MALE;
//            if (values[5].equals("f")){
//                gender = Gender.FEMALE;
//            } else if (values[5].equals("t")){
//                gender = Gender.TRANSGENDER;
//            }
//
//            UserType userType = UserType.valueOf(values[6].toUpperCase());
//
//            User user = UserManager.getInstance().createUser(Long.parseLong(values[0]), values[1], values[2], values[3], values[4], gender, userType );
//            users.add(user);
//        }
//    }

    private static void loadUsers(Statement stmt) throws SQLException {

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM User");
        while (resultSet.next()){
            User user = UserManager.getInstance().createUser(
                    resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                     Gender.values()[resultSet.getByte("gender_id")],
                     UserType.values()[resultSet.getByte("user_type_id")]
            );

            users.add(user);
        }

    }

//    private static void loadWebLinks(){
//
//        List<String> data = new ArrayList<>();
//
//        try {
//            File file = FileResourceUtils.getFileFromResource("WebLink");
//            IOUtil.read(data,file);
//            for(String line : data){
//
//                String[] fields = line.split("\t");
//
//                 WebLink webLink = BookmarkManager.getInstance().createWebLink(Long.parseLong(fields[0]), fields[1], fields[2], fields[3]);
//                bookmarks.get(EnumEntities.WebLink).add(webLink);
//            }
//
//
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void loadWebLinks(Statement stmt) throws SQLException {

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM WebLink");

        while(resultSet.next()){
            WebLink webLink = BookmarkManager.getInstance().createWebLink(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("url"),
                    resultSet.getString("host")
            );
            bookmarks.get(EnumEntities.WebLink).add(webLink);
        }

    }

//    private static void loadMovies(){
//
//        List<String> data = new ArrayList<>();
//
//        try {
//            File file = FileResourceUtils.getFileFromResource("Movie");
//
//            IOUtil.read(data,file);
//            for(String line : data){
//
//                String[] fields = line.split("\t");
//
//                String[] cast = fields[3].split(",");
//                String[] directors = fields[4].split(",");
//
//                Movie movie = BookmarkManager.getInstance().createMovie(Long.parseLong(fields[0]), fields[1], Integer.parseInt(fields[2]), cast, directors, MovieGenre.valueOf(fields[5].toUpperCase().replace(" ","_")), Double.parseDouble(fields[6]));
//                bookmarks.get(EnumEntities.Movie).add(movie);
//            }
//
//
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void loadMovies(Statement stmt) throws SQLException {

        String sqlQuery = "select m.id, m.title, m.release_year, group_concat(DISTINCT a.name ORDER BY a.name ASC) as cast, group_concat(DISTINCT d.name ORDER BY d.name ASC) as directors, m.movie_genre_id, m.imdb_rating\n" +
                "from movie m\n" +
                "inner join movie_actor ma\n" +
                "on m.id = ma.movie_id\n" +
                "inner join actor a\n" +
                "on ma.actor_id = a.id\n" +
                "inner join movie_director md\n" +
                "on md.movie_id = m.id\n" +
                "inner join director d\n" +
                "on d.id = md.director_id\n" +
                "group by m.id, m.title, m.release_year, m.movie_genre_id, m.imdb_rating";

        ResultSet resultSet = stmt.executeQuery(sqlQuery);

        while(resultSet.next()){
            Movie movie = BookmarkManager.getInstance().createMovie(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getInt("release_year"),
                    resultSet.getString("cast").split(","),
                    resultSet.getString("directors").split(","),
                    MovieGenre.values()[resultSet.getByte("movie_genre_id")],
                    resultSet.getDouble("imdb_rating")
            );
            bookmarks.get(EnumEntities.Movie).add(movie);
        }

    }

//    private static void loadBooks(){
//
//        List<String> data = new ArrayList<>();
//
//        try {
//            File file = FileResourceUtils.getFileFromResource("Book");
//
//            IOUtil.read(data,file);
//            int rowCount = 0;
//            for(String line : data){
//
//                String[] fields = line.split("\t");
//                String[] authors = fields[4].split(",");
//
//                Book book = BookmarkManager.getInstance().createBook(Long.parseLong(fields[0]), fields[1], Integer.parseInt(fields[2]), fields[3], authors, BookGenre.valueOf(fields[5].toUpperCase()), Double.parseDouble(fields[6]));
//                bookmarks.get(EnumEntities.Book).add(book);
//            }
//
//
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void loadBooks(Statement stmt) throws SQLException {

        String sqlQuery = "select b.id, b.title, b.publication_year, p.name as publisher, group_concat(DISTINCT a.name ORDER BY a.name ASC) as authors, b.book_genre_id, b.amazon_rating\n" +
                "from book b\n" +
                "inner join book_author ba\n" +
                "on b.id = ba.book_id\n" +
                "inner join author a\n" +
                "on ba.author_id = a.id\n" +
                "inner join publisher p\n" +
                "on b.publisher_id = p.id\n" +
                "group by b.id, b.title, b.publication_year, p.name, b.book_genre_id, b.amazon_rating";


        ResultSet resultSet = stmt.executeQuery(sqlQuery);

        while(resultSet.next()){
            Book book = BookmarkManager.getInstance().createBook(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getInt("publication_year"),
                    resultSet.getString("publisher"),
                    resultSet.getString("authors").split(","),
                    BookGenre.values()[resultSet.getByte("book_genre_id")],
                    resultSet.getDouble("amazon_rating")
            );
            bookmarks.get(EnumEntities.Book).add(book);
        }

    }

    public static void saveUserBookmark(User user, Bookmark bookmark) {

        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/jid_thrillio", "root", "root");
            Statement stmt = conn.createStatement();){

            BookmarkType bookmarkType = bookmark.getBookmarkType();
            String tableName = bookmarkType.getTableName();
            String columnName = bookmarkType.getColumnName();
            String sqlQuery = String.format("INSERT INTO %s(user_id,%s) VALUES(%s,%s)",tableName,columnName, user.getId(), bookmark.getId());
            stmt.executeUpdate(sqlQuery);
        } catch (SQLException e){
            e.printStackTrace();
        }
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
