package com.travels.searchtravels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.api.services.vision.v1.model.LatLng;
import com.travels.searchtravels.api.OnVisionApiListener;
import com.travels.searchtravels.api.VisionApi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ApiTests {
    private static final String TOKEN = "YOUR_TOKEN_HERE";
    private static final List<List<String>> locations = new ArrayList<List<String>>() {
        {
            add(Arrays.asList("https://cutt.ly/efRlsjQ", "37.8595769", "20.6226222", "sea")); // Бухта Навайо, остров Закинф (Закинтос), Греция
            add(Arrays.asList("https://cutt.ly/FfRlngr", "34.0219267", "-118.5157749", "ocean")); // Санта-Моника, Лос-Анджелес, Калифорния, США
            add(Arrays.asList("https://cutt.ly/IfRlJwp", "37.8535194", "-119.8312955", "mountain")); // Йосемитский национальный парк, Калифорния, США
            add(Arrays.asList("https://cutt.ly/9fRlgcm", "36.2635972", "29.4107775", "beach")); // Пляж Капуташ, Турция
            add(Arrays.asList("https://cutt.ly/xfRlMGw", "43.6727703", "40.2953235", "snow")); // Роза Хутор, Сочи, Россия
            add(Arrays.asList("https://cutt.ly/TfRl9r0", "41.9102415", "12.3959141", "other")); // Рим, Италия
        }};

    @Test
    public void findLocationTest() {

        for (List<String> location : locations) {
            VisionApi.findLocation(getBitmapFromURL(location.get(0)),
                    TOKEN,
                    new OnVisionApiListener() {
                        @Override
                        public void onSuccess(LatLng latLng) {
                            Assert.fail();
                            LatLng targetLatLng = new LatLng();
                            targetLatLng.setLatitude(Double.valueOf(location.get(1)));
                            targetLatLng.setLongitude(Double.valueOf(location.get(2)));
                            assertThat(latLng, samePropertyValuesAs(targetLatLng));
                        }

                        @Override
                        public void onErrorPlace(String category) {
                            Assert.assertEquals(category, location.get(3));
                        }

                        @Override
                        public void onError() {
                            Assert.fail();
                        }
                    });
        }
    }

    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
}

