# API менеджер задач

## Возможности

- Хранение задач в памяти
- Хранение/восстановление задач в/из файла
- Хранение/восстановление задач в/из сервера
- Запуск нескольких серверов для работы с несколькими хранилищами задач

## Описание

- Позволяет создать 3 вида задач: 
   * одиночная задача, 
   * глобальная задача, 
   * глобальная задача с подзадачами
- Задача содержит: 
   * название, 
   * описание, 
   * статус: новая, в процессе выполнения, выполненная
   * время начала выполнения и продолжительность

## Особенности

- Особенность всех задач:
   * не могут пересекаться во времени выполнения
- Особенности подзадачи: 
   * не может быть создана без глобальной задачи
- Особенности глобальной задачи с подзадачами: 
   * статус выполнения зависит от статуса выполнения подзадач, 
   * время начала зависит от времени начала подзадач,
   * продолжительность зависит от продолжительности подзадач
- Особенности глобальной задачи без подзадач:  
   * имеет только название и опиание 


## Технологии

- Java 11
- Maven
- JUnit 5
