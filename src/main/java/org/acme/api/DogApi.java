package org.acme.api;

import org.acme.api.dto.DogInputDto;
import org.acme.api.dto.DogOutputDto;
import org.acme.model.Dog;
import org.acme.service.DogService;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/dogs")
public class DogApi {

    private final DogService dogService;

    public DogApi(DogService dogService) {
        this.dogService = dogService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(final DogInputDto dogInputDto) {
        ModelMapper modelMapper = new ModelMapper();
        Dog dog = modelMapper.map(dogInputDto, Dog.class);
        dogService.create(dog);
        DogOutputDto dogOutputDto = map(dog);
        return Response.status(Response.Status.CREATED)
                .entity(dogOutputDto)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@DefaultValue(value = "") @QueryParam("name") String name) {
        List<Dog> dogs = dogService.list(name);
        List<DogOutputDto> dogsOutput = dogs.stream()
                .map(this::map)
                .collect(Collectors.toList());
        return Response.ok(dogsOutput).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consult(@PathParam(value = "id") Long id) {
        System.out.println("Caiu no id");
        return dogService.consult(id)
                .filter(Objects::nonNull)
                .map(this::map)
                .map(dogOutputDto ->  Response.ok(dogOutputDto).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, final DogInputDto dogInputDto) {
        ModelMapper modelMapper = new ModelMapper();
        Dog dog = modelMapper.map(dogInputDto, Dog.class);
        dogService.update(dog, id);
        DogOutputDto dogOutputDto = map(dog);
        return Response.ok(dogOutputDto)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Optional<Dog> dog = dogService.consult(id);
        if(dog.isPresent()) {
            dogService.delete(dog.get());
            return Response.ok().build();
        }
        return Response.noContent().build();
    }

    private DogOutputDto map(Dog dog) {
        ModelMapper modelMapper = new ModelMapper();
        DogOutputDto dogOutputDto = modelMapper.map(dog, DogOutputDto.class);
        dogOutputDto.setId(dog.id);
        return dogOutputDto;
    }
}
