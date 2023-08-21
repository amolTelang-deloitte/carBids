package com.CarBids.CarBidscommentservice.service;

import com.CarBids.CarBidscommentservice.dto.FallbackResponse;
import com.CarBids.CarBidscommentservice.dto.ResponseDTO;
import com.CarBids.CarBidscommentservice.dto.UserIdCheck;
import com.CarBids.CarBidscommentservice.entity.Comment;
import com.CarBids.CarBidscommentservice.entity.Reply;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidContentException;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidIdException;
import com.CarBids.CarBidscommentservice.feignClient.AuthFeignClient;
import com.CarBids.CarBidscommentservice.feignClient.LotFeignClient;
import com.CarBids.CarBidscommentservice.repository.CommentRepository;
import com.CarBids.CarBidscommentservice.repository.ReplyRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sun.media.sound.InvalidDataException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService implements ICommentService{
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final ReplyRepository repository;

    private final AuthFeignClient authFeignClient;

    private final LotFeignClient lotFeignClient;

    @Autowired
    public CommentService(CommentRepository commentRepository, ReplyRepository repository, AuthFeignClient authFeignClient, LotFeignClient lotFeignClient) {
        this.commentRepository = commentRepository;
        this.repository = repository;
        this.authFeignClient = authFeignClient;
        this.lotFeignClient = lotFeignClient;
    }

    @Override
    public ResponseEntity<?> addComment(String commentString, Long lotId, Long userId) throws InvalidDataException {
        logger.info("Attempting to comment on lot"+" "+lotId+" "+LocalDateTime.now());
        if(!checkLotStatus(lotId)){
            logger.error("Attempting to comment on closed lot"+" "+lotId+" "+LocalDateTime.now());
            throw new InvalidIdException("Auction Closed, Cant comment anymore");
        }

        if (commentString.isEmpty()){
            logger.warn("Entered empty comment" +" "+lotId+" "+LocalDateTime.now());
            throw new InvalidContentException("Comment is empty");
        }
        ResponseDTO lotDto = checkLotId(lotId);
        if(lotDto.getData().equals(false)){
            logger.error("Invalid lotId"+" "+lotId+" "+LocalDateTime.now());
            throw new InvalidIdException("Invald Lot Id, Check again");
        }

        ResponseDTO responseDTO = checkUserId(userId);
        if(responseDTO.getData().equals(false)){
            logger.error("Invalid lotId"+" "+lotId+" "+LocalDateTime.now());
            throw new InvalidIdException("Invalid User Id, Check again");
        }

        Comment newComment = Comment.builder()
                .commentString(commentString)
                .parentUserId(userId)
                .lotId(lotId)
                .build();
        commentRepository.save(newComment);
        ResponseDTO<Object> finalresponseDTO = ResponseDTO.builder()
                .status(HttpStatus.CREATED)
                .message("Successfully added comment")
                .data(newComment)
                .build();
        return new ResponseEntity<>(finalresponseDTO,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addReplyToComment(String replyString, Long commentId,Long userId) {
        logger.info("Attempting to reply on comment"+" "+commentId+" "+LocalDateTime.now());
        if (replyString.isEmpty()) throw new InvalidContentException("Reply is empty");

        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new InvalidIdException("Invalid Comment Id! Please check again"));

        if(!checkLotStatus(parentComment.getLotId())){
            logger.error("Attempting to comment on closed lot"+" "+parentComment.getLotId()+" "+LocalDateTime.now());
            throw new InvalidIdException("Auction Closed, Can't comment anymore");
        }

        Reply reply = Reply.builder()
                .parentComment(parentComment)
                .content(replyString)
                .parentUserId(userId)
                .build();
        repository.save(reply);
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.CREATED)
                .message("Successfully added reply to comment")
                .data(reply)
                .build();
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCommentForLot(Long lotId) {
        ResponseDTO responseDTO = checkLotId(lotId);
        if(responseDTO.getData().equals(false)){
            throw new InvalidIdException("Invald Lot Id, Check again");
        }

        List<Comment> comment = commentRepository.findAllBylotId(lotId);
        ResponseDTO<Object> finalresponseDTO = ResponseDTO.builder()
                .status(HttpStatus.OK)
                .message("all comments for lot"+" "+lotId)
                .data(comment)
                .build();
        return new ResponseEntity<>(finalresponseDTO,HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> deleteComment(Long commentId,Long userId) {
        logger.info("Attempting to delete comment"+" "+commentId+" "+LocalDateTime.now());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new InvalidIdException("Invalid Comment Id! Please check again"));
        if(!comment.getParentUserId().equals(userId)){
            logger.warn("Invalid User attempting to delete comment"+" "+LocalDateTime.now());
            throw new InvalidIdException("Cant delete, Invalid Credentials");
        }
        commentRepository.delete(comment);
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.NO_CONTENT)
                .message("successfully deleted comment")
                .build();
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteReply(Long replyId,Long userId) {
        logger.info("Attempting to delete reply"+" "+replyId+" "+LocalDateTime.now());
        Reply reply = repository.findById(replyId)
                .orElseThrow(() -> new InvalidIdException("Invalid Reply Id! Please check again"));
        if(!reply.getParentComment().equals(userId))
            throw new InvalidIdException("Cant delete, Invalid Credentials");
        repository.delete(reply);
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.NO_CONTENT)
                .message("successfully deleted reply")
                .data(reply)
                .build();
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);


    }

    @HystrixCommand(fallbackMethod = "authServiceFallback")
    public ResponseDTO checkUserId(Long userId) throws InvalidDataException {

        ResponseEntity<UserIdCheck> response = authFeignClient.checkUserId(userId);
        if (response.getStatusCode().is2xxSuccessful()) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .status(HttpStatus.OK)
                    .data(response.getBody().getCheck())
                    .build();
            return responseDTO;
        } else {
            throw new InvalidDataException("Invalid UserId");
        }

    }
    @HystrixCommand(fallbackMethod = "lotServiceFallback")
    public ResponseDTO checkLotId(Long lotId){
        ResponseEntity<UserIdCheck> response = lotFeignClient.checkLotId(lotId);
        if (response.getStatusCode().is2xxSuccessful()) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .status(HttpStatus.OK)
                    .data(response.getBody().getCheck())
                    .build();
            return responseDTO;
        } else {
            throw new InvalidIdException("Invalid UserId");
        }
    }

    @HystrixCommand(fallbackMethod = "lotServiceFallback")
    public ResponseDTO checkLotStatus(Long lotId) {
            ResponseEntity<UserIdCheck> response = lotFeignClient.checkLotStatus(lotId);
            if(response.getStatusCode().is2xxSuccessful()){
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .status(HttpStatus.OK)
                        .data(response.getBody().getCheck())
                        .build();
                return responseDTO;
            }else{
                throw new InvalidIdException("Invalid LotId,Check again");
            }
        }

    public ResponseEntity<?> authServiceFallback(){
        FallbackResponse fallbackResponse = FallbackResponse.builder()
                .message("Authentication Service is down, Try again later.")
                .timeStamp(LocalDateTime.now())
                .httpCode(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
        return new ResponseEntity<>(fallbackResponse,HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<?> lotServiceFallback(Long lotId){
        FallbackResponse fallbackResponse = FallbackResponse.builder()
                .message("Lot Service is down, Try again later.")
                .timeStamp(LocalDateTime.now())
                .httpCode(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
        return new ResponseEntity<>(fallbackResponse,HttpStatus.SERVICE_UNAVAILABLE);
    }
}
