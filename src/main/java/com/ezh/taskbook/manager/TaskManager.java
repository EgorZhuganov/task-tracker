package com.ezh.taskbook.manager;

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

    List<Subtask> getListSubtasksByEpic(Epic epic);

    Epic getEpicByUuid(UUID uuid);

    Subtask getSubtaskByUuid(UUID uuid);

    SingleTask getSingleTaskByUuid(UUID uuid);

    Epic getEpicBySubtaskUuid(UUID subtaskUuid);

    void addEpic(Epic epic);

    void addEpicWithSubtask(Epic epic, Subtask... subtask);

    void addSingleTask(SingleTask task);

    void addSubtaskInAddedEpic(Epic epic, Subtask subtask);

    /*Before change SingleTask you have to put in taskList one or more SingleTask*/
    void changeSingleTaskByUuid(UUID uuid, SingleTask newTask);

    /*Before change subtask you have to put in Map Epic with old Subtask*/
    void changeEpicByUuid(UUID uuid, Epic newEpic);

    /*Before change subtask you have to put in Map Epic with Subtask*/
    void changeSubtaskByUuid(UUID uuid, Subtask newSubtask);

    void clearSingleTasks();

    void clearEpics();

    void clearSubtasksInAllEpic();

    void clearSubtasksInEpic(Epic epic);

    void removeSingleTaskByUuid(UUID uuid);

    void removeSubtaskByUuid(UUID uuid);

    void removeEpicByUuid(UUID uuid);

    List<AbstractTask> getHistory();

    List <AbstractTask> getPrioritizedTasks();
}
