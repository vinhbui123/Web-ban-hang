package vn.edu.hcmuaf.fit.Web_ban_hang.model;

import java.util.Date;

public class Log {
    private String level;
    private String action;
    private String username;
    private String message;
    private Date timestamp;

    public Log(String level, String action, String username, String message) {
        this.level = level;
        this.action = action;
        this.username = username;
        this.message = message;
        this.timestamp = new Date();
    }

    public String getLevel() { return level; }
    public String getAction() { return action; }
    public String getUsername() { return username; }
    public String getMessage() { return message; }
    public Date getTimestamp() { return timestamp; }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] [" + level + "] [" + action + "] " + username + ": " + message;
    }
}
