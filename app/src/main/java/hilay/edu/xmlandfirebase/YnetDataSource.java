package hilay.edu.xmlandfirebase;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Android2017 on 6/2/2017.
 */

public class YnetDataSource {
    public interface OnYnetArriveListener {
        void onYnetArrived(List<Ynet> data, Exception e);
    }

    public static void getYnet(final OnYnetArriveListener listener) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.ynet.co.il/Integration/StoryRss2.xml");
                    URLConnection con = url.openConnection();
                    InputStream in = con.getInputStream();
                    String xml = IO.read(in, "windows-1255");
                    List<YnetDataSource.Ynet> result = parse(xml);
                    listener.onYnetArrived(result, null);
                } catch (Exception e) {
                    listener.onYnetArrived(null, e);
                }
            }
        });

    }

    private static List<Ynet> parse(String xml) {
        Document document = Jsoup.parse(xml);
        List<Ynet> ynetNews = new ArrayList<>();
        Elements allYnetNews = document.getElementsByTag("item");
        for (Element y : allYnetNews) {
            String title = y.getElementsByTag("title").get(0).text()
                    .replace("<![CDATA[", "")
                    .replace("]]>", "");
            String fullDescription = y.getElementsByTag("description").get(0).text();
            Document descriptionElement = Jsoup.parse(fullDescription);
            String link = descriptionElement.getElementsByTag("a").get(0).attr("href");
            String image = descriptionElement.getElementsByTag("img").get(0).attr("src");
            String description = descriptionElement.text();

            Ynet ynet = new Ynet(title, link, description, image);
            Log.d("Ness",ynet.toString());
        }
        return ynetNews;
    }

    public static class Ynet {
        private String title;
        private String link;
        private String description;
        private String image;

        public Ynet(String title, String link, String description, String image) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getDescription() {
            return description;
        }

        public String getImage() {
            return image;
        }

        @Override
        public String toString() {
            return "Ynet{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", description='" + description + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }
}
