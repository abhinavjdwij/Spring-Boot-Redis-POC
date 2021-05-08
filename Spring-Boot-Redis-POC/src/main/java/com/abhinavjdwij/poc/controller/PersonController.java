package com.abhinavjdwij.poc.controller;

import com.abhinavjdwij.poc.entity.Person;
import com.abhinavjdwij.poc.service.PersonService;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/get")
    public ResponseEntity<List<Person>> getAllPerson() {
        List<Person> persons = personService.getAllPerson();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Integer id) {
        try {
            Person person = personService.getPersonById(id);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/add")
    public ResponseEntity<Person> addNewPerson(@RequestBody Person person) {
        try {
            person = personService.addPerson(person);
            return new ResponseEntity<>(person, HttpStatus.ACCEPTED);
        } catch (DuplicateMemberException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        try {
            person = personService.updatePerson(person);
            return new ResponseEntity<>(person, HttpStatus.ACCEPTED);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePersonById(@PathVariable Integer id) {
        try {
            personService.deletePersonById(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
