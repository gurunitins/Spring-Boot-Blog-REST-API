package com.sopromadze.blogapi.infrastructure.rest.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoPostRequestDto {

    @NotEmpty(message = "The title is required.")
    @Size(min = 2, max = 100, message = "The length of title must be between 2 and 100 characters.")
    private String title;

}
