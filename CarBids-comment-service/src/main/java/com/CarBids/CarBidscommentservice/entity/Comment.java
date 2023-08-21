package com.CarBids.CarBidscommentservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    @NonNull
    private String commentString;
    @NonNull
    private Long lotId;
    @NonNull
    private Long parentUserId;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("parentComment")
    private List<Reply> replies = new ArrayList<>();
}
