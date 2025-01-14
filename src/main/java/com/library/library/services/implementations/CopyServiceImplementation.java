package com.library.library.services.implementations;

import com.library.library.dtos.CopyDTO;
import com.library.library.exceptions.BookNotFoundException;
import com.library.library.exceptions.CopyNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.models.Book;
import com.library.library.models.Copy;
import com.library.library.models.UserCopy;
import com.library.library.repositories.BookRepository;
import com.library.library.repositories.CopyRepository;
import com.library.library.services.CopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CopyServiceImplementation implements CopyService {

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Copy> getAllCopies() {
        return copyRepository.findAll();
    }

    @Override
    public CopyDTO getCopyById(Long id) {
        return new CopyDTO(copyRepository.findById(id)
        .orElseThrow(() -> new CopyNotFoundException("Copy not found with ID: " + id)));
    }

    @Override
    public Copy saveCopy(Copy copy) {
        return copyRepository.save(copy);
    }

    @Override
    public CopyDTO createCopy(CopyDTO copyDTO) throws BookNotFoundException, IllegalAttributeException {
        validateCopy(copyDTO);

        Copy copy = new Copy();
        copy.setLocation(copyDTO.getLocation());

        Long bookId = copyDTO.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow( () -> new BookNotFoundException("Book not found with ID: " + bookId) );
        copy.setBook(book);

        Copy savedCopy = copyRepository.save(copy);
        return new CopyDTO(savedCopy);
    }

    @Override
    public CopyDTO updateCopy(Long id, CopyDTO copyDTO) throws IllegalAttributeException {
        validateCopy(copyDTO);

        Copy existingCopy = copyRepository.findById(id)
                .orElseThrow(() -> new CopyNotFoundException("Copy not found with ID: " + id));

        existingCopy.setLocation(copyDTO.getLocation());

        copyRepository.save(existingCopy);
        return new CopyDTO(existingCopy);
    }

    @Override
    public void deleteCopy(Long id) {
        if (!copyRepository.existsById(id)) {
            throw new CopyNotFoundException("Copy not found with ID: " + id);
        }
        copyRepository.deleteById(id);
    }

    @Override
    public void validateCopy(CopyDTO copyDTO) throws IllegalAttributeException {
        if (copyDTO.getLocation() == null) {
            throw new IllegalAttributeException("Location cannot be null");
        }
    }
}
