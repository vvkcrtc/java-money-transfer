# Описание
Назначение классов:

TransferController - слой controller - взаимодействие с frontend приложением и передача информации классу  service

TransferService - слой service - восприятие данных запроса от controller, осуществление операции перевода
Logger - протоколирование информации переводов

CardsRepository -  слой repository - хранение данных банковских карт
Card - описание данных банковской карты


### Создание Docker образов
Создание образа backend приложения: 

docker build -t money-transfer:latest .

Создание образа frontend приложения:

docker build -t card-transfer:latest .

### Запуск приложений
docker-compose up

### Остановка приложений

docker-compose down



