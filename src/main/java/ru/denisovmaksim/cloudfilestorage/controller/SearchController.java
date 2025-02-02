package ru.denisovmaksim.cloudfilestorage.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.denisovmaksim.cloudfilestorage.service.FileService;
import ru.denisovmaksim.cloudfilestorage.validation.ValidPath;

@Controller
@AllArgsConstructor
@RequestMapping("/search")
public class SearchController {

    public final FileService fileService;

    @GetMapping("")
    public String searchObjectsByPath(Model model, Authentication authentication,
                                      @ValidPath
                                      @RequestParam(required = false, defaultValue = "/") String path,
                                      @RequestParam() String query) {
        model.addAttribute("username", authentication.getName());
        return "search-explorer";
    }

}
