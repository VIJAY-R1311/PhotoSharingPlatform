package Photo_Sharing_Platform.controller;

import Photo_Sharing_Platform.model.Event;
import Photo_Sharing_Platform.model.Role;
import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.repository.UserRepository;
import Photo_Sharing_Platform.service.EventService;
import Photo_Sharing_Platform.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final UserRepository userRepository;
    private final UserService userService;

    public AdminController(EventService eventService,
                           UserRepository userRepository,
                           UserService userService) {

        this.eventService = eventService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model) {

        User admin = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        List<Event> events =
                eventService.getAdminEvents(admin);

        model.addAttribute("events", events);

        return "admin-dashboard";
    }

    @GetMapping("/create-event")
    public String showCreateEventPage(Model model) {

        model.addAttribute("event", new Event());

        return "create-event";
    }

    @PostMapping("/create-event")
    public String createEvent(
            @ModelAttribute("event") Event event,
            Authentication authentication) {

        User admin = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        eventService.createEvent(event, admin);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/add-team-member")
    public String showAddTeamMemberPage(Model model) {

        model.addAttribute("user", new User());

        return "add-team-member";
    }

    @PostMapping("/add-team-member")
    public String addTeamMember(
            @ModelAttribute("user") User user,
            Model model) {

        if (userService.usernameExists(user.getUsername())) {

            model.addAttribute(
                    "error",
                    "Username already exists"
            );

            return "add-team-member";
        }

        if (userService.emailExists(user.getEmail())) {

            model.addAttribute(
                    "error",
                    "Email already exists"
            );

            return "add-team-member";
        }

        userService.createTeamMember(user);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/event/{eventId}/assign")
    public String showAssignPage(
            @PathVariable Long eventId,
            Model model) {

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return "redirect:/admin/dashboard";
        }

        List<User> teamMembers =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                user.getRole() == Role.TEAM_MEMBER)
                        .toList();

        model.addAttribute("event", event);
        model.addAttribute("teamMembers", teamMembers);

        return "assign-team-member";
    }

    @PostMapping("/event/{eventId}/assign")
    public String assignTeamMember(
            @PathVariable Long eventId,
            @RequestParam Long userId) {

        Event event = eventService.getEventById(eventId);

        User teamMember =
                userRepository.findById(userId)
                        .orElseThrow();

        if (event != null &&
                teamMember.getRole() == Role.TEAM_MEMBER) {

            eventService.addTeamMember(
                    event,
                    teamMember
            );
        }

        return "redirect:/admin/dashboard";
    }
}