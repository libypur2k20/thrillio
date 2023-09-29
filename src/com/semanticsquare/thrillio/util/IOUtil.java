package com.semanticsquare.thrillio.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IOUtil {

    public static void read(List<String> data, File file) {

        try(BufferedReader reader = new BufferedReader(new InputStreamReader( new FileInputStream(file), "UTF-8"))){
            String line;
            while((line = reader.readLine()) != null){
                data.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void read(List<String> data, String fileName) throws IOException {
        File file = new File(fileName);
        read(data,file);
    }

    public static String read(InputStream in) {

        StringBuilder sb = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))){
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public static void write(String webPage, Long bookmarkId) {

        String filePath = Paths.get("").toAbsolutePath().toString();
        String fileName = Path.of(filePath,new String[]{"pages", String.valueOf(bookmarkId) + ".html"}).toString();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, Charset.forName("UTF-8")))){
            writer.write(webPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
