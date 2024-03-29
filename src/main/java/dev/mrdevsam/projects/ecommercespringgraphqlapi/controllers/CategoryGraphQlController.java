package dev.mrdevsam.projects.ecommercespringgraphqlapi.controllers;

import dev.mrdevsam.projects.ecommercespringgraphqlapi.model.Category;
import dev.mrdevsam.projects.ecommercespringgraphqlapi.model.CategoryUpdater;
import dev.mrdevsam.projects.ecommercespringgraphqlapi.repositories.CategoryRepo;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@Slf4j
public class CategoryGraphQlController {

	private final CategoryRepo repo;

	public CategoryGraphQlController(CategoryRepo repo) {
		this.repo = repo;
	}

	@PreAuthorize("hasRole('USER')")
	@QueryMapping
	public List<Category> findAllCategories() {
		log.debug("getting all categories from database");
		return repo.findAll();
	}

	@PreAuthorize("hasRole('USER')")
	@QueryMapping()
	public Category findCategory(@Argument Integer id) {
		log.info("requesting category details for id: " + id);
		return repo.findById(id).get();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@MutationMapping()
	public List<Category> createCategory(@Argument String name, @Argument String picture) {
		Category newcat = new Category(name, picture);
		repo.save(newcat);
		return repo.findAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@MutationMapping()
	public Category updateCategory(@Argument Integer id, @Argument CategoryUpdater update) {
		Category catToUp = repo.findById(id).orElse(null);

		if(catToUp == null) {
			throw new RuntimeException("Invalid category!!!");
		}

		catToUp.setId(id);
		catToUp.setName(update.name());
		catToUp.setPicture(update.picture());
		
		repo.save(catToUp);
		return repo.findById(id).get();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@MutationMapping
	public List<Category> deleteCategory(@Argument Integer id) {
		if(repo.existsById(id)) {
			repo.deleteById(id);
		}else{
			throw new RuntimeException("Invalid category!!!");
		}
		
		return repo.findAll();
	}

}
