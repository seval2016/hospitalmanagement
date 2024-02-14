package com.project.contactmessage.service;

import com.project.contactmessage.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

 //Service ile anote edilmesi gerekli çünkü bu bir semantik yani business lojic katımız burası developerdan ziyade framework'e de haber vermiş oluyoruz, component yazmış olsaydık bir sorun olmazdı fakat
//Araştırma Konusu : Kotlin ile entity classlar nasıl yazılır ?
@Service
@RequiredArgsConstructor
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;
}
