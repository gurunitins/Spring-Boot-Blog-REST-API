package com.sopromadze.blogapi.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sopromadze.blogapi.model.audit.DateAudit;
import com.sopromadze.blogapi.model.v2.role.MRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@Table(name = "t_users", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class MUser extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    @Size(max = 40)
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 40)
    private String lastName;

    @NotBlank
    @Column(name = "username")
    @Size(max = 50)
    private String userName;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 100)
    @Column(name = "password")
    private String password;

    @NotBlank
    @NaturalId
    @Size(max = 40)
    @Email
    private String email;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private MAddress address;

    @Column(name = "phone")
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<MRole> roles;


    @ManyToOne(optional = true, fetch = FetchType.EAGER) // A user can optionally belong to a hotel
    @JoinColumn(name = "hotel_id") // Foreign key column in the `t_users` table
    private MHotel hotel;


    public MUser(String firstName, String lastName, String username, String email,String phone, String password,MHotel hotel) {
        this.firstName =  firstName;
        this.lastName = lastName;
        this.userName = username;
        this.email = email;
        this.password = password;
        this.phone= phone;
        this.hotel = hotel;
    }
}
