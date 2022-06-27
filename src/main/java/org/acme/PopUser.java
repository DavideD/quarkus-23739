package org.acme;

import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NaturalId;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

@Entity
public class PopUser extends PanacheEntity {

	public String userName;
	@NaturalId
	public String email;

	@OneToMany
	public Set<Role> roles;

	public PopUser(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return email + ":" + id;
	}
}
