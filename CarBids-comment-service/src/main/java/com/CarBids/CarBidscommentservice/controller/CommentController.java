package com.CarBids.CarBidscommentservice.controller;

import com.CarBids.CarBidscommentservice.dto.CommentDetails;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidCredentialsException;
import com.CarBids.CarBidscommentservice.service.ICommentService;
import com.CarBids.CarBidscommentservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final ICommentService commentService;
    private final JwtUtil jwtUtil;

    @Autowired
    public CommentController(ICommentService commentService, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add/newComment")
    public ResponseEntity<?> addCommentToLot(@RequestBody CommentDetails commentDetails, @RequestParam(required = true)String lotId, HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            return commentService.addComment(commentDetails.getCommentString(),Long.parseLong(lotId),userId);
        }
        else{
            throw new RuntimeException("Invalid Credentials");
        }
    }

    @PostMapping("/add/reply")
    public ResponseEntity<?> addReplyToComment(@RequestBody CommentDetails commentDetails, @RequestParam(required = true)String commentId,HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            return commentService.addReplyToComment(commentDetails.getCommentString(),Long.parseLong(commentId),userId);
        }
        else{
            throw new InvalidCredentialsException("Invalid Credentials");
        }
    }

    @GetMapping("/get/lotComment")
    public ResponseEntity<?> getCommentForLot(@RequestParam(required = true)Long lotId){
        return commentService.getCommentForLot(lotId);
    }

    @DeleteMapping("/delete")
    public  ResponseEntity<?> deleteComment(@RequestParam(required = true)Long commentId,HttpServletRequest request){

        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            return commentService.deleteComment(commentId,userId);
        }
        else{
            throw new InvalidCredentialsException("Invalid Credentials");
        }
    }

    @DeleteMapping("/deleteReply")
    public  ResponseEntity<?> deleteReply(@RequestParam(required = true)Long replyId,HttpServletRequest request){

        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            authorizationHeader = authorizationHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(authorizationHeader));
            return commentService.deleteReply(replyId,userId);
        }
        else{
            throw new InvalidCredentialsException("Invalid Credentials");
        }
    }

}
