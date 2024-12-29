package dao;

public interface DAO<T>{
    boolean saveOrUpdate(T t);
    void delete(T t);

}
