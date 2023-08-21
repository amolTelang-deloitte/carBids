package com.CarBids.CarBidscommentservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "comment_reply")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String content;
    @NonNull
    private Long parentUserId;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    @JsonIgnoreProperties("replies")
    private Comment parentComment;
}
