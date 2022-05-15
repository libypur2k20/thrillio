package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.constants.BookGenre;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void isKidFriendlyEligible() {

        // Test 1 - Book genre is PHILOSOPHY = false
        Book book = BookmarkManager.getInstance().createBook(4000, "Walden",	1854, "Wilder Publications", new String[]{"Henry David Thoreau"}, BookGenre.PHILOSOPHY,	4.3);

        boolean isKindFriendlyEligible = book.isKidFriendlyEligible();

        assertFalse(isKindFriendlyEligible,"Book of genre PHILOSOPHY must return false");

        // Test 2 - Book genre is SELF-HELP = false
        book = BookmarkManager.getInstance().createBook(4000, "Walden",	1854, "Wilder Publications", new String[]{"Henry David Thoreau"}, BookGenre.SELF_HELP,	4.3);

        isKindFriendlyEligible = book.isKidFriendlyEligible();

        assertFalse(isKindFriendlyEligible,"Book of genre SELF_HELP must return false");

        // Test 3 - Otherwise = true
        book = BookmarkManager.getInstance().createBook(4000, "Walden",	1854, "Wilder Publications", new String[]{"Henry David Thoreau"}, BookGenre.TECHNICAL,	4.3);

        isKindFriendlyEligible = book.isKidFriendlyEligible();

        assertTrue(isKindFriendlyEligible,"Book of genre not equal to PHILOSOPHY OR SELF_HELP must return true");
    }
}