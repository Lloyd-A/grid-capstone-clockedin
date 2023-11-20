package com.clockedIn.notificationservice;

import com.clockedIn.userservice.AbstractRequest;
import com.clockedIn.userservice.patterns.observers.Observer;

/**
 * The {@code NotificationService} interface defines the contract for a notification service that can be used to
 * send notifications and manage observers who are interested in receiving notifications.
 *
 * @param <T> The type of User for which the service is generic.
 */
public interface NotificationService{
    /**
     * Adds an observer to the list of subscribers who will receive notifications.
     *
     * @param observer The observer to add.
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from the list of subscribers.
     *
     * @param observer The observer to remove.
     */
    void removeObserver(Observer observer);

    /**
     * Sends a notification using the provided {@code AbstractRequest}.
     *
     * @param request         The request containing information about the notification to be sent.
     *
     */
    void send(AbstractRequest request);

    /**
     * Clears the list of registered observers.
     */
    void clearObservers();
}
