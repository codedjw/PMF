package org.pmf.eao;

import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class EaoImpl implements Eao{

	// 依赖注入容器管理的EntityManager
	@PersistenceContext(unitName="mydbUnit")
	protected EntityManager em;
	
	@Override
	public <T> T get(Class<T> entityClass, Object pk) {
		// TODO Auto-generated method stub
		T obj = em.find(entityClass, pk);
		return obj;
	}
	
	@Override
	public void save(Object entity) {
		// TODO Auto-generated method stub
		em.persist(entity);
	}

	@Override
	public void update(Object entity) {
		// TODO Auto-generated method stub
		em.merge(entity);
	}

	@Override
	public void delete(Class<?> entityClass, Object pk) {
		// TODO Auto-generated method stub
		em.remove(em.getReference(entityClass, pk));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getResultList(Class<T> entityClass, String whereJpql,
			LinkedHashMap<String, String> orderBy, Object... args) {
		// TODO Auto-generated method stub
		// 获取实体类的实体名。在默认情况下，实体名和类名相同
		String entityName = entityClass.getSimpleName();
		// 创建查询
		whereJpql = (whereJpql == null) ? "" : whereJpql;
		Query query = em.createQuery("select o from " + entityName + " as o " + whereJpql + buildOrderby(orderBy));
		// 为查询字符串的参数设置值
		for (int i=0; i<args.length; i++) {
			query.setParameter(i+1, args[i]);
		}
		return (List<T>) query.getResultList();
	}
	
	/**
	 * 构建排序子句
	 * @author Du Jiawei
	 * @param orderBy LinkedHashMap对象，每个key-value对制定一个排序条件
	 * @return
	 */
	private static String buildOrderby(LinkedHashMap<String, String> orderBy) {
		// TODO Auto-generated method stub
		StringBuffer out = new StringBuffer("");
		if(orderBy != null && orderBy.size() > 0) {
			// 增加order by子句
			out.append(" order by ");
			// 遍历key-value对生成一个排序条件
			for(String key : orderBy.keySet()) {
				out.append("o." + key + " " + orderBy.get(key));
				out.append(",");
			}
			// 最后多了一个“,”
			out.deleteCharAt(out.length()-1);
		}
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getResultList(Class<T> entityClass, String whereJpql,
			int firstResult, int maxResult,
			LinkedHashMap<String, String> orderBy, Object... args) {
		// TODO Auto-generated method stub
		// 获取实体类的实体名。在默认情况下，实体名和类名相同
		String entityName = entityClass.getSimpleName();
		// 创建查询
		whereJpql = (whereJpql == null) ? "" : whereJpql;
		Query query = em.createQuery("select o from " + entityName + " as o " + whereJpql + buildOrderby(orderBy));
		// 为查询字符串的参数设置值
		for (int i=0; i<args.length; i++) {
			query.setParameter(i+1, args[i]);
		}
		// 对查询结果集进行分页
		query.setMaxResults(maxResult).setFirstResult(firstResult);
		return (List<T>) query.getResultList();
	}
	
}
