package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object PokemonAbilityMap : IntIdTable("pokemon_ability_map") {
    val pokemon = reference("pokemon", Pokemons)
    val ability = reference("ability", Abilities)
    val isHidden = bool("is_hidden").default(false)
}