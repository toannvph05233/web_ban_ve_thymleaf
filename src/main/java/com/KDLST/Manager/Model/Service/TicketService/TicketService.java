package com.KDLST.Manager.Model.Service.TicketService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.KDLST.Manager.Model.Entity.Ticket.Ticket;
@Service
public interface TicketService {
    public ArrayList<Ticket> getAll();
    public Ticket getByID(int id);
    public boolean update(Ticket ticket);
    public boolean add(Ticket ticket);

    public ArrayList<Ticket> getTickets(String search, int page, int size);


    public int getTotalTickets(String search);
}  

