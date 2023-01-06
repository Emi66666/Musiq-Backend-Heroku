package MojKvart.Projekt.domain;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "ZAHTJEVULOGE")
public class ZahtjevUloge implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long idZahtjeva;
  private int vrstaZatrazenogRolea;
  private Long userId;

  public ZahtjevUloge() {}

  public ZahtjevUloge(int vrstaZatrazenogRolea, Long userId) {
    this.userId = userId;
    this.vrstaZatrazenogRolea = vrstaZatrazenogRolea;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  public int getVrstaZatrazenogRolea() {
    return vrstaZatrazenogRolea;
  }

  public void setVrstaZatrazenogRolea(int vrstaZatrazenogRolea) {
    this.vrstaZatrazenogRolea = vrstaZatrazenogRolea;
  }

  public Long getIdZahtjeva() {
    return idZahtjeva;
  }

  public void setIdZahtjeva(Long idZahtjeva) {
    this.idZahtjeva = idZahtjeva;
  }

  @Override
  public String toString() {
    return "ZahtjevUloge{" +
      "idZahtjeva=" + idZahtjeva +
      ", vrstaZatrazenogRolea='" + vrstaZatrazenogRolea + '\'' +
      ", userId='" + userId + '\'' +
      '}';
  }
}
