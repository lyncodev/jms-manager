package uk.gov.dwp.jms.manager.core.dao.mongo;

public interface ObjectConverter<T, F> {

    T convertToObject(F dbObject);

    F convertFromObject(T item);
}
