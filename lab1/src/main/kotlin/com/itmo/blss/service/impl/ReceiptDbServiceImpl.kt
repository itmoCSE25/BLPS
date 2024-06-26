package com.itmo.blss.service.impl

import com.itmo.blss.model.db.Receipt
import com.itmo.blss.model.enums.TransactionStatus
import com.itmo.blss.service.ReceiptDbService
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class ReceiptDbServiceImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : ReceiptDbService {
    override fun saveReceiptInfo(receipt: Receipt): Boolean {
        val sql = """
            insert into receipt (user_id, ticket_id, transaction_id, transaction_status) 
            values (:userId, :ticketId, :transactionId, :transactionStatus)
        """.trimIndent()

        return namedParameterJdbcTemplate.update(
            sql,
            with(receipt) {
                MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("ticketId", ticketId)
                    .addValue("transactionId", transactionId)
                    .addValue("transactionStatus", transactionStatus.code)
            }
        ) > 0
    }

    override fun updateTransactionInfo(userId: Long, transactionStatus: TransactionStatus) {
        val sql = """
            update tickets set
            transaction_status = :transactionStatus
            where user_id = :userId
        """.trimIndent()

        namedParameterJdbcTemplate.update(
            sql,
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("transactionStatus", transactionStatus.code)
        )
    }
}
