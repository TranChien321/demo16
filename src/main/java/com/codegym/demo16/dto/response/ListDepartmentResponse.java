package com.codegym.demo16.dto.response;

import com.codegym.demo16.dto.DepartmentDTO;
import com.codegym.demo16.dto.UserDTO;

import java.util.List;

public class ListDepartmentResponse {
    public List<UserDTO> users;
    public int totalPage;
    public int currentPage;
    public int lastPage;
    public int firstPage;

    public ListDepartmentResponse(){}

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public void setDepartments(List<DepartmentDTO> departmentDTOs) {
    }

    public List<DepartmentDTO> getDepartments() {
        return null; // This method should return a list of DepartmentDTO, but currently returns null.
    }
}