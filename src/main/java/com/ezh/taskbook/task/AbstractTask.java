package main.java.com.ezh.taskbook.task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* Не хочется отдавать пользователю логику по генерации ID или присвоению уникального номера к задаче, потому что
* пользователю тогда невозможно будет создать задачу без номера? Или как фронт будет брать номер задачи и передавать
* его в бэк? А мне тогда нужно будет проверить пришел мне уникальный ID или нет, и в случае чего бросить
* RuntimeException?
* Если это так, то у меня один вариант - private static final List<Integer> uuidStorage = new ArrayList<>();
* статический лист id один для всех тасок в который будет записываться пришедший с фронта id и проверяться на
* уникальность.
* Я так понимаю, что минус текущего решения - отсутствие возможности у пользователя - найти таску по id который он
* будет задавать?
* А я не могу на фронт отдать конвертацию uuid в какое-то число и вывод пользователю
* "Ваша Таска сохранена под номером 5". И потом когда от пользователя приходит номер 5, в бэк падает расшифровка, то
* есть UUID таски и возвращается ему в виде "как надо".
 */

abstract class AbstractTask {

    private String name;
    private String description;
    private StatusTask status;
    private final UUID uuid;
    private static final List<UUID> uuidStorage = new ArrayList<>();

    public AbstractTask()  {
        this.status = StatusTask.NEW;
        this.uuid = UUID.randomUUID();
        if (uuidStorage.contains(uuid)){
            throw new RuntimeException("Duplicate keys, try create new Task");
        } else {
            uuidStorage.add(uuid);
        }
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public UUID getUuid() { return uuid; }

    public StatusTask getStatus() { return status; }

    public void setStatus(StatusTask status) { this.status = status; }

}
