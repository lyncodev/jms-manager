package uk.gov.dwp.jms.manager.core.service.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.cxf.common.util.ClasspathScanner;

import java.util.HashMap;
import java.util.Map;

public abstract class ClasspathTypeIdResolver extends TypeIdResolverBase {
    private final Map<String, Class> typeMap;

    public ClasspathTypeIdResolver() {
        this.typeMap = new HashMap<>();

        try {
            ClasspathScanner.findClasses(baseType().getPackage().getName(), JsonTypeName.class)
                    .get(JsonTypeName.class).stream()
                    .filter(x -> baseType().isAssignableFrom(x))
                    .forEach(x -> {
                        typeMap.put(x.getAnnotation(JsonTypeName.class).value(), x);
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Class baseType ();

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getAnnotation(JsonTypeName.class).value();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        return TypeFactory.defaultInstance().uncheckedSimpleType(typeMap.get(id));
    }
}
