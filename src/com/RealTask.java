package com;

public class RealTask extends AbstractTask {

    @Override
    public String toString() {
        return "RealTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", uuid=" + getUuid() +
                "}";
    }
}
