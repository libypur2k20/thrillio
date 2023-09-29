package com.semanticsquare.thrillio.bgjobs;

import com.semanticsquare.thrillio.dao.BookmarkDao;
import com.semanticsquare.thrillio.entities.WebLink;
import com.semanticsquare.thrillio.util.HttpConnect;
import com.semanticsquare.thrillio.util.IOUtil;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WebpageDownloaderTask implements Runnable {

    private static BookmarkDao dao = new BookmarkDao();

    private static final long TIME_FRAME = 3000000000L; // 3 seconds
    private static boolean downloadAll;

    ExecutorService downloadExecutor = Executors.newFixedThreadPool(5);

    private static class Downloader<T extends WebLink> implements Callable<T> {

        private final T weblink;

        public Downloader(T weblink) {
            this.weblink = weblink;
        }
        @Override
        public T call() throws Exception {
            try{
                if (!weblink.getUrl().endsWith(".pdf")) {
                    weblink.setDownloadStatus(WebLink.DownloadStatus.FAILED);
                    String htmlPage = HttpConnect.download(weblink.getUrl());
                    weblink.setHtmlPage(htmlPage);
                } else{
                    weblink.setDownloadStatus(WebLink.DownloadStatus.NOT_ELIGIBLE);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return weblink;
        }
    }

    public WebpageDownloaderTask(boolean downloadAll) {
        this.downloadAll = downloadAll;
    }
    @Override
    public void run() {

        while(!Thread.currentThread().isInterrupted()) {

            // Get weblinkks
            List<WebLink> weblinks = getWeblinks();

            // Download concurrently
            if (weblinks.size() > 0){
                download(weblinks);
            } else{
                System.out.println("No new weblinks to download");
            }

            // Wait
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        downloadExecutor.shutdown();
    }

    private void download(List<WebLink> weblinks) {

        List<Downloader<WebLink>> tasks = getTasks(weblinks);
        List<Future<WebLink>> futures = new ArrayList<>();

        try {
            futures = downloadExecutor.invokeAll(tasks, TIME_FRAME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Future<WebLink> future : futures) {
            try{
                if (!future.isCancelled()){
                    WebLink weblink = future.get();
                    String htmlPage = weblink.getHtmlPage();
                    if (htmlPage!= null) {
                        IOUtil.write(htmlPage, weblink.getId());
                        weblink.setDownloadStatus(WebLink.DownloadStatus.SUCCESS);
                        System.out.println("Download Success " + weblink.getUrl());
                    } else {
                        System.out.println("Webpage not downloaded " + weblink.getUrl());
                    }
                } else {
                    System.out.println("\n\nTask is cancelled --> " + Thread.currentThread());
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<Downloader<WebLink>> getTasks(List<WebLink> weblinks) {
        List<Downloader<WebLink>> tasks = new ArrayList<>();

        for (WebLink weblink : weblinks) {
            tasks.add(new Downloader<>(weblink));
        }

        return tasks;
    }

    private List<WebLink> getWeblinks() {
        List<WebLink> weblinks = null;

        if (downloadAll) {
            weblinks = dao.getAllWebLinks();
            downloadAll = false;
        } else{
            weblinks = dao.getWebLinks(WebLink.DownloadStatus.NOT_ATTEMPTED);
        }

        return weblinks;
    }
}
