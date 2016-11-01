package com.carepuppy.pirtu.caremypuppy.Models;


/**
 * Created by Marcel on 11/11/2015.
 */



public class UsersChatModel {

    /*recipient info - Destino*/
    private String firstName;
    private String createdAt;
    private String avatarId;
    private String mRecipientUid;

    /*Current user (or sender) info*/
    private String mCurrentUserName;
    private String mCurrentUserUid;
    private String mCurrentUserCreatedAt;

    public UsersChatModel() {
        //required empty username
    }

    public UsersChatModel(String mCurrentUserName, String firstName, String createdAt, String avatarId, String mRecipientUid, String mCurrentUserUid, String mCurrentUserCreatedAt) {
        this.mCurrentUserName = mCurrentUserName;
        this.firstName = firstName;
        this.createdAt = createdAt;
        this.avatarId = avatarId;
        this.mRecipientUid = mRecipientUid;
        this.mCurrentUserUid = mCurrentUserUid;
        this.mCurrentUserCreatedAt = mCurrentUserCreatedAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getmCurrentUserUid() {
        return mCurrentUserUid;
    }

    public void setmCurrentUserUid(String mCurrentUserUid) {
        this.mCurrentUserUid = mCurrentUserUid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getmRecipientUid() {
        return mRecipientUid;
    }

    public void setmRecipientUid(String mRecipientUid) {
        this.mRecipientUid = mRecipientUid;
    }

    public String getmCurrentUserName() {
        return mCurrentUserName;
    }

    public void setmCurrentUserName(String mCurrentUserName) {
        this.mCurrentUserName = mCurrentUserName;
    }

    public String getmCurrentUserCreatedAt() {
        return mCurrentUserCreatedAt;
    }

    public void setmCurrentUserCreatedAt(String mCurrentUserCreatedAt) {
        this.mCurrentUserCreatedAt = mCurrentUserCreatedAt;
    }


    @Override
    public String toString() {
        return "UsersChatModel{" +
                "firstName='" + firstName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", avatarId='" + avatarId + '\'' +
                ", mRecipientUid='" + mRecipientUid + '\'' +
                ", mCurrentUserName='" + mCurrentUserName + '\'' +
                ", mCurrentUserUid='" + mCurrentUserUid + '\'' +
                ", mCurrentUserCreatedAt='" + mCurrentUserCreatedAt + '\'' +
                '}';
    }
}

