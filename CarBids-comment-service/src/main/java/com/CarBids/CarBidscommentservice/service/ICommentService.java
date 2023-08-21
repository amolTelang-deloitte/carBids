package com.CarBids.CarBidscommentservice.service;

import org.springframework.http.ResponseEntity;

public interface ICommentService {
    ResponseEntity<?> addComment(String commentString,Long lotId,Long userId);
    ResponseEntity<?> addReplyToComment(String replyString,Long commentId,Long userId);
    ResponseEntity<?> getCommentForLot(Long lotId);
    ResponseEntity<?>deleteComment(Long lotId,Long userId);
    ResponseEntity<?>deleteReply(Long replyId,Long userId);

}
