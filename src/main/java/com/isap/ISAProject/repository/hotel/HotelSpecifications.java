package com.isap.ISAProject.repository.hotel;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.hotel.Hotel;

public class HotelSpecifications {
	
	public static Specification<Hotel> findByHotelNameLocationName(String name, String locationName){
		return new Specification<Hotel>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();
				if(name != null) {
					final Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%"+name+"%");
					predicates.add(namePredicate);
				}
				if(locationName != null) {
					final Predicate locationNamePredicate = criteriaBuilder.like(root.join("location").get("name"), "%"+locationName+"%");
					predicates.add(locationNamePredicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
