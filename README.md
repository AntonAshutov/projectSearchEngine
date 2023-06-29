# projectSearchEngine
educational project <br><br>
Проект представляет собой сервис по сканированию и индексации сайтов для дальнейшего поиска по ним страниц, наиболее релевантных вашему запросу.
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

  ---
  
Как запустить приложение локально:
- Установите на свой пк sql
- Создайте бд для работы приложения. Таблицы создавть не надо.
- Скопируйте содержимое репозитория на свой пк.
- В конфигурационном файле application.yaml установите значения spring.datasource.username и spring.datasource.password в соответствии с настройка вашей бд.
---

Используемые технологии:
- Java
- Spring(Boot, MVC, Data JPA, Web, REST)
- maven
- jsoup
- multythreading
- MySQL

  ---
  
