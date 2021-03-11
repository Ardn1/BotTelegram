package com.sech530.demo.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sech530.demo.commands.impl.ParseCommand;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

@Slf4j
public class RemoteDriver {
    private static final String SELENOID_URL = "http://127.0.0.1:4444/wd/hub";
    private static final DesiredCapabilities capabilities;
    private static RemoteWebDriver webDriver;
    static {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "88.0");
        try {
            webDriver = new RemoteWebDriver(
                    URI.create(SELENOID_URL).toURL(),
                    capabilities);
        } catch (MalformedURLException e) {
            System.out.println(e);
        }
    }

    public synchronized static List<String> parseData(
            @NonNull String url,
            @NonNull List<String> xpaths,
            String clickXpath
    ) throws InterruptedException {
        log.info("Getting url:{}", url);

        if (webDriver == null || webDriver.getSessionId() == null) {
            try {
                System.out.println("CREATE WEBD_RIVER " + ParseCommand.jopaCounter);
                webDriver = new RemoteWebDriver(
                        URI.create(SELENOID_URL).toURL(),
                        capabilities);

                System.out.println("CREATE WEBD_RIVER SESSION " + webDriver.getSessionId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        webDriver.get(url);

        Thread.sleep(750);
        if (clickXpath != null) {
            if (clickXpath.contains("//a")) {
                var button = webDriver.findElement(By.xpath(clickXpath));
                JavascriptExecutor jse = (JavascriptExecutor) webDriver;
                jse.executeScript("arguments[0].click();", button);
            } else {
                webDriver.findElement(By.xpath(clickXpath)).click();
            }
            Thread.sleep(1000);
        }
        List<String> results = new ArrayList<>();
        try {
            for (String xpath : xpaths) {
                var element = webDriver.findElement(By.xpath(xpath));
                results.add(element.getText());
            }
        } catch (Exception e) {
            return null;
        } finally {
            ParseCommand.jopaCounterDecrease();
        }
        return results;
    }

    public static String parseCostData(
            @NonNull String url,
            @NonNull List<String> cssSelector
    ) throws InterruptedException, IOException {
        Document doc = Jsoup.connect(url).get();
        Elements newsHeadlines = doc.select(cssSelector.get(0));

        for (Element headline : newsHeadlines) {
            return headline.text();
        }
        return "Ошибка парсинга";
    }
}
