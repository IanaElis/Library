package org.example;

import dao.RegisterFormDAO;
import dao.RegisterStatusDAO;
import dao.RoleDAO;
import dao.UserDAO;
import entity.RegisterForm;
import entity.RegisterStatus;
import entity.Role;
import entity.User;

import java.time.LocalDate;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        //RoleDAO roledao = new RoleDAO();
        RegisterStatusDAO registerStatusDAO = new RegisterStatusDAO();
        RegisterForm regForm = new RegisterForm(1,"ivanov@gmail.bg",
                "1234", "Ivan Ivanov", 885678907,
                registerStatusDAO.getRegisterStatusById(1), LocalDate.now());

        RegisterFormDAO registerFormDAO = new RegisterFormDAO();
        //registerFormDAO.saveRegisterForm(regForm);



    //    registerFormDAO.deleteRegisterForm(regForm);
        /*User user = new User(regForm.getEmail(), regForm.getPassword(),
                regForm.getName(), regForm.getPhoneNumber(), regForm.getDateCreated(),
                regForm, roledao.getRoleById(1) );
        UserDAO.saveUser(user);

         */
    }
}