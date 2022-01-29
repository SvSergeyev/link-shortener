# link-shortener | Сокращатель ссылок
Программа для создания коротких ссылок.

Исходные данные для разработки - [здесь](https://docs.google.com/document/d/1VHPb0Ylq4K8cSpvwMxYR6U1dYcSyzY2aFxadP4hFALg/edit?usp=sharing)

### Краткое описание
Программа позволяет аутентифицированным пользователям создавать короткие ссылки 
(по-умолчанию вида localhost:8080/1n2YLRN3), осуществлять переход по ним, 
а так же удалять их и ограничивать время действия (по-умолчанию 1 минута).

### Из чего же, из чего же...
* Maven
* Spring Security
* Spring Data JPA
* PostgreSQL
* Apache Commons Text

### Описание работы
Основным элементом, используемым для хранения данных о ссылке, является объект класса [ShortLink](https://github.com/SvSergeyev/link-shortener/blob/master/src/main/java/tech/sergeyev/linkshortener/persistence/model/ShortLink.java).
Перед началом работы в базе данных создается таблица [пользователей](https://github.com/SvSergeyev/link-shortener/blob/master/src/main/resources/schema.sql)
и в нее вносится пользователь [по-умолчанию](https://github.com/SvSergeyev/link-shortener/blob/master/src/main/resources/data.sql  "username: user; password: 111").

#### Создание ссылки 
При отправке POST-запроса на localhost:8080/ для находящегося в теле запроса объекта (ShortLink с единственным полем {"originalUrl": "www.yandex.ru"}) 
создается короткий токен и назначается [автор](https://github.com/SvSergeyev/link-shortener/blob/master/src/main/java/tech/sergeyev/linkshortener/persistence/model/Author.java)
(создание ссылки доступно только аутентифицированным пользователям). Перед созданием производится проверка на наличие исходной ссылки в базе данных и в случае,
если таковая имеется - вместо создания нового объекта возвращается объекта из БД.

#### Переход по короткой ссылке
При отправке GET-запроса на адрес вида localhost:8080/token по указанному токену из базы данных возвращается объект класса ShortLink и осуществляет переход по ссылке,
сохраненной в поле originalUrl полученного объекта. При каждом переходе параметр clickCount объекта увеличивается на 1.

#### Задание "времени жизни" ссылок
При отправке PATCH-запроса на localhost:8080/token из БД по токену выгружается ShortLink-объект, которому в поле expirationTime устанавливается время, после которого
ссылка перестает быть доступна.

#### Удаление ссылок
DELETE-запрос на localhost:8080/token удаляет запись c объектом ShortLink из БД.

### Запуск приложения
Из консоли:
> java -jar target/link-shortener-0.0.1-SNAPSHOT.jar    

Из терминала Intellij IDEA:
> mvn spring-boot:run
