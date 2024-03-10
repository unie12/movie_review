package com.example.movie_review.domain.review;

import com.example.movie_review.domain.DTO.ReviewCreateRequest;
import com.example.movie_review.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    /**
     * user가 작서항 댓글
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 리뷰에 달린 댓글
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    /**
     * 부모 댓글을 삭제해도 자식 댓글은 살아있게
      */
    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    private boolean isRemoved=false;

    public void update(String newBody) {
        this.body = newBody;
    }

    /**
     * 연관관계 메서드
     */
    public void confirmUser(User user) {
        this.user = user;
        user.addComment(this);
    }

    public void confirmReview(Review review) {
        this.review = review;
        user.addComment(this);
    }

    public void confirmParent(Comment parent) {
        this.parent = parent;
        parent.addChild(this);
    }
    public void addChild(Comment child) {
        childList.add(child);
    }

    /**
     * 모든 자식 댓글이 삭제되었는지 판단
     */
    private boolean isAllChildRemoved() {
        return getChildList().stream()
                .map(Comment::isRemoved)
                .filter(isRemove -> !isRemove)
                .findAny()
                .orElse(true);
    }


}
