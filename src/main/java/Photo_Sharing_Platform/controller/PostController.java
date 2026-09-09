package Photo_Sharing_Platform.controller;

import Photo_Sharing_Platform.model.Post;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.UserRepository;
import Photo_Sharing_Platform.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    public PostController(PostService postService,
                          UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping("/create-post")
    public String showCreatePostPage(Model model) {

        model.addAttribute("post", new Post());

        return "create-post";
    }

    @PostMapping("/create-post")
    public String createPost(
            @ModelAttribute("post") Post post,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Logged-in user not found"));

        post.setUser(user);

        postService.savePost(post);

        return "redirect:/home";
    }
}
