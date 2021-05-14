package com.example.todolist.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.todolist.common.Utils;
import com.example.todolist.entity.Todo;
import com.example.todolist.entity.Todo_;
import com.example.todolist.form.TodoQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TodoDaoImpl implements TodoDao{
	
	private final EntityManager entitiyManager;
	
	
	
	//JPQLによる検索
	@Override
	public List<Todo>findByJPQL(TodoQuery todoQuery){
		//ここを"todo"にすると実行時エラーになる
		StringBuilder sb = new StringBuilder("select t from Todo t where1=1");
		List<Object>params = new ArrayList<>();
		int pos = 0;
		
		//	実行するJPQLの組み立て
		//件名
		if(todoQuery.getTitle().length()>0) {
			sb.append("and t.title like?" + (++pos));
			
			params.add("%"+todoQuery.getTitle() + "%");
		}
		
		//重要度
		if(todoQuery.getImportance()!=-1) {
			sb.append("and t.importance = ?"+(++pos));
			params.add(todoQuery.getImportance());
		}
		
		//緊急度
		if(todoQuery.getUrgency()!=-1) {
			sb.append("and Lugency = ?"+(++pos));
			params.add(todoQuery.getUrgency());
		}
		
		//期限：開始～
		if(!todoQuery.getDeadlineTo().equals("")) {
			sb.append("and t.deadline >= ?"+(++pos));
			params.add(Utils.str2date(todoQuery.getDeadlineFrom()));
		}
		
		//期限：終了で検索
		if(!todoQuery.getDeadlineTo().equals("")) {
			sb.append("and t.deadline >= ?"+(++pos));
			params.add(Utils.str2date(todoQuery.getDeadlineFrom()));
		}
		
		//完了
		if(todoQuery.getDone()!=null&&todoQuery.getDone().equals("Y")) {
			sb.append("and t.done=?"+(++pos));
			params.add(todoQuery.getDone());
		}
		
		//order
		sb.append("order by id");
		
		Query query = entitiyManager.createQuery(sb.toString());
		for(int i = 0; i < params.size(); ++i) {
			query = query.setParameter(i + 1, params.get(i));
		}
		
		@SuppressWarnings("unchecked")
		List<Todo>list = query.getResultList();
		return list;
		
	}
	//Crieria APIによる検索
	@Override
	public List<Todo>findByCriteria(TodoQuery todoQuery) {
		//CriteriaBuilderをよびだし使えるようにする
		CriteriaBuilder builder = entitiyManager.getCriteriaBuilder();
		//queryにTodo.classをセット
		CriteriaQuery<Todo>query=builder.createQuery(Todo.class);
		//rootはfrom（a）のaをすべて格納
		Root<Todo>root=query.from(Todo.class);
		//検索条件はPredicte型に格納する
		List<Predicate>predicates=new ArrayList<>();
		
		//件名
		String title = "";
		//s
		if(todoQuery.getTitle().length()>0) {
			//曖昧検索
			title = "%" + todoQuery.getTitle() + "%";
		}else {
			title="%";
		}
		//titleとデータ上の検索内容で曖昧検索
		predicates.add(builder.like(root.get(Todo_.TITLE),title));
		
		//重要度
		if(todoQuery.getUrgency()!=-1) {
			predicates.add(
					builder.and(
							builder.equal(
									root.get(Todo_.URGENCY),todoQuery.getUrgency())));
		}
		
		//緊急度
		if(todoQuery.getUrgency()!=-1) {
			predicates.add(
					builder.and(
							builder.equal(
									root.get(Todo_.URGENCY),todoQuery.getUrgency())));
		}
			
			
		//期限：開始～
		if(!todoQuery.getDeadlineFrom().equals("")) {
			predicates.add(
					builder.and(
							builder.greaterThanOrEqualTo(
									root.get(Todo_.DEADLINE),
									Utils.str2date(todoQuery.getDeadlineFrom()))));
		}
		
		//期限：終了で検索
		if(todoQuery.getDeadlineTo().equals("")) {
			predicates.add(
					builder.and(
							builder.lessThanOrEqualTo(
									root.get(Todo_.DEADLINE),
									Utils.str2date(todoQuery.getDeadlineTo()))));
		}
		
		//完了
		if(todoQuery.getDone() != null && todoQuery.getDone().equals("Y")) {
			predicates.add(
					builder.and(
							builder.equal(
									root.get(Todo_.DONE),todoQuery.getDone())));
		}
		
		//SELECT作成
		Predicate[] predArray = new Predicate[predicates.size()];
		predicates.toArray(predArray);
//		query = query.select(root).where(predArray.orderBy(builder.asc(root.get(Todo_.id))));
		query = query.select(root).where(predArray).orderBy(builder.asc(root.get(Todo_.id)));
		
		//検索
		List<Todo>list = entitiyManager.createQuery(query).getResultList();
		
		return list;
	}

}
