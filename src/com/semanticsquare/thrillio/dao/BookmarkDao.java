package com.semanticsquare.thrillio.dao;

import com.semanticsquare.thrillio.DataStore;
import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.entities.UserBookmark;
import com.semanticsquare.thrillio.entities.WebLink;
import com.semanticsquare.thrillio.util.EnumEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookmarkDao {

    public Map<EnumEntities,List<Bookmark>> getBookmarks() {

        return DataStore.getBookmarks();

    }

    public void saveUserBookmark(User user, Bookmark bookmark) {

        DataStore.saveUserBookmark(user, bookmark);
    }



    // In real application, we would have SQL or hibernate queries.
    public List<WebLink> getAllWebLinks(){
        List<WebLink> result;

        result = DataStore.getBookmarks().get(EnumEntities.WebLink).stream().map(b -> (WebLink)b).toList();

        return result;
    }

    public List<WebLink> getWebLinks(WebLink.DownloadStatus downloadStatus){
        List<WebLink> result = new ArrayList<WebLink>();

        result = getAllWebLinks().stream().filter(webLink -> webLink.getDownloadStatus().equals(downloadStatus)).collect(Collectors.toList());

        return result;
    }

    public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
        DataStore.setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
    }
}
