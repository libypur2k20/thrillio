package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.managers.BookmarkManager;

import static org.junit.jupiter.api.Assertions.*;

class WebLinkTest {

    @org.junit.jupiter.api.Test
    void isKidFriendlyEligible() {

        // Test 1 - porn in url = false
        WebLink weblink = BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-porn--part-2.html","http://www.javaworld.com");

        boolean isKindFriendlyEligible = weblink.isKidFriendlyEligible();

        assertFalse(isKindFriendlyEligible,"For porn in url - isKidFriendlyEligible must return false");

        // Test 2 - porn in title = false
        weblink = BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger porn, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-part-2.html","http://www.javaworld.com");

        isKindFriendlyEligible = weblink.isKidFriendlyEligible();

        assertFalse(isKindFriendlyEligible,"For porn in title - isKidFriendlyEligible must return false");

        // Test 3 - adult in host = false
        weblink = BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-part-2.html","http://www.javaworld.adult.com");

        isKindFriendlyEligible = weblink.isKidFriendlyEligible();

        assertFalse(isKindFriendlyEligible,"For adult in host - isKidFriendlyEligible must return false");

        // Test 4 - adult in url, but not in host part = true
        weblink = BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-adult-part-2.html","http://www.javaworld.com");

        isKindFriendlyEligible = weblink.isKidFriendlyEligible();

        assertTrue(isKindFriendlyEligible,"For adult int url, but not in host - isKidFriendlyEligible must return true");

        // Test 5 - adult in title only = true
        weblink = BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger adult, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-part-2.html","http://www.javaworld.com");

        isKindFriendlyEligible = weblink.isKidFriendlyEligible();

        assertTrue(isKindFriendlyEligible,"For adult in title only - isKidFriendlyEligible must return true");
    }
}