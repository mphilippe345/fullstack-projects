package com.superhero.superheroapi.domains.product;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> withReleaseDateLessThanOrEqual(LocalDate releaseDate) {
        return (root, query, cb) -> {
            if (releaseDate == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("releaseDate"), releaseDate);
        };
    }

    // marked for removal
    public static Specification<Product> withAuthors(List<String> authors) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(authors)) {
                return null;
            }
            return root.get("author").in(authors);
        };
    }

    public static Specification<Product> withGenres(List<String> genres) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(genres)) {
                return null;
            }
            return root.get("genre").in(genres);
        };
    }

    public static Specification<Product> withPublishers(List<String> publishers) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(publishers)) {
                return null;
            }
            return root.get("publisher").in(publishers);
        };
    }

    public static Specification<Product> withPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (ObjectUtils.isEmpty(minPrice) && ObjectUtils.isEmpty(maxPrice)) {
                return null;
            }

            Predicate pricePredicate = cb.conjunction();

            if (!ObjectUtils.isEmpty(minPrice)) {
                pricePredicate = cb.and(pricePredicate, cb.ge(root.get("price"), minPrice));
            }

            if (!ObjectUtils.isEmpty(maxPrice)) {
                pricePredicate = cb.and(pricePredicate, cb.le(root.get("price"), maxPrice));
            }

            return pricePredicate;
        };
    }

    public static Specification<Product> withNewRelease(Boolean newrelease) {
        return (root, query, cb) -> {
            if (ObjectUtils.isEmpty(newrelease)) {
                return null;
            }
            return cb.equal(root.get("newrelease"), newrelease);
        };
    }

    public static Specification<Product> withBestSeller(Boolean bestSeller) {
        return (root, query, cb) -> {
            if (ObjectUtils.isEmpty(bestSeller)) {
                return null;
            }
            return cb.equal(root.get("bestSeller"), bestSeller);
        };
    }

    public static Specification<Product> withActive(Boolean active) {
        return (root, query, cb) -> {
            if (ObjectUtils.isEmpty(active)) {
                return cb.equal(root.get("active"), true);
            }
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<Product> withSearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) {
                return null;
            } else {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate titlePredicate = cb.like(cb.lower(root.get("title")), searchPattern);
                Predicate authorPredicate = cb.like(cb.lower(root.get("author")), searchPattern);
                Predicate publisherPredicate = cb.like(cb.lower(root.get("publisher")), searchPattern);

                return cb.or(titlePredicate, authorPredicate, publisherPredicate);
            }
        };
    }
}
