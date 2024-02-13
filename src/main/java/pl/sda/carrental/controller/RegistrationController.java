package pl.sda.carrental.controller;


import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sda.carrental.model.dataTransfer.UserRegistrationDTO;
import pl.sda.carrental.model.entity.userEntities.Role;
import pl.sda.carrental.model.repository.userRepositories.RoleRepository;
import pl.sda.carrental.security.PrincipalRole;
import pl.sda.carrental.service.UserRegistrationService;

@Controller
public class RegistrationController implements WebMvcConfigurer {
    private final RoleRepository roleRepository;
    private final UserRegistrationService userRegistrationService;

    public RegistrationController(RoleRepository roleRepository, UserRegistrationService userRegistrationService) {
        this.roleRepository = roleRepository;
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping("/register")
    public String registerDefaults(Model model) {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setEmail("bartek@gmail.com");
        userRegistrationDTO.setPassword("pass1");
        userRegistrationDTO.setRepeatPassword("pass1");
        userRegistrationDTO.setFirstName("Jakub");
        userRegistrationDTO.setLastName("Igraszka");
        userRegistrationDTO.setUsername("bartek1994");

        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("userRegistrationDTO", userRegistrationDTO);
        return "/registration/register";
    }

    @PostMapping("/register/validate")
    public String checkPersonInfo(@Valid @ModelAttribute("userRegistrationDTO") UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getRepeatPassword()))
            bindingResult.rejectValue("repeatPassword", "error.code", "Passwords are not the same");

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "/registration/register";
        }

        redirectAttributes.addFlashAttribute("userRegistrationDTO", userRegistrationDTO);
        return "redirect:/register/validated";
    }

    @GetMapping("/register/validated")
    public String registerValidUser(@ModelAttribute("userRegistrationDTO") UserRegistrationDTO userRegistrationDTO) {

        Role selectedRole = userRegistrationDTO.getRole();
        if (selectedRole.getRoleName().equals(PrincipalRole.ADMIN.name())) {
            userRegistrationService.registerAdmin(userRegistrationDTO);
        } else if (selectedRole.getRoleName().equals(PrincipalRole.EMPLOYEE.name())) {
            userRegistrationService.registerEmployee(userRegistrationDTO);
        } else {
            userRegistrationService.registerCustomer(userRegistrationDTO);
        }

        return "redirect:/login";
    }
}
