package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.constants.MovieGenre;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void isKidFriendlyEligible() {

        // Test 1 - Movie genre is HORROR = false
        Movie movie = BookmarkManager.getInstance().createMovie(3000,"Citizen Kane",1941, new String[]{"Orson Welles","Joseph Cotten"},new String[]{"Orson Welles"}, MovieGenre.HORROR, 8.5);

        boolean isKindFriendlyEligible = movie.isKidFriendlyEligible();

        assertFalse(isKindFriendlyEligible, "Movie of genre HORROR must return false");

        // Test 2 - Movie genre is THRILLERS = false
        movie = BookmarkManager.getInstance().createMovie(3000,"Citizen Kane",1941, new String[]{"Orson Welles","Joseph Cotten"},new String[]{"Orson Welles"}, MovieGenre.THRILLERS, 8.5);

        isKindFriendlyEligible = movie.isKidFriendlyEligible();

        assertFalse(isKindFriendlyEligible, "Movie of genre THRILLERS must return false");

        // Test 3 - Otherwise = true
        movie = BookmarkManager.getInstance().createMovie(3000,"Citizen Kane",1941, new String[]{"Orson Welles","Joseph Cotten"},new String[]{"Orson Welles"}, MovieGenre.CLASSICS, 8.5);

        isKindFriendlyEligible = movie.isKidFriendlyEligible();

        assertTrue(isKindFriendlyEligible, "Movie of genre not equal to HORROR or THRILLERS must return true");
    }
}