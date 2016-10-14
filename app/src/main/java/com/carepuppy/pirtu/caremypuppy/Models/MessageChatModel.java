package com.carepuppy.pirtu.caremypuppy.Models;

/**
 * Created by pirtu on 03/10/2016.
 */
public class MessageChatModel {

    private String message;
    private String recipient;
    private String sender;

    public MessageChatModel(String recipient, String sender, String message) {
        this.recipient = recipient;
        this.sender = sender;
        this.message = message;
    }

    public MessageChatModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "MessageChatModel{" +
                "message_item='" + message + '\'' +
                ", recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
