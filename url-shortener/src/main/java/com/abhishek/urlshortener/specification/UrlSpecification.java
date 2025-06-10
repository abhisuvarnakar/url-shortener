package com.abhishek.urlshortener.specification;

import com.abhishek.urlshortener.dto.UrlSearchCriteria;
import com.abhishek.urlshortener.entity.ShortenedUrl;
import com.abhishek.urlshortener.entity.enums.Status;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UrlSpecification {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd");

    public static Specification<ShortenedUrl> build(UrlSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addUserIdPredicate(criteria, root, cb, predicates);
            addSearchTermPredicate(criteria, root, cb, predicates);
            addStatusPredicate(criteria, root, cb, predicates);
            addFromDatePredicate(criteria, root, cb, predicates);
            addToDatePredicate(criteria, root, cb, predicates);
            addMinClickPredicate(criteria, root, cb, predicates);
            addMaxClickPredicate(criteria, root, cb, predicates);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addUserIdPredicate(UrlSearchCriteria criteria, Root<ShortenedUrl> root,
                                           CriteriaBuilder cb, List<Predicate> predicates) {

        predicates.add(cb.equal(root.get("user").get("id"), criteria.getUserId()));
    }

    private static void addSearchTermPredicate(UrlSearchCriteria criteria,
                                               Root<ShortenedUrl> root, CriteriaBuilder cb,
                                               List<Predicate> predicates) {

        if (StringUtils.isBlank(criteria.getSearchTerm())) {
            return;
        }
        String searchTerm = "%" + criteria.getSearchTerm().toLowerCase().trim() + "%";
        Predicate originalUrl = cb.like(cb.lower(root.get("originalUrl")), searchTerm);
        Predicate shortCode = cb.like(cb.lower(root.get("shortCode")), searchTerm);
        predicates.add(cb.or(originalUrl, shortCode));
    }

    private static void addStatusPredicate(UrlSearchCriteria criteria, Root<ShortenedUrl> root,
                                           CriteriaBuilder cb, List<Predicate> predicates) {

        if (StringUtils.isBlank(criteria.getStatus())) {
            return;
        }
        try {
            Status status = Status.valueOf(criteria.getStatus().toUpperCase());
            predicates.add(cb.equal(root.get("status"), status));
        } catch (Exception ignored) {

        }
    }

    private static void addFromDatePredicate(UrlSearchCriteria criteria, Root<ShortenedUrl> root,
                                             CriteriaBuilder cb, List<Predicate> predicates) {

        if (StringUtils.isBlank(criteria.getFromDate())) {
            return;
        }
        LocalDate fromDate = LocalDate.parse(criteria.getFromDate(), dateFormatter);
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromDateTime));
    }

    private static void addToDatePredicate(UrlSearchCriteria criteria, Root<ShortenedUrl> root,
                                           CriteriaBuilder cb, List<Predicate> predicates) {

        if (StringUtils.isBlank(criteria.getToDate())) {
            return;
        }
        LocalDate toDate = LocalDate.parse(criteria.getToDate(), dateFormatter);
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);
        predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toDateTime));
    }

    private static void addMinClickPredicate(UrlSearchCriteria criteria, Root<ShortenedUrl> root,
                                             CriteriaBuilder cb, List<Predicate> predicates) {

        if (criteria.getMinClicks() == null) {
            return;
        }
        predicates.add(cb.greaterThanOrEqualTo(root.get("clickCount"), criteria.getMinClicks()));
    }

    private static void addMaxClickPredicate(UrlSearchCriteria criteria, Root<ShortenedUrl> root,
                                             CriteriaBuilder cb, List<Predicate> predicates) {

        if (criteria.getMinClicks() == null) {
            return;
        }
        predicates.add(cb.lessThanOrEqualTo(root.get("clickCount"), criteria.getMaxClicks()));
    }
}
