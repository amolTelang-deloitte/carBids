package com.CarBids.CarBidscommentservice.repository;


import com.CarBids.CarBidscommentservice.entity.Comment;
import com.CarBids.CarBidscommentservice.service.CommentService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;


    @Test
    public void testFindAllByLotId() {
        Long lotId = 1L;
        Comment comment1 = new Comment(1L, "Comment 1", lotId, 1L, new ArrayList<>());
        Comment comment2 = new Comment(2L, "Comment 2", lotId, 2L, new ArrayList<>());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        when(commentRepository.findAllBylotId(lotId)).thenReturn(comments);

        List<Comment> resultComments = commentRepository.findAllBylotId(lotId);

        assertEquals(2, resultComments.size());
        assertEquals(comment1, resultComments.get(0));
        assertEquals(comment2, resultComments.get(1));

        verify(commentRepository, times(1)).findAllBylotId(lotId);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    public void testFindAllByLotId_NoComments() {
        Long lotId = 1L;

        when(commentRepository.findAllBylotId(lotId)).thenReturn(new ArrayList<>());

        List<Comment> resultComments = commentRepository.findAllBylotId(lotId);

        assertTrue(resultComments.isEmpty());

        verify(commentRepository, times(1)).findAllBylotId(lotId);
        verifyNoMoreInteractions(commentRepository);
    }
}
