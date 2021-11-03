package lukasztracz195.barnpjee.chat.server.model.repository;

import lukasztracz195.barnpjee.chat.server.model.entity.Category;
import lukasztracz195.barnpjee.chat.server.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    void save(Category category);

    boolean exists(String nameOfCategory);

    Optional<Category> get(String nameOfCategory);

    List<Category> get(int numberOfCategory);
}
