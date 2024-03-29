package top.e_learn.learnEnglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "way_for_pay_module")
public class WayForPayModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String merchantAccount;

    @Column
    private String merchantSecretKey;

    @Column
    private boolean active = false;


    public WayForPayModule() {
    }
}
