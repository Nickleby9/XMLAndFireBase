package hilay.edu.xmlandfirebase;

import android.text.LoginFilter;
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

public class CurrencyDataSource {

    public interface OnCurrencyArriveListener {
        void onCurrencyArrive(List<Currency> data, Exception e);
    }

    public static void getCurrencies(final OnCurrencyArriveListener listener) {
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.boi.org.il/currency.xml");
                    URLConnection con = url.openConnection();
                    InputStream in = con.getInputStream();
                    String xml = IO.read(in);
                    List<Currency> result = parse(xml);
                    listener.onCurrencyArrive(result, null);
                } catch (Exception e) {
                    listener.onCurrencyArrive(null, e);
                }
            }
        });
    }

    private static List<Currency> parse(String xml) {
        Document document = Jsoup.parse(xml);
        List<Currency> currencies = new ArrayList<>();
        Elements allCurrencies = document.getElementsByTag("CURRENCY");
        for (Element c : allCurrencies) {
            String name = c.getElementsByTag("NAME").get(0).text();
            double unit = Double.valueOf(c.getElementsByTag("UNIT").get(0).text());
            String currencyCode = c.getElementsByTag("CURRENCYCODE").get(0).text();
            String country = c.getElementsByTag("COUNTRY").get(0).text();
            double rate = Double.valueOf(c.getElementsByTag("RATE").get(0).text());
            double change = Double.valueOf(c.getElementsByTag("CHANGE").get(0).text());

            Currency currency = new Currency(name, unit, currencyCode, country, rate, change);
            currencies.add(currency);
        }
        Log.d("Ness", currencies.toString());
        return currencies;
    }

    public static class Currency {
        private final String name;
        private final double unit;
        private final String currencyCode;
        private final String Country;
        private final double rate;
        private final double change;

        public Currency(String name, double unit, String currencyCode, String country, double rate, double change) {
            this.name = name;
            this.unit = unit;
            this.currencyCode = currencyCode;
            Country = country;
            this.rate = rate;
            this.change = change;
        }

        public String getName() {
            return name;
        }

        public double getUnit() {
            return unit;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getCountry() {
            return Country;
        }

        public double getRate() {
            return rate;
        }

        public double getChange() {
            return change;
        }

        @Override
        public String toString() {
            return "Currency{" +
                    "name='" + name + '\'' +
                    ", unit=" + unit +
                    ", currencyCode='" + currencyCode + '\'' +
                    ", Country='" + Country + '\'' +
                    ", rate=" + rate +
                    ", change=" + change +
                    '}';
        }
    }
}
