package pl.sda.carrental.constructs.division;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.constructs.division.exceptions.EmployeeIsManager;
import pl.sda.carrental.exception.CannotBecomeManagerException;
import pl.sda.carrental.model.dataTransfer.CreateDivisionDTO;
import pl.sda.carrental.model.dataTransfer.mappers.EmployeeMapper;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.repository.DivisionRepository;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;
import pl.sda.carrental.service.EmployeeService;

import java.util.List;

@Controller
public class DivisionController {
    private final DivisionRepository divisionRepository;
    private final DivisionMapperForPanel divisionMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DivisionService divisionService;
    private final EmployeeService employeeService;

    public DivisionController(DivisionRepository divisionRepository, DivisionMapperForPanel divisionMapper, EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DivisionService divisionService, EmployeeService employeeService) {
        this.divisionRepository = divisionRepository;
        this.divisionMapper = divisionMapper;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.divisionService = divisionService;
        this.employeeService = employeeService;
    }
    @GetMapping("/divisions/hello-world")
    public String helloWorld() {
        return "home";
    }
    @GetMapping("/divisions/{division_id}")
    public String editDivision(Model model, @PathVariable long division_id) {
        DivisionDTOForPanel divisionDTO = divisionMapper.getDivisionDTO(divisionRepository.findById(division_id).get());

        model.addAttribute("division", divisionDTO);
        model.addAttribute("positions", Employee.Position.values());
        return "divisionPanels/divisionEdit";
    }
    @PostMapping("/divisions/edit/save")
    public String saveDivision(DivisionDTOForPanel divisionDTO) {
        Division division = divisionMapper.getDivisionObject(divisionDTO);
        divisionRepository.save(division);
        return "redirect:/divisions/" + divisionDTO.getDivision_id();
    }
    @GetMapping("/divisions")
    public String getDivisions(Model model) {
       List<Division> divisions = divisionRepository.findAll();
       List<DivisionDTOForPanel> divisionDTOs = divisions.stream().map(divisionMapper::getDivisionDTO).toList();
       model.addAttribute("divisions", divisionDTOs);
       return "divisionPanels/divisionPanel";
    }

    @GetMapping("/divisions/{division_id}/edit/removeEmployee/{employee_id}")
    public String removeEmployeeFromDivision(@PathVariable Long division_id, @PathVariable Long employee_id) {
        Division division = divisionRepository.getReferenceById(division_id);
        Employee employee = employeeRepository.getReferenceById(employee_id);

        try {
            divisionService.removeEmployee(division, employee);
        } catch (EmployeeIsManager ignore) { }

        return "redirect:/divisions/" + division_id;
    }
    @PostMapping("/divisions/employeeSelection")
    public String addEmployeesToDivision(@RequestParam(value = "selectedUsers", required = false) List<Long> selectedUserIds, @RequestParam Long division_id) {
        Division division = divisionRepository.getReferenceById(division_id);
        List<Employee> employees = employeeRepository.findAllById(selectedUserIds);
        //TODO Handle that, show a message to user
        try {
            divisionService.addEmployees(division,employees);
        } catch (EmployeeIsManager ignore) {System.out.println("Couldn't add some employees");}

        return "redirect:/divisions/" + division_id;
    }

    @GetMapping("/divisions/employeeSelection/{division_id}")
    public String selectEmployees(Model model, @PathVariable Long division_id) {
        List<Employee> eligibleEmployees = employeeRepository.findAllActiveNonManagersNotInThisDivision(division_id);
        model.addAttribute("users", eligibleEmployees.stream().map(employeeMapper::getDto).toList());
        model.addAttribute("division_id", division_id);
        return "divisionPanels/addEmployee";
    }

    @GetMapping("/divisions/new")
    public String createDivision(Model model) {
        model.addAttribute("newDivision", new CreateDivisionDTO());
        model.addAttribute("employees", employeeRepository.findAllActiveNonManagers());
        return "divisionPanels/createDivision";
    }
    @PostMapping("/divisions/new")
    public String createDevision(@ModelAttribute("newDivision") CreateDivisionDTO newDivision, BindingResult bindingResult, Model model) {
        //TODO write a unit test for that
        try {
            divisionService.createDivision(newDivision, employeeService);
        } catch (CannotBecomeManagerException exc) {
            model.addAttribute("employees", employeeRepository.findAllActiveNonManagers());
            bindingResult.addError(new FieldError("manager", "manager", "This employee cannot become a manager. "));
            return "divisionPanels/createDivision";
        }
        return "redirect:/divisions";
    }

    // TODO this was just added for testing because MVC didn't work with thymeleaf
//    @PostMapping("/divisions/new/json")
//    public String createDivisionJson(@RequestBody CreateDivisionDTO newDivision, RedirectAttributes redirectAttributes) {
//        if (!employeeService.canBecomeManager(newDivision.getManager().getId()))
//            throw new IllegalArgumentException("This employee cannot become manager");
//
//        divisionService.createDivision(newDivision);
//        return "redirect:/divisions";
////        redirectAttributes.addFlashAttribute("newDivision", newDivision);
////        return "forward:/divisions/new";
//    }

}
