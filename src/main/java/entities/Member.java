package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private Date membership_date;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<BorrowRecords> borrowRecordsList = new ArrayList<>();

}
