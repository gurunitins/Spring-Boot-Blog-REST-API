package com.sopromadze.blogapi.model.v2.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "t_roles")
public class MRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(name = "name")
	private MRoleName name;

	public MRole(MRoleName name) {
		this.name = name;
	}
}
