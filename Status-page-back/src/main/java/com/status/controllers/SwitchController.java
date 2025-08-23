package com.status.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.status.entities.SwitchEntity;
import com.status.services.SwitchService;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "*")
public class SwitchController {
    @Autowired
    private SwitchService switchService;

    @PostMapping("/save")
    private ResponseEntity<SwitchEntity> newSwitch(@RequestBody SwitchEntity request) {
        return new ResponseEntity<>(switchService.newSwitch(request), HttpStatus.OK);
    }

    @GetMapping("/all")
    private ResponseEntity<List<SwitchEntity>> getAll() {
        return new ResponseEntity<>(switchService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/update")
    private ResponseEntity<SwitchEntity> updateSwitch(@RequestBody SwitchEntity request) {
        SwitchEntity response = switchService.updateSwitch(request);
        if (response == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Boolean> deleteSwitch(@PathVariable Long id) {
        boolean response = switchService.deleteSwitch(id);
        if (!response)
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
