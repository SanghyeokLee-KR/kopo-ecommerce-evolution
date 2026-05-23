package kr.co.javaex.sec23.domain;

import kr.co.javaex.sec23.util.common.enums.user.UserRole;
import kr.co.javaex.sec23.util.common.enums.user.UserStatus;

/**
 * <h3>유저 클래스</h3>
 */
public class User {
    private String userId;
    private String userName;
    private String userPassword;
    private String userPhoneNumber;
    private String userEmail;
    private UserStatus userStatus;
    private UserRole userRole;

    public User() {
    }

    public User(String userId, String userName, String userPassword, String userPhoneNumber, String userEmail, UserStatus userStatus, UserRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User{" + "userId='" + userId + '\'' + ", userName='" + userName + '\'' + ", userPassword='" + userPassword + '\'' + ", userPhoneNumber='" + userPhoneNumber + '\'' + ", userEmail='" + userEmail + '\'' + ", userStatus=" + userStatus + ", userRole=" + userRole + '}';
    }
}
