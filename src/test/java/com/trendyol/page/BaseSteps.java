package com.trendyol.page;

import com.thoughtworks.gauge.Step;
import com.trendyol.base.BaseTest;
import com.trendyol.model.ElementInfo;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;

public class BaseSteps extends BaseTest {

    public static int DEFAULT_MAX_ITERATION_COUNT = 100;  // bir eylemin kaç kez tekrarlanacağını ifade eder
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 300; // her bir kontrol arasında bekleyeceğimiz süre
    /*
    static olarak tanımlamamızın sebebi sınıfın tüm örneklerin arasında paylaşılmasını ve değiştirilmesi gerektiği zaman tek bir yerden değiştirilmesini sağlamaktır.
     */

    public BaseSteps() throws IOException {
        String workingDir = System.getProperty("user.dir");
        initMap(getFileList(workingDir + "/src"));

    }

    public By getElementInfoBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        }
        return by;
    }

    WebElement findElement(String key) {

        By by = getElementInfoBy(findElementInfoByKey(key));
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})", element);
        return element;
    }

    List<WebElement> findElements(String key) {
        return driver.findElements(getElementInfoBy(findElementInfoByKey(key)));
    }

    private void clickTo(WebElement element) {
        element.click();
    }

    private void sendKeysTo(WebElement element, String text) {
        element.sendKeys(text);
    }

    public void javaScriptClickTo(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    @Step("<key> li elementi bul temizle ve <text> değerini yaz")
    public void sendKeys(String key, String text) {
        WebElement element = findElement(key);
        element.clear();
        sendKeysTo(element, text);
        logger.info("Element bulundu ve yazıldı: Key : " + key + " text : " + text);
    }

    @Step("Elementine tıkla <key>")
    public void clickElement(String key) {
        clickTo(findElement(key));
        logger.info(key + " elementine tıklandı.");
    }

    @Step("<int> saniye bekle")
    public void waitSecond(int seconds) throws InterruptedException {
        try {
            logger.info(seconds + " saniye bekleniyor");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("<key> elementinin disabled olduğunu kotrol et")
    public void checkDisabled(String key) {
        WebElement element = findElement(key);
        Assertions.assertTrue(element.isDisplayed(), " Element disabled değil");
        logger.info(key + " elementi disabled");
    }

    @Step("<key> elementi <expected> değerini içerdiği <unexpected> değerini içermediği kontrol edilir")
    public void checkExpectedEqualsUnexpected(String key, String expected, String unexpected) {
        String elementText = findElement(key).getText();

        if (!elementText.equals(expected)) {
            Assertions.fail(key + " elementi " + expected + " değerini içeriyor.");
        }
        logger.info(key + " elementi beklenen '" + expected + "' değerini içeriyor.");

        if (elementText.equals(unexpected)) {
            Assertions.fail(key + " elementi beklenmeyen '" + unexpected + "' metnini içeriyor. Alınan metin: " + elementText);
        }
        logger.info(key + " elementi beklenmeyen '" + unexpected + "' değerini içermiyor.");
    }

    @Step("<key> elementinin <attribute> niteliği <value> değerine sahip mi")
    public void elementAttributeValueCheck(String key, String attribute, String value) throws InterruptedException {
        WebElement element = findElement(key);
        String actualValue;
        int count = 0;
        while (count < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.equals(value)) {
                logger.info(key + " elementinin " + attribute + " niteliği " + value + " değerine sahip.");
                return;
            }
            waitSecond(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(key + " elementinin " + attribute + " niteliği " + value + " değeri ile eşleşmiyor.");
    }

    @Step("<key> elementi <expectedText> değerini içeriyor mu kontrol et")
    public void checkElementEqualsText(String key, String expectedText) {

        String actualText = findElement(key).getText();
        logger.info("Element str:" + actualText);
        logger.info("Expected str:" + expectedText);
        Assertions.assertEquals(actualText, expectedText, "Beklenen metni içermiyor " + key);
        logger.info(key + " elementi " + expectedText + " degerine eşittir.");
    }

    @Step("Api'den film başlığını al <url>")
    public void getTitleFromAPI(String url) {

        Response response = given()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract().response();
        logger.info("Api response : " + response.asString());

        String title = response.jsonPath().getString("Title");
        logger.info("Api'den gelen başlık : " + title);

        saveValue("Title", title);
        logger.info("Api'den gelen başlık kaydedildi : " + title);
    }

    @Step("<key> Arama sonuçlarının titlekey başlığını içerdiğini doğrula")
    public void verifySearchResultContainsTitle(String key) {
        String expectedTitle = getValue("Title");
        List<WebElement> searchResults = findElements(key);

        boolean isTitleFound = false;

        for (WebElement result : searchResults) {
            if (result.getText().toLowerCase().contains(expectedTitle.toLowerCase())) {
                isTitleFound = true;
                break;
            }else {
                logger.info("Arama sonuçları içerisinde '" + expectedTitle + "' başlığı bulunamadı.");
            }
        }
        Assertions.assertTrue(isTitleFound, "Arama sonuçları içerisinde '" + expectedTitle + "' başlığı bulunamadı.");
    }
    @Step("<key> li elementi bul temizle text değerini yaz(api)")
    public void sendKeysWithTitle(String key) {

        String titleValue = getValue("Title"); // "titleKey" ile alınan değeri yaz
        WebElement element = findElement(key);
        element.clear();
        sendKeysTo(element,titleValue); // "titleKey" ile alınan değeri yaz
        logger.info("Element bulundu ve yazıldı: Key : " + key + ", Title : " + titleValue);
    }
}


                                     /*
                                     @Step anatosyonu Gauge kütüphanesine ait bir anatosyondur. bunun ile testlerimizde sürekli olarak çağırabileceğimiz
                                     cümlecikler halinde metodlar oluşturuyoruz.
                                     Bu sınıfı BaseTest sınıfı ile extends ediyoruz çünkü BaseTest sınıfında driver nesnesini oluşturuyoruz.
                                     BaseTest de olması gerekenler burada da olmalıdır.
                                     Extend ederek bir başka sınıfın özelliklerini miras alıp kullanabiliriz.

                                     bir sınıf başka bir sınıfı "extend" ettiğinde, temel alınan sınıfın ("superclass" veya "parent class" olarak adlandırılır) tüm halka açık metotları ve özellikleri, türetilen sınıfa ("subclass" veya "child class") aktarılır.
                                     Bu işlem sayesinde, kod tekrarını önlemek ve kodun yeniden kullanılabilirliğini artırmak mümkün olur.

                                     Polimorfizm: Alt sınıflar, üst sınıfın metodlarını kendi ihtiyaçlarına göre "override" edebilir (üzerine yazabilir),
                                     böylece aynı metot adı farklı sınıflarda farklı davranışlar sergileyebilir.
                                      */

