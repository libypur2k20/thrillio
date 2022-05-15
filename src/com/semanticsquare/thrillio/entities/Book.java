package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.constants.BookGenre;

import java.util.Arrays;

public class Book extends Bookmark{
	
	private int publicationYear;
	
	private String publisher;
	
	private String[] authors;
	
	private String genre;
	
	private double amazonRating;

	public int getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String[] getAuthors() {
		return authors;
	}

	public void setAuthors(String[] authors) {
		this.authors = authors;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public double getAmazonRating() {
		return amazonRating;
	}

	public void setAmazonRating(double amazonRating) {
		this.amazonRating = amazonRating;
	}


	@Override
	public String toString() {
		return "Book{" +
				"publicationYear=" + publicationYear +
				", publisher='" + publisher + '\'' +
				", authors=" + Arrays.toString(authors) +
				", genre='" + genre + '\'' +
				", amazonRating=" + amazonRating +
				"} " + super.toString();
	}

	@Override
	public boolean isKidFriendlyEligible() {
		String bookGenre = this.getGenre();
		if(bookGenre == BookGenre.PHILOSOPHY || bookGenre == BookGenre.SELF_HELP){
			return false;
		}
		return true;
	}
}
