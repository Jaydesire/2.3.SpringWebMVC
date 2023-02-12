package ru.netology.repository;
import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    private AtomicLong currentID = new AtomicLong(0);
    private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {

        if (posts.containsKey(id)) {
            return Optional.of(posts.get(id));
        } else {
            return Optional.empty();
        }
    }

    public Post save(Post post) {
        //Если от клиента приходит пост с id=0, значит, это создание нового поста
        if (post.getId() == 0){
            post.setId(Long.valueOf(currentID.incrementAndGet()));
            posts.put(post.getId(), post);
        }
        //id !=0, значит, это сохранение (обновление) существующего поста.
        else if (posts.containsKey(post.getId())){
            posts.put(post.getId(), post);
        } else {
            throw new NotFoundException("ID Not Found");
        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
