package com.example.library_management.mapper;

import com.example.library_management.dto.AuthorDTO;
import com.example.library_management.entities.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorDTO toDTO(Author author){
        if(author == null) return null;

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(authorDTO.getName());
        authorDTO.setBiography(author.getBiography());
        return authorDTO;
    }

    public Author toEntity(AuthorDTO dto){
        if(dto == null) return null;

        Author author = new Author();
        author.setId(dto.getId());
        author.setName(dto.getName());
        author.setBiography(dto.getBiography());
        return author;
    }

    public void updateEntity(Author author,AuthorDTO dto){
        if(dto == null ) return;
        author.setName(dto.getName());
        author.setBiography(dto.getBiography());
    }

}
