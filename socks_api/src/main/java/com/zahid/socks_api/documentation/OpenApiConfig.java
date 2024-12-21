package com.zahid.socks_api.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Ranis Zagidulin",
                        email = "zagidulin.ran@yandex.ru"
                ),
                title = "API для учета носков на складе магазина",
                description = "В программе подразумевается, что все носки учитываются парами." +
                        " Хранение данных в БД подразумевается таким образом, " +
                        "что каждая строка содержит информацию об уникальной разновидности носков. " +
                        "Под разновидностью подразумевается сочетание свойств" +
                        " \"цвет\"+\"процентное содержание хлопка\". Соответственно, при " +
                        "добавлении/удалении данных о конкретной разновидности носков, которая " +
                        "уже имеется в наличии, происходит изменение количества товара, то есть не " +
                        "предполагается появление новой или исчезновения существующей записи " +
                        "(строки) в таблице (только если количество товара становится равным нулю)." +
                        " SQL-скрипт используемой базы данных находится в корневой папке проекта в " +
                        "файле \"data.sql\". Процентное содержание хлопка указывается целым числом" +
                        " в диапазоне от 0 до 100. Предусмотрена возможность загрузки данных из файла " +
                        "формата .xlsx"
        ),
        servers = {
                @Server(
                        description = "Local host",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}