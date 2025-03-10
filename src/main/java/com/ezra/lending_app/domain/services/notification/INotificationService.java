package com.ezra.lending_app.domain.services;

public interface INotificationService {
    // support sending notification via email, sms and push notification
    // define a method that will be overridden by the implementing class
    void sendNotification(String message);

}
