package com.semanticsquare.thrillio;

import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.constants.UserType;
import com.semanticsquare.thrillio.controllers.BookmarkController;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.partner.Shareable;
import com.semanticsquare.thrillio.util.EnumEntities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class View {

    public static void bookmark(User user, Map<EnumEntities, List<Bookmark>> bookmarks){

        System.out.println("\n" + user.getEmail() + "is bookmarking...");

        //TODO Calculate total number of items in bookmarks.

        List<EnumEntities> keysList = bookmarks.keySet().stream().collect(Collectors.toList());

            int typeOffset = (int)(Math.random() * bookmarks.keySet().size());
            EnumEntities key = keysList.get(typeOffset);

            int bookmarkOffset = (int)(Math.random() * bookmarks.get(key).size());
            Bookmark bookmark = bookmarks.get(key).get(bookmarkOffset);

            boolean isBookmarked = getBookmarkDecision(bookmark);
            if (isBookmarked){
                BookmarkController.getInstance().saveUserBookmark(user,bookmark);
                System.out.println("New Item Bookmarked -- " + bookmark);
            }

    }


    public static void browse(User user, Map<EnumEntities,List<Bookmark>> bookmarks){

        System.out.println("\n" + user.getEmail() + "is browsing items...");
        int bookmarkCount = 0;

        for(List<Bookmark> bookmarkList : bookmarks.values()){
            for(Bookmark bookmark : bookmarkList){
                //Bookmarking

                boolean isBookmarked = getBookmarkDecision(bookmark);
                if (isBookmarked){
                    bookmarkCount++;
                    BookmarkController.getInstance().saveUserBookmark(user,bookmark);
                    System.out.println("New Item Bookmarked -- " + bookmark);
                }


                // Mark as kid-friendly
                if (user.getUserType().equals(UserType.EDITOR) || user.getUserType().equals(UserType.CHIEF_EDITOR)){
                    // Mark as kid-friendly
                    if (bookmark.isKidFriendlyEligible() && bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)){
                        KidFriendlyStatus kidFriendlyStatus = getKidFriendlyStatusDecision(bookmark);
                        if (!kidFriendlyStatus.equals(KidFriendlyStatus.UNKNOWN)){
                            BookmarkController.getInstance().setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
                        }
                    }

                    //Sharing
                    if(bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED)){
                        if (bookmark instanceof Shareable){
                            boolean isShared = getShareDecision();
                            if (isShared){
                                BookmarkController.getInstance().share(user, bookmark);
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO: Below methods simulate user input. After IO, we take input via console.

    private static boolean getShareDecision() {
        return (Math.random() < 0.5 ? true : false);
    }

    private static KidFriendlyStatus getKidFriendlyStatusDecision(Bookmark bookmark) {

        double randomVal = Math.random();
        return (randomVal < 0.4 ? KidFriendlyStatus.APPROVED : (
                (randomVal >= 0.4 && randomVal < 0.8) ? KidFriendlyStatus.REJECTED :
                KidFriendlyStatus.UNKNOWN
                 ));
    }

    private static boolean getBookmarkDecision(Bookmark bookmark) {

        return (Math.random() < 0.5 ? true : false);

    }
}
