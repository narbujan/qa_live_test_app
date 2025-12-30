package qa_live_test_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;
	private String name;
	private String surname;

	@Column(name = "person_id", unique = true, nullable = false)
	private Integer personId;

	@Column(name = "mintos_employee")
	private boolean mintosEmployee;
}
