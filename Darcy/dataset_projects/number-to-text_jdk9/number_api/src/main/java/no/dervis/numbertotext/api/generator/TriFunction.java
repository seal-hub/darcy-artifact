package no.dervis.numbertotext.api.generator;

@FunctionalInterface
public interface TriFunction <T, U, K, R> {

        /**
         * Applies this function to the given arguments.
         *
         * @param t the first function argument
         * @param u the second function argument
         * @param k the third function argument
         * @return the function result
         */
        R apply(T t, U u, K k);
    }
