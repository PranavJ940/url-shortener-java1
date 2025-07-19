package com.example.url_shortener_java;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UrlShortenerServiceTest {
    @Test
    public void testShortenAndRetrieve() throws Exception {
        UrlShortenerService service = new UrlShortenerService();
        String url = "https://www.example.com";
        String code = service.shorten(url);
        assertNotNull(code);
        String retrieved = service.getOriginalUrl(code);
        assertEquals(url, retrieved);
    }
}
