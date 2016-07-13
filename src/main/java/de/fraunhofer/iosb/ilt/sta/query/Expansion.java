package de.fraunhofer.iosb.ilt.sta.query;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;

/**
 * Type safe builder of $expand queries.
 * 
 * @author Nils Sommer
 *
 */
public class Expansion {
	private final Set<ExpandedEntity> entities = new HashSet<>();
	private final EntityType type;

	private Expansion(EntityType type) {
		this.type = type;
	}
	
	/**
	 * Start an expansion for the original entity type being requested.
	 * 
	 * @param type the entity type
	 * @return the expansion
	 */
	public static Expansion of(EntityType type) {
		return new Expansion(type);
	}

	/**
	 * Start an expansion query.
	 * 
	 * @param entity the referenced entity
	 * @return the Expansion instance
	 * @throws InvalidRelationException the expanded entity is not related to the requested entity
	 */
	public Expansion with(ExpandedEntity entity) throws InvalidRelationException {
		if (!this.type.hasRelationTo(entity.getDirectSibling()))
			throw new InvalidRelationException(
					String.format("%s is not directly related to %s",
							this.type.getName(),
							entity.getDirectSibling().getName()));
		
		this.entities.add(entity);
		return this;
	}
	
	/**
	 * Expand to a referenced entity.
	 * 
	 * @param entity the referenced entity
	 * @return the Expansion instance
	 * @throws InvalidRelationException  the expanded entity is not related to the requested entity
	 */
	public Expansion and(ExpandedEntity entity) throws InvalidRelationException {
		return this.with(entity);
	}
	
	@Override
	public String toString() {		
		return this.entities
				.stream()
				.map(e -> e.toString())
				.collect(Collectors.joining(","));
	}
}
