# Medsoftpizza

## Описание

Рестсервис для оформления заказа, с возможностью заказать пользовательскую пиццу из предоставленных пользователю на выбор ингредиентов.

## Оглавление

- [Установка](#installation)
- [Entity Relationship Diagram](#erd)
- [Список эндпоинтов для создания позиции меню](#usagemenu)
- [Список эндпоинтов для создания заказа](#usageorder)

##  Установка
Для корректной работы приложения необходимо создать файл "application.properties" в соответствии с файлом "application.properties.example" в директории [resources](src/main/resources/). Поднять базу можно используя [backup](assets).
## Entity Relationship Diagram
![ERD diagram](assets/images/Schemas.png)

## Список эндпоинтов для создания позиции меню
* **/GET/menu_position** - получение списка всех позиций из таблицы меню (за исключением позиций "пользовательских" пицц);
* **/GET/menu_position/{id}** - получение конкретной позиции из таблицы меню;
* **/POST/menu_position** - создание новой позиции в таблице меню:

```json
{
  "name": "Meatballs",
  "description": "tasty meatballs from spicy mackerel"
}
```
* **/PUT/menu_position/{id}** - изменение существующей позиции меню.

```json
{
  "name": "Diablo",
  "description": "hot smoky pizza"
}
```
* **/DELETE/menu_position/{id}** - удаление позиции из таблицы меню по id.

## Список эндпоинтов для создания заказа
* **/GET/orders** - получение списка всех заказов;
* **/GET/orders/{id}** - получение данных о заказе;
* **/POST/orders** - создание нового заказа:

```json
{
  "orderComposition": [
    {
      "id": 3
    },
    {
      "id": 5
    }
  ],
  "customPizzas": [
    {
      "customPizzaIngredients": [
        {
          "id": 5
        },
        {
          "id": 3
        },
        {
          "id": 1
        }
      ]
    },
    {
      "customPizzaIngredients": [
        {
          "id": 4
        },
        {
          "id": 2
        },
        {
          "id": 1
        }
      ]
    }
  ]
}
```
* **/PUT/orders/{id}** - обновление <strong>статуса</strong> заказа

```json
{
  "id" :8,
  "orderStatus": "Ready"
}
```
* **/DELETE/orders/{id}** - удаление заказа по id.

Получение списка ингредиентов для пользователя:
* /GET/ingredient
