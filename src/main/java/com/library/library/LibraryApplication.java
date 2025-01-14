package com.library.library;

import com.library.library.enums.RoleEnum;
import com.library.library.models.Author;
import com.library.library.models.Book;
import com.library.library.models.Copy;
import com.library.library.models.UserEntity;
import com.library.library.repositories.AuthorRepository;
import com.library.library.repositories.BookRepository;
import com.library.library.repositories.CopyRepository;
import com.library.library.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(AuthorRepository authorRepository, BookRepository bookRepository, UserRepository userRepository, CopyRepository copyRepository) {
		return args -> {
			Author author = new Author("Pablo");
			authorRepository.save(author);

			UserEntity user1 = new UserEntity("Pablo", "pabloromerook@gmail.com" ,"100022022", "Salta", passwordEncoder.encode("test123"), RoleEnum.ADMIN);
			userRepository.save(user1);

			Book book = new Book("El Alquimista", "Planeta", 12345l, 912128l);
			author.addBook(book);
			bookRepository.save(book);

			Copy copy = new Copy("Yenny");
			book.addCopy(copy);
			copyRepository.save(copy);

			Copy copy2 = new Copy("Rayuela");
			book.addCopy(copy2);
			copyRepository.save(copy2);

			Copy copy3 = new Copy("Rayuela");
			book.addCopy(copy3);
			copyRepository.save(copy3);

			UserEntity user2 = new UserEntity("Nacho", "pabloromerook2@gmail.com", "100022022", "Salta",  passwordEncoder.encode("test123"), RoleEnum.USER);
			userRepository.save(user2);

			UserEntity user3 = new UserEntity("Nacho", "pabloromerook3@gmail.com", "100022022", "Salta", passwordEncoder.encode("test123"), RoleEnum.USER);
			userRepository.save(user3);
		};
	}

}
