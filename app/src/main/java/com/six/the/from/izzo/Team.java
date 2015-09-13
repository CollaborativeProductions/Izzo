package com.six.the.from.izzo;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private int id;
    private int creator_id;
    private Set<Integer> admin_ids;
    private String group_name;
    private String description;
    private Set<Integer> member_ids;

    public Team(int id, int creator_id, String group_name, String description, int member_id) {
        setId(id);
        setCreatorId(id);
        this.admin_ids = new HashSet(); addAdminId(creator_id);
        setGroupName(group_name);
        setDescription(description);
        this.member_ids = new HashSet(); addMemberId(member_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creator_id;
    }

    public void setCreatorId(int creator) {
        this.creator_id = creator;
    }

    public Set<Integer> getAdminIds() {
        return admin_ids;
    }

    public void addAdminId(int new_admin_id) {
        this.admin_ids.add(new_admin_id);
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

    public Set<Integer> getMemberIds() {
        return member_ids;
    }

    public void addMemberId(int member_id) {
        this.member_ids.add(member_id);
    }
}
