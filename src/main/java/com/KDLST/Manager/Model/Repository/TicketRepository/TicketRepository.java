package com.KDLST.Manager.Model.Repository.TicketRepository;

import java.util.ArrayList;
import java.sql.*;

import com.KDLST.Manager.Model.Repository.ServiceProjectRepository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KDLST.Manager.Model.BaseConnection;
import com.KDLST.Manager.Model.Entity.Ticket.Ticket;
import com.KDLST.Manager.Model.Entity.Ticket.TicketType;

import jakarta.el.ELException;

@Repository
public class TicketRepository {
    private static ArrayList<Ticket> ticketList = new ArrayList<>();
    @Autowired
    private TicketTypeRepository ticketTypeRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    public ArrayList<Ticket> getTickets(String search, int limit, int offset) {
        try {
            ticketList.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            String query = "SELECT * FROM KDLST.Ticket WHERE title LIKE ? LIMIT ? OFFSET ?";
            PreparedStatement stsm = con.prepareStatement(query);
            stsm.setString(1, "%" + search + "%");
            stsm.setInt(2, limit);
            stsm.setInt(3, offset);
            ResultSet rs = stsm.executeQuery();
            while (rs.next()) {
                int ticketID = rs.getInt("ticketID");
                int serviceID = rs.getInt("serviceID");
                TicketType ticketType = ticketTypeRepository.getById(rs.getInt("ticketTypeID"));
                String title = rs.getString("title");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String image = rs.getString("image");
                Boolean status = rs.getBoolean("status");
                Ticket ticket = new Ticket(ticketID, serviceRepository.getById(serviceID), ticketType, title, description, price, image, status);

                ticketList.add(ticket);
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return ticketList;
    }

    public int getTotalTickets(String search) {
        int count = 0;
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            String query = "SELECT COUNT(*) AS total FROM KDLST.Ticket WHERE title LIKE ?";
            PreparedStatement stsm = con.prepareStatement(query);
            stsm.setString(1, "%" + search + "%");
            ResultSet rs = stsm.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return count;
    }
    public ArrayList<Ticket> getAll() {
        try {
            ticketList.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stsm = con.createStatement();
            ResultSet rs = stsm.executeQuery("select * from KDLST.Ticket");
            while (rs.next()) {
                int ticketID = rs.getInt("ticketID");
                int serviceID = rs.getInt("serviceID");
                TicketType TicketType = ticketTypeRepository.getById(rs.getInt("ticketTypeID"));
                String title = rs.getString("title");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String image = rs.getString("image");
                Boolean status = rs.getBoolean("status");
                Ticket ticket = new Ticket(ticketID,serviceRepository.getById(serviceID), TicketType, title, description, price, image, status);

                ticketList.add(ticket);
            }
            con.close();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            e.printStackTrace();
        }
        return ticketList;
    }

    public Ticket getById(int id) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection conn = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement st = conn.prepareStatement(
                    "select * from KDLST.Ticket where KDLST.Ticket.ticketID = ?;");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                throw new ELException("Cannot find");
            }
            int ticketID = rs.getInt("ticketID");
            int serviceID = rs.getInt("serviceID");
            TicketType TicketType = ticketTypeRepository.getById(rs.getInt("ticketTypeID"));
            String title = rs.getString("title");
            String description = rs.getString("description");
            double price = rs.getDouble("description");
            String image = rs.getString("image");
            Boolean status = rs.getBoolean("status");
            Ticket ticket = new Ticket(ticketID,serviceRepository.getById(serviceID), TicketType, title, description, price, image, status);
            st.close();
            return ticket;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean update(Ticket ticket) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement(
                    "update KDLST.Ticket set KDLST.Ticket.ticketTypeID =?, KDLST.Ticket.title=?, KDLST.Ticket.description = ?, KDLST.Ticket.price =?, KDLST.Ticket.image =?, KDLST.Ticket.status =? ,serviceID= ? where KDLST.Ticket.ticketID =?");
            prsm.setInt(1, ticket.getTicketTypeID().getTicketTypeID());
            prsm.setString(2, ticket.getTitle());
            prsm.setString(3, ticket.getDescription());
            prsm.setDouble(4, ticket.getPrice());
            prsm.setString(5, ticket.getImage());
            prsm.setBoolean(6, ticket.isStatus());
            prsm.setInt(7, ticket.getServiceID().getServiceID());
            prsm.setInt(8, ticket.getTicketID());

            System.out.println(ticket.toString());
            int result = prsm.executeUpdate();
            System.out.println(result);
            con.close();
            return result > 0;
        } catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception
        }
        return false;
    }

    public boolean add(Ticket ticket) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement(
                    "insert into KDLST.Ticket (ticketTypeID, title, description, price, image, status, serviceID) values(?,?,?,?,?,?,?)");
            prsm.setInt(1, ticket.getTicketTypeID().getTicketTypeID());
            prsm.setString(2, ticket.getTitle());
            prsm.setString(3, ticket.getDescription());
            prsm.setDouble(4, ticket.getPrice());
            prsm.setString(5, ticket.getImage());
            prsm.setBoolean(6, ticket.isStatus());
            prsm.setInt(7, ticket.getServiceID().getServiceID());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}