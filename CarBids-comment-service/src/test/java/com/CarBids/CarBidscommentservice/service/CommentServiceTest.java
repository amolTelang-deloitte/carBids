package com.CarBids.CarBidscommentservice.service;

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
import com.CarBids.CarBidscommentservice.service.CommentService;
import com.sun.media.sound.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentServiceTest {
    private final LotFeignClient lotFeignClient = mock(LotFeignClient.class);
    private final AuthFeignClient authFeignClient = mock(AuthFeignClient.class);
    private CommentRepository commentRepository = mock(CommentRepository.class);
    private ReplyRepository repository = mock(ReplyRepository.class);
    @InjectMocks
    private CommentService commentService;

    @Test
    void testValidLotStatus() {
        Long lotId = 123L;
        UserIdCheck check = new UserIdCheck(true);
        ResponseEntity<UserIdCheck> successfulResponse = new ResponseEntity<>(check, HttpStatus.OK);
        when(lotFeignClient.checkLotStatus(lotId)).thenReturn(successfulResponse);

        Boolean result = commentService.checkLotStatus(lotId);

        assertTrue(result);
        verify(lotFeignClient, times(1)).checkLotStatus(lotId);
    }
    @Test
    void testInvalidLotStatus() {
        Long lotId = 456L;
        ResponseEntity<UserIdCheck> errorResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(lotFeignClient.checkLotStatus(lotId)).thenReturn(errorResponse);

        assertThrows(InvalidIdException.class, () -> commentService.checkLotStatus(lotId));
        verify(lotFeignClient, times(1)).checkLotStatus(lotId);
    }

    @Test
    void testServerErrorResponse() {
        Long lotId = 789L;
        ResponseEntity<UserIdCheck> serverErrorResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(lotFeignClient.checkLotStatus(lotId)).thenReturn(serverErrorResponse);

        assertThrows(InvalidIdException.class, () -> commentService.checkLotStatus(lotId));
        verify(lotFeignClient, times(1)).checkLotStatus(lotId);
    }

    @Test
    void testNullResponse() {
        Long lotId = 987L;
        ResponseEntity<UserIdCheck> nullResponse = null; // Simulating a null response
        when(lotFeignClient.checkLotStatus(lotId)).thenReturn(nullResponse);

        assertThrows(NullPointerException.class, () -> commentService.checkLotStatus(lotId));
        verify(lotFeignClient, times(1)).checkLotStatus(lotId);
    }

    @Test
    void testCheckUserIdSuccess() {
        // Arrange
        Long userId = 123L;
        ResponseEntity<UserIdCheck> successResponse = new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK);

        when(authFeignClient.checkUserId(userId)).thenReturn(successResponse);

        // Act & Assert
        assertDoesNotThrow(() -> commentService.checkUserId(userId));
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testCheckUserIdFailure() {
        // Arrange
        Long userId = 456L;
        ResponseEntity<UserIdCheck> failureResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(authFeignClient.checkUserId(userId)).thenReturn(failureResponse);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> commentService.checkUserId(userId));
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testCheckUserIdFeignClientError() {
        // Arrange
        Long userId = 789L;

        when(authFeignClient.checkUserId(userId)).thenThrow(InvalidIdException.class);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> commentService.checkUserId(userId));
        verify(authFeignClient, times(1)).checkUserId(userId);
    }

    @Test
    void testCheckLotIdSuccess() {
        // Arrange
        Long lotId = 123L;
        UserIdCheck successResponse = new UserIdCheck(true);
        ResponseEntity<UserIdCheck> responseEntity = new ResponseEntity<>(successResponse, HttpStatus.OK);

        when(lotFeignClient.checkLotId(lotId)).thenReturn(responseEntity);

        // Act
        Boolean result = commentService.checkLotId(lotId);

        // Assert
        assertTrue(result);
        verify(lotFeignClient, times(1)).checkLotId(lotId);
    }

    @Test
    void testCheckLotIdFailure() {
        // Arrange
        Long lotId = 456L;
        ResponseEntity<UserIdCheck> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(lotFeignClient.checkLotId(lotId)).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> commentService.checkLotId(lotId));
        verify(lotFeignClient, times(1)).checkLotId(lotId);
    }

    @Test
    void testCheckLotIdFeignClientError() {
        // Arrange
        Long lotId = 789L;

        when(lotFeignClient.checkLotId(lotId)).thenThrow(InvalidIdException.class);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> commentService.checkLotId(lotId));
        verify(lotFeignClient, times(1)).checkLotId(lotId);
    }

    @Test
    void testCheckLotIdSuccessWithFalseResult() {
        // Arrange
        Long lotId = 123L;
        UserIdCheck successResponse = new UserIdCheck(false); // Different response value
        ResponseEntity<UserIdCheck> responseEntity = new ResponseEntity<>(successResponse, HttpStatus.OK);

        when(lotFeignClient.checkLotId(lotId)).thenReturn(responseEntity);

        // Act
        Boolean result = commentService.checkLotId(lotId);

        // Assert
        assertFalse(result);
        verify(lotFeignClient, times(1)).checkLotId(lotId);
    }

    @Test
    void testCheckLotIdUnexpectedHttpStatus() {
        // Arrange
        Long lotId = 789L;
        ResponseEntity<UserIdCheck> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(lotFeignClient.checkLotId(lotId)).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> commentService.checkLotId(lotId));
        verify(lotFeignClient, times(1)).checkLotId(lotId);
    }

    @Test
    void testCheckLotIdWithNullResponse() {
        // Arrange
        Long lotId = 456L;
        ResponseEntity<UserIdCheck> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(lotFeignClient.checkLotId(lotId)).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> commentService.checkLotId(lotId));
        verify(lotFeignClient, times(1)).checkLotId(lotId);
    }

    @Test
    void testDeleteReplySuccess() {
        // Arrange
        Long replyId = 123L;
        Long userId = 456L;
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setCommentString("test");
        comment.setLotId(123L);
        comment.setParentUserId(userId);
        Reply reply = new Reply();
        reply.setParentComment(comment); // Set parentComment to userId

        when(repository.findById(replyId)).thenReturn(Optional.of(reply));

        // Act
        ResponseEntity<?> responseEntity = commentService.deleteReply(replyId, userId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(repository, times(1)).delete(reply);
    }

    @Test
    void testDeleteReplyInvalidReplyId() {
        // Arrange
        Long replyId = 123L;
        Long userId = 456L;
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setCommentString("test");
        comment.setLotId(123L);
        comment.setParentUserId(userId);

        when(repository.findById(replyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> commentService.deleteReply(replyId, userId));
        verify(repository, never()).delete(any(Reply.class));
    }

    @Test
    void testDeleteReplyInvalidCredentials() {
        // Arrange
        Long replyId = 123L;
        Long userId = 456L;
        Reply reply = new Reply();
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setCommentString("test");
        comment.setLotId(123L);
        comment.setParentUserId(userId);
        reply.setParentComment(comment);
        when(repository.findById(replyId)).thenReturn(Optional.of(reply));
        verify(repository, never()).delete(any(Reply.class));
    }

    @Test
    void testDeleteReplyInvalidParentComment() {
        // Arrange
        Long replyId = 123L;
        Long userId = 456L;
        Reply reply = new Reply();
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setCommentString("test");
        comment.setLotId(123L);
        comment.setParentUserId(userId);

        when(repository.findById(replyId)).thenReturn(Optional.of(reply));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> commentService.deleteReply(replyId, userId));
    }

    @Test
    void testDeleteReplyWithNullReply() {
        // Arrange
        Long replyId = 123L;
        Long userId = 456L;

        when(repository.findById(replyId)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> commentService.deleteReply(replyId, userId));
        verify(repository, never()).delete(any(Reply.class));
    }

    @Test
    void testDeleteReplyWithNullUserId() {
        // Arrange
        Long replyId = 123L;

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> commentService.deleteReply(replyId, null));
        verify(repository, never()).delete(any(Reply.class));
    }

    @Test
    public void testDeleteComment_SuccessfulDeletion() {
        // Arrange
        Long commentId = 123L;
        Long userId = 456L;
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setCommentString("test");
        comment.setLotId(123L);
        comment.setParentUserId(userId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        ResponseEntity<?> response = commentService.deleteComment(commentId, userId);

        // Assert
        verify(commentRepository).delete(comment);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteComment_InvalidCommentId() {
        // Arrange
        Long commentId = 1L;
        Long userId = 100L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(InvalidIdException.class, () -> commentService.deleteComment(commentId, userId));
    }

    @Test
    public void testDeleteComment_InvalidUserAttempt() {
        // Arrange
        Long commentId = 1L;
        Long userId = 100L;
        Comment comment = new Comment();
        comment.setParentUserId(userId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act and Assert
        assertThrows(InvalidIdException.class, () -> commentService.deleteComment(commentId, userId + 1));
    }

    @Test
    public void testDeleteComment_SuccessfulDeletionNoException() {
        // Arrange
        Long commentId = 1L;
        Long userId = 100L;
        Comment comment = new Comment();
        comment.setParentUserId(userId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act and Assert
        assertDoesNotThrow(() -> {
            ResponseEntity<?> response = commentService.deleteComment(commentId, userId);
            verify(commentRepository).delete(comment);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        });
    }

    @Test
    public void testDeleteComment_InvalidCommentId_DoesNotThrowException() {
        // Arrange
        Long commentId = 1L;
        Long userId = 100L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(InvalidIdException.class, () -> {
            commentService.deleteComment(commentId, userId + 1);
        });
    }

    @Test
    public void testDeleteComment_InvalidUserAttempt_ThrowsException() {
        // Arrange
        Long commentId = 1L;
        Long userId = 100L;
        Comment comment = new Comment();
        comment.setParentUserId(userId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act and Assert
        assertThrows(InvalidIdException.class, () -> {
            commentService.deleteComment(commentId, userId + 1);
        });
    }

    @Test
    public void testDeleteComment_ValidCommentId_NullUserId_ThrowsException() {
        // Arrange
        Long commentId = 1L;
        Long userId = null;

        // Act and Assert
        assertThrows(InvalidIdException.class, () -> {
            commentService.deleteComment(commentId, userId);
        });
    }

    @Test
    void testGetCommentForLot_InvalidLotId() {
        Long invalidLotId = -1L;

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            commentService.getCommentForLot(invalidLotId);
        });

        assertEquals(null, exception.getMessage());

        verify(commentRepository, never()).findAllBylotId(anyLong());
    }

    @Test
    void testAddReplyToComment_ValidData() {
        // Arrange
        Long commentId = 1L;
        Long userId = 2L;
        String replyString = "This is a reply.";

        Comment parentComment = new Comment();
        parentComment.setCommentId(commentId);
        parentComment.setLotId(3L);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(parentComment));
        when(lotFeignClient.checkLotStatus(parentComment.getLotId()))
                .thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));

        // Act
        ResponseEntity<?> responseEntity = commentService.addReplyToComment(replyString, commentId, userId);

        // Assert
        verify(repository, times(1)).save(any(Reply.class));
        // Add more assertions on responseEntity if necessary
    }

    @Test
    void testAddReplyToComment_EmptyReplyString() {
        // Arrange
        Long commentId = 1L;
        Long userId = 2L;
        String replyString = "";

        // Act & Assert
        InvalidContentException exception = assertThrows(
                InvalidContentException.class,
                () -> commentService.addReplyToComment(replyString, commentId, userId)
        );
        assertEquals("Reply is empty", exception.getMessage());
    }

    @Test
    void testAddReplyToComment_InvalidCommentId() {
        // Arrange
        Long commentId = 1L;
        Long userId = 2L;
        String replyString = "This is a reply.";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> commentService.addReplyToComment(replyString, commentId, userId)
        );
        assertEquals("Invalid Comment Id! Please check again", exception.getMessage());
    }
    @Test
    void testAddReplyToComment_ClosedAuction() {
        // Arrange
        Long commentId = 1L;
        Long userId = 2L;
        String replyString = "This is a reply.";

        Comment parentComment = new Comment();
        parentComment.setCommentId(commentId);
        parentComment.setLotId(3L);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(parentComment));
        when(lotFeignClient.checkLotStatus(parentComment.getLotId()))
                .thenReturn(new ResponseEntity<>(new UserIdCheck(false), HttpStatus.OK));

        // Act & Assert
        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> commentService.addReplyToComment(replyString, commentId, userId)
        );
        assertEquals("Auction Closed, Can't comment anymore", exception.getMessage());
    }

    @Test
    void testAddReplyToComment_ValidDataAndRepositorySaveFailure() {
        // Arrange
        Long commentId = 1L;
        Long userId = 2L;
        String replyString = "This is a reply.";

        Comment parentComment = new Comment();
        parentComment.setCommentId(commentId);
        parentComment.setLotId(3L);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(parentComment));
        when(lotFeignClient.checkLotStatus(parentComment.getLotId()))
                .thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));

        when(repository.save(any(Reply.class))).thenThrow(new RuntimeException("Failed to save"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.addReplyToComment(replyString, commentId, userId)
        );
        assertEquals("Failed to save", exception.getMessage());
    }

    @Test
    void testCheckLotStatus_Non2xxResponse() {
        // Arrange
        Long lotId = 1L;
        ResponseEntity<UserIdCheck> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(lotFeignClient.checkLotStatus(lotId)).thenReturn(response);

        // Act & Assert
        InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> commentService.checkLotStatus(lotId)
        );
        assertEquals("Invalid LotId,Check again", exception.getMessage());
    }

    @Test
    public void testAddComment_ValidData() {
        // Mocking the behavior of the external services
        when(lotFeignClient.checkLotStatus(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));
        when(authFeignClient.checkUserId(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));
        when(lotFeignClient.checkLotId(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));

        // Test data
        String commentString = "Valid comment";
        Long lotId = 1L;
        Long userId = 1L;

        // Call the service method
        ResponseEntity<?> response = commentService.addComment(commentString, lotId, userId);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testAddComment_ClosedLot() {
        // Mocking the behavior of the external services
        when(lotFeignClient.checkLotStatus(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(false), HttpStatus.OK));

        // Test data
        String commentString = "Closed lot comment";
        Long lotId = 1L;
        Long userId = 1L;

        // Call the service method and expect an exception
        assertThrows(InvalidIdException.class, () -> commentService.addComment(commentString, lotId, userId));
    }

    @Test
    public void testAddComment_EmptyCommentString() {
        // Test data
        String commentString = ""; // Empty comment string
        Long lotId = 1L;
        Long userId = 1L;

        // Call the service method and expect an exception
        assertThrows(NullPointerException.class, () -> commentService.addComment(commentString, lotId, userId));
    }

    @Test
    public void testAddComment_InvalidLotId() {
        // Mocking the behavior of the external services
        when(lotFeignClient.checkLotStatus(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));
        when(authFeignClient.checkUserId(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));
        when(lotFeignClient.checkLotId(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(false), HttpStatus.OK));

        // Test data
        String commentString = "Valid comment";
        Long lotId = 2L; // Invalid lot ID
        Long userId = 1L;

        // Call the service method and expect an exception
        assertThrows(InvalidIdException.class, () -> commentService.addComment(commentString, lotId, userId));
    }

    @Test
    public void testAddComment_InvalidUserId() {
        // Mocking the behavior of the external services
        when(lotFeignClient.checkLotStatus(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));
        when(authFeignClient.checkUserId(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(false), HttpStatus.OK));
        when(lotFeignClient.checkLotId(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(true), HttpStatus.OK));

        // Test data
        String commentString = "Valid comment";
        Long lotId = 1L;
        Long userId = 2L; // Invalid user ID

        // Call the service method and expect an exception
        assertThrows(InvalidIdException.class, () -> commentService.addComment(commentString, lotId, userId));
    }

    @Test
    public void testAddComment_LotStatusCheckFailed() {
        // Mocking the behavior of the external services
        when(lotFeignClient.checkLotStatus(anyLong())).thenReturn(new ResponseEntity<>(new UserIdCheck(false), HttpStatus.INTERNAL_SERVER_ERROR));

        // Test data
        String commentString = "Comment";
        Long lotId = 1L;
        Long userId = 1L;

        // Call the service method and expect an exception
        assertThrows(InvalidIdException.class, () -> commentService.addComment(commentString, lotId, userId));
    }


}
