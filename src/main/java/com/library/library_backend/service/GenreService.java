package com.library.library_backend.service;

import com.library.library_backend.exception.GenreException;
import com.library.library_backend.payload.dto.GenreDTO;

import java.util.List;


public interface GenreService {
    GenreDTO createGenre(GenreDTO genre);
    List<GenreDTO> getAllGenres();
    GenreDTO getGenreById(Long genreId) throws GenreException;
    GenreDTO updateGenre(Long genreId,GenreDTO genre) throws GenreException;
    void deleteGenre(Long genreId) throws GenreException;
    void hardDeleteGenre(Long genreId) throws GenreException;
    List<GenreDTO> getAllActiveGenresWithSubGenres();
    List<GenreDTO> getTopLevelGenres();
//    Page<GenreDTO> searchGenres(String searchTerm, Pageable pageable);
    long getTotalActiveGenres();
    long getBookCountByGenre(Long genreId);
}
