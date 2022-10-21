# bus-route-accounting-system

Задание
-------------------------

Разработать программу, ведущую список сведений об отправлении и прибытии автобусов г.Ростова-на-Дону. Исходная информация должна содержать: номер рейса, название, время отправления и прибытия, время в пути, стоимость билета до конечного пункта. 
В программе должны быть реализованы функции поиска и редактирования данных в БД MySQL. Для ввода/вывода использовать стандартные элементы - кнопки, поля, списки и так далее.

Дополнение: Должен быть разрешён выбор конечного и промежуточного пункта следования.

Видео с работающим приложением
-------------------------

[Демонстрация работоспособности](https://youtu.be/UZtpHN8QZAs)

База данных
-------------------------

Схема базы данных выглядит следующим образом: 

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/database_schema.PNG' width='600'/>

Файлы для экспорта структуры базы содержатся в папке `src/main/resources/com/example/demo/sql_files`. В них уже содержатся значения, но их можно перезаписать путём редактирования соответствующих файлов из папки `src/main/resources/com/example/demo/files_for_db`.

О самом приложении
-------------------------

Таким образом выглядит начальный экран, где можно выбрать пункт назначения и дату. Пункт назначения выбирается из соответствующей таблицы базы данных - таблицы routes, присоединённой к destination для получения названия - в данном случае имеются в виду конечные пункты каждого маршрута. При этом можно выбрать промежуточный пункт из любого маршрута, получаемый из таблицы stops.

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/initial_menu.PNG' width='600'/>

Вид интерфейса для каждого случая будет отличаться - в случае выбора конечного пункта меню маршрутов будет выглядеть таким образом:

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/choose_end_of_route.PNG' width='600'/>

В случае же выбора промежуточного пункта меню маршрутов будет выглядеть так:

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/choose_intermediate_point_of_route.PNG' width='600'/>

Также будет отличаться список остановок - с конечным пунктом назначения маршрут будет отображаться полностью, а с промежуточным - до выбранной остановки:

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/stops_chosen_end.PNG' width='600'/><img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/stops_chosen_intermediate.PNG' width='600'/>

Это часть, к которой могут иметь доступ пользователи. Для администратора добавлена функция редактирования маршрутов. 
Но сначала администратору нужно подтвердить свой статус, войдя в систему. Эта часть сделана упрощённо, без добавления пользователей в базу данных - в программе есть только лишь администратор:

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/log_as_admin.PNG' width='600'/>

>username = admin, password = sds2013

Из-за допущенных упрощений пользователь не сможет зарегестрироваться, а, соответственно, и войти, поэтому его просто выбросит в главное меню.
Войдя в систему, администратор увидит следующую таблицу:

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/edit_routes_table.PNG' width='600'/>

Он сможет найти нужный маршрут с помощью поля поиска:

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/admin_search_route.PNG' width='600'/>

А также скопировать маршрут в поля для ввода либо ввести собственный маршрут - и, нажав на кнопку "Добавить маршрут" - добавить соответствующий маршрут в таблицу и базу. Соответственно по кнопке "Обновить маршрут" и выбранному маршруту можно его отредактировать, а по кнопке "Удалить маршрут" - удалить. 
По двойному нажатию по любому маршруту можно открыть его остановки для редактирования. Здесь можно совершать аналогичные действия, что и с маршрутами:

<img src='https://github.com/ajdivotf/bus-route-accounting-system/blob/master/src/main/resources/com/example/demo/images/edit_stops_table.PNG' width='600'/>

Цена на маршрут фиксированная, но её может изменить администратор. Цена за каждую остановку рассчитывается по цене маршрута и времени до этой остановки, т.е.

`общая цена за маршрут / количество остановок в маршруте * время до остановки / 100`
