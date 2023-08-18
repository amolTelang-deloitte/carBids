package com.CarBids.carBidslotsservice.specification;

import com.CarBids.carBidslotsservice.entity.Lot;
import com.CarBids.carBidslotsservice.enums.CarEnum.BodyType;
import com.CarBids.carBidslotsservice.enums.CarEnum.TransmissionType;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;


public class LotSpecification {
    public static Specification<Lot> withCriteria(String modelYear, String bodyType, String transmissionType){
        return (root, query, criteriaBuilder) -> {
          
            List<Predicate> predicates = new ArrayList<>();

            if (modelYear != null && !modelYear.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("modelYear"), modelYear));
            }

            if (bodyType != null && !bodyType.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("bodyType"), BodyType.valueOf(bodyType)));
            }

            if (transmissionType != null && !transmissionType.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("transmissionType"), TransmissionType.valueOf(transmissionType)));
            }

            // Combine the predicates using AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
