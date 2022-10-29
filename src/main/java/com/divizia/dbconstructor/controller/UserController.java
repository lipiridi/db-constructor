package com.divizia.dbconstructor.controller;

import com.divizia.dbconstructor.exceptions.UserNotFoundException;
import com.divizia.dbconstructor.exceptions.UserPermissionException;
import com.divizia.dbconstructor.model.entity.CustomTable;
import com.divizia.dbconstructor.model.entity.Subscription;
import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.enums.RequisiteType;
import com.divizia.dbconstructor.model.enums.Role;
import com.divizia.dbconstructor.model.service.CustomTableService;
import com.divizia.dbconstructor.model.service.SubscriptionService;
import com.divizia.dbconstructor.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomTableService customTableService;
    private final SubscriptionService subscriptionService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("all")
    public String getAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/all";
    }

    private void checkPermission(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getAuthorities().contains(Role.ADMIN) && !id.equals(authentication.getName()))
            throw new UserPermissionException();
    }

    @GetMapping("edit/{id}")
    public String getEdit(@PathVariable String id, Model model) {
        checkPermission(id);

        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return getEditWithValues(model, user);
    }

    public String getEditWithValues(Model model, User user) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setCustomTable(new CustomTable());

        return getEditWithValues(model, user, subscription);
    }

    public String getEditWithValues(Model model, User user, Subscription subscription) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("customTables", customTableService.findAll());
        model.addAttribute("subscriptions", subscriptionService.findByUserId(user.getId()));
        model.addAttribute("subscription", subscription);
        return "users/edit";
    }

    @PostMapping("edit/{id}")
    public String postEdit(@PathVariable String id, @Valid User user, BindingResult result, Model model) {
        checkPermission(id);

        if (ControllerHelper.hasErrors(result, model))
            return getEditWithValues(model, user);

        Optional<User> foundInDB = userService.findById(id);
        if (foundInDB.isPresent())
            user = foundInDB.get().updateAllowed(user);

        userService.saveAndFlush(user);

        return "redirect:/users/edit/" + id;
    }

    @PostMapping("delete/{id}")
    public String postDelete(@PathVariable String id) {
        checkPermission(id);

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.deleteById(id);

        if (id.equals(currentUsername))
            return "redirect:/logout";
        else
            return "redirect:/users/all";
    }

    @PostMapping("subscription/add")
    public String postAdd(@Valid Subscription subscription, BindingResult result, Model model) {
        checkPermission(subscription.getUser().getId());

        if (ControllerHelper.hasErrors(result, model))
            return getEditWithValues(model, subscription.getUser(), subscription);
        if (subscriptionService.findByUserAndCustomTableId(
                        subscription.getUser().getId(),
                        subscription.getCustomTable().getId())
                .isPresent()) {
            model.addAttribute("errorList", List.of("This subscription already exists!"));
            return getEditWithValues(model, subscription.getUser(), subscription);
        }

        subscriptionService.saveAndFlush(subscription);

        return "redirect:/users/edit/" + subscription.getUser().getId();
    }

    @PostMapping("subscription/delete/{id}/{customTableId}")
    public String postDelete(@PathVariable String id, @PathVariable String customTableId) {
        checkPermission(id);

        subscriptionService.deleteByUserAndCustomTableId(id, customTableId);
        return "redirect:/users/edit/" + id;
    }

}
