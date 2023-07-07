# projectSearchEngine
educational project <br><br>
Проект представляет собой сервис по сканированию и индексации сайтов для дальнейшего поиска по ним страниц, наиболее релевантных вашему запросу.

---
Используемые технологии:
- Java
- Spring(Boot, MVC, Data JPA, Web, REST)
- maven
- jsoup
- multythreading
- MySQL
---

Как запустить приложение локально:
- Установите на свой пк sql
- Создайте бд для работы приложения. Таблицы создавать не надо.
- Скопируйте содержимое репозитория на свой пк.
- В конфигурационном файле application.yaml установите значения spring.datasource.username и spring.datasource.password в соответствии с настройками вашей бд.
- Запустите приложение


---
endpoints:

 - Запуск полной индексации — GET /api/startIndexing <br>
Метод запускает полную индексацию всех сайтов или полную
переиндексацию, если они уже проиндексированы.
Если в настоящий момент индексация или переиндексация уже
запущена, метод возвращает соответствующее сообщение об ошибке.<br>
Метод без параметров<br>
Формат ответа в случае успеха:<br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'result': true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Формат ответа в случае ошибки:<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'result': false,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'error': "Индексация уже запущена"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>
 - Остановка текущей индексации — GET /api/stopIndexing<br>
Метод останавливает текущий процесс индексации (переиндексации).
Если в настоящий момент индексация или переиндексация не происходит,
метод возвращает соответствующее сообщение об ошибке.<br>
Метод без параметров.<br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Формат ответа в случае успеха:<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'result': true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Формат ответа в случае ошибки:<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'result': false,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'error': "Индексация не запущена"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>

 - Добавление или обновление отдельной страницы — POST /api/indexPage<br>
Метод добавляет в индекс или обновляет отдельную страницу, адрес
которой передан в параметре.
Если адрес страницы передан неверно, метод возвращает
соответствующую ошибку.<br>
Параметры:<br>
● url — адрес страницы, которую нужно переиндексировать.<br>
Формат ответа в случае успеха:<br><br>
{<br>
'result': true<br>
}<br><br>
Формат ответа в случае ошибки:<br><br>
{<br>
'result': false,<br>
'error': "Данная страница находится за пределами сайтов,
указанных в конфигурационном файле"<br>
}<br><br>

 - Статистика — GET /api/statistics<br>
Метод возвращает статистику и другую служебную информацию о
состоянии поисковых индексов и самого движка.<br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Метод без параметров.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Формат ответа:<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'result': true,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'statistics': {<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"total": {<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"sites":,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"pages": 436423,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"lemmas": 5127891,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"indexing": true<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"detailed": [<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"url": "http://www.site.com",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"name": "Имя сайта",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"status": "INDEXED",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"statusTime": 1600160357,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"error": "Ошибка индексации: главная
страница сайта недоступна",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"pages": 5764,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"lemmas": 321115<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;]<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>

 - Получение данных по поисковому запросу — GET /api/search<br>
Метод осуществляет поиск страниц по переданному поисковому запросу.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Если поисковый запрос не задан или ещё нет готового индекса (сайт, по
которому ищем, или все сайты сразу не проиндексированы), метод возвращает
вернуть соответствующую ошибку. <br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Параметры:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;● query — поисковый запрос;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;● site — сайт, по которому осуществлять поиск (если не задан, поиск
происходит по всем проиндексированным сайтам); задаётся в
формате адреса, например: http://www.site.com (без слэша в конце);<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;● offset — сдвиг от 0 для постраничного вывода (параметр
необязательный; если не установлен, то значение по умолчанию равно
нулю);<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;● limit — количество результатов, которое необходимо вывести (параметр
необязательный; если не установлен, то значение по умолчанию равно
20).<br><br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Формат ответа в случае успеха:<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'result': true,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'count': 574,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'data': [<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"site": "http://www.site.com",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"siteName": "Имя сайта",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"uri": "/path/to/page/6784",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"title": "Заголовок страницы,
которую выводим",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"snippet": "Фрагмент текста,
в котором найдены
совпадения, <b>выделенные
жирным</b>, в формате HTML",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"relevance": 0.93362<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;]<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Формат ответа в случае ошибки:<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'result': false,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'error': "Задан пустой поисковый запрос"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>


---
Подробнее как сервис работает внутри:<br>
Приложение работает с четырьмя таблицами, в коде представленными классами сущностями: <br>
- site - в этой таблице хранятся записи об индексируемых сайтач: их id, название, адрес, текст ошибки, статус, время обнавления
- page - здесь хранятся записи о проиндексорованных страницах: их id, код ответа, контент, путь от домашней страницы сайта и id записи из таблицы site с сайтом, к которому данная страница относится.
- lemma - в этой таблице находятся записи о леммах - начальных формах слов, встречающихся на индексируемых сайтах. Записи содержат следующую информацию: id записи, сама лемма, id сайта, количество страниц этого сайта, на которых встречается хотябы одно слово, от которого образованна данная лемма.
- index - таблица с записями о том, сколько раз каждая лемма встречается на каждой проиндексированной странице. Содержит id записи, id леммы, id страницы, и количество слов, образумех от данной леммы на данной странице. <br><br>
При начале индексации таблицы при необходимости создаются/очищаются и сервис начинает обход сайтов. Обход происходит "в ширину", т.е. программа извлекает все ссылки из контента домашней страницы, и начинает их обходить. А получаемые ссылки из контента обходимых страниц пока сохраняются. Когда все страницы, ссылки которых получены из контента домашней страницы страницы отсканированны - программа спускается на следующий уровень, и так далее. При сканировании каждой страницы кроме извлечения ссылок происходит анализ отображаемого текста - поиск и подсчёт встречающихся лемм. Соответствующая информация заносится в таблицы. Дожидаться полного сканировани необязательно, его можно остановать, нажав на кнопк "stop indexing", или перейдя по ссылке http://localhost:8080/api/stopIndexing. После этого можно производить поиск по проиндексированным страницам. Программа разобьёт запрос на леммы и найдёт страницы, на которых встречаются они все. В ответе страницы будут отсортированы по релевантности и будут показаны сниппеты - части текста на страницах, на которых встречаются леммы, имеющиеся в запросе.


---
<br><br>
Веб интерфейс доступен по адресу http://localhost:8080/<br>
На странице приложения три вкладки:<br><br>
 -DASHBOARD<br> ![dashboard](https://github.com/AntonAshutov/projectSearchEngine/assets/116497853/7639cb78-f58b-4f02-bbee-0a9284ebca37)
На этой вкладке показано:<br>

  -общая статистика<br> 
  -статус индексации сайтов<br> 
  -статистика по каждому сайту<br>

  ----
  -MANAGEMENT<br>![management](https://github.com/AntonAshutov/projectSearchEngine/assets/116497853/c1427bba-ec31-4c95-b839-db6973bad76b)

  на этой вкладке находится кнопка старта/остановки индексации и поле для индексации отдельной страницы.<br>

  ---
  -SEARCH <br>
  ![search](https://github.com/AntonAshutov/projectSearchEngine/assets/116497853/5347e258-1ed8-4455-975b-62bfa40b5e21)

  эта вкладка используется для поиска релевантных запросу страниц из числа проиндексированных.
  Можно искать среди страниц всех проиндексированных сайтов либо среди страниц одного сайта.


