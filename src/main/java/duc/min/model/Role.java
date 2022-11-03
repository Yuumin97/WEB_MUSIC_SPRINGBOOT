package duc.min.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId // id tu nhien
    @Enumerated(EnumType.STRING)// convert enum sang string
    private RoleName name;

    public Long getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public Role() {
    }

    public Role(Long id, RoleName name) {
        this.id = id;
        this.name = name;
    }
}
