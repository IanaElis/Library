package observer;

import entity.Notification;

public interface Observer {
    void update(Notification notification);
}
