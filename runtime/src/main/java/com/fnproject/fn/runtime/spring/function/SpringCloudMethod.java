package com.fnproject.fn.runtime.spring.function;

import com.fnproject.fn.api.MethodWrapper;
import com.fnproject.fn.api.TypeWrapper;
import com.fnproject.fn.runtime.DefaultTypeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionInspector;
import reactor.core.publisher.Flux;

import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class SpringCloudMethod implements MethodWrapper {
    private FunctionInspector inspector;

    SpringCloudMethod(FunctionInspector inspector) {
        this.inspector = inspector;
    }

    @Override
    public Class<?> getTargetClass() {
        return getFunction().getClass();
    }

    @Override
    public Method getTargetMethod() {
        Class<?> cls = getTargetClass();
        String methodName = getMethodName();

        return Arrays.stream(cls.getMethods())
                .filter((m) -> m.getName().equals(methodName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find " + methodName + " on " + cls));
    }

    /**
     * Returns the name of the method used to invoke the function object.
     * e.g. {@code apply} for {@link java.util.function.Function},
     * {@code accept} for {@link java.util.function.Consumer},
     * {@code get} for {@link java.util.function.Supplier}
     *
     * @return name of the method used to invoke the function object
     */
    protected abstract String getMethodName();

    /**
     * Returns the target function object as an {@link Object}.
     * (used for type inspection purposes)
     *
     * @return target function object
     */
    protected abstract Object getFunction();

    @Override
    public TypeWrapper getParamType(int index) {
        return new DefaultTypeWrapper(inspector.getInputType(getFunction()));
    }

    @Override
    public TypeWrapper getReturnType() {
        return new DefaultTypeWrapper(inspector.getOutputType(getFunction()));
    }

    /**
     * Invoke the target function object
     *
     * @param userFunctionParams
     * @return
     */
    public abstract Flux<?> invoke(Object... userFunctionParams);
}
