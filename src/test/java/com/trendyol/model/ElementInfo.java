package com.trendyol.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElementInfo {

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("index")
    @Expose
    private int index;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ElementInfo() {
    }

}
                         /*
                         ElementInfo sınıfı, Selenium WebDriver ile web otomasyonu yaparken kullanılacak web elementlerinin bilgilerini saklamak için tasarlanmış bir Java sınıfıdır.
                         Bu sınıf, Gson kütüphanesinin sağladığı özelliklerle, JSON dosyalarından okunan element bilgilerini nesnelere dönüştürmek için kullanılır.

                         Test ortamında alacağımız locatorları ayrı json dosyalarında (src>test>resources>elementValues) belirlediğimiz kurallarla ve
                         isimlendirmelerle tutabileceğimiz anatasyonlar(@SerializedName) ekliyoruz.

                         Burada amaç birden fazla kez kullanacağımız locaterları her seferinde tanımlamak yerinde tek seferde tanımlamak ve sürekli çağırabilmek.

                         key, value, type, index gibi özellikler, bir web elementi için gerekli bilgileri tutar.
                         key: Elementin benzersiz tanımlayıcısı.
                         value: Elementin bulunmasında kullanılacak sorgu (örneğin XPath veya CSS seçici).
                         type: Elementin bulunma tipini belirtir (örneğin xpath, css, id gibi).
                         index: Eğer birden fazla element varsa ve bunlardan belirli birini seçmek gerekiyorsa kullanılır.

                         Her özellik için tanımlanan getter ve setter metodları, sınıfın özelliklerine erişimi ve bu özelliklerin değiştirilmesini sağlar.
                         Bu, nesne yönelimli programlamada enkapsülasyon prensibine uygun bir yaklaşımdır.
                          */

