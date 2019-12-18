package org.acme.service;

import org.acme.model.Dog;
import org.acme.respository.DogRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DogService {

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public void create(Dog dog) {
        dog.persist();
    }

    public List<Dog> list(String name) {
        return dogRepository.list(name);
    }

    public Optional<Dog> consult(Long id) {
        return Optional.ofNullable(Dog.findById(id));
    }

    public void update(Dog dog, Long id) {
        Optional<Dog> dogToUpdate = consult(id);
        if(dogToUpdate.isPresent()) {
            dogToUpdate.get().setName(dog.getName());
            dogToUpdate.get().setAge(dog.getAge());
            dogToUpdate.get().setSize(dog.getSize());
            dogToUpdate.get().setRace(dog.getRace());
            dogToUpdate.get().persist();
            dog.id = id;
        }
    }

    public void delete(Dog dog) {
        dog.delete();
    }
}
