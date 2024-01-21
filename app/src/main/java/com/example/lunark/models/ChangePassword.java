package com.example.lunark.models;

public class ChangePassword {
    private Long accountId;
    private String oldPassword;
    private String newPassword;

    public ChangePassword(Long accountId, String oldPassword, String newPassword) {
        this.accountId = accountId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
