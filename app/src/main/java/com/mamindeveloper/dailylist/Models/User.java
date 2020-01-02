package com.mamindeveloper.dailylist.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

public class User {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("backups")
    @Expose
    private ArrayList<Backup> backups;

    public User(int id, String email, String name, ArrayList<Backup> backups) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.backups = backups;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Backup> getBackups() {
        return backups;
    }

    public void setBackups(ArrayList<Backup> backups) {
        this.backups = backups;
    }

    static final public class Backup {
        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("dateCreated")
        @Expose
        private DateTime dateCreated;
        @SerializedName("isAutoCreated")
        @Expose
        private Boolean isAutoCreated;

        public Backup(int id, DateTime dateCreated, Boolean isAutoCreated) {
            this.id = id;
            this.dateCreated = dateCreated;
            this.isAutoCreated = isAutoCreated;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public DateTime getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(DateTime dateCreated) {
            this.dateCreated = dateCreated;
        }

        public Boolean getAutoCreated() {
            return isAutoCreated;
        }

        public void setAutoCreated(Boolean autoCreated) {
            isAutoCreated = autoCreated;
        }
    }
}
