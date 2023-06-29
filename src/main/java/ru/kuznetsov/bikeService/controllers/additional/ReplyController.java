package ru.kuznetsov.bikeService.controllers.additional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.ReplyMessage;
import ru.kuznetsov.bikeService.services.ReplyMessageService;

import static ru.kuznetsov.bikeService.config.SecurityConfiguration.USERMODEL;

@Controller
@RequestMapping("/reply")
public class ReplyController extends AbstractController {
    private ReplyMessageService service;

    @GetMapping()
    @Secured("ROLE_ADMIN")
    public String index(Model model){
        this.addMessagesToModel(model);
        model.addAttribute("user", USERMODEL);
        return "reply_index";
    }

    private void addMessagesToModel(Model model){
        model.addAttribute("replyMessages", this.service.index());
    }

    @GetMapping("/new")
    public String newReply(Model model) {
        model.addAttribute("replyMessage", new ReplyMessage());
        model.addAttribute("user", USERMODEL);
        return "reply";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("replyMessage") ReplyMessage message,
                       BindingResult bindingResult,
                       Model model) {
        model.addAttribute("user", USERMODEL);
        if (bindingResult.hasErrors()) {
            model.addAttribute("replyMessage", message);
            return "reply";
        }
        this.service.save(message);
        return "title";
    }

    @PostMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public String delete(@PathVariable("id") Long id,
                         Model model){
        this.service.delete(id);
        this.addMessagesToModel(model);
        model.addAttribute("user", USERMODEL);
        return "reply_index";
    }

    @Autowired
    public void setService(ReplyMessageService service) {
        this.service = service;
    }
}
