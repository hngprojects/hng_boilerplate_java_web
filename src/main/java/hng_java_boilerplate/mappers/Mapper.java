package hng_java_boilerplate.mappers;


public interface Mapper <A,B>{

    B mapTo(A a);

    A mapFrom(B b);
}

