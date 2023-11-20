package com.clockedIn.notificationservice;

import com.clockedIn.userservice.AbstractRequest;
import com.clockedIn.userservice.patterns.observers.Observer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing an Email Notification Service that implements the NotificationService interface.
 * This service allows observers to be notified of requests via email.
 */
@Service
public class EmailNotificationService implements NotificationService {
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Sends an abstract request to all registered observers.
     *
     * @param request The abstract request to be sent.
     */
    @Override
    public void send(AbstractRequest request) {
        for (Observer observer : observers) {
            observer.updateRequestList(request);
        }
    }

    /**
     * Clears the list of registered observers.
     */
    public void clearObservers() {
        observers.clear();
    }
}
