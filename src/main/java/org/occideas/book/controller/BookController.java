package org.occideas.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.occideas.book.request.BookRequest;
import org.occideas.book.response.BookResponse;
import org.occideas.book.response.BookVO;
import org.occideas.book.service.BookService;
import org.occideas.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;

@Path("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GET
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> list() {
        return ResponseEntity.ok(new BookResponse(bookService.getBooks()));
    }

    @POST
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBook(Book book) {
        bookService.createBook(book);
        return ResponseEntity.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteBook(@PathParam("id") Long id) {
        final BookVO bookById = bookService.findBookById(id);
        bookService.delete(bookById.getBook());
        return ResponseEntity.ok().build();
    }

    @POST
    @Path("addToBook")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBook(BookRequest bookRequest) throws JsonProcessingException {
        bookService.addModuleToBook(bookRequest);
        return ResponseEntity.ok().build();
    }

}
