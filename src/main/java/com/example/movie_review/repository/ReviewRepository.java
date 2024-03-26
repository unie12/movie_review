package com.example.movie_review.repository;

import com.example.movie_review.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@RequiredArgsConstructor
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    Page<Review> findAll(Pageable pageable);

    List<Review> findAllByUserLoginId(String loginId);

    Page<Review> findByTitleContaining(String keyword, Pageable pageable);

//    private final EntityManager em;
//
//    public void save(Review review) {
//       em.persist(review);
//    }
//
//    public Review findOne(Long id) {
//        return em.find(Review.class, id);
//    }
//
//    public List<Review> findALl() {
//        return em.createQuery("select r from Review r", Review.class)
//                .getResultList();
//    }

//    public Page<Review> findAll(Pageable pageable) {
//        return em.createQuery("select r from Review r", Review.class)
//                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
//                .setMaxResults(pageable.getPageSize())
//                .getResultList();
//    }
}
