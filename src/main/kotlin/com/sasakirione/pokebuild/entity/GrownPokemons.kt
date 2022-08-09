package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object GrownPokemons: IntIdTable("grown_pokemons") {
    val pokemon = reference("pokemon", Pokemons)
    val good = reference("good", Goods).nullable()
    val ability = reference("ability", Abilities)
    val nature = reference("nature", Natures)
    val move1 = reference("move1", Moves).nullable()
    val move2 = reference("move2", Moves).nullable()
    val move3 = reference("move3", Moves).nullable()
    val move4 = reference("move4", Moves).nullable()
    val evH = integer("evH").default(0)
    val evA = integer("evA").default(0)
    val evB = integer("evB").default(0)
    val evC = integer("evC").default(0)
    val evD = integer("evD").default(0)
    val evS = integer("evS").default(0)
    val ivH = integer("ivH").default(31)
    val ivA = integer("ivA").default(31)
    val ivB = integer("ivB").default(31)
    val ivC = integer("ivC").default(31)
    val ivD = integer("ivD").default(31)
    val ivS = integer("ivS").default(31)
    val isShiny = bool("isShiny").default(false)
    val comment = text("comment").nullable()
    val user = reference("user", Users)
}