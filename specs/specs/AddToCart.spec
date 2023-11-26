Scenarios
==========

Searchbox ürün arama, Sepete ürün ekleme-çıkarma, sepet kontrolü
---------------------------------------------------------------
* Çerezler kabul edilir
* Searchbox input alanında "laptop" araması yapılır ve ENTER key yollanır
* Arama sonucuna göre random bir ürün seçilir
* Yeni sekme açılır ve açılan yeni sekmeye odaklama yapılır ve ürün fiyatı belleğe kaydedilir
* Sepete Ekle butonuna tıklanır
* Sepetim butonuna tıklanır
* 'Anladım' butonuna tıklanır
* Sepetteki fiyat ile belleğe kaydedilen fiyatın eşit olduğu kontrol edilir
* Searchbox input alanında "laptop" araması yapılır ve ENTER key yollanır
* Arama sonucuna göre random bir ürün seçilir
* Sepete Ekle butonuna tıklanır
* Sepetim butonuna tıklanır
* Sepetteki ürün miktarının "2" olduğu kontrol edilir
* Sepet boşaltılır
* Sepetim butonuna tıklanır
* Sepetin boş olduğu kontrol edilir

Başarısız login, yanlış E-posta ile giriş yapma
-------------------------
* Çerezler kabul edilir
* Ana sayfa 'Giriş Yap' butonuna tıklanır
* "asd@gmail.com" kullanıcı adı ve "Zaha1905*" şifresi ile login olunur
* "E-posta adresiniz ve/veya şifreniz hatalı." uyarı mesajının görüntülendiği kontrol edilir

Başarısız login, yanlış Şifre ile giriş yapma
------------------------------
* Çerezler kabul edilir
* Ana sayfa 'Giriş Yap' butonuna tıklanır
* "yasinargln264@gmail.com" kullanıcı adı ve "Zaha1905*" şifresi ile login olunur
* "E-posta adresiniz ve/veya şifreniz hatalı." uyarı mesajının görüntülendiği kontrol edilir

Başarısız login, boş E-posta ve şifre ile giriş denemesi
---------------------------------------
* Çerezler kabul edilir
* Ana sayfa 'Giriş Yap' butonuna tıklanır
* "" kullanıcı adı ve "" şifresi ile login olunur
* "Lütfen geçerli bir e-posta adresi giriniz." uyarı mesajının görüntülendiği kontrol edilir

Başarısız login, boş şifre ile giriş denemesi
----------------------------
* Çerezler kabul edilir
* Ana sayfa 'Giriş Yap' butonuna tıklanır
* "yasinargln264@gmail.com" kullanıcı adı ve "" şifresi ile login olunur
* "Lütfen şifrenizi giriniz." uyarı mesajının görüntülendiği kontrol edilir

Başarısız login, geçersiz E-Posta Formatı ile Giriş Denemesi
------------------------------------------
* Çerezler kabul edilir
* Ana sayfa 'Giriş Yap' butonuna tıklanır
* "asddfdd.com½()" kullanıcı adı ve "" şifresi ile login olunur
* "Lütfen geçerli bir e-posta adresi giriniz." uyarı mesajının görüntülendiği kontrol edilir