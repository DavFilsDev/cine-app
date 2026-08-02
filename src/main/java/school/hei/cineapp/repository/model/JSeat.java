package school.hei.cineapp.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"seat\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JSeat {
  @Id private String id;

  private String number;
  private String roomId;
}
