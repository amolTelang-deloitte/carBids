package com.CarBids.CarBidscommentservice.controller;

import com.CarBids.CarBidscommentservice.dto.CommentDetails;
import com.CarBids.CarBidscommentservice.dto.ResponseDTO;
import com.CarBids.CarBidscommentservice.entity.Comment;
import com.CarBids.CarBidscommentservice.entity.Reply;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidCredentialsException;
import com.CarBids.CarBidscommentservice.exception.exceptions.InvalidIdException;
import com.CarBids.CarBidscommentservice.service.CommentService;
import com.CarBids.CarBidscommentservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentControllerTest {
    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private JwtUtil jwtUtil;


    @Test
    public void testAddCommentToLotWithValidAuthorization() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("123");
        when(commentService.addComment(anyString(), anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        CommentDetails commentDetails = new CommentDetails("Test Comment");
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.CREATED);

        // Act
        ResponseEntity<?> response = commentController.addCommentToLot(
                commentDetails, "1", request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(commentService).addComment("Test Comment", 1L, 123L);
    }

    @Test
    public void testAddCommentToLotWithInvalidAuthorization() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                commentController.addCommentToLot(
                        new CommentDetails("Test Comment"), "1", request));
    }


    @Test
    public void testAddCommentToLotWithEmptyLotId() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("123");

        // Act & Assert
        assertThrows(NumberFormatException.class, () ->
                commentController.addCommentToLot(
                        new CommentDetails("Test Comment"), "", request));
    }

    @Test
    public void testAddCommentToLotWithInvalidUserId() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.getUserIdFromToken("validToken")).thenReturn("invalidUserId");

        // Act & Assert
        assertThrows(NumberFormatException.class, () ->
                commentController.addCommentToLot(
                        new CommentDetails("Test Comment"), "1", request));
    }


    @Test
    public void testAddReplyToComment_InvalidAuthorization() {
        String commentId = "1";
        String commentString = "Test reply";
        String authorizationHeader = "InvalidToken";

        CommentDetails commentDetails = new CommentDetails();
        commentDetails.setCommentString(commentString);

        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);

        assertThrows(InvalidCredentialsException.class,
                () -> commentController.addReplyToComment(commentDetails, commentId, request));
    }

    @Test
    public void testAddReplyToComment_MissingAuthorization() {
        String commentId = "1";
        String commentString = "Test reply";

        CommentDetails commentDetails = new CommentDetails();
        commentDetails.setCommentString(commentString);

        when(request.getHeader("Authorization")).thenReturn(null);

        assertThrows(InvalidCredentialsException.class,
                () -> commentController.addReplyToComment(commentDetails, commentId, request));
    }

    @Test
    public void testGetCommentForLot_ServiceError() {
        Long lotId = 456L;
        when(commentService.getCommentForLot(lotId)).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        ResponseEntity<?> response = commentController.getCommentForLot(lotId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(commentService, times(1)).getCommentForLot(lotId);
    }

    @Test
    public void testDeleteReply_InvalidAuthorizationHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () -> commentController.deleteReply(1L, request));
    }

    @Test
    public void testDeleteReply_InvalidAuthorizationHeaderFormat() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("InvalidFormatToken");

        assertThrows(InvalidCredentialsException.class, () -> commentController.deleteReply(1L, request));
    }

    @Test
    public void testDeleteReply_InvalidReplyId() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");

        Long replyId = 1L;
        Long userId = 123L;
        when(commentService.deleteReply(eq(replyId), eq(userId))).thenThrow(InvalidIdException.class);

        assertThrows(NumberFormatException.class, () -> commentController.deleteReply(replyId, request));
    }

    @Test
    public void testDeleteReply_UnauthorizedDelete() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");

        Long replyId = 1L;
        Long userId = 123L;
        when(commentService.deleteReply(eq(replyId), eq(userId))).thenThrow(InvalidIdException.class); // Assuming this exception for unauthorized delete

        assertThrows(NumberFormatException.class, () -> commentController.deleteReply(replyId, request));
    }

    @Test
    void testDeleteComment_InvalidCredentials() {
        // Arrange
        Long commentId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> commentController.deleteComment(commentId, request));
        verify(commentService, never()).deleteComment(any(), any());
    }

}
