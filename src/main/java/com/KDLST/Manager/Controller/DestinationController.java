package com.KDLST.Manager.Controller;

import com.KDLST.Manager.Model.Service.TicketService.TicketService;
import com.KDLST.Manager.Model.Entity.Ticket.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "/destinations")
public class DestinationController {
    @Autowired
    TicketService ticketService;

    @GetMapping
    public ModelAndView getAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView("/destination");
        ArrayList<Ticket> tickets = ticketService.getTickets(search, page, size);
        int totalTickets = ticketService.getTotalTickets(search);
        int totalPages = (int) Math.ceil((double) totalTickets / size);

        modelAndView.addObject("tickets", tickets);
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("search", search);
        return modelAndView;
    }
}
