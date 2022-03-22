package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.task.*;

import java.util.*;

public class InMemoryTasksManager implements TaskManager {

    protected Map<UUID, AbstractTask> storage;
    protected HistoryManager inMemoryHistoryManager;

    public InMemoryTasksManager() {
        System.out.println("Начинаем строить грандиозный план!");
        this.storage = new HashMap<>();
        this.inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    private void checkUuid(AbstractTask task) {
        if (storage.containsKey(task.getUuid())) {
            throw new RuntimeException("Duplicate keys, try create new Task");
        }
    }

    private void checkTaskNotContainsInStorage(UUID  uuid) {
        if (storage.get(uuid) == null){
            throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    public List<Subtask> getListSubtasks() {
        List<Subtask> subtaskList = new ArrayList<>();
        for (AbstractTask task : storage.values()) {
            if (task instanceof Subtask) {
                subtaskList.add((Subtask) task);
            }
        }
        return subtaskList;
    }

    @Override
    public List<Epic> getListEpics() {
        List<Epic> epicList = new ArrayList<>();
        for (AbstractTask task : storage.values()) {
            if (task instanceof Epic) {
                epicList.add((Epic) task);
            }
        }
        return epicList;
    }

    @Override
    public List<SingleTask> getListSingleTasks() {
        List<SingleTask> singleTasksList = new ArrayList<>();
        for (AbstractTask task : storage.values()) {
            if (task instanceof SingleTask) {
                singleTasksList.add((SingleTask) task);
            }
        }
        return singleTasksList;
    }

    @Override
    public List<Subtask> getListSubtasksByEpic(Epic epic) {
        checkTaskNotContainsInStorage(epic.getUuid());
        return new ArrayList<>(((Epic)storage.get(epic.getUuid())).getSubtaskList());
    }

    @Override
    public Epic getEpicByUuid(UUID uuid) {
        if (storage.get(uuid) instanceof Epic) {
            inMemoryHistoryManager.add(storage.get(uuid));
            return (Epic) storage.get(uuid);
        }
        throw new TaskNotFoundException("Can not find task with this UUID");
    }

    @Override
    public Subtask getSubtaskByUuid(UUID uuid) {
        if (storage.get(uuid) instanceof Subtask) {
            inMemoryHistoryManager.add(storage.get(uuid));
            return (Subtask) storage.get(uuid);
        }
        throw new TaskNotFoundException("Can not find task with this UUID");
    }

    @Override
    public SingleTask getSingleTaskByUuid(UUID uuid) {
        if (storage.get(uuid) instanceof SingleTask) {
            inMemoryHistoryManager.add(storage.get(uuid));
            return (SingleTask) storage.get(uuid);
        }
        throw new TaskNotFoundException("Can not find task with this UUID");
    }

    @Override
    public Epic getEpicBySubtaskUuid(UUID subtaskUuid) {
        if (storage.get(subtaskUuid) instanceof Subtask) {
            Subtask subtask = (Subtask) storage.get(subtaskUuid);
            inMemoryHistoryManager.add(subtask.getEpic());
            return subtask.getEpic();
        }
        throw new TaskNotFoundException("Can not find task with this UUID");
    }

    @Override
    public void addEpic(Epic epic) {
        checkUuid(epic);
        storage.put(epic.getUuid(), epic);
    }

    @Override
    public void addEpicWithSubtask(Epic epic, Subtask... subtasks) {
        checkUuid(epic);
        Arrays.stream(subtasks).forEach(this::checkUuid);
        epic.getSubtaskList().addAll(Arrays.asList(subtasks));
        storage.put(epic.getUuid(), epic);
        Arrays.stream(subtasks).forEach(subtask -> subtask.setEpic(epic));
        Arrays.stream(subtasks).forEach(subtask -> storage.put(subtask.getUuid(), subtask));
    }

    @Override
    public void addSubtaskInAddedEpic(Epic epic, Subtask subtask) {
        checkUuid(subtask);
        if (subtask.getEpic() == null || !epic.getUuid().equals(subtask.getEpic().getUuid())) {
            subtask.setEpic(epic);
        }
        ((Epic)storage.get(epic.getUuid())).getSubtaskList().add(subtask);
        storage.put(subtask.getUuid(), subtask);
    }

    @Override
    public void addSingleTask(SingleTask task) {
        checkUuid(task);
        storage.put(task.getUuid(), task);
    }

    /*Before change SingleTask you have to put in storage old SingleTask*/
    @Override
    public void changeSingleTaskByUuid(UUID uuidOldSingleTask, SingleTask newTask) {
        checkTaskNotContainsInStorage(uuidOldSingleTask);
        if (uuidOldSingleTask.equals(newTask.getUuid())) {
            throw new RuntimeException("Trying changing two task with the same uuid");
        }
        SingleTask oldTask = (SingleTask) storage.get(uuidOldSingleTask);
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
        oldTask.setDuration(newTask.getDuration());
        oldTask.setStartTime(newTask.getStartTime());
    }

    /*Before change Epic you have to put in storage old Epic*/
    @Override
    public void changeEpicByUuid(UUID uuidOldEpic, Epic newEpic) {
        checkTaskNotContainsInStorage(uuidOldEpic);
        if (uuidOldEpic.equals(newEpic.getUuid())) {
            throw new RuntimeException("Trying changing two task with the same uuid");
        }
        Epic oldEpic = (Epic) storage.get(uuidOldEpic);
        oldEpic.setName(newEpic.getName());
        oldEpic.setDescription(newEpic.getDescription());
    }

    /*Before change subtask you have to put in storage Epic with old Subtask*/
    @Override
    public void changeSubtaskByUuid(UUID uuidOldSubtask, Subtask newSubtask) {
        checkTaskNotContainsInStorage(uuidOldSubtask);
        Subtask oldSubtask = (Subtask) storage.get(uuidOldSubtask);
        if (uuidOldSubtask.equals(newSubtask.getUuid())) {
            throw new RuntimeException("Trying changing two task with the same uuid");
        } else if (oldSubtask.getEpic().getUuid() != newSubtask.getEpic().getUuid()) {
            throw new RuntimeException("Old subtask and new cannot exist in different epic");
        } else {
            oldSubtask.setName(newSubtask.getName());
            oldSubtask.setDescription(newSubtask.getDescription());
            oldSubtask.setStatus(newSubtask.getStatus());
            oldSubtask.setDuration(newSubtask.getDuration());
            oldSubtask.setStartTime(newSubtask.getStartTime());
        }
    }

    @Override
    public void clearSingleTasks() {
        storage.values().stream().
                filter(task -> task instanceof SingleTask).forEach(this::removeTaskFromHistory);
        storage.values().removeIf(task -> task instanceof SingleTask);
    }

    @Override
    public void clearEpics() {
        storage.values().stream().
                filter(task -> task instanceof Epic).
                peek(this::removeTaskFromHistory).
                peek(epic -> ((Epic) epic).getSubtaskList().forEach(this::removeTaskFromHistory)).
                forEach(epic -> ((Epic) epic).getSubtaskList().clear());
        storage.values().removeIf(task -> task instanceof Epic);
    }

    @Override
    public void clearSubtasksInAllEpic() {
        storage.values().stream().
                filter(at -> at instanceof Epic).
                peek(epic -> ((Epic) epic).getSubtaskList().forEach(this::removeTaskFromHistory)).
                forEach(epic -> ((Epic) epic).getSubtaskList().clear());
        storage.values().removeIf(task -> task instanceof Subtask);
    }

    @Override
    public void clearSubtasksInEpic(Epic epic) {
        checkTaskNotContainsInStorage(epic.getUuid());
        epic = (Epic)storage.get(epic.getUuid());
        epic.getSubtaskList().stream().
                peek(this::removeTaskFromHistory).forEach(subtask -> storage.remove(subtask.getUuid()));
        epic.getSubtaskList().clear();
    }

    @Override
    public void removeSingleTaskByUuid(UUID uuid) {
        checkTaskNotContainsInStorage(uuid);
        removeTaskFromHistory(storage.get(uuid));
        storage.remove(uuid);
    }

    @Override
    public void removeSubtaskByUuid(UUID uuid) {
        checkTaskNotContainsInStorage(uuid);
        Subtask subtask = (Subtask) storage.get(uuid);
        Epic epic = (Epic) storage.get(subtask.getEpic().getUuid());
        removeTaskFromHistory(subtask);
        epic.getSubtaskList().remove(subtask);
        storage.remove(subtask.getUuid());
    }

    @Override
    public void removeEpicByUuid(UUID uuid) {
        checkTaskNotContainsInStorage(uuid);
        Epic epic = (Epic) storage.get(uuid);
        epic.getSubtaskList().stream().
                peek(this::removeTaskFromHistory).forEach(subtask -> storage.remove(subtask.getUuid()));
        removeTaskFromHistory(epic);
        storage.remove(uuid);
    }

    private void removeTaskFromHistory(AbstractTask task) {
        inMemoryHistoryManager.remove(task.getUuid());
    }

    @Override
    public List<AbstractTask> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}

