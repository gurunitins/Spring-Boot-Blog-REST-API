package com.sopromadze.blogapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSummary {
	private Long id;
	private String userName;
	private String firstName;
	private String lastName;
}
