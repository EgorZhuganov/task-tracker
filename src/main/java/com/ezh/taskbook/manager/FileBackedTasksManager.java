package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.ManagerRestoreException;
import com.ezh.taskbook.exception.ManagerSaveException;
import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
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

    protected void save() {
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
                    boolean check = fileBackedTasksManager.storage.containsKey(((Subtask) task).getEpicId());
                    if (check) {
                        Epic epic = (Epic) fileBackedTasksManager.storage.get(((Subtask) task).getEpicId());
                        epic = (Epic) fileBackedTasksManager.storage.get(epic.getUuid());
                        ((Subtask) task).setEpicId(epic);
                        epic.getSubtaskList().add((Subtask) task);
                    } else {
                        tempStorage.add((Subtask) task);
                    }
                }
            }
            if (!tempStorage.isEmpty()) {
                for (Subtask subtask : tempStorage) {
                    Epic epic = (Epic) fileBackedTasksManager.storage.get(subtask.getEpicId());
                    epic.getSubtaskList().add(subtask);
                    subtask.setEpicId(epic);
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
    public List<Subtask> getListSubtasksByEpicUuid(UUID epicId) throws TaskNotFoundException {
        return super.getListSubtasksByEpicUuid(epicId);
    }

    @Override
    public Epic getEpicByUuid(UUID uuid) throws TaskNotFoundException {
        Epic epic = super.getEpicByUuid(uuid);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskByUuid(UUID uuid) throws TaskNotFoundException {
        Subtask subtask = super.getSubtaskByUuid(uuid);
        save();
        return subtask;
    }

    @Override
    public SingleTask getSingleTaskByUuid(UUID uuid) throws TaskNotFoundException {
        SingleTask singleTask = super.getSingleTaskByUuid(uuid);
        save();
        return singleTask;
    }

    @Override
    public Epic getEpicBySubtaskUuid(UUID subtaskUuid) throws TaskNotFoundException {
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
    public void addEpicWithSubtask(Epic epic, Subtask... subtask) throws TasksIntersectionException {
        super.addEpicWithSubtask(epic, subtask);
        save();
    }

    @Override
    public void addSingleTask(SingleTask task) throws TasksIntersectionException {
        super.addSingleTask(task);
        save();
    }

    @Override
    public void addSubtaskInAddedEpic(Subtask subtask) throws TasksIntersectionException, TaskNotFoundException {
        super.addSubtaskInAddedEpic(subtask);
        save();
    }

    @Override
    public void changeSingleTaskByUuid(UUID uuid, SingleTask newTask) throws TaskNotFoundException, TasksIntersectionException {
        super.changeSingleTaskByUuid(uuid, newTask);
        save();
    }

    @Override
    public void changeEpicByUuid(UUID uuid, Epic newEpic) throws TaskNotFoundException {
        super.changeEpicByUuid(uuid, newEpic);
        save();
    }

    @Override
    public void changeSubtaskByUuid(UUID uuid, Subtask newSubtask) throws TaskNotFoundException, TasksIntersectionException {
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
    public void clearSubtasksInEpicByEpicUuid(UUID epicId) throws TaskNotFoundException {
        super.clearSubtasksInEpicByEpicUuid(epicId);
        save();
    }

    @Override
    public void removeSingleTaskByUuid(UUID uuid) throws TaskNotFoundException {
        super.removeSingleTaskByUuid(uuid);
        save();
    }

    @Override
    public void removeSubtaskByUuid(UUID uuid) throws TaskNotFoundException {
        super.removeSubtaskByUuid(uuid);
        save();
    }

    @Override
    public void removeEpicByUuid(UUID uuid) throws TaskNotFoundException {
        super.removeEpicByUuid(uuid);
        save();
    }

    @Override
    public List<AbstractTask> getHistory() {
        return super.getHistory();
    }
}
