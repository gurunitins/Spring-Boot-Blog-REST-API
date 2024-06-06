package com.sopromadze.blogapi.domain.port;

import com.sopromadze.blogapi.domain.model.Todo;

public interface TodoPersistencePort {

    Todo insert(Todo todo, String creatorUsername);

}
