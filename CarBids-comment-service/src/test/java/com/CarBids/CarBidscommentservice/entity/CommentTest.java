package com.CarBids.CarBidscommentservice.entity;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest
public class CommentTest {
    @Mock
    private Reply replyMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCommentIdGetterAndSetter() {
        Comment comment = new Comment();
        comment.setCommentId(1L);
        assertEquals(1L, comment.getCommentId());
    }

    @Test
    public void testCommentStringGetterAndSetter() {
        Comment comment = new Comment();
        comment.setCommentString("Test comment");
        assertEquals("Test comment", comment.getCommentString());
    }

    @Test
    public void testLotIdGetterAndSetter() {
        Comment comment = new Comment();
        comment.setLotId(2L);
        assertEquals(2L, comment.getLotId());
    }

    @Test
    public void testParentUserIdGetterAndSetter() {
        Comment comment = new Comment();
        comment.setParentUserId(3L);
        assertEquals(3L, comment.getParentUserId());
    }

    @Test
    public void testRepliesGetterAndSetter() {
        Comment comment = new Comment();
        List<Reply> replies = new ArrayList<>();
        replies.add(replyMock);
        comment.setReplies(replies);

        assertEquals(replies, comment.getReplies());
    }

    @Test
    public void testNoArgsConstructor() {
        Comment comment = new Comment();
        assertNotNull(comment);
    }

    @Test
    public void testAllArgsConstructor() {
        List<Reply> replies = new ArrayList<>();
        replies.add(replyMock);
        Comment comment = new Comment(1L, "Test comment", 2L, 3L, replies);

        assertNotNull(comment);
        assertEquals(1L, comment.getCommentId());
        assertEquals("Test comment", comment.getCommentString());
        assertEquals(2L, comment.getLotId());
        assertEquals(3L, comment.getParentUserId());
        assertEquals(replies, comment.getReplies());
    }

    @Test
    public void testToString() {
        List<Reply> replies = new ArrayList<>();
        replies.add(replyMock);
        Comment comment = new Comment(1L, "Test comment", 2L, 3L, replies);

        String expectedToString = "Comment(commentId=1, commentString=Test comment, lotId=2, parentUserId=3, replies=" + replies + ")";
        assertEquals(expectedToString, comment.toString());
    }
}
