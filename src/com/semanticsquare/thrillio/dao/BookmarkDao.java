package com.semanticsquare.thrillio.dao;

import com.semanticsquare.thrillio.DataStore;
import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.entities.UserBookmark;
import com.semanticsquare.thrillio.entities.WebLink;
import com.semanticsquare.thrillio.util.EnumEntities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookmarkDao {

    public Map<EnumEntities, List<Bookmark>> getBookmarks() {

        return DataStore.getBookmarks();

    }

    public void saveUserBookmark(User user, Bookmark bookmark) {

        DataStore.saveUserBookmark(user, bookmark);
    }


    // In real application, we would have SQL or hibernate queries.
    public List<WebLink> getAllWebLinks() {
        List<WebLink> result;

        result = DataStore.getBookmarks().get(EnumEntities.WebLink).stream().map(b -> (WebLink) b).toList();

        return result;
    }

    public List<WebLink> getWebLinks(WebLink.DownloadStatus downloadStatus) {
        List<WebLink> result = new ArrayList<WebLink>();

        result = getAllWebLinks().stream().filter(webLink -> webLink.getDownloadStatus().equals(downloadStatus)).collect(Collectors.toList());

        return result;
    }

    public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
        DataStore.setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
    }

    public void updateKidFriendlyStatus(Bookmark bookmark) {

        int kidFriendlyStatus = bookmark.getKidFriendlyStatus().ordinal();
        long userId = bookmark.getKidFriendlyMarkedBy().getId();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/jid_thrillio", "root", "root");
             Statement stmt = conn.createStatement();) {
            String tableToUpdate = bookmark.getClass().getSimpleName();
            String sqlQuery = String.format("UPDATE %s SET kid_friendly_status = %s, kid_friendly_marked_by = %s WHERE id = %s", tableToUpdate, kidFriendlyStatus, userId, bookmark.getId());
            System.out.println("query (updateKidFriendlyStatus): " + sqlQuery);
            stmt.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sharedByInfo(Bookmark bookmark) {
        long userId = bookmark.getSharedBy().getId();
        String tableName = bookmark.getClass().getSimpleName();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/jid_thrillio", "root", "root");
             Statement stmt = conn.createStatement();) {
            String sqlQuery = String.format("UPDATE %s SET shared_by = %s WHERE id = %s", tableName, userId, bookmark.getId());
            System.out.println("query (sharedByInfo): " + sqlQuery);
            stmt.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
