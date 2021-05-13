package com.example.todolist.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "todo")
@Data
public class Todo {
	@Id //主キーを表す
	@GeneratedValue(strategy = GenerationType.IDENTITY) //自動採番を表す　SERIAL型の場合（自動採番の方法によってかわる）
	//プロパティに対応するテーブルの列を判定する（指定する変数が同じなら省略可能）
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "importance")
	private Integer importance;
	
	@Column(name = "urgency")
	private Integer urgency;
	
	@Column(name = "deadline")
	private Date deadline;
	
	@Column(name = "done")
	private String done;
	
	
}
