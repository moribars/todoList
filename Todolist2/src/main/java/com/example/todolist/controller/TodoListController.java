package com.example.todolist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.entity.Todo;
import com.example.todolist.repository.TodoRepository;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor //全てにコンストラクタを設定する
public class TodoListController {
	private final TodoRepository todoRepository; //コンストラクタで初期化する
	
	@GetMapping("/todo")
	public ModelAndView showTodoList(ModelAndView mv) {
		//一覧を検索して表示する
		mv.setViewName("todoList"); //viewをファイルを設定
		List<Todo>todoList = todoRepository.findAll(); //全て検索
		mv.addObject("todoList",todoList);
		return mv;
	}
	
}
