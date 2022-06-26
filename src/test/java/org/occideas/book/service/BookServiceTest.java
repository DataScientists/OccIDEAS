package org.occideas.book.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.book.dao.BookDao;
import org.occideas.book.dao.BookModuleDao;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookModuleDao bookModuleDao;
    @Mock
    BookDao bookDao;
    @InjectMocks
    BookService bookService;

//    @Test
//    void givenBookModule_whenExistInBook_thenReturnListOfExisting() {
//        when(bookModuleDao.save(any())).thenReturn(new BookModule());
//        when(bookModuleDao.createBlob(any())).thenReturn(new Blob(new byte[]{}, null));
//        String userId = "u12345";
//        String master = UUID.randomUUID().toString();
//        final JobModule existing = new JobModule();
//        final Fragment nonExisting = new Fragment();
//        when(bookModuleDao.findByFileNameAndBookId(existing.getClass().getName(), 1l))
//                .thenReturn(Optional.of(new BookModule()));
//        when(bookModuleDao.findByFileNameAndBookId(nonExisting.getClass().getName(), 1l))
//                .thenReturn(Optional.empty());
//        List<Object> listOfObjects = new ArrayList<>();
//        listOfObjects.add(existing);
//        listOfObjects.add(nonExisting);
//
//        List<Object> objects = bookService.addAllToBookExcludeExisting(1L, listOfObjects, userId);
//
//        assertFalse(objects.isEmpty());
//        assertEquals(1, objects.size());
//        verify(bookModuleDao, times(1)).save(any(BookModule.class));
//        verify(bookModuleDao, times(1)).createBlob(any());
//        verify(bookModuleDao, times(2)).findByFileNameAndBookId(anyString(), anyLong());
//
//    }

}