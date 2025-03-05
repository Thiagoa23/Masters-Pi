/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.masterspi.service;

import com.masterspi.util.CPFValidator;
import org.springframework.stereotype.Service;

/**
 *
 * @author andra
 */
@Service
public class CPFValidatorService {
    public boolean isValid(String cpf) {
        return CPFValidator.isValidCPF(cpf);
    }
}

