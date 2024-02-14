package com.project.contactmessage.repository;

import com.project.contactmessage.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Burda repository yerine component yazsak yine çalışırdı fakat repository katmanına has bazı özelliklerden mahrum kalmış oluruz. Service deki kadar esnek değil.
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}
