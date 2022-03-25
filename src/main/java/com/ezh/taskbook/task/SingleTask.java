package com.ezh.taskbook.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class SingleTask extends AbstractTask {

    private StatusTask status;
    private Duration duration;
    private LocalDateTime startTime;

    public SingleTask() {
        super();
        setStatus(StatusTask.NEW);
    }

    public SingleTask(String id) {
        super(id);
        setStatus(StatusTask.NEW);
    }

    @Override
    public TypeTask getType() { return TypeTask.SINGLE_TASK; }

    @Override
    public Duration getDuration() { return duration; }

    @Override
    public LocalDateTime getStartTime() { return startTime; }

    public void setStartTimeAndDuration(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SingleTask{" +
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
