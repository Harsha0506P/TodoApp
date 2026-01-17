package com.example.taskflow.controller;

import com.example.taskflow.entity.Task;
import com.example.taskflow.entity.User;
import com.example.taskflow.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("tasks", taskService.all(user));
        addStats(model, user);
        return "dashboard";
    }

    @GetMapping("/today")
    public String today(HttpSession session, Model model) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("tasks", taskService.todayTasks(user));
        addStats(model, user);
        return "dashboard";
    }

    @GetMapping("/upcoming")
    public String upcoming(HttpSession session, Model model) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("tasks", taskService.upcomingTasks(user));
        addStats(model, user);
        return "dashboard";
    }

    @GetMapping("/completed")
    public String completed(HttpSession session, Model model) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("tasks", taskService.completedTasks(user));
        addStats(model, user);
        return "dashboard";
    }

    @PostMapping("/add")
    public String add(Task task, HttpSession session) {

    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
        return "redirect:/";
    }

    User user = taskService.getUserById(sessionUser.getId());
    task.setUser(user);
    taskService.add(task);

    return "redirect:/dashboard";
}
    @PostMapping("/complete/{id}")
    public String complete(@PathVariable Long id, HttpSession session) {
        if (getUser(session) == null) return "redirect:/";
        taskService.markCompleted(id);
        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (getUser(session) == null) return "redirect:/";
        taskService.deleteTask(id);
        return "redirect:/dashboard";
    }


    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    private void addStats(Model model, User user) {
        long total = taskService.totalCount(user);
        long completed = taskService.completedCount(user);

        model.addAttribute("totalTasks", total);
        model.addAttribute("completedTasks", completed);
        model.addAttribute("pendingTasks", total - completed);

        int productivity = total == 0 ? 0 : (int) (completed * 100 / total);
        model.addAttribute("productivity", productivity);
    }
}

