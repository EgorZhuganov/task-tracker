package com.ezh.taskbook.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Subtask extends AbstractTask {

    private StatusTask status;
    private UUID epicId;
    private Duration duration;
    private LocalDateTime startTime;

    public Subtask (Epic epic) {
        super();
        setStatus(StatusTask.NEW);
        this.epicId = epic.getUuid();
    }

    public Subtask (String id) {
        super(id);
        setStatus(StatusTask.NEW);
    }

    @Override
    public TypeTask getType() { return TypeTask.SUBTASK; }

    @Override
    public Duration getDuration() { return duration; }

    @Override
    public LocalDateTime getStartTime() { return startTime; }

    public void setStartTimeAndDuration (LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public StatusTask getStatus() { return status; }

    public void setStatus(StatusTask status) { this.status = status; }

    public UUID getEpicId() { return epicId; }

    public void setEpicId(Epic epic) { this.epicId = epic.getUuid(); }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + status +
                ", uuid=" + getUuid() +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        if (duration == null || startTime == null) {
            return null;
        }
        return startTime.plus(duration);
    }
}
