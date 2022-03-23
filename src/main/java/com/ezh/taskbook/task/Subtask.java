package com.ezh.taskbook.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends AbstractTask {

    private StatusTask status;
    private Epic epic;
    private Duration duration;
    private LocalDateTime startTime;

    public Subtask (Epic epic) {
        super();
        setStatus(StatusTask.NEW);
        this.epic = epic;
    }

    public Subtask (String id) {
        super(id);
        setStatus(StatusTask.NEW);
    }

    @Override
    public TypeTask getType() { return TypeTask.SUBTASK; }

    @Override
    public Duration getDuration() { return duration; }

    public void setDuration(Duration duration) { this.duration = duration; }

    @Override
    public LocalDateTime getStartTime() { return startTime; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    @Override
    public StatusTask getStatus() { return status; }

    public void setStatus(StatusTask status) { this.status = status; }

    public Epic getEpic() { return epic; }

    public void setEpic(Epic epic) { this.epic = epic; }

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
