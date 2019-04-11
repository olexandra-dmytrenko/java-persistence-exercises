package ua.procamp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity
public class Card {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @Column
    private double amount;

    @ManyToOne
//    @OneToMany(optional = false)
    @JoinColumn(name = "account_id")
    private Account holder;

}
