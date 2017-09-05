package com.fnproject.fn.api;

import java.lang.reflect.Method;

/**
 * Represents a method, similar to {@link Method} but with methods for resolving
 * parameter and return types that are reified generics.
 */
public interface MethodWrapper {
    /**
     * The class from which the wrapper was created, may not necessarily be the declaring class of the method, but a
     * subclass.
     *
     * @return class from which this wrapper was created
     */
    Class<?> getTargetClass();

    /**
     * Gets the underlying wrapped {@link Method}
     *
     * @return {@link Method} which this class wraps
     */
    Method getTargetMethod();

    /**
     * Get the {@link MethodType} for a parameter specified by {@code index}
     * @param index    index of the parameter whose type to retrieve
     * @return the type of the parameter at {@code index}, reified generics will resolve to the reified type
     *         and not {@link Object}
     */
    MethodType getParamType(int index);

    /**
     * Get the {@link MethodType} for the return type of the method.
     * @return the return type, reified generics will resolve to the reified type and not {@link Object}
     */
    MethodType getReturnType();

    /**
     * Get the name of the method including the package path built from {@link Class#getCanonicalName()} and
     * {@link Method#getName()}. e.g. {@code com.fnproject.fn.api.MethodWrapper::getLongName} for this method.
     *
     * @return long method name
     */
    default String getLongName() {
        return getTargetClass().getCanonicalName() + "::" + getTargetMethod().getName();
    }
}
