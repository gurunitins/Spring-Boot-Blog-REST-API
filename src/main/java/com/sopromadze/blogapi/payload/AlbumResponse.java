package com.sopromadze.blogapi.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(Include.NON_NULL)
public class AlbumResponse extends UserDateAuditPayload {

    private Long id;

    private String title;

    private UserEntity userEntity;

    private List<Photo> photo;

    public List<Photo> getPhoto() {

        return photo == null ? null : new ArrayList<>(photo);
    }

    public void setPhoto(List<Photo> photo) {

        if (photo == null) {
            this.photo = null;
        } else {
            this.photo = Collections.unmodifiableList(photo);
        }
    }

}
