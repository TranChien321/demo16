package com.codegym.demo16.dto.response;

import com.codegym.demo16.dto.UserDTO;

import java.util.List;

public class ListUserSearchResponse {
    public List<UserDTO> users;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}