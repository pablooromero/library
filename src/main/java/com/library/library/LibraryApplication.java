package com.library.library;

import com.library.library.models.Author;
import com.library.library.models.Book;
import com.library.library.repositories.AuthorRepository;
import com.library.library.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(AuthorRepository authorRepository, BookRepository bookRepository) {
		return args -> {
			Author author = new Author("Pablo", 9122018l);
			authorRepository.save(author);


			Book book = new Book("El Alquimista", "Planeta", 12345l, 912128l, 300l);
			author.addBokk(book);
			bookRepository.save(book);

		};
	}

}
