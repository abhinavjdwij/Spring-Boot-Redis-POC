package com.abhinavjdwij.poc.service;

import com.abhinavjdwij.poc.config.RedisConfig;
import com.abhinavjdwij.poc.entity.Person;
import com.abhinavjdwij.poc.repository.PersonRepository;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    public List<Person> getAllPerson() {
        List<Person> persons = personRepository.findAll();
        return persons;
    }

    // Will cache data for that after first request for that id (only for age <= 40)
    @Cacheable(key = "#id", value = RedisConfig.PERSON, unless = "#result.age > 40")
    public Person getPersonById(Integer id) throws NoSuchElementException {
        if (id == null || !personRepository.existsById(id)) {
            throw new NoSuchElementException("Person does not exist");
        }
        System.out.println("Fetching data from person Repository");
        return personRepository.findById(id).get();
    }

    public Person addPerson(Person person) throws DuplicateMemberException {
        if (person == null || person.getId() == null || personRepository.existsById(person.getId())) {
            throw new DuplicateMemberException("Person already exists");
        }
        System.out.println("Adding data to person Repository");
        personRepository.save(person);
        return person;
    }

    // Will update data for the key whenever data updates
    @CachePut(key = "#result.id", value = RedisConfig.PERSON)
    public Person updatePerson(Person person) throws NoSuchElementException {
        if (person == null || person.getId() == null || !personRepository.existsById(person.getId())) {
            throw new NoSuchElementException("Person does not exist");
        }
        System.out.println("Updating data in person Repository");
        personRepository.save(person);
        return person;
    }

    @CacheEvict(key = "#id", value = RedisConfig.PERSON)
    public void deletePersonById(Integer id) throws NoSuchElementException {
        if (id == null || !personRepository.existsById(id)) {
            throw new NoSuchElementException("Person does not exist");
        }
        System.out.println("Deleting data in person Repository");
        personRepository.deleteById(id);
    }
}
