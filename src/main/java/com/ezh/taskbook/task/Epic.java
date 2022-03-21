package com.ezh.taskbook.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends AbstractTask {

    private final List<Subtask> subtaskList = new ArrayList<>();

    public Epic() { super(); }

    public Epic(String id) { super(id); }

    public List<Subtask> getSubtaskList() { return subtaskList; }

    @Override
    public TypeTask getType() { return TypeTask.EPIC; }

    @Override
    public StatusTask getStatus() {
        int counterNew = 0;
        int counterDone = 0;
        for (Subtask currentSubtask : subtaskList) {
            if (currentSubtask.getStatus() == StatusTask.NEW) {
                counterNew++;
            } else if (currentSubtask.getStatus() == StatusTask.DONE) {
                counterDone++;
            }
        }
        StatusTask status;
        if (counterNew == subtaskList.size() || subtaskList.isEmpty()) {
            status = StatusTask.NEW;
        } else if (counterDone == subtaskList.size()) {
            status = StatusTask.DONE;
        } else {
            status = StatusTask.IN_PROGRESS;
        }
        return status;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", uuid=" + getUuid() +
                ", subtaskList=" + subtaskList +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        return subtaskList.stream().
                map(Subtask::getEndTime).
                map(endTime -> Optional.ofNullable(endTime)).
                filter(endTime -> endTime.isPresent()).
                max(Comparator.comparing(Optional::get)).get().get();

//        return subtaskList.stream().max(Comparator.comparing(Subtask::getEndTime)).get().getEndTime();
    }

    @Override /* Subtask may not have Duration, Epic will have duration if subtask will have duration */
    public Duration getDuration() {
        boolean matcher = subtaskList.stream().allMatch(subtask -> subtask.getDuration() == null);
        if (matcher) {
            throw new NoSuchElementException("Try to add one or more duration to subtask before count this epic");
        }
        long nanos = subtaskList.stream().
                map(Subtask::getDuration).
                map(duration -> Optional.ofNullable(duration)).
                filter(duration -> duration.isPresent()).
                mapToLong(optional -> optional.get().toNanos()).
                sum();
        return Duration.ofNanos(nanos);
    }

    @Override
    public LocalDateTime getStartTime() {
        return subtaskList.stream().
                map(Subtask::getStartTime).
                map(startTime -> Optional.ofNullable(startTime)).
                filter(startTime -> startTime.isPresent()).
                min(Comparator.comparing(Optional::get)).get().get();

//        return subtaskList.stream().min(Comparator.comparing(Subtask::getStartTime)).get().getStartTime();
    }
}
