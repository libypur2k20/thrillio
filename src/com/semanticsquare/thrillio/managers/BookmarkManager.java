
package com.semanticsquare.thrillio.managers;

import com.semanticsquare.thrillio.constants.BookGenre;
import com.semanticsquare.thrillio.constants.BookmarkType;
import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.constants.MovieGenre;
import com.semanticsquare.thrillio.dao.BookmarkDao;
import com.semanticsquare.thrillio.entities.*;
import com.semanticsquare.thrillio.partner.Shareable;
import com.semanticsquare.thrillio.util.EnumEntities;
import com.semanticsquare.thrillio.util.HttpConnect;
import com.semanticsquare.thrillio.util.IOUtil;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

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
						   String publisher, String[] authors, BookGenre genre, double amazonRating) {
		
		Book book = new Book();

		book.setBookmarkType(BookmarkType.BOOK);
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
							 String[] cast, String[] directors, MovieGenre genre, double imdbRating) {
		
		Movie movie = new Movie();

		movie.setBookmarkType(BookmarkType.MOVIE);
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

		webLink.setBookmarkType(BookmarkType.WEBLINK);
		webLink.setId(id);
		webLink.setTitle(title);
		webLink.setUrl(url);
		webLink.setHost(host);
		webLink.setDownloadStatus(WebLink.DownloadStatus.NOT_ATTEMPTED);
		
		return webLink;
	}


	public Map<EnumEntities, List<Bookmark>> getBookmarks() {

		return dao.getBookmarks();

	}


	public void saveUserBookmark(User user, Bookmark bookmark) {
		dao.saveUserBookmark(user, bookmark);
	}

	private void downloadWebLink(String url, Long bookmarkId) {

		if (!url.endsWith(".pdf")){
			try {
				String webPage = HttpConnect.download(url);
				if (webPage != null){
					IOUtil.write(webPage, bookmarkId);
				}
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {

		bookmark.setKidFriendlyStatus(kidFriendlyStatus);
		bookmark.setKidFriendlyMarkedBy(user);

		dao.updateKidFriendlyStatus(bookmark);
		System.out.println("Kid-friendly status: " + kidFriendlyStatus + ", Marked by: " + user.getEmail() + ", " + bookmark);
	}

	public void share(User user, Bookmark bookmark) {
		bookmark.setSharedBy(user);

		System.out.println("Data to be shared:\n " + ((Shareable) bookmark).getItemData());

		dao.sharedByInfo(bookmark);
	}

}
	


