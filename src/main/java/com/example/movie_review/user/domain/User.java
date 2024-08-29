package com.example.movie_review.user.domain;

import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.comment.Comment;
import com.example.movie_review.feedback.Feedback;
import com.example.movie_review.heart.Heart;
import com.example.movie_review.review.Review;
import com.example.movie_review.favoriteMovie.UserFavoriteMovie;
import com.example.movie_review.genre.PreferredGenres;
import com.example.movie_review.subscription.Subscription;
import com.example.movie_review.user.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Setter @Getter
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 구글 로그인 시 기본 데이터 저장
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // primary key

    private String name; // 이름
    private String email;
    private String picture;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role; // 등급

    /**
     * 추가 정보 입력
     */
    private String nickname; // 닉네임
    private String gender;
    private Long age;
    private String mbti;
    private String department;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PreferredMovies> preferredMovies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PreferredGenres> preferredGenres = new ArrayList<>();


    /**
     * 사용자 이용 현황
     */
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<DbRatings> dbRatings = new ArrayList<>();

    @OneToMany(mappedBy = "subscriber", cascade = ALL, orphanRemoval = true)
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "subscribed", cascade = ALL, orphanRemoval = true)
    private List<Subscription> subscribers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<UserFavoriteMovie> userFavoriteMovies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Feedback> userFeedbacks = new ArrayList<>();





    /**
     * 연관관계 메서드
     */
    public void addReview(Review review) {
        reviews.add(review);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addPreferredMovie(PreferredMovies preferredMovie) { preferredMovies.add(preferredMovie); }

    public void addPreferredGenre(PreferredGenres preferredGenre) { preferredGenres.add(preferredGenre); }

    public void addSubscription(User userToSubscribe) {
        Subscription subscription = new Subscription();
        subscription.setSubscriber(this);
        subscription.setSubscribed(userToSubscribe);
        subscription.setSubscriptionDate(LocalDateTime.now());
        this.subscriptions.add(subscription);
        userToSubscribe.getSubscribers().add(subscription);
    }

    public void removeSubscription(User userToUnsubscribe) {
        Subscription subscription = this.subscriptions.stream()
                .filter(s -> s.getSubscribed().equals(userToUnsubscribe))
                .findFirst()
                .orElse(null);
        if(subscription != null) {
            this.subscriptions.remove(subscription);
            userToUnsubscribe.getSubscribers().remove(subscription);
        }
    }


    public String getRoleKey() {
        return this.role.getKey();
    }

    public User update(String nickname, String gender, Long age, String mbti, String department, String picture) {
        this.nickname = nickname != null ? nickname : this.nickname;
        this.gender = gender != null ? gender : this.gender;
        this.age = age != null ? age : this.age;
        this.mbti = mbti != null ? mbti : this.mbti;
        this.department = department != null ? department : this.department;
        this.picture = picture != null ? picture : this.picture;
        return this;
    }

    public int getFavoriteCount() {
        return userFavoriteMovies.size();
    }

    public int getReviewCount() {
        return reviews.size();
    }

    public int getRatingCount() {
        return dbRatings.size();
    }

    public int getHeartCount() {
        return hearts.size();
    }

    public int getSubscriptionCount() {
        return subscriptions.size();
    }

    public int getSubscriberCount() {
        return subscribers.size();
    }

    public void updateRole(UserRole newRole) { this.role = newRole; }

    public List<User> getSubscribedUsers() {
        return subscriptions.stream()
                .map(Subscription::getSubscribed)
                .collect(Collectors.toList());
    }

    public List<User> getSubscriberUsers() {
        return subscribers.stream()
                .map(Subscription::getSubscriber)
                .collect(Collectors.toList());
    }


}
