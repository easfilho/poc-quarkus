package org.acme.api;

import org.acme.api.dto.DogInputDto;
import org.acme.api.dto.DogOutputDto;
import org.acme.model.Dog;
import org.acme.service.DogService;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/dogs")
public class DogApi {

    private final DogService dogService;
    private final ModelMapper modelMapper;

    public DogApi(DogService dogService) {
        this.dogService = dogService;
        this.modelMapper = new ModelMapper();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(final @RequestBody DogInputDto dogInputDto) {
        return Stream.of(modelMapper.map(dogInputDto, Dog.class))
                .map(dog -> {
                    dogService.create(dog);
                    return map(dog);
                }).map(dogOutputDto -> Response.status(Response.Status.CREATED)
                        .entity(dogOutputDto)
                        .build())
                .findFirst()
                .get();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@DefaultValue(value = "") @QueryParam("name") String name) {
        List<DogOutputDto> dogsOutput = dogService.list(name)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
        return Response.ok(dogsOutput).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "consult dog by id")
    public Response consult(@PathParam(value = "id") Long id) {
        return dogService.consult(id)
                .map(this::map)
                .map(dogOutputDto ->  Response.ok(dogOutputDto).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, final @RequestBody DogInputDto dogInputDto) {
        return Stream.of(modelMapper.map(dogInputDto, Dog.class))
                .map(dog -> {
                    dogService.update(dog, id);
                    return map(dog);
                })
                .map(dogOutputDto -> Response.ok(dogOutputDto)
                        .build())
                .findFirst()
                .get();
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
