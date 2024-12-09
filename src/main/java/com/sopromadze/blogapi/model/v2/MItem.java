package com.sopromadze.blogapi.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopromadze.blogapi.model.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "t_item")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor
public class MItem extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "title")
    private String title;

    @Column(name = "alias")
    private String alias;

    @Column(name = "shortcut")
    private String shortcut;


    private String description;

    private Boolean isActive = true;

    @Column(name = "is_veg")
    private Boolean isVeg = true;

    @NonNull
    @Column(name = "price")
    private Long price;


    @JsonIgnore
    @Column(name = "menu_id")
    private Long menuId;

}
