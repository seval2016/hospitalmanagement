package com.project.contactmessage.controller;

import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.service.ContactMessageService;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {
    private final ContactMessageService contactMessageService;

    @PostMapping("/save") //http://localhost:8080/contactMessages/save + POST + JSON
    public ResponseMessage<ContactMessageResponse> save(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {
        return contactMessageService.save(contactMessageRequest);
    }

    //Pageable -> veriyi sıralayarak değil sayfalara bölerek gösterme teknolojisine denir.
    @GetMapping("/getAll") // http://localhost:8080/contactMessages/getAll + GET
    public Page<ContactMessageResponse> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.getAll(page, size, sort, type);
    }

    @GetMapping("/searchByEmail")  // http://localhost:8080/contactMessages/searchByEmail?email=aaa@bbb.com  + GET
    public Page<ContactMessageResponse> searchByEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.searchByEmail(email, page, size, sort, type);
    }

    @GetMapping("/searchBySubject")  // http://localhost:8080/contactMessages/searchBySubject?subject=odev + GET
    public Page<ContactMessageResponse> searchBySubject(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.searchBySubject(subject, page, size, sort, type);
    }


    @DeleteMapping("/deleteById/{contactMessageId}") // http://localhost:8080/contactMessages/deleteById/2 + DELETE
    public ResponseEntity<String> deleteByIdPath(@PathVariable Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

    @DeleteMapping("/deleteByIdParam")
    // http://localhost:8080/contactMessages/deleteByIdParam?contactMessageId=4 + DELETE
    public ResponseEntity<String> deleteByIdParam(@RequestParam(value = "contactMessageId") Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }


    @GetMapping("/searchBetweenDates")
    //http://localhost:8080/contactMessages/searchBetweenDates?beginDate=2023-09-13&endDate=2023-09-15
    public ResponseEntity<List<ContactMessage>> searchBetweenDates(
            @RequestParam(value = "beginDate") String beginDateString,
            @RequestParam(value = "endDate") String endDateString) {
        List<ContactMessage> contactMessages = contactMessageService.searchBetweenDates(beginDateString, endDateString);
        return ResponseEntity.ok(contactMessages);
    }
}
