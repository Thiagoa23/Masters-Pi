/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.masterspi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//talvez temporario para isADMIN
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class BackofficeController {

    @GetMapping("/backoffice/login")
    public String showBackofficeLogin() {
        return "login";
    }

    @GetMapping("/backoffice")
    public String showBackofficeHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "backoffice";
    }

    @GetMapping("/backoffice/usuarios")
    public String listarUsuarios() {
        return "usuarios";
    }
}
