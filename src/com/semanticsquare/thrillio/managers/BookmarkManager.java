
package com.semanticsquare.thrillio.managers;

import com.semanticsquare.thrillio.dao.BookmarkDao;
import com.semanticsquare.thrillio.entities.*;

public class BookmarkManager {

	private static BookmarkDao dao = new BookmarkDao();
	
	private BookmarkManager() {}


	private static class SingletonInstance{
		private static BookmarkManager INSTANCE = new BookmarkManager();
	}
	
	public static BookmarkManager getInstance(){
		return SingletonInstance.INSTANCE;
	}
	
	
	public Book createBook(long id, String title, int publicationYear,
			String publisher, String[] authors, String genre, double amazonRating) {
		
		Book book = new Book();
		
		book.setId(id);
		book.setTitle(title);
		book.setPublicationYear(publicationYear);
		book.setPublisher(publisher);
		book.setAmazonRating(amazonRating);
		book.setAuthors(authors);
		book.setGenre(genre);
		
		return book;
	}
	
	
	public Movie createMovie(long id, String title, int releaseYear,
			String[] cast, String[] directors, String genre, double imdbRating) {
		
		Movie movie = new Movie();
		
		movie.setId(id);
		movie.setTitle(title);
		movie.setReleaseYear(releaseYear);
		movie.setCast(cast);
		movie.setDirectors(directors);
		movie.setGenre(genre);
		movie.setImdbRating(imdbRating);				
		
		return movie;
	}
	
	
	public WebLink createWebLink(long id, String title, String url, String host) {
		
		WebLink webLink = new WebLink();
		
		webLink.setId(id);
		webLink.setTitle(title);
		webLink.setUrl(url);
		webLink.setHost(host);
		
		return webLink;
	}


	public Bookmark[][] getBookmarks() {

		return dao.getBookmarks();

	}


	public void saveUserBookmark(User user, Bookmark bookmark) {

		dao.saveUserBookmark(user, bookmark);
	}
	

}
