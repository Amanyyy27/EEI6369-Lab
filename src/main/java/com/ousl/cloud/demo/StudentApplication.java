package com.ousl.cloud.demo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/api/students")
public class StudentApplication {

	private final StudentRepository repository;

	public StudentApplication(StudentRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(StudentApplication.class, args);
	}

	@GetMapping
	public List<Student> getStudents() {
		return repository.findAll(Sort.by("id"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Student addStudent(@RequestBody StudentRequest request) {
		return repository.save(new Student(request.name().trim(), request.course().trim()));
	}
}

@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(name = "uk_students_email", columnNames = "email"))
class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(length = 254)
	private String email;

	@Column(nullable = false, length = 100)
	private String course;

	protected Student() {
	}

	Student(String name, String course) {
		this.name = name;
		this.course = course;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCourse() {
		return course;
	}
}

record StudentRequest(
		String name,
		String course) {
}

interface StudentRepository extends JpaRepository<Student, Long> {
}
