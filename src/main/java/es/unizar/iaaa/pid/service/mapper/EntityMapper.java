package es.unizar.iaaa.pid.service.mapper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Contract for a generic dto to entity mapper.
 @param <D> - DTO type parameter.
 @param <E> - Entity type parameter.
 */

public interface EntityMapper <D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List <E> toEntity(List<D> dtoList);

    List <D> toDto(List<E> entityList);

    default Pageable toPage(Pageable pageable) {
        if (pageable == null || pageable.getSort() == null) {
            return pageable;
        }
        List<Sort.Order> mappedOrder = new ArrayList<>();
        for (Sort.Order o : pageable.getSort()) {
            String property = getConversions().getOrDefault(o.getProperty(), o.getProperty());
            mappedOrder.add(o.withProperty(property));
        }

        return new Pageable() {
            @Override
            public int getPageNumber() {
                return pageable.getPageNumber();
            }

            @Override
            public int getPageSize() {
                return pageable.getPageSize();
            }

            @Override
            public int getOffset() {
                return pageable.getOffset();
            }

            @Override
            public Sort getSort() {
                return new Sort(mappedOrder);
            }

            @Override
            public Pageable next() {
                return pageable.next();
            }

            @Override
            public Pageable previousOrFirst() {
                return pageable.previousOrFirst();
            }

            @Override
            public Pageable first() {
                return pageable.first();
            }

            @Override
            public boolean hasPrevious() {
                return pageable.hasPrevious();
            }
        };
    }

    default Map<String, String> getConversions() {
        return Collections.emptyMap();
    }
}
