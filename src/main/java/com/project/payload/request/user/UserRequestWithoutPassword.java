package com.project.payload.request.user;

import com.project.payload.request.abstracts.AbstractUserRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserRequestWithoutPassword extends AbstractUserRequest {
}
