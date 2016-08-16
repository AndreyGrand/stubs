Сборка и запуск

1) Настроить репозитории
	https://mysberbank-net.sigma.sbrf.ru/wikis/home?lang=ru-ru#!/wiki/W319951d938d7_49a4_ac76_35ef2d2361f3/page/Settings.xml%20%D0%B4%D0%BB%D1%8F%20%D1%81%D0%B8%D0%B3%D0%BC%D0%B0
2) установить локально корневой проект модуля заглушек
  cd km-cib-bh-stubs
  mvn clean install


Spring boot run as windows service Procrun

-1.mvn package
0. DISTR_ALL above project directory
1. install the service procrun/install.bat
2. uninstall the service procrun/uninstall.bat

3. Java jar works too
java -jar rm-km-tester-app.jar start

default embedded tomcat port: 9900

h2-console
http://localhost:9900/console
db.url=jdbc:h2:~/h2db/rmkmdb
db.username=sa
db.password=

Замечания:
    1. В h2 добавлять нужные для тестирвания данные
    2. Текущий действующий route -> ru.sbrf.efs.rmkmcib.bht.app.routes.CRMMockRoute - самый важный метод - configure()

Для добавления нового сервиса в заглушку:
    1.добавить в корневой pom.xml в dependencyManagement зависимость
    2. добавить mock-xml в /resources/mockfiles -> важно, чтобы он назвался как имя класса-ответа сервиса в lowercase(см имеюшиеся примеры)
    3. если нужно, добавить сервис - шаг работы с базой данных(см пример ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.db.GetLegalClientManagerTaskListService)

