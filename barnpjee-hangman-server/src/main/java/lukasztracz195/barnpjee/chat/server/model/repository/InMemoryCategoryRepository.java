package lukasztracz195.barnpjee.chat.server.model.repository;

import lukasztracz195.barnpjee.chat.server.model.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository{

    private final List<Category> categoryList;

    public InMemoryCategoryRepository() {
        categoryList = new CopyOnWriteArrayList<>();
        initializeCategoryList();
    }

    private void initializeCategoryList(){
        categoryList.add(Category.builder()
                .name("Sport")
                .keys(Arrays.asList("lekkoatletyka", "gimnastyka", "kulturystyka", "narciarstwo alpejskie",
                        "łyżwiarstwo szybkie", "wioślarstwo"))
                .build());
        categoryList.add(Category.builder()
                .name("Biologia")
                .keys(Arrays.asList("metamorfoza", "deoksyrybonukleinowy", "biczykoodwłokowiec", "nondysjunkcja",
                        "zaleszczotek", "koracidium"))
                .build());
        categoryList.add(Category.builder()
                .name("Chemia")
                .keys(Arrays.asList("etylodichloroarsyna", "triwodoroortokrzemian", "fenoloftaleina", "trinitrotoluen",
                        "", "adenozynotrifosforan"))
                .build());
        categoryList.add(Category.builder()
                .name("Fizyka")
                .keys(Arrays.asList("ferromagnetyzm", "zasada nieoznaczoności", "fotoemisja elektronów",
                        "indukcja elektromagnetyczna", "jednostka masy atomowej", "kwazistatyczny proces"))
                .build());
        categoryList.add(Category.builder()
                .name("J. Polski")
                .keys(Arrays.asList("dźwiękonaśladownictwo", "konstantynopolitański", "pięćdziesięciogroszówka",
                        "anatomopatologiczny", "gżegżółka", "skrzyżować"))
                .build());
    }

    @Override
    public void save(Category category) {
        categoryList.add(category);
    }

    @Override
    public boolean exists(String nameOfCategory) {
        return categoryList.stream().anyMatch(f->f.getName().equals(nameOfCategory));
    }

    @Override
    public Optional<Category> get(String nameOfCategory) {
        return categoryList.stream().filter(f->f.getName().equals(nameOfCategory)).findFirst();
    }

    @Override
    public List<Category> get(int numberOfCategory) {
        final List<Category> tmpCategory = new ArrayList<>(categoryList);
        Collections.shuffle(tmpCategory);
        if(tmpCategory.size() > numberOfCategory) {
            return tmpCategory.subList(0, numberOfCategory);
        }
        return tmpCategory;
    }
}
