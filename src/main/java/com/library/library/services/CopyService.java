package com.library.library.services;

import com.library.library.dtos.CopyDTO;
import com.library.library.exceptions.BookNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.models.Copy;

import java.util.List;

public interface CopyService {
    List<Copy> getAllCopies();
    CopyDTO getCopyById(Long id);

    Copy saveCopy(Copy copy);

    CopyDTO createCopy(CopyDTO copyDTO) throws BookNotFoundException, IllegalAttributeException;
    CopyDTO updateCopy(Long id, CopyDTO copyDTO) throws IllegalAttributeException;

    void deleteCopy(Long id);
    void validateCopy(CopyDTO copyDTO) throws IllegalAttributeException;
}
