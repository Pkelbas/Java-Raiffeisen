# Java-Raiffeisen


## Тестовое задание на стажировку в Райффайзен Банк. Лето 2024

1. Реализован базовый функционал требуемый в задании.

2. Реализована аутенфикация с помощью JWT-токена.

3. Реализован доступ к эндпоинтам по ролям, в частности по permisson-ам:

   1. Регистрация\аутенфикация\swagger-ui доступны без ограничений по ролям.
   2. Клиент может выполнять CRUD операции только со своими счетами.

4. Реализован Swagger UI, в том числе и авторизация.

5. Используется СУБД Postgre. Поднимается в докер-контейнер.

6. Реализованы миграции БД с помощью Flyway


## Пример работы приложения

Поднимается на порте 8080. По ссылке http://localhost:8080/swagger-ui/index.htm доступен Swagger UI.

![img.png](images/swagger_init.png)

Пытаемся открыть новый счет не пройдя аутенфикацию :

![img.png](images/try_add_without_auth.png)

Получаем 403 :

![img.png](images/try_add_without_auth_response.png)

Требуется аутенфикация\регистрация. Зарегистрируем нового клиента:

![img.png](images/reg_request.png)

В ответ получаем JWT-токен :

![img.png](images/reg_response.png)

Проверим что новый клиент появился в БД и что его пароль был зашифрован:

![img.png](images/reg_db.png)

Полученный токен вставляем в Authorize:

![img.png](images/authorize_arrows.png)

![img.png](images/authorize_token_in.png)

![img_1.png](images/authorize_success.png)

Возвращаемся обратно к созданию счета. Можно заметить, что замок закрылся.

![img.png](images/try_add_with_auth.png)

В этот раз создание счета прошло успешно

![img.png](images/try_add_with_auth_response.png)


Проверим наличие счета в БД :

![img.png](images/account_in_db.png)

Попробуем положить деньги на этот счет :

![img.png](images/try_add_money_with_permission.png)

Успешно :

![img.png](images/try_add_money_with_permission_response.png)

Проверим БД - баланс изменился :

![img.png](images/try_add_money_with_permission_db.png)

В базе данных доступен еще один счет. Попробуем положить на него деньги.

![img.png](images/try_add_money_to_account_with_no_permission.png)

Т.к. счет зарегестрирован за другим клиентом, запрос вернул ошибку.

![img.png](images/try_add_money_to_account_with_no_permission_response.png)






