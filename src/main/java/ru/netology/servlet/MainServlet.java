package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private static final String API_POSTS = "/api/posts";
    private static final String API_POST_ID = "/api/posts/\\d+";
    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("ru.netology");

        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals("GET")) {
                handleGet(path, resp);
                return;
            }
            if (method.equals("GET") && path.matches(API_POST_ID)) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals("/api/posts")) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGet(String path, HttpServletResponse resp) throws IOException {
        if (path.equals(API_POSTS)) {
            controller.all(resp);
        } else if (path.equals(API_POST_ID)) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
            controller.getById(id, resp);
        } else resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}

