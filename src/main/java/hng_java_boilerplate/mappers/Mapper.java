package hng_java_boilerplate.mappers;

public interface Mapper <Entity, Dto>{

    Dto mapTo(Entity dtoClass);

    Entity mapFrom(Dto entityClass);
}

