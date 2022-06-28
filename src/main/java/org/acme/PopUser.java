package org.acme;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "pop_user")
public class PopUser extends PanacheEntityBase {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Long id;

	@NaturalId
	public String email;

	@Override
	public String toString() {
		return email + ":" + id;
	}
}
