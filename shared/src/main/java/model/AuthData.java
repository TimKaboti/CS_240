package model;

import java.util.Objects;

public class AuthData {
    String authToken = "";
    String username = "";

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthData authData)) return false;
        return Objects.equals(authToken, authData.authToken) && Objects.equals(username, authData.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "authToken='" + authToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
