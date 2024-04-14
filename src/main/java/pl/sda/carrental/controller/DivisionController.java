package pl.sda.carrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sda.carrental.exception.CannotBecomeManagerException;
import pl.sda.carrental.model.dataTransfer.CreateDivisionDTO;
import pl.sda.carrental.model.dataTransfer.DivisionDTOForPanel;
import pl.sda.carrental.model.dataTransfer.mappers.DivisionMapperForPanel;
import pl.sda.carrental.model.dataTransfer.mappers.EmployeeMapper;
import pl.sda.carrental.model.entity.Address;
import pl.sda.carrental.model.entity.Division;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.repository.AddressRepository;
import pl.sda.carrental.model.repository.DivisionRepository;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;
import pl.sda.carrental.service.DivisionService;
import pl.sda.carrental.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class DivisionController {
    private final DivisionRepository divisionRepository;
    private final DivisionMapperForPanel divisionMapper;
    private final AddressRepository addressRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DivisionService divisionService;
    private final EmployeeService employeeService;

    public DivisionController(DivisionRepository divisionRepository, DivisionMapperForPanel divisionMapper, AddressRepository addressRepository, EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DivisionService divisionService, EmployeeService employeeService) {
        this.divisionRepository = divisionRepository;
        this.divisionMapper = divisionMapper;
        this.addressRepository = addressRepository;
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
        List<Address> addresses = new ArrayList<>();
        addresses.add(divisionDTO.getAddress());
        addresses.addAll(addressRepository.findAllUnusedAddresses());


        model.addAttribute("division", divisionDTO);
        model.addAttribute("addresses", addresses);
        model.addAttribute("positions", Employee.Position.values());
        return "divisionPanels/divisionEdit";
    }
    @PostMapping("/divisions/edit/save")
    public String saveDivision(DivisionDTOForPanel divisionDTO) {
        divisionRepository.save(divisionMapper.getDivisionObject(divisionDTO));
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

        divisionService.removeEmployee(division, employee);
        return "redirect:/divisions/" + division_id;
    }
    @PostMapping("/divisions/employeeSelection")
    public String addEmployeesToDivision(@RequestParam(value = "selectedUsers", required = false) List<Long> selectedUserIds, @RequestParam Long division_id) {
        Division division = divisionRepository.getReferenceById(division_id);
        List<Employee> employees = employeeRepository.findAllById(selectedUserIds);
        divisionService.addEmployees(division,employees);
        return "redirect:/divisions/" + division_id;
    }

    @GetMapping("/divisions/employeeSelection/{division_id}")
    public String selectEmployees(Model model, @PathVariable Long division_id) {
        List<Employee> eligibleEmployees = employeeRepository.findAllActiveNonManagersNotInDivision(division_id);
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
    public String createDevision(CreateDivisionDTO newDivision) {
        //TODO: Rewrite this to that CreateDivisionDTO is not nested
        if (!employeeService.canBecomeManager(newDivision.getManager().getId()))
            throw new IllegalArgumentException("This employee cannot become manager");

        divisionService.createDivision(newDivision);
        return "redirect:/divisions";
    }

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
