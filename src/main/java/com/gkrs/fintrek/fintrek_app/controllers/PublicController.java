package com.gkrs.fintrek.fintrek_app.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

        @GetMapping("/health")
        public String checkhealth() {
            return "works fine!";
        }
}
