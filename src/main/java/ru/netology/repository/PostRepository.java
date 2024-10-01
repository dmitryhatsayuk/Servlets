package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {
    public ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    public AtomicLong lastPostId = new AtomicLong(0);

    public List<Post> all() {
        return posts.values().stream().toList();
    }

    public Optional<Post> getById(long id) {
        return posts.values().stream().filter(post -> post.getId() == id).findFirst();
    }

    public Post save(Post post) {
        if (post.getId() != 0L) {
            posts.put(post.getId(), post);
        } else {
            posts.put(findNewId(posts), post);
        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }

    public Long findNewId(ConcurrentHashMap<Long, Post> posts) {
        long value = 0;
        while (posts.containsKey(lastPostId.get())) {
            value = lastPostId.getAndIncrement();
        }

        return value;

    }


}
