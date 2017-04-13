package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.data.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


@Controller
@RequestMapping(value = "categories")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("title", "My Categories");
        model.addAttribute("categories", categoryDao.findAll());

        return "categories/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {

        model.addAttribute("title", "Add Category");
        model.addAttribute(new Category());

        return "categories/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Category category, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Category");
            return "categories/add";
        }
        categoryDao.save(category);
        return "redirect:";

    }
}
