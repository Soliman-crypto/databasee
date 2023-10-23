//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package repositories;

import java.util.List;

public interface CrudRepository<T> {
    void save(T var1);

    List<T> findAll();

    List<T> findAllByAge(int var1);

    void update(int var1, String var2, String var3, String var4);

    void delete(int var1);
}
