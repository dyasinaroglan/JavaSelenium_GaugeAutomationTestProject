package com.trendyol.base;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.trendyol.model.ElementInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static Actions action;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    DesiredCapabilities capabilities;
    ChromeOptions chromeOptions;
    FirefoxOptions firefoxOptions;
    String browserName = "chrome";
    String selectPlatform = "windows";
    String testUrl = "https://www.trendyol.com/";

    ConcurrentMap<String, Object> elementMapList = new ConcurrentHashMap<>();

    public ChromeOptions chromeOptions() { //chrome tarayıcısının ayarlarını özelleştirilmesi
        chromeOptions = new ChromeOptions();
        capabilities = DesiredCapabilities.chrome(); // chrome için özel yetenekler oluşturur
        Map<String, Object> prefs = new HashMap<>(); // tarayıcı tercihlerini bir map 'in içine atıyoruz
        prefs.put("profile.default_content_setting_values.notifications", 2);
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.addArguments("--ignore-certificate-errors");
        //chromeOptions.addArguments("--kiosk"); // tarayıcıyı tam ekran boyutunda başlatır
        chromeOptions.addArguments("--disable-notifications"); // web bildirimlerini devre dışı bırakır
        //chromeOptions.addArguments("--start-fullscreen");
        System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver.exe"); //Selenium'un chrome tarayıcısını kontrol etmesi için, zorunlu
        chromeOptions.merge(capabilities); //DesiredCapalities nesnesini ChromeOptions ile birleştirir.
        return chromeOptions;

    }

    public FirefoxOptions firefoxOptions() { //chrome tarayıcısının ayarlarını özelleştirilmesi
        firefoxOptions = new FirefoxOptions();
        capabilities = DesiredCapabilities.firefox(); // chrome için özel yetenekler oluşturur
        Map<String, Object> preference = new HashMap<>(); // tarayıcı tercihlerini bir map 'in içine atıyoruz
        preference.put("profile.default_content_setting_values.notifications", 2);
        chromeOptions.setExperimentalOption("preference", preference);
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--kiosk"); // tarayıcıyı tam ekran boyutunda başlatır
        chromeOptions.addArguments("--disable-notifications"); // web bildirimlerini devre dışı bırakır
        chromeOptions.addArguments("--start-fullscreen");
        System.setProperty("webdriver.firefox.driver", "web_driver/chromedriver.exe"); //Selenium'un chrome tarayıcısını kontrol etmesi için, zorunlu
        firefoxOptions.merge(capabilities); //DesiredCapalities nesnesini ChromeOptions ile birleştirir.
        return firefoxOptions;
    }
    private void initializeDriver() {     //belirtilen tarayıcı türüne göre(Chrome, Firefox) WebDriver nesnesi oluşturur. Arabanın modelini seçmek gibi
        switch (browserName.toLowerCase()) {
            case "chrome":                                 //Eğer browserName chrome ise bana ChromeDriver nesnesi oluşturur.
                driver = new ChromeDriver(chromeOptions()); //Sedan
                break;
            case "firefox":
                driver = new FirefoxDriver(firefoxOptions()); //Hatchback
                break;
            default:
                throw new IllegalStateException("Desteklenmeyen tarayıcı");
        }
    }
    private void setupBrowser() {
        if ("mac".equalsIgnoreCase(selectPlatform) || "windows".equalsIgnoreCase(selectPlatform)) {
            initializeDriver();
            driver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS); //tarayıcının bekleyeceği max süre
            driver.get(testUrl);
            logger.info(testUrl + " adresi açılıyor.");

            if ("windows".equalsIgnoreCase(selectPlatform)) {
                action = new Actions(driver);
            }
        }

    }
    @BeforeScenario
    public void startSetup() {
        try {
            logger.info("Cihazda " + selectPlatform + " ortamında " + browserName + " browserinda test ayağa kalkacak");
            setupBrowser();

        } catch (Exception e) {
            logger.info("Driver başlatılırken hata oluştu " + e.getMessage());
        }
    }
    @AfterScenario
    public void quitDriver() {
        driver.quit();
    }
    public void initMap(List<File> fileList) throws FileNotFoundException {  //json dosyaları işlenir ve sonrasında elementMapList' de bunları saklar
        elementMapList = new ConcurrentHashMap<>();
        Type elementType = new TypeToken<List<ElementInfo>>() {
        }.getType();
        Gson gson = new Gson();
        List<ElementInfo> elementInfoList = null;
        for (File file : fileList) {
            try {
                FileReader filez = new FileReader(file);
                elementInfoList = gson.fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream().forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
            } catch (FileNotFoundException e) {

            }
        }
    }

    /*
    getFileList metodu public olarak yazılmıştır, sonuçta bu tüm json dosyalarını ilgilendiren yani herkese erişilebilir olması gereken bir metod
    static olması ise doğrudan sınıf adıyla çağrılmasını sağlar.
    Bu metod sıkça kullanılacak işlevleri yerine getirecek, dolayısıyla her yerden erişilebilir olması gerekir.
     */
    public static List<File> getFileList(String directoryName) throws IOException { //Json dosyalarını bulup liste haline döndüren metod, kütüphane
        List<File> dirList = new ArrayList<>(); //File nesnelerini saklayacak bir list oluşturulur
        try (Stream<Path> walkStream = Files.walk(Paths.get(directoryName))) { // tüm dosya ve klasörleri gezer, kütüphanede rafları gezmesi örneği
            walkStream.filter(p -> p.toFile().isFile()).forEach(f -> { // sadece dosyaları filtreler, kütüphanede yer alan kitapları sadece (gazete ve kitapları değil)
                if (f.toString().endsWith(".json")) { // dosya json ile bitiyorsa dirList değişkenine ekler, kütüphanede sadece romanları aramak gibi
                    dirList.add(f.toFile());
                }
            });
        }
        return dirList;
    }

    /*
    elementMapList'de saklanan ElementInfo nesnelerinden birini bulup döndürür
     */
    public ElementInfo findElementInfoByKey(String key) {
        return (ElementInfo) elementMapList.get(key);
    }

    public void saveValue(String key, String value) {
        elementMapList.put(key, value);
    }

    public String getValue(String key) {
        return elementMapList.get(key).toString();

    }
}

                  /*                                                               ÖZET
                    Bu sınıf, testlerimizin koşmaya başlamadan önce tanımlamalar yaptığımız ve her testten önce gerçekleşmesini istediğimiz genel kuralları belirler.
                    Driver'ın ayağ kalkması
                    browser'ın seçilmesi ve modifiye edilmesi
                    Test senaryolarından sonra driver'ın kapatılması gibi işlemleri gerçekleştiriyoruz.
                  */
