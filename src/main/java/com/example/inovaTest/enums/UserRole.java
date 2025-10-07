package com.example.inovaTest.enums;


public enum UserRole {
    SUPERADMIN("superadmin"), // administrador geral(nós)
    ADMIN("admin"),  // sindico
    USER("user"); // morador
    
    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
