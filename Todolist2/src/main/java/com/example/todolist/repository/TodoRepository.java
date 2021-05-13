package com.example.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Integer>{ //第１引数対象とするエンティティ、第２引数主キーの型

	
	
}
