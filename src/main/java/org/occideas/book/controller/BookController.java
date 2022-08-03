package org.occideas.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.occideas.book.request.AddNodeRequest;
import org.occideas.book.request.BookRequest;
import org.occideas.book.response.BookResponse;
import org.occideas.book.response.BookVO;
import org.occideas.book.service.BookService;
import org.occideas.entity.Book;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@RestController
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
        bookService.delete(bookById);
        return ResponseEntity.ok().build();
    }

    @DELETE
    @Path("/{bookId}/{name}")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteBookModuleInBook(@PathParam("bookId") Long bookId, @PathParam("name") String name) throws IOException {
        bookService.deleteBookModuleInBook(bookId, name);
        return ResponseEntity.ok().build();
    }


    @POST
    @Path("addToBook")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addModuleToBook(AddNodeRequest addNodeRequest) throws JsonProcessingException {
        TokenManager tokenManager = new TokenManager();
        String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
        String updatedBy = tokenManager.parseUsernameFromToken(token);
        bookService.addModuleToBookByNode(addNodeRequest.getIdNode(), addNodeRequest.getBookId(), updatedBy, new ArrayList<>(), true);
        return ResponseEntity.ok().build();
    }

    @POST
    @Path("uploadJsonToBook")
    public ResponseEntity uploadJsonToBook(FormDataMultiPart multiPart) {
        InputStream uploadedInputStream = multiPart.getField("file").getValueAs(InputStream.class);
        long bookId = multiPart.getField("bookId").getValueAs(Long.class);
        String fileName = multiPart.getField("fileName").getValueAs(String.class);
        TokenManager tokenManager = new TokenManager();
        String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
        String updatedBy = tokenManager.parseUsernameFromToken(token);
        String content;
        try {
            content = new String(uploadedInputStream.readAllBytes(), StandardCharsets.UTF_8);
            bookService.addModuleToBook(new BookRequest(bookId, fileName, "Task Modules", content, updatedBy));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

}
