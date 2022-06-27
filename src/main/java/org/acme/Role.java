package org.acme;

import javax.persistence.Entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

@Entity
public class Role extends PanacheEntityBase {
	public String name;
}
