package com.sopromadze.blogapi.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

	@NotBlank(message = "first Name cannot be blank")
	@Size(min = 4, max = 20)
	private String firstName;

	@Size( max = 20)
	private String lastName;

	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	@NotBlank
	@Size(max = 40)
	@Email
	private String email;

	@NotBlank
	@Size(min = 6, max = 20)
	private String password;

	@NotBlank(message = "Phone number cannot be blank")
	@Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
	@Pattern(regexp = "\\d{10}", message = "Phone number must contain only digits")
	private String phone;
}
