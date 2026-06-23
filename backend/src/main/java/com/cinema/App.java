package com.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import javax.persistence.*;
import java.util.List;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

@Entity
@Table(name = "bookings")
class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String movie;
    private String seats;
    private Double amount;
    
    // Getters and Setters
    public Long getId() { return id; }
    public String getMovie() { return movie; }
    public void setMovie(String m) { this.movie = m; }
    public String getSeats() { return seats; }
    public void setSeats(String s) { this.seats = s; }
    public Double getAmount() { return amount; }
    public void setAmount(Double a) { this.amount = a; }
}

@Repository
interface BookingRepository extends org.springframework.data.jpa.repository.JpaRepository<Booking, Long> {}

@RestController
@RequestMapping("/api/v1/tickets")
@CrossOrigin(origins = "*")
class TicketController {
    
    private final BookingRepository repo;
    
    public TicketController(BookingRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/book")
    public Booking createBooking(@RequestBody BookingRequest req) {
        Booking booking = new Booking();
        booking.setMovie(req.getMovie());
        booking.setSeats(String.join(",", req.getSeats()));
        booking.setAmount(req.getAmount());
        return repo.save(booking);
    }
    
    @GetMapping("/history")
    public List<Booking> getHistory() {
        return repo.findAll();
    }
}

class BookingRequest {
    private String movie;
    private List<String> seats;
    private Double amount;
    
    public String getMovie() { return movie; }
    public List<String> getSeats() { return seats; }
    public Double getAmount() { return amount; }
}
