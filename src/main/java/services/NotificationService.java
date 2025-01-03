package services;

import dao.NotificationDAO;
import dao.UserDAO;
import dao.UserNotificationDAO;
import entity.Notification;
import entity.User;
import entity.UserNotification;
import observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private UserNotificationDAO userNotificationDAO = new UserNotificationDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();
    private UserDAO userDAO = new UserDAO();
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyAdminsOperators(Notification notification) {
        observers.clear();
        List<User> obs = userDAO.getAllOperators();
        obs.addAll(userDAO.getAllAdmins());
        observers.addAll(obs);
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }

    public void notifyReaders(Notification notification) {
        observers.clear();
        List<User> readers = userDAO.getAllReaders();
        observers.addAll(readers);
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }

    public void notifySpecificReader(Notification notification, User user) {
        observers.clear();
        User u = userDAO.getUserByEmail(user.getEmail());
        addObserver(u);
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }

    public void markNotificationsAsRead(int id) {
        List<UserNotification> un = userNotificationDAO.getUserNotificationsByUserId(id);
        for (UserNotification userNotification : un) {
            userNotification.setRead(true);
            userNotificationDAO.saveOrUpdate(userNotification);
        }
    }
    public void createNotification(Notification notification) {
        notificationDAO.saveOrUpdate(notification);
    }

    public int getUnreadNotificationCount(int userId){
        return userNotificationDAO.getUnreadNotificationCount(userId);
    }

    public List<UserNotification> getNotificationsByUser(int userId){
        return userNotificationDAO.getUserNotificationsByUserId(userId);
    }

    public Notification getNotificationById(int notificationId){
        return notificationDAO.getNotificationById(notificationId);
    }
}
