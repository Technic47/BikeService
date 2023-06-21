package ru.kuznetsov.bikeService.controllers.additional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.ReplyMessage;
import ru.kuznetsov.bikeService.services.ReplyMessageService;

@Controller
@RequestMapping("/reply")
public class ReplyController extends AbstractController {
    private ReplyMessageService service;

    @GetMapping("/new")
    public String reply(Model model) {
        model.addAttribute("replyMessage", new ReplyMessage());
        return "reply";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("replyMessage") ReplyMessage message,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("replyMessage", message);
            return "reply";
        }
        this.service.save(message);
        return "title";
    }

    @Autowired
    public void setService(ReplyMessageService service) {
        this.service = service;
    }
}
