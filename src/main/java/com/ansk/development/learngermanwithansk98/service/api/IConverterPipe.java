package com.ansk.development.learngermanwithansk98.service.api;

/**
 * Interface for converting an object from {@code Input} to {@code Output}.
 *
 * @param <Input>  initial object
 * @param <Output> converted object
 * @author Anton Skripin
 */
public interface IConverterPipe<Input, Output> {

    /**
     * Pipes an object A to object B
     *
     * @param input input
     * @return output
     */
    Output pipe(Input input);
}
