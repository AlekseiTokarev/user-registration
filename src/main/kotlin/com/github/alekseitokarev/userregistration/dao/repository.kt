package com.github.alekseitokarev.userregistration.dao

import com.github.alekseitokarev.userregistration.domain.User
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository


@Repository
class UserRepository(private val databaseClient: DatabaseClient) {

    fun findById(userId: Long): User? {
        val select = """
            SELECT users.user_id, users.first_name, users.last_name, users.email, address.line1, address.line2, address.city, address.state,address.zip
            FROM users
            LEFT JOIN address ON users.user_id = address.user_id AND address.archived = FALSE
            WHERE users.user_id = $userId"""

        return databaseClient.sql(select)
            .map { row ->
                User(
                    id = row.get("user_id", Integer::class.java)!!.toLong(),
                    firstName = row.get("first_name", String::class.java)!!,
                    lastName = row.get("last_name", String::class.java)!!,
                    email = row.get("email", String::class.java)!!
                )
            }
            .one()
            .block()
    }

    fun existByEmail(email: String): Boolean {
        val select = """
            SELECT 1
            FROM users
            WHERE email = '$email' AND users.active LIMIT 1;"""

        return databaseClient.sql(select).fetch().first().block() != null
    }

    fun insert(user: User): User {
        val insert = """
            INSERT INTO users (first_name, last_name, email)
                VALUES('${user.firstName}', '${user.lastName}', '${user.email}') 
            ON CONFLICT DO NOTHING 
            RETURNING user_id; """

        var result = databaseClient.sql(insert).fetch().first().block()

        val idConflict = result == null
        if (idConflict) {
            //happens when user_id got provided during user insertion so current counter of user_id sequence is left behind
            println("resetting user_id sequence then repeat an insert")
            val resetSequenceThenInsert =
                "select setval('users_user_id_seq', (SELECT MAX(user_id) FROM users));\n$insert"
            result = databaseClient.sql(resetSequenceThenInsert).fetch().all().blockLast()
        }
        val id: Long = result?.get("user_id").toString().toLong()
        user.id = id;
        return user;
    }

    fun upsert(user: User): User {
        val query = """
            INSERT INTO users (user_id, first_name, last_name, email)
                VALUES('${user.id}','${user.firstName}', '${user.lastName}', '${user.email}') 
            ON CONFLICT (user_id) 
                DO 
            UPDATE SET first_name = EXCLUDED.first_name, last_name = EXCLUDED.last_name, email = EXCLUDED.email"""

        val result = databaseClient.sql(query).fetch().one().block()
        return user
    }
}
