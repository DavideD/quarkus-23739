package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class RolesAugmentor implements SecurityIdentityAugmentor {

	private static final Logger log = Logger.getLogger( RolesAugmentor.class );

	@Override
	public int priority() {
		return SecurityIdentityAugmentor.super.priority();
	}

	@Override
	@ReactiveTransactional
	@ActivateRequestContext
	public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
		if ( identity.isAnonymous() ) {
			return Uni.createFrom().item( identity );
		}

		log.debug( "Getting user info" );
		OidcJwtCallerPrincipal jwtPrinciple = (OidcJwtCallerPrincipal) identity.getPrincipal();
		String email = "test@email.it";//jwtPrinciple.getClaim( EMAIL_CLAIM_NAME );

		log.debugf( "Got email for [{}]", email );

		if ( email == null ) {
			return Uni.createFrom().failure( new RuntimeException( String.format( "Unable to get email for user." ) );
		}

		log.debug( "Looking up user in the database." );
		return PopUser.<PopUser>find( "email", email )
				.singleResult()
				.map( user -> {
					log.debugf( "Adding roles to security identity for user with id [{}]", user.id );
					QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder()
							.setPrincipal( identity.getPrincipal() )
							.addAttributes( identity.getAttributes() )
							.addCredentials( identity.getCredentials() )
							.addRoles( identity.getRoles() );


					user.roles.stream().forEach( role -> builder.addRole( role.name ) );
//					builder.addAttribute( POPUSER_ATTRIBUTE_NAME, user );

					log.debugf( "Returning builder for user id [{}]", user.id );
					return builder.build();
				} );
	}
}