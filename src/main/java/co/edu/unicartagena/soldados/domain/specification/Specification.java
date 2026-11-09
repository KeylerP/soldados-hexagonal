package co.edu.unicartagena.soldados.domain.specification;

/** Patrón Specification: una regla de negocio con nombre propio que se puede combinar. */
public interface Specification<T> {

    boolean esSatisfechaPor(T candidato);

    default Specification<T> y(Specification<T> otra) {
        return c -> this.esSatisfechaPor(c) && otra.esSatisfechaPor(c);
    }

    default Specification<T> o(Specification<T> otra) {
        return c -> this.esSatisfechaPor(c) || otra.esSatisfechaPor(c);
    }

    default Specification<T> no() {
        return c -> !this.esSatisfechaPor(c);
    }
}
