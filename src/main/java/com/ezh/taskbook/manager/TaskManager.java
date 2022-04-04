package com.ezh.taskbook.manager;

import com.ezh.taskbook.exception.TaskNotFoundException;
import com.ezh.taskbook.exception.TasksIntersectionException;
import com.ezh.taskbook.task.AbstractTask;
import com.ezh.taskbook.task.Epic;
import com.ezh.taskbook.task.SingleTask;
import com.ezh.taskbook.task.Subtask;

import java.util.List;
import java.util.UUID;

public interface TaskManager {
    List<Subtask> getListSubtasks();

    List<Epic> getListEpics();

    List<SingleTask> getListSingleTasks();

    List<Subtask> getListSubtasksByEpicId(UUID epicId) throws TaskNotFoundException;

    Epic getEpicByUuid(UUID uuid) throws TaskNotFoundException;

    Subtask getSubtaskByUuid(UUID uuid) throws TaskNotFoundException;

    SingleTask getSingleTaskByUuid(UUID uuid) throws TaskNotFoundException;

    Epic getEpicBySubtaskUuid(UUID subtaskUuid) throws TaskNotFoundException;

    void addEpic(Epic epic);

    void addEpicWithSubtask(Epic epic, Subtask... subtask) throws TasksIntersectionException;

    void addSingleTask(SingleTask task) throws TasksIntersectionException;

    void addSubtaskInAddedEpic(Epic epic, Subtask subtask) throws TasksIntersectionException;

    /*Before change SingleTask you have to put in taskList one or more SingleTask*/
    void changeSingleTaskByUuid(UUID uuid, SingleTask newTask) throws TaskNotFoundException, TasksIntersectionException;

    /*Before change subtask you have to put in Map Epic with old Subtask*/
    void changeEpicByUuid(UUID uuid, Epic newEpic) throws TaskNotFoundException;

    /*Before change subtask you have to put in Map Epic with Subtask*/
    void changeSubtaskByUuid(UUID uuid, Subtask newSubtask) throws TaskNotFoundException, TasksIntersectionException;

    void clearSingleTasks();

    void clearEpics();

    void clearSubtasksInAllEpic();

    void clearSubtasksInEpic(Epic epic) throws TaskNotFoundException;

    void removeSingleTaskByUuid(UUID uuid) throws TaskNotFoundException;

    void removeSubtaskByUuid(UUID uuid) throws TaskNotFoundException;

    void removeEpicByUuid(UUID uuid) throws TaskNotFoundException;

    List<AbstractTask> getHistory();

    List <AbstractTask> getPrioritizedTasks();
}
