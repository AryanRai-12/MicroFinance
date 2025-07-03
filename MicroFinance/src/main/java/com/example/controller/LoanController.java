package com.example.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Loan;
import com.example.model.User;
import com.example.repository.LoanRepository;
import com.example.service.LoanService;
import com.example.service.UserService;

@Controller
@RequestMapping("/loans")
public class LoanController {
	@Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;
    
    @Autowired 
    private LoanRepository loanRepository;
    
    
 // Show loan‑application form
    @GetMapping("/apply")
    public String showLoanForm(Model model) {
        model.addAttribute("loan", new Loan());   // still binds amount, status, etc.
        return "loan-form";                       // loan-form.html (see form update below)
    }

    
    @PostMapping("/apply")
    public String submitLoanForm(@ModelAttribute Loan loan,
    							@RequestParam("tenureYears") int years,
    							@RequestParam("tenureMonths") int months,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttr) {

        // Look‑up logged‑in user
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {                      // should never happen
            return "redirect:/loans/apply";
        }
        if (loan.getAmount() == null ||
                loan.getAmount().compareTo(new BigDecimal("2000")) < 0) {

                redirectAttr.addFlashAttribute("error","Loan amount must be at least ₹2,000");
                redirectAttr.addFlashAttribute("loan", loan); // keep entered data
                return "redirect:/loans/apply";
            }
        /* ---- Calculate due‑date ---- */
        LocalDate dueDate = LocalDate.now()
                                     .plusYears(years)
                                     .plusMonths(months);

        loan.setUser(user);
        loan.setStatus("PENDING");
        loan.setDueDate(dueDate);                // ← set the computed date

        loanService.applyForLoan(loan);          // persist
        return "redirect:/loans/my";             // user’s own‑loans page
    }

    
    @GetMapping("/allLoan")
    public String showAllLoan(Model model) {
        model.addAttribute("loanList", loanRepository.findAll());
        return "loan-list";  // This refers to loan-list.html (Thymeleaf template)
    }
    // Show loan application form
//    @GetMapping("/apply")
//    public String showLoanForm(Model model) {
//        model.addAttribute("loan", new Loan());
//        return "loan-form"; // Thymeleaf view name (loan-form.html)
//    }

//    // Process loan application securely using logged-in user
//    @PostMapping("/apply")
//    public String submitLoanForm(@ModelAttribute Loan loan,
//                                 @AuthenticationPrincipal UserDetails userDetails) {
//
//        String username = userDetails.getUsername();
//        User user = userService.findByUsername(username);
//
//        if (user == null) {
//            return "redirect:/error";
//        }
//
//        loan.setUser(user);
//        loanService.applyForLoan(loan);
//        return "redirect:/loans/my";
//    }
    
    

    // Show only loans for the currently logged-in user
    @GetMapping("/my")
    public String viewMyLoans(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);

        List<Loan> loans = loanService.getLoansByUserId(user.getId());
        model.addAttribute("loans", loans);
        return "user-list"; // Thymeleaf view name (loan-list.html)
    }

    // Admin only – approve a specific loan by ID
    @PostMapping("/approve/{loanId}")
    public String approveLoan(@PathVariable Long loanId) {
        loanService.approveLoan(loanId);
        return "redirect:/admin/loans";
    }
    
    
}
