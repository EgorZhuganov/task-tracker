package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.ManagerRestoreException;
import com.ezh.taskbook.exception.ManagerSaveException;
import com.ezh.taskbook.task.*;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerSingleTaskToString;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerSubtaskToString;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerToString;
import com.ezh.taskbook.task.taskSerializers.TaskSerializerEpicToString;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTasksManager {

    private File file;
    private static Map<TypeTask, TaskSerializerToString<AbstractTask>> taskSerializerMap;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
        taskSerializerMap = new HashMap<>();
        taskSerializerMap.put(TypeTask.EPIC, new TaskSerializerEpicToString());
        taskSerializerMap.put(TypeTask.SINGLE_TASK, new TaskSerializerSingleTaskToString());
        taskSerializerMap.put(TypeTask.SUBTASK, new TaskSerializerSubtaskToString());
    }

    private void save() {
        try (FileWriter fw = new FileWriter(file)) {
            fw.append("id,type,name,status,description,start_time,duration,epic\n");
            storage.values().forEach(t -> {
                try {
                    fw.write(taskSerializerMap.get(t.getType()).taskAsString(t));
                    fw.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.append("\n").append(HistoryManager.asString(inMemoryHistoryManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Непредвиденная ошибка при сохранении записи в файл");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        List<Subtask> tempStorage = new ArrayList<>();
        try {
            String[] lines = Files.readString(Path.of(String.valueOf(file))).split("\\R",-1);
            for (int i = 1; i < lines.length - 2; i++) {
                String[] fields = lines[i].split(";");
                TypeTask type = TypeTask.valueOf(fields[1]);
                AbstractTask task = taskSerializerMap.get(type).taskFromString(lines[i]);
                fileBackedTasksManager.storage.put(task.getUuid(), task);
                if (type == TypeTask.SUBTASK) {
                    Epic epic = ((Subtask)task).getEpic();
                    boolean check = fileBackedTasksManager.storage.containsKey(epic.getUuid());
                    if (check) {
                        epic = (Epic) fileBackedTasksManager.storage.get(epic.getUuid());
                        ((Subtask) task).setEpic(epic);
                        epic.getSubtaskList().add((Subtask) task);
                    } else {
                        tempStorage.add((Subtask) task);
                    }
                }
            }
            if (!tempStorage.isEmpty()) {
                for (Subtask subtask : tempStorage) {
                    Epic epic = (Epic) fileBackedTasksManager.storage.get(subtask.getEpic().getUuid());
                    epic.getSubtaskList().add(subtask);
                    subtask.setEpic(epic);
                }
            }

            if (!lines[lines.length-1].isEmpty()) {
                List<UUID> uuid = HistoryManager.fromString(lines[lines.length - 1]);
                for (UUID id : uuid) {
                    fileBackedTasksManager.inMemoryHistoryManager.add(fileBackedTasksManager.storage.get(id));
                }
            }
        } catch (IOException e) {
            throw new ManagerRestoreException("Непредвиденная ошибка при восстановлении записей в хранилище");
        }
        return fileBackedTasksManager;
    }


    @Override
    public List<Subtask> getListSubtasks() {
        return super.getListSubtasks();
    }

    @Override
    public List<Epic> getListEpics() {
        return super.getListEpics();
    }

    @Override
    public List<SingleTask> getListSingleTasks() {
        return super.getListSingleTasks();
    }

    @Override
    public List<Subtask> getListSubtasksByEpic(Epic epic) {
        return super.getListSubtasksByEpic(epic);
    }

    @Override
    public Epic getEpicByUuid(UUID uuid) {
        Epic epic = super.getEpicByUuid(uuid);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskByUuid(UUID uuid) {
        Subtask subtask = super.getSubtaskByUuid(uuid);
        save();
        return subtask;
    }

    @Override
    public SingleTask getSingleTaskByUuid(UUID uuid) {
        SingleTask singleTask = super.getSingleTaskByUuid(uuid);
        save();
        return singleTask;
    }

    @Override
    public Epic getEpicBySubtaskUuid(UUID subtaskUuid) {
        Epic epic = super.getEpicBySubtaskUuid(subtaskUuid);
        save();
        return epic;
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addEpicWithSubtask(Epic epic, Subtask... subtask) {
        super.addEpicWithSubtask(epic, subtask);
        save();
    }

    @Override
    public void addSingleTask(SingleTask task) {
        super.addSingleTask(task);
        save();
    }

    @Override
    public void addSubtaskInAddedEpic(Epic epic, Subtask subtask) {
        super.addSubtaskInAddedEpic(epic, subtask);
        save();
    }

    @Override
    public void changeSingleTaskByUuid(UUID uuid, SingleTask newTask) {
        super.changeSingleTaskByUuid(uuid, newTask);
        save();
    }

    @Override
    public void changeEpicByUuid(UUID uuid, Epic newEpic) {
        super.changeEpicByUuid(uuid, newEpic);
        save();
    }

    @Override
    public void changeSubtaskByUuid(UUID uuid, Subtask newSubtask) {
        super.changeSubtaskByUuid(uuid, newSubtask);
        save();
    }

    @Override
    public void clearSingleTasks() {
        super.clearSingleTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasksInAllEpic() {
        super.clearSubtasksInAllEpic();
        save();
    }

    @Override
    public void clearSubtasksInEpic(Epic epic) {
        super.clearSubtasksInEpic(epic);
        save();
    }

    @Override
    public void removeSingleTaskByUuid(UUID uuid) {
        super.removeSingleTaskByUuid(uuid);
        save();
    }

    @Override
    public void removeSubtaskByUuid(UUID uuid) {
        super.removeSubtaskByUuid(uuid);
        save();
    }

    @Override
    public void removeEpicByUuid(UUID uuid) {
        super.removeEpicByUuid(uuid);
        save();
    }

    @Override
    public List<AbstractTask> getHistory() {
        return super.getHistory();
    }
}
