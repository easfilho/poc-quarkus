package org.acme.respository;

import io.quarkus.panache.common.Parameters;
import org.acme.model.Dog;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DogRepository {

   public List<Dog> list(String name) {
       return Dog.find(" from Dog dog " +
                               " where dog.name like :name ",
                       Parameters.with("name", "%" + name + "%"))
              .list();
   }
}
