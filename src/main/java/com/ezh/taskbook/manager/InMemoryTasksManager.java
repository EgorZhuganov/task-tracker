package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.task.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTasksManager implements TaskManager  {

    protected Map<UUID, AbstractTask> storage;
    protected Map<LocalDateTime, UUID> tasksIdSortByDataTime;
    protected Set<UUID> uuidsWithNullStartTime;
    protected HistoryManager inMemoryHistoryManager;

    public InMemoryTasksManager() {
        System.out.println("Начинаем строить грандиозный план!");
        this.storage = new HashMap<>();
        tasksIdSortByDataTime = new TreeMap<>();
        uuidsWithNullStartTime = new HashSet<>();
        this.inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    private void checkUuid(AbstractTask task) {
        if (storage.containsKey(task.getUuid())) {
            throw new RuntimeException("Duplicate keys, try create new Task");
        }
    }

    private void checkTaskNotContainsInStorage(UUID uuid) throws TaskNotFoundException {
        if (storage.get(uuid) == null) {
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
    public List<Subtask> getListSubtasksByEpic(Epic epic) throws TaskNotFoundException {
        checkTaskNotContainsInStorage(epic.getUuid());
        return new ArrayList<>(((Epic) storage.get(epic.getUuid())).getSubtaskList());
    }

    @Override
    public Epic getEpicByUuid(UUID uuid) throws TaskNotFoundException {
        if (storage.get(uuid) instanceof Epic) {
            inMemoryHistoryManager.add(storage.get(uuid));
            return (Epic) storage.get(uuid);
        }
        throw new TaskNotFoundException("Can not find task with this UUID");
    }

    @Override
    public Subtask getSubtaskByUuid(UUID uuid) throws TaskNotFoundException {
        if (storage.get(uuid) instanceof Subtask) {
            inMemoryHistoryManager.add(storage.get(uuid));
            return (Subtask) storage.get(uuid);
        }
        throw new TaskNotFoundException("Can not find task with this UUID");
    }

    @Override
    public SingleTask getSingleTaskByUuid(UUID uuid) throws TaskNotFoundException {
        if (storage.get(uuid) instanceof SingleTask) {
            inMemoryHistoryManager.add(storage.get(uuid));
            return (SingleTask) storage.get(uuid);
        }
        throw new TaskNotFoundException("Can not find task with this UUID");
    }

    @Override
    public Epic getEpicBySubtaskUuid(UUID subtaskUuid) throws TaskNotFoundException {
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
        uuidsWithNullStartTime.add(epic.getUuid());
        storage.put(epic.getUuid(), epic);
    }

    private void addTaskToDateTimeStorage(AbstractTask task) throws TasksIntersectionException {
        if (task.getStartTime() == null) {
            uuidsWithNullStartTime.add(task.getUuid());
        } else if (isTimeValid(task)) {
            tasksIdSortByDataTime.put(task.getStartTime(), task.getUuid());
        }
    }

    @Override
    public void addEpicWithSubtask(Epic epic, Subtask... subtasks) throws TasksIntersectionException {
        checkUuid(epic);
        Arrays.stream(subtasks).forEach(this::checkUuid);
        epic.getSubtaskList().addAll(Arrays.asList(subtasks));

        uuidsWithNullStartTime.add(epic.getUuid());
        storage.put(epic.getUuid(), epic);

        Arrays.stream(subtasks).forEach(subtask -> subtask.setEpic(epic));

        Arrays.stream(subtasks).forEach(subtask -> storage.put(subtask.getUuid(), subtask));
        for (Subtask s:subtasks) { addTaskToDateTimeStorage(s); }
    }

    @Override
    public void addSubtaskInAddedEpic(Epic epic, Subtask subtask) throws TasksIntersectionException {
        checkUuid(subtask);
        if (subtask.getEpic() == null || !epic.getUuid().equals(subtask.getEpic().getUuid())) {
            subtask.setEpic(epic);
        }
        ((Epic) storage.get(epic.getUuid())).getSubtaskList().add(subtask);

        addTaskToDateTimeStorage(subtask);
        storage.put(subtask.getUuid(), subtask);
    }

    @Override
    public void addSingleTask(SingleTask task) throws TasksIntersectionException {
        checkUuid(task);
        addTaskToDateTimeStorage(task);
        storage.put(task.getUuid(), task);
    }

    private void removeTaskFromDateTimeStorage(AbstractTask t) {
        if (t.getStartTime() == null) {
            uuidsWithNullStartTime.remove(t.getUuid());
        } else {
            tasksIdSortByDataTime.remove(t.getStartTime());
        }
    }

    /*Before change SingleTask you have to put in storage old SingleTask*/
    @Override
    public void changeSingleTaskByUuid(UUID uuidOldSingleTask, SingleTask newTask) throws TaskNotFoundException, TasksIntersectionException {
        checkTaskNotContainsInStorage(uuidOldSingleTask);
        if (uuidOldSingleTask.equals(newTask.getUuid())) {
            throw new RuntimeException("Trying changing two task with the same uuid");
        }
        SingleTask oldTask = (SingleTask) storage.get(uuidOldSingleTask);
        removeTaskFromDateTimeStorage(oldTask);

        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
        oldTask.setStartTimeAndDuration(newTask.getStartTime(), newTask.getDuration());

        addTaskToDateTimeStorage(oldTask);
    }

    /*Before change Epic you have to put in storage old Epic*/
    @Override
    public void changeEpicByUuid(UUID uuidOldEpic, Epic newEpic) throws TaskNotFoundException {
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
    public void changeSubtaskByUuid(UUID uuidOldSubtask, Subtask newSubtask) throws TaskNotFoundException, TasksIntersectionException {
        checkTaskNotContainsInStorage(uuidOldSubtask);
        if (uuidOldSubtask.equals(newSubtask.getUuid())) {
            throw new RuntimeException("Trying changing two task with the same uuid");
        } else if (((Subtask)storage.get(uuidOldSubtask)).getEpic().getUuid() != newSubtask.getEpic().getUuid()) {
            throw new RuntimeException("Old subtask and new cannot exist in different epic");
        }
        Subtask oldSubtask = (Subtask) storage.get(uuidOldSubtask);
        removeTaskFromDateTimeStorage(oldSubtask);

        oldSubtask.setName(newSubtask.getName());
        oldSubtask.setDescription(newSubtask.getDescription());
        oldSubtask.setStatus(newSubtask.getStatus());
        oldSubtask.setStartTimeAndDuration(newSubtask.getStartTime(), newSubtask.getDuration());

        addTaskToDateTimeStorage(oldSubtask);
    }

    @Override
    public void clearSingleTasks() {
        storage.values().stream()
                .filter(t -> t instanceof SingleTask)
                .peek(t -> removeTaskFromHistory(t))
                .forEach(t -> removeTaskFromDateTimeStorage(t));
        storage.values().removeIf(t -> t instanceof SingleTask);
    }

    @Override
    public void clearEpics() {
        storage.values().stream()
                .filter(task -> task instanceof Epic)
                .peek(epic -> removeTaskFromHistory(epic))
                .peek(epic -> removeTaskFromDateTimeStorage(epic))
                .peek(epic -> ((Epic) epic).getSubtaskList()
                        .stream()
                        .peek(this::removeTaskFromHistory)
                        .forEach(this::removeTaskFromDateTimeStorage))
                .forEach(epic -> ((Epic) epic).getSubtaskList().clear());
        storage.values().removeIf(task -> task instanceof Epic);
        storage.values().removeIf(task -> task instanceof Subtask);
    }

    @Override
    public void clearSubtasksInAllEpic() {
        storage.values().stream()
                .filter(t -> t instanceof Epic)
                .peek(epic -> ((Epic) epic).getSubtaskList()
                        .stream()
                        .peek(this::removeTaskFromHistory)
                        .forEach(this::removeTaskFromDateTimeStorage))
                .forEach(epic -> ((Epic) epic).getSubtaskList().clear());
        storage.values().removeIf(task -> task instanceof Subtask);
    }

    @Override
    public void clearSubtasksInEpic(Epic epic) throws TaskNotFoundException {
        checkTaskNotContainsInStorage(epic.getUuid());
        epic = (Epic) storage.get(epic.getUuid());
        epic.getSubtaskList().stream()
                .peek(this::removeTaskFromHistory)
                .peek(t -> removeTaskFromDateTimeStorage(t))
                .forEach(t -> storage.remove(t.getUuid()));
        epic.getSubtaskList().clear();
    }

    @Override
    public void removeSingleTaskByUuid(UUID uuid) throws TaskNotFoundException {
        checkTaskNotContainsInStorage(uuid);
        removeTaskFromHistory(storage.get(uuid));
        removeTaskFromDateTimeStorage(storage.get(uuid));
        storage.remove(uuid);
    }

    @Override
    public void removeSubtaskByUuid(UUID uuid) throws TaskNotFoundException {
        checkTaskNotContainsInStorage(uuid);
        Subtask subtask = (Subtask) storage.get(uuid);
        Epic epic = (Epic) storage.get(subtask.getEpic().getUuid());
        removeTaskFromHistory(subtask);
        removeTaskFromDateTimeStorage(subtask);
        epic.getSubtaskList().remove(subtask);
        storage.remove(uuid);
    }

    @Override
    public void removeEpicByUuid(UUID uuid) throws TaskNotFoundException {
        checkTaskNotContainsInStorage(uuid);
        Epic epic = (Epic) storage.get(uuid);
        epic.getSubtaskList().stream()
                .peek(this::removeTaskFromHistory)
                .peek(t -> removeTaskFromDateTimeStorage(t))
                .forEach(subtask -> storage.remove(subtask.getUuid()));
        removeTaskFromHistory(epic);
        removeTaskFromDateTimeStorage(epic);
        storage.remove(uuid);
    }

    @Override
    public List<AbstractTask> getPrioritizedTasks() {
        List<AbstractTask> prioritizedTasks = new ArrayList<>();
        tasksIdSortByDataTime.values().stream().map(id -> storage.get(id)).forEach(prioritizedTasks::add);
        uuidsWithNullStartTime.stream().map(id -> storage.get(id)).forEach(prioritizedTasks::add);
        return prioritizedTasks;
    }

    //validator only for subtask and single task, don't use it for epic
    private boolean isTimeValid(AbstractTask task) throws TasksIntersectionException {
        var startTimeCurrentTask = task.getStartTime();
        var endTimeCurrentTask = task.getEndTime();

        List<LocalDateTime> ldtList = new ArrayList<>(tasksIdSortByDataTime.keySet());

        for (int i = 0; i < ldtList.size(); i++) {
            AbstractTask t1 = storage.get(tasksIdSortByDataTime.get(ldtList.get(i)));
            boolean matcher = startTimeCurrentTask.isEqual(t1.getStartTime()) ||
                    startTimeCurrentTask.isEqual(t1.getEndTime()) ||
                    endTimeCurrentTask.isEqual(t1.getStartTime()) ||
                    endTimeCurrentTask.isEqual(t1.getEndTime()) ||
                    startTimeCurrentTask.isAfter(t1.getStartTime()) && startTimeCurrentTask.isBefore(t1.getEndTime()) ||
                    t1.getStartTime().isAfter(startTimeCurrentTask) && t1.getStartTime().isBefore(endTimeCurrentTask);
            if (matcher)
                throw new TasksIntersectionException("Task " + task.getName() + " intersect with " + t1.getName());
        }
        return true;
    }

    private void removeTaskFromHistory(AbstractTask task) {
        inMemoryHistoryManager.remove(task.getUuid());
    }

    @Override
    public List<AbstractTask> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}

