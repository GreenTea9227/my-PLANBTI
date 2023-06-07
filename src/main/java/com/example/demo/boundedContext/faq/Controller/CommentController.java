package com.example.demo.boundedContext.faq.Controller;

import com.example.demo.boundedContext.faq.Service.CommentService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
//@PreAuthorize("hasRole('ADMIN')") 로컬 서버 테스트를 위해 주석 처리
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final FaqService faqService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentForm {
        @NotBlank
        private String content;
    }

    @GetMapping("/create/{id}")
    public String create(Model model, @PathVariable Long id) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        model.addAttribute("faq", faq);
        return "comment/create";
    }

    @PostMapping("/create/{id}")
    public String create(@PathVariable Long id, @Valid CommentForm form) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        commentService.create(faq, form.getContent());
        return "redirect:/faq/detail/%s".formatted(id);
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        Comment comment = commentService.findByFaq(faq);
        model.addAttribute("faq", faq);
        model.addAttribute("comment", comment);
        return "comment/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @Valid CommentForm form) {
        Faq faq = faqService.findByIdAndDeleteDateIsNull(id);
        Comment comment = commentService.findByFaq(faq);
        commentService.modify(faq, comment, form.getContent());
        return "redirect:/faq/detail/%s".formatted(id);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Comment comment = commentService.findByIdAndDeleteDateIsNull(id);
        Faq faq = comment.getFaq();
        commentService.delete(faq, comment);
        return "redirect:/faq/detail/%s".formatted(id);
    }

}
