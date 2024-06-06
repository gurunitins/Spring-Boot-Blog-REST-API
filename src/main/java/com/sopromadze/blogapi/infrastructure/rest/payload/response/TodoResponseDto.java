package com.sopromadze.blogapi.infrastructure.rest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponseDto {

    private Long todoId;

    private String title;

    private boolean completed;

}
