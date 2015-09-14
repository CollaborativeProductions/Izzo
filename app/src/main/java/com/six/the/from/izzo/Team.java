package com.six.the.from.izzo;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private int id;
    private String group_name;
    private String description;

    public Team(int id, int creator_id, String group_name, String description, int member_id) {
        setId(id);
        setGroupName(group_name);
        setDescription(description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
