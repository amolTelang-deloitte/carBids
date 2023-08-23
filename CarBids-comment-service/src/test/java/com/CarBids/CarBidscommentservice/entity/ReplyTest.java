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

public class ReplyTest {
    @Mock
    private Comment commentMock;
    @Test
    public void testIdGetterAndSetter() {
        Reply reply = new Reply();
        reply.setId(1L);
        assertEquals(1L, reply.getId());
    }

    @Test
    public void testContentGetterAndSetter() {
        Reply reply = new Reply();
        reply.setContent("Test content");
        assertEquals("Test content", reply.getContent());
    }

    @Test
    public void testParentUserIdGetterAndSetter() {
        Reply reply = new Reply();
        reply.setParentUserId(2L);
        assertEquals(2L, reply.getParentUserId());
    }

    @Test
    public void testParentCommentGetterAndSetter() {
        Reply reply = new Reply();
        reply.setParentComment(commentMock);
        assertEquals(commentMock, reply.getParentComment());
    }

    @Test
    public void testNoArgsConstructor() {
        Reply reply = new Reply();
        assertNotNull(reply);
    }

    @Test
    public void testAllArgsConstructor() {
        Reply reply = new Reply(1L, "Test content", 2L, commentMock);
        assertNotNull(reply);
        assertEquals(1L, reply.getId());
        assertEquals("Test content", reply.getContent());
        assertEquals(2L, reply.getParentUserId());
        assertEquals(commentMock, reply.getParentComment());
    }

    @Test
    public void testToString() {
        Reply reply = new Reply(1L, "Test content", 2L, commentMock);
        String expectedToString = "Reply(id=1, content=Test content, parentUserId=2, parentComment=" + commentMock + ")";
        assertEquals(expectedToString, reply.toString());
    }
}
