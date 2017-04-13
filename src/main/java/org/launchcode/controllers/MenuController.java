package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by kaylabarton on 4/10/17.
 */

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    CheeseDao cheeseDao;

    @Autowired
    MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {

        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu newMenu, Model model, Errors errors){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }
        menuDao.save(newMenu);

        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int id) {


        model.addAttribute("menu", menuDao.findOne(id));

        return "menu/view";
    }

    @RequestMapping(value = "add-item/{id}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int id) {

        Menu menu = menuDao.findOne(id);
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        AddMenuItemForm form = new AddMenuItemForm(menu, cheeses);

        model.addAttribute("title", "Add cheese to menu: " + menu.getName());
        model.addAttribute("form", form);


        return "menu/add-item";
    }
    @RequestMapping(value = "add-item/{id}", method = RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("form", form);
            return "menu/add-item";
        }
        Menu menu = menuDao.findOne(form.getMenuId());
        Cheese cheese = cheeseDao.findOne(form.getCheeseId());
        menu.addItem(cheese);
        menuDao.save(menu);

        return "redirect:/menu/view/" + menu.getId();
    }
}
